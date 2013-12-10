package simcity;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

import simcity.agent.Agent;
import simcity.gui.PersonGui;
import simcity.CityDirectory;
import simcity.gui.CityGui;
import simcity.interfaces.Person;
import simcity.interfaces.RestCustomer;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.Resident;
import simcity.interfaces.BankDepositor;
import simcity.interfaces.JobInterface;
import simcity.interfaces.Bus;
import simcity.interfaces.BusStop;
import simcity.mock.LoggedEvent;
import simcity.role.Role;
import simcity.housing.LandlordRole;
import simcity.housing.ResidentRole;
import simcity.market.MarketCustomerRole;
import simcity.bank.BankDepositorRole;
import simcity.bank.BankTellerRole;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketDelivererRole;
import simcity.role.JobRole;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;

import java.util.*;
import java.util.concurrent.Semaphore;

public class PersonAgent extends Agent implements Person 
{
	private int money;
	private int maxBalance;
	private int minBalance;
	private String name;
	private boolean haveBankAccount;
	private boolean rentDue;
	private Bus bus;
	private String destination;
	public boolean unitTesting = false;
	private boolean testingAnimation;
	private boolean usingBus;
	private boolean goingHome;
	private String foodPreference;

	private CityDirectory city;
	private CityGui cG;

	private PersonGui gui;
	private Semaphore atDestination = new Semaphore(0,true);
	private Semaphore onBus = new Semaphore(0,true);

	public List<BusStop> busStops = Collections.synchronizedList(new ArrayList<BusStop>());
	public List<ItemOrder> foodNeeded = Collections.synchronizedList(new ArrayList<ItemOrder>());
	public List<ItemOrder> groceries = Collections.synchronizedList(new ArrayList<ItemOrder>());

	public List<MyRole> roles = Collections.synchronizedList(new ArrayList<MyRole>());
	public List<Restaurant> restaurants = Collections.synchronizedList(new ArrayList<Restaurant>());
	public List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	public List<Bank> banks = Collections.synchronizedList(new ArrayList<Bank>());
	public List<Housing> houses = Collections.synchronizedList(new ArrayList<Housing>());

	private static final int nearDistance = 180;
	private Job job;
	private Time time = new Time(Day.Sun, 0, 0);
	private boolean robber = false;

	public enum NourishmentState {unknown, normal, gotHungry, hungry, full};
	public enum LocationState {unknown, outside, leavingHouse, home, ownerHouse, restaurant, market, bank, atDestination};
	public enum WorkingState {unknown, notWorking, working};
	public enum EconomicState {unknown, poor, middle, rich};
	public enum TransportationState {unknown, waitingForBus, ridingBus, inCar, walking, walkingFromVehicle};
	public enum PhysicalState {unknown, fit, average, lazy};

	public PersonState state = new PersonState();

	public PersonAgent(String name) {
		super();
		this.name = name;

		haveBankAccount = false;
		rentDue = false;
		destination = null;
		job = null;
		money = 0;
		minBalance = 100;
		maxBalance = 1000;

		state.ns = NourishmentState.normal;
		state.ls = LocationState.outside;
		state.ws = WorkingState.notWorking;
		state.ts = TransportationState.walking;

		houses.add(new Housing("home", "residentRole"));
		houses.add(new Housing("ownerHouse", "residentRole"));

		restaurants.add(new Restaurant("joshRestaurant", "italian", "joshCustomerRole"));
		restaurants.add(new Restaurant("cherysRestaurant", "blah", "AnjaliCustomerRole"));
		restaurants.add(new Restaurant("jesusRestaurant", "bleh", "jesusCustomerRole"));
		restaurants.add(new Restaurant("anjaliRestaurant", "blih", "anjaliCustomerRole"));

		markets.add(new Market("market1", "market1CustomerRole"));
		markets.add(new Market("market2", "market2CustomerRole"));

		banks.add(new Bank("bank1", "bank1DepositorRole"));
		banks.add(new Bank("bank2", "bank2DepositorRole"));
	}

	public void setCityDirectory(CityDirectory c) {
		city = c;
	}

	public void setCityGui(CityGui g) {
		cG = g;
	}

	public void setGui(PersonGui g) {
		gui = g;
	}

	public void addResidentRole() {
		Housing h = houses.get(0);	
		if (!findRole(h.residentRole)) {
			ResidentRole r = city.ResidentFactory(h.residentRole);
			addRole(r, h.residentRole);
			cG.addResident(r, h.location, houses.get(1).location);
		}
	}

	public void setHome(String home) {
		houses.get(0).location = home;
		if (job != null && job.location.equals("home")) {
			job.location = home;
		}
	}

	public void setOwnerHome(String home) {
		houses.get(1).location = home;
		for(MyRole mr : roles)
		{
			if(mr.name.equals("residentRole"))
			{
				if(mr.r instanceof ResidentRole)
				{
					ResidentRole r = (ResidentRole)(mr.r);
					cG.addResident(r, houses.get(0).location, houses.get(0).location);
				}
			}
		}
	}

	public void setTestingAnimation(boolean tA) {
		testingAnimation = tA;
	}

	public void setUsingBus(boolean uB) {
		usingBus = uB;
	}

	public void setGoingHome(boolean gH) {
		goingHome = gH;
	}
	
	public void setPreference(String p) {
		foodPreference = p;
	}

	public String getName() {
		return name;
	}

	public int getSalary() {
		return job.payrate;
	}

	public String getJobLocation() {
		return job.location;
	}

	public int getMoney() {
		return money;
	}

	public String getHome() {
		return houses.get(0).location;
	}

	public String getJob() {
		return job.role;
	}

	public Resident getResident() {
		for (MyRole mr : roles) {
			if (mr.name.equals("residentRole")) {
				if (mr.r instanceof Resident) {
					return (Resident)(mr.r);
				}
			}
		}
		return null;
	}

	public String getDestination() {
		return destination;
	}

	public void setRentDue(boolean b) {
		rentDue = b;
	}

	public void setPState(PhysicalState ps) {
		state.ps = ps;
	}

	public void setLState(LocationState ls) {
		state.ls = ls;
	}

	public void setEState(String es) {
		switch(es) {
		case "poor":
			state.es = EconomicState.poor;
			money = 50;
			minBalance = 10;
			maxBalance = 200;
			break;
		case "middle":
			state.es = EconomicState.middle;
			money = 250;
			minBalance = 100;
			maxBalance = 400;
			break;
		case "rich":
			state.es = EconomicState.rich;
			money = 500;
			minBalance = 200;
			maxBalance = 1000;
			break;
		default:
			state.es = EconomicState.middle;
			money = 250;
			minBalance = 100;
			maxBalance = 400;
			break;
		}
	}

	public void setPState(String ps) {
		switch(ps) {
		case "lazy":
			state.ps = PhysicalState.lazy;
			break;
		case "average":
			state.ps = PhysicalState.average;
			break;
		case "fit":
			state.ps = PhysicalState.fit;
			break;
		default:
			state.ps = PhysicalState.average;
			break;
		}
	}

	public void addRole(Role r, String n) {
		r.setPerson(this);
		roles.add(new MyRole(r, n));
	}

	public void addBusStops(List<BusStop> stops) {
		for (BusStop b : stops) {
			busStops.add(b);
		}
	}

	private boolean wantToGoToRestaurant() {
		if (allRestaurantsClosed()) {
			return false;
		}
		if (state.ps == PhysicalState.fit) {
			return false;
		}
		if (state.ps == PhysicalState.average){
			int rand = (int)(Math.random() * 2);
			if (rand == 0) {
				return true;
			}
			return false;
		}
		return true;
	}

	private BusStop closestBusStop() {
		int distance = 100000000;
		BusStop temp = busStops.get(0);
		for (BusStop b : busStops) {
			int sum = Math.abs(gui.getXPos()-city.getBuildingEntrance(b.getName()).x) + Math.abs(gui.getYPos()-city.getBuildingEntrance(b.getName()).y);
			if (sum < distance) {
				distance = sum;
				temp = b;
			}
		}
		return temp;
	}
	
	private Bank closestBank() {
		int distance = 100000000;
		Bank temp = banks.get(0);
		for (Bank b : banks) {
			int sum = Math.abs(gui.getXPos()-city.getBuildingEntrance(b.location).x) + Math.abs(gui.getYPos()-city.getBuildingEntrance(b.location).y);
			if (sum < distance) {
				distance = sum;
				temp = b;
			}
		}
		return temp;
	}

	private BusStop closestBusStop(String destination) {
		int distance = 100000000;
		BusStop temp = busStops.get(0);
		for (BusStop b : busStops) {
			int sum = Math.abs(city.getBuildingEntrance(destination).x-city.getBuildingEntrance(b.getName()).x) + Math.abs(city.getBuildingEntrance(destination).y-city.getBuildingEntrance(b.getName()).y);
			if (sum < distance) {
				distance = sum;
				temp = b;
			}
		}
		return temp;
	}

	private boolean nearDestination(String destination) {
		if (unitTesting) {
			return false;
		}
		if (Math.abs(gui.getXPos()-city.getBuildingEntrance(destination).x) <= nearDistance && Math.abs(gui.getYPos()-city.getBuildingEntrance(destination).y) <= nearDistance) {
			return true;
		}
		return false;
	}

	private boolean takeBus(String destination) {
		//		if (job != null && destination.equals(job.location) && (time.plus(30)).greaterThanOrEqualTo(job.startShifts.get(time.getDay())) && !time.greaterThanOrEqualTo(job.endShifts.get(time.getDay()))) {
		//			return true;
		//		}
		//
		if (nearDestination(destination)) {
			return false;
		}
		if (state.ps == PhysicalState.fit) {
			return false;
		}
		if (state.ps == PhysicalState.average) {
			int rand = (int)(Math.random() * 2);
			if (rand == 0) {
				return true;
			}
			return false;
		}
		if (state.ps == PhysicalState.lazy) {
			return true;
		}
		return false;
	}

	private boolean allRestaurantsClosed() {
		for (Restaurant r : restaurants) {
			if (!r.closed) {
				return false;
			}
		}
		return true;
	}

	private boolean allMarketsClosed() {
		for (Market m : markets) {
			if (!m.closed) {
				return false;
			}
		}
		return true;
	}

	private boolean allBanksClosed() {
		for (Bank b : banks) {
			if (!b.closed) {
				return false;
			}
		}
		return true;
	}

	private Restaurant chooseRestaurant() {
		if (destination != null) {
			if (destination.equals("joshRestaurant")) {
				return restaurants.get(0);
			}
			if (destination.equals("cherysRestaurant")) {
				return restaurants.get(1);
			}
			if (destination.equals("jesusRestaurant")) {
				return restaurants.get(2);
			}
			if (destination.equals("anjaliRestaurant")) {
				return restaurants.get(3);
			}
		}
		if (foodPreference.equals("anjaliRestaurant") && !restaurants.get(3).closed) {
			return restaurants.get(3);
		} else if (foodPreference.equals("cherysRestaurant") && !restaurants.get(1).closed) {
			return restaurants.get(1);
		} else if (foodPreference.equals("jesusRestaurant") && !restaurants.get(2).closed) {
			return restaurants.get(2);
		} else {
			return restaurants.get(0);
		}
	}

	private Restaurant getRestaurant(String building) {
		for (Restaurant r : restaurants) {
			if (r.location.equals(building)) {
				return r;
			}
		}
		return restaurants.get(0);
	}

	private Market chooseMarket() {
		if (destination != null) {
			if (destination.equals("market1")) {
				return markets.get(0);
			}
			if (destination.equals("market2")) {
				return markets.get(1);
			}
		}
		if (houses.get(0).location.contains("house") || houses.get(0).location.contains("apartment1") || houses.get(0).location.contains("apartment2")) {
			return markets.get(0);
		} else {
			return markets.get(1);
		}
	}

	private Market getMarket(String building) {
		for (Market m : markets) {
			if (m.location.equals(building)) {
				return m;
			}
		}
		return markets.get(0);
	}

	private Bank chooseBank() {
		int distance = 100000000;
		Bank temp = banks.get(0);
		for (Bank b : banks) {
			int sum = Math.abs(gui.getXPos()-city.getBuildingEntrance(b.location).x) + Math.abs(gui.getYPos()-city.getBuildingEntrance(b.location).y);
			if (sum < distance) {
				distance = sum;
				temp = b;
			}
		}
		return temp;
	}

	private Bank getBank(String building) {
		for (Bank b : banks) {
			if (b.location.equals(building)) {
				return b;
			}
		}
		return banks.get(0);
	}

	public void businessIsClosed(String building, boolean closed) {
		if (!unitTesting) {
			cG.businessIsClosed(building, closed);
		}
	}

	public void setBusinessClosed(String building, boolean closed) {
		if (building.contains("market")) {
			for (Market m : markets) {
				if (m.location.equals(building)) {
					m.closed = closed;
				}
			}
		}
		if (building.contains("Restaurant")) {
			for (Restaurant r : restaurants) {
				if (r.location.equals(building)) {
					r.closed = closed;
				}
			}
		}
		if (building.contains("bank")) {
			for (Bank b : banks) {
				if (b.location.equals(building)) {
					b.closed = closed;
				}
			}
		}
	}

	public boolean businessOpen(String building) {
		if (building.contains("market")) {
			for (Market m : markets) {
				if (m.location.equals(building)) {
					if (!m.closed) {
						return true;
					}
				}
			}
		}
		if (building.contains("restaurant")) {
			for (Restaurant r : restaurants) {
				if (r.location.equals(building)) {
					if (!r.closed) {
						return true;
					}
				}
			}
		}
		if (building.contains("bank")) {
			for (Bank b : banks) {
				if (b.location.equals(building)) {
					if (!b.closed) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean findRole(String roleName) {
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.name.equals(roleName)) {
					return true;
				}
			}
		}
		return false;
	}

	public void addJobRole() {
		if (job.role.equals("restWaiter1Role") || job.role.equals("restWaiter2Role")) {
			if (job.jobRole instanceof RestWaiterRole) {
				RestWaiterRole rW = (RestWaiterRole)(job.jobRole);
				cG.addRestWaiter(rW);
			}
		}
		if (job.role.equals("marketEmployeeRole")) {
			MarketEmployeeRole e = (MarketEmployeeRole)(job.jobRole);
			cG.addMarketEmployee(e);
		}
		if (job.role.equals("marketDelivererRole")) {
			MarketDelivererRole d = (MarketDelivererRole)(job.jobRole);
			cG.addMarketDeliverer(d);
		}
		if (job.role.equals("bankTellerRole")) {
			BankTellerRole t = (BankTellerRole)(job.jobRole);
			cG.addBankTeller(t);
		}
		if (job.role.equals("landlordRole")) {
			LandlordRole l = (LandlordRole)(job.jobRole);

			cG.addLandlord(l, houses.get(0).location);

		}
	}


	//Messages

	public void msgAtDestination() {
		atDestination.release();
		stateChanged();
	}

	public void msgOnBus() {
		onBus.release();
		stateChanged();
	}

	public void msgUpdateWatch(Day d, int h, int m) {
		log.add(new LoggedEvent("Received msgUpdateWatch"));
		time.day = d;
		time.hour = h;
		time.minute = m;
		if (time.getHour() == 8 && time.getMinute() == 0) {
			if (name.equals("bankDepositor")) {
				money += 600;
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I now have $" + money);
			}
			if (name.equals("robber")) {
				robber = true;
				money = minBalance;
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I now have $" + money);
			}
			if (name.equals("marketCustomer")) {
				foodNeeded.add(new ItemOrder("Steak",2));
				foodNeeded.add(new ItemOrder("Pozole",2));
				foodNeeded.add(new ItemOrder("Chicken",2));
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Food is low");
			}
			if (name.equals("hungryResident") || name.equals("restCustomer")) {
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Got hungry");
				state.ns = NourishmentState.gotHungry;
			}
		}
		if (name.equals("normA")) {
			if (time.getHour() == 4 && time.getMinute() == 0) {
				money = 225;
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I now have $" + money);
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Got hungry");
				state.ns = NourishmentState.gotHungry;
			}
			if (time.getHour() == 9 && time.getMinute() == 0) {
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Got hungry");
				state.ps = PhysicalState.lazy;
				state.ns = NourishmentState.gotHungry;
			}
			if (time.getHour() == 16 && time.getMinute() == 0) {
				if (money > minBalance) {
					money = minBalance;
					AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I now have $" + money);
				}
			}
		}
		if (name.contains("normB")) {
			if (time.getHour() == 4 && time.getMinute() == 0) {
				money = 225;
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I now have $" + money);
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Got hungry");
				state.ns = NourishmentState.gotHungry;
			}
			if (name.equals("normB2") || name.equals("normB3")) {
				if (time.getHour() == 5 && time.getMinute() == 0) {
					state.ps = PhysicalState.lazy;
				}
			}
			if (time.getHour() == 9 && time.getMinute() == 0) {
				AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Got hungry");
				state.ps = PhysicalState.lazy;
				state.ns = NourishmentState.gotHungry;
			}
			if (time.getHour() == 16 && time.getMinute() == 0) {
				if (money > minBalance) {
					money = minBalance;
					AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I now have $" + money);
				}
			}
		}
		stateChanged();
	}

	public void msgImHungry() {
		log.add(new LoggedEvent("Received msgImHungry"));
		state.ns = NourishmentState.gotHungry;
		stateChanged();
	}

	public void msgYoureHired(String role, int payrate, Map<Day,Time> startShifts, Map<Day,Time> endShifts) {
		JobRole j = city.JobFactory(role);
		addRole(j, role);
		job = new Job(j, j.getJobLocation(), role, payrate, startShifts, endShifts);
		stateChanged();
	}

	public void msgYoureHired(String jobLocation, String role, int payrate, Map<Day,Time> startShifts, Map<Day,Time> endShifts) {
		job = new Job(jobLocation, role, payrate, startShifts, endShifts);
		stateChanged();
	}

	public void msgLeftDestination(Role r) {
		log.add(new LoggedEvent("Received msgLeftDestination"));
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == r) {
					mr.active = false;
					state.ls = LocationState.outside;
					destination = null;
				}
			}
		}
		stateChanged();
	}

	public void msgFoodLow(List<ItemOrder> items) {
		log.add(new LoggedEvent("Received msgFoodLow"));
		AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Food is low");
		for (ItemOrder i : items) {
			foodNeeded.add(i);
		}
		stateChanged();
	}

	public void msgExpense(int cost) {
		log.add(new LoggedEvent("Received msgExpense"));
		money -= cost;
		AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Spent $" + cost + ". Money = $" + money);
		stateChanged();
	}

	public void msgReceivedItems(List<ItemOrder> items) {
		log.add(new LoggedEvent("Received msgReceivedItems"));
		for (ItemOrder i : items) {
			groceries.add(i);
		}
		foodNeeded.clear();
		stateChanged();
	}

	public void msgIncome(int cash) {
		log.add(new LoggedEvent("Received msgIncome"));
		money += cash;
		AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Earned $" + cash + ". Money = $" + money);
		stateChanged();
	}

	public void msgDoneEating() {
		log.add(new LoggedEvent("Received msgDoneEating"));
		AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Done eating");
		state.ns = NourishmentState.normal;
		stateChanged();
	}

	public void msgEndShift() {
		state.ws = WorkingState.notWorking;
		stateChanged();
	}

	public void msgCreatedAccount() {
		log.add(new LoggedEvent("Received msgCreatedAccount"));
		haveBankAccount = true;
	}

	public void msgBusIsHere(Bus b) {
		bus = b;
		stateChanged();
	}

	public void msgGoodGuyAgain() {
		log.add(new LoggedEvent("Received msgGoodGuyAgain"));
		robber = false;
	}

	public void msgAtDestination(String d) {
		state.ts = TransportationState.walkingFromVehicle;
		destination = d;
		stateChanged();
	}

	//Scheduler

	public boolean pickAndExecuteAnAction() {
		if (state.ts == TransportationState.walking || state.ts == TransportationState.walkingFromVehicle) {
			if (job != null && state.ws == WorkingState.notWorking && !job.startShifts.get(time.getDay()).isEqualTo(job.endShifts.get(time.getDay())) && (time.plus(90)).greaterThanOrEqualTo(job.startShifts.get(time.getDay())) && !time.greaterThanOrEqualTo(job.endShifts.get(time.getDay()))) { //if an hour before your shift starts
				if (state.ls == LocationState.home) {
					leaveHouse();
					return true;
				} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
					goToWork();
					return true;
				}
			}
			if (state.ws == WorkingState.working && time.greaterThanOrEqualTo(job.endShifts.get(time.getDay()) ) && !job.role.equals("landlordRole")) { //if your shift ends
				endShift();
				return true;
			} 
			if (state.ws == WorkingState.notWorking) {
				if (money <= minBalance && state.ls != LocationState.bank) {
					Bank b;
					if (destination != null && destination.contains("bank")) {
						b = getBank(destination);
					} else {
						b = chooseBank();
					}
					if (!b.closed) {
						destination = b.location;
						if (state.ls == LocationState.home) {
							leaveHouse();
							return true;
						} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
							goToBank(b);
							return true;
						} 
					}
				} 
				if (rentDue) {
					if (state.ls == LocationState.home) {
						leaveHouse();
						return true;
					} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
						goToOwnerHouse();
						return true;
					} 
				} 
				if (!foodNeeded.isEmpty()) {
					Market m;
					if (destination != null && destination.contains("market")) {
						m = getMarket(destination);
					} else {
						m = chooseMarket();
					}
					if (!m.closed) {
						destination = m.location;
						if (state.ls == LocationState.home) {
							leaveHouse();
							return true;
						} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
							goToMarket();
							return true;
						} 
					}
				} 
				if (state.ns == NourishmentState.gotHungry) {
					if (wantToGoToRestaurant()) {
						Restaurant r;
						if (destination != null && destination.contains("restaurant")) {
							r = getRestaurant(destination);
						} else {
							r = chooseRestaurant();
						}
						if (!r.closed) {
							destination = r.location;
							if (state.ls == LocationState.home) {
								leaveHouse();
								return true;
							} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
								goToRestaurant();
								return true;
							} 
						}
					} else {
						if (state.ls == LocationState.home) {
							goEat();
							return true;
						} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
							goHome();
							return true;
						} 
					} 
				} 
				if (!groceries.isEmpty()) {
					if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
						goHome();
						return true;
					} 
				} 
				if (money >= maxBalance && state.ls != LocationState.bank) {
					Bank b;
					if (destination != null && destination.contains("bank")) {
						b = getBank(destination);
					} else {
						b = chooseBank();
					}
					if (!b.closed) {
						destination = b.location;
						if (state.ls == LocationState.home) {
							leaveHouse();
							return true;
						} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
							goToBank(b);
							return true;
						} 
					}
				} 
			} 
		}

		if (state.ts == TransportationState.waitingForBus && bus != null) {
			boardBus();
			return true;
		}

		boolean anytrue = false;
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.active) {
					anytrue = mr.r.pickAndExecuteAnAction();
					return anytrue;
				}
			}
		}

		if (!unitTesting && goingHome && state.ts == TransportationState.walking && state.ls != LocationState.home && state.ls != LocationState.leavingHouse) {
			goHome(); //if nothing left to do, go home and do whatever
			return true;
		}

		return false;
	}

	//Actions

	private void endShift() {
		AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "My shift is over");
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.name.equals(job.role)) {
					if (mr.r instanceof JobInterface) {
						JobInterface j = (JobInterface)(mr.r);
						j.msgEndShift();
						money += job.payrate;
						AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Earned $" + job.payrate + ". Money = $" + money);
						state.ws = WorkingState.notWorking;
					}
					return;
				}
			}
		}
	}

	private void boardBus() {
		AlertLog.getInstance().logMessage(AlertTag.PERSON, name, destination);
		gui.DoBoardBus(closestBusStop().getName(), closestBusStop(destination).getName());
		try {
			onBus.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bus.msgComingAboard(this, destination);
		bus = null;
		state.ts = TransportationState.ridingBus;
	}

	private void goHome() {
		Housing h = houses.get(0); //person's house
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(h.location))) {
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm going home to " + h.location);
			goToDestination(h.location);
		} else {
			if (!findRole(h.residentRole)) {
				ResidentRole r = city.ResidentFactory(h.residentRole);
				addRole(r, h.residentRole);
				cG.addResident(r, h.location, houses.get(1).location);
			}
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(h.residentRole)) {
						if (mr.r instanceof Resident) {
							mr.active = true;
							state.ls = LocationState.home;
							Resident r = (Resident)(mr.r);
							mr.r.setPerson(this);
							r.msgImHome();
							if (!groceries.isEmpty()) {
								AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Bringing groceries home");
								r.msgGroceries(groceries);
								groceries.clear();
							} 
							if (state.ns == NourishmentState.gotHungry) {
								AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Eating at home");
								r.msgEat();
								state.ns = NourishmentState.hungry;
							} 
						}
						return;
					}
				}
			}
		}
	}

	private void goEat() {
		Housing h = houses.get(0); //person's house
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.name.equals(h.residentRole)) {
					if (mr.r instanceof Resident) {
						Resident r = (Resident)(mr.r);
						mr.r.setPerson(this);
						mr.active = true;
						AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Eating at home");
						r.msgEat();
						state.ns = NourishmentState.hungry;
					}
					return;
				}
			}
		}       
	}

	private void leaveHouse() {
		Housing h = houses.get(0); //person's house
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.name.equals(h.residentRole)) {
					if (mr.r instanceof Resident) {
						Resident r = (Resident)(mr.r);
						mr.r.setPerson(this);
						mr.active = true;
						AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "Leaving house");
						r.msgLeave();
						state.ls = LocationState.leavingHouse;
					}
					return;
				}
			}
		} 
	}

	private void goToOwnerHouse() {
		Housing h = houses.get(1); //owner's house
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(h.location))) {
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm going to pay rent");
			goToDestination(h.location);
		} else {
			if (!findRole(h.residentRole)) {
				ResidentRole r = city.ResidentFactory(h.residentRole);
				addRole(r, h.residentRole);
				cG.addResident(r, houses.get(0).location, h.location);
			} 
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(h.residentRole)) {
						if (mr.r instanceof Resident) {
							Resident r = (Resident)(mr.r);
							mr.r.setPerson(this);
							mr.active = true;
							state.ls = LocationState.ownerHouse;
							r.msgAtLandlord();
						}
						return;
					}
				}
			} 
		}
	}

	private void goToRestaurant() {
		Restaurant r = chooseRestaurant();
		if (r.closed) {
			return;
		}
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(r.location))) {
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm going to eat at a restaurant");
			goToDestination(r.location);
		} else {
			if (!findRole(r.customerRole)) {
				RestCustomerRole c = city.RestCustomerFactory(r.customerRole);
				addRole(c, r.customerRole);
				cG.addRestCustomer(c, r.location);
			}
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(r.customerRole)) {
						if (mr.r instanceof RestCustomer) {
							RestCustomer c = (RestCustomer)(mr.r);
							mr.r.setPerson(this);
							mr.active = true;
							state.ls = LocationState.restaurant;
							c.gotHungry();
							state.ns = NourishmentState.hungry;
						}
						return;
					}
				}
			}	
		}
	}

	private void goToMarket() {
		Market m = chooseMarket();
		if (m.closed) {
			return;
		}
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(m.location))) {
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm going to the market");
			goToDestination(m.location);
		} else {
			if (!findRole(m.customerRole)) {
				MarketCustomerRole c = city.MarketCustomerFactory(m.customerRole);
				addRole(c, m.customerRole);
				cG.addMarketCustomer(c, m.location);
			} 
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(m.customerRole)) {
						if (mr.r instanceof MarketCustomer) {
							MarketCustomer c = (MarketCustomer)(mr.r);
							mr.r.setPerson(this);
							mr.active = true;
							state.ls = LocationState.market;
							c.msgOrderItems(foodNeeded);
						}
						return;
					}
				}
			}
		}
	}

	private void goToBank(Bank b) {
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(b.location))) {
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm going to the bank");
			goToDestination(b.location);
		} else {
			if (!findRole(b.depositorRole)) {
				BankDepositorRole d = city.BankDepositorFactory(b.depositorRole);
				addRole(d, b.depositorRole);
				cG.addBankDepositor(d, b.location);
			}
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(b.depositorRole)) {
						if (mr.r instanceof BankDepositor) {
							BankDepositor d = (BankDepositor)(mr.r);
							mr.r.setPerson(this);
							mr.active = true;
							state.ls = LocationState.bank;
							if (robber) {
								d.msgImARobber();
							} else {
								if (money >= maxBalance) {
									d.msgMakeDeposit(money-minBalance-(maxBalance-minBalance)/2);
								}
								if (money <= minBalance) {
									d.msgMakeWithdrawal(minBalance+(maxBalance-minBalance)/3-money);
								}
							}
						}
						return;
					}
				}
			}
		}
	}

	private void goToWork() {
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(job.location))) { 
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm going to work at " + job.location);
			if (destination != null && !destination.equals(job.location)) {
				state.ts = TransportationState.walking;
			}
			goToDestination(job.location);
		} else {
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(job.role)) {
						if (mr.r instanceof JobInterface) {
							JobInterface j = (JobInterface)(mr.r);
							mr.r.setPerson(this);
							mr.active = true;
							j.msgStartShift();
							state.ls = job.jobLocation;
							state.ws = WorkingState.working;
						}
						return;
					}
				}
			}
		}
	}

	private void goToDestination(String d) {
		if (usingBus && takeBus(d) && state.ts != TransportationState.walkingFromVehicle) {
			BusStop b = closestBusStop();
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm taking the bus. Going to " + b.getName());
			if (!unitTesting) {
				gui.DoGoToDestination(b.getName()); //just walk there
				try {
					atDestination.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "At the bus stop");
			state.ts = TransportationState.waitingForBus;
			destination = d;
			b.msgWaitingForBus(this);
		} else {
			AlertLog.getInstance().logMessage(AlertTag.PERSON, name, "I'm walking there");
			state.ts = TransportationState.walking;
			destination = d;
			if (!unitTesting) {
				gui.DoGoToDestination(d); //just walk there
				try {
					atDestination.acquire();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			state.ls = LocationState.atDestination;
		}
	}


	//inner classes

	public class MyRole {
		Role r;
		String name;
		boolean active;

		MyRole(Role role, String n) {
			r = role;
			name = n;
			active = false;
		}

		public boolean isActive() {
			return active;
		}
	}

	public class PersonState {
		NourishmentState ns;
		LocationState ls;
		WorkingState ws;
		TransportationState ts;
		EconomicState es;
		PhysicalState ps;

		PersonState() {

		}

		public NourishmentState getNState() {
			return ns;
		}
		public LocationState getLState() {
			return ls;
		}
		public WorkingState getWState() {
			return ws;
		}
		public TransportationState getTState() {
			return ts;
		}
		public EconomicState getEState() {
			return es;
		}
		public PhysicalState getPState() {
			return ps;
		}
	}

	public class Job {
		Job(JobRole j, String l, String r, int p, Map<Day,Time> startShifts, Map<Day,Time> endShifts) {
			jobRole = j;
			location = l;
			role = r;
			payrate = p;
			this.startShifts = startShifts;
			this.endShifts = endShifts;
			switch (location) {

				case "joshRestaurant": case "cherysRestaurant": case "alfredRestaurant": case "anjaliRestaurant": case "jesusRestaurant":
					jobLocation = LocationState.restaurant;
					break;
				case "market1": case "market2":
					jobLocation = LocationState.market;
					break;
				case "home":
					endShifts.get(Day.Mon).hour = startShifts.get(Day.Mon).hour;	//Landlord only collects rent once per week
					endShifts.get(Day.Tue).hour = startShifts.get(Day.Tue).hour;
					endShifts.get(Day.Wed).hour = startShifts.get(Day.Wed).hour;
					endShifts.get(Day.Thu).hour = startShifts.get(Day.Thu).hour;
					endShifts.get(Day.Fri).hour = startShifts.get(Day.Fri).hour;
					endShifts.get(Day.Sun).hour = startShifts.get(Day.Sun).hour;
					jobLocation = LocationState.home;
					break;
				case "bank1": case "bank2":
					endShifts.get(Day.Sun).hour = startShifts.get(Day.Sun).hour;	//Banks are closed on weekends
					endShifts.get(Day.Sat).hour = startShifts.get(Day.Sat).hour;
					jobLocation = LocationState.bank;
					break;
				default:
					break;
			}
		}
		
		Job(String l, String r, int p, Map<Day,Time> startShifts, Map<Day,Time> endShifts) {
			location = l;
			role = r;
			payrate = p;
			this.startShifts = startShifts;
			this.endShifts = endShifts;
			switch (location) {

				case "joshRestaurant": case "cherysRestaurant": case "alfredRestaurant": case "anjaliRestaurant": case "jesusRestaurant":
					jobLocation = LocationState.restaurant;
					break;
				case "market1": case "market2":
					jobLocation = LocationState.market;
					break;
				case "home":
					endShifts.get(Day.Mon).hour = startShifts.get(Day.Mon).hour;	//Landlord only collects rent once per week
					endShifts.get(Day.Tue).hour = startShifts.get(Day.Tue).hour;
					endShifts.get(Day.Wed).hour = startShifts.get(Day.Wed).hour;
					endShifts.get(Day.Thu).hour = startShifts.get(Day.Thu).hour;
					endShifts.get(Day.Fri).hour = startShifts.get(Day.Fri).hour;
					endShifts.get(Day.Sun).hour = startShifts.get(Day.Sun).hour;
					jobLocation = LocationState.home;
					break;
				case "bank1": case "bank2":
					endShifts.get(Day.Sun).hour = startShifts.get(Day.Sun).hour;	//Banks are closed on weekends
					endShifts.get(Day.Sat).hour = startShifts.get(Day.Sat).hour;
					jobLocation = LocationState.bank;
					break;
				default:
					break;

			}
		}
		JobRole jobRole;
		String role;
		LocationState jobLocation;
		String location;
		Map<Day, Time> startShifts;
		Map<Day, Time> endShifts;
		int payrate;
	}

	public class Housing {
		Housing(String l, String r) {
			location = l;
			residentRole = r;
		}
		String residentRole;
		String location;
	}

	public class Restaurant {
		Restaurant(String l, String t, String r) {
			location = l;
			type = t;
			customerRole = r;
			closed = true;
		}
		String location;
		String type;
		String customerRole;
		boolean closed;
	}

	public class Market {
		Market(String l, String r) {
			location = l;
			customerRole = r;
			closed = false;
		}
		String customerRole;
		String location;
		boolean closed;
	}


	public class Bank {
		Bank(String l, String r) {
			location = l;
			depositorRole = r;
			closed = true;
		}
		String depositorRole;
		String location;
		boolean closed;
	}
}
