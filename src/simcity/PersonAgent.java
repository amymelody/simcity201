package simcity;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

import simcity.agent.Agent;
import simcity.interfaces.RestCustomer;
import simcity.interfaces.MarketCustomer;
import simcity.interfaces.Resident;
import simcity.interfaces.Depositor;
import simcity.interfaces.JobInterface;
import simcity.interfaces.Bus;
import simcity.interfaces.Car;
import simcity.interfaces.BusStop;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

import java.util.*;

public class PersonAgent extends Agent 
{
	private int money;
	private int maxBalance;
	private int minBalance;
	private String name;
	private boolean haveBankAccount;
	private boolean rentDue;
	private Car car;
	private Bus bus;
	private String destination;
	
	public List<BusStop> busStops = Collections.synchronizedList(new ArrayList<BusStop>());
	public List<ItemOrder> foodNeeded = Collections.synchronizedList(new ArrayList<ItemOrder>());
	public List<ItemOrder> groceries = Collections.synchronizedList(new ArrayList<ItemOrder>());

	public List<MyRole> roles = Collections.synchronizedList(new ArrayList<MyRole>());
	public List<Restaurant> restaurants = Collections.synchronizedList(new ArrayList<Restaurant>());
	public List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	public List<Bank> banks = Collections.synchronizedList(new ArrayList<Bank>());
	public List<Housing> houses = Collections.synchronizedList(new ArrayList<Housing>());
	public Map<String, List> places;
	public Map<String, Point> locations;

	private Job job;
	private Time time = new Time(Day.Sun, 0, 0);

	public enum NourishmentState {unknown, normal, gotHungry, hungry, full};
	public enum LocationState {unknown, outside, leavingHouse, home, ownerHouse, restaurant, market, bank, atDestination};
	public enum WorkingState {unknown, notWorking, working};
	public enum FinancialState {unknown, poor, middleClass, rich};
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
		restaurants.add(new Restaurant("cherysRestaurant", "blah", "cherysCustomerRole"));
		restaurants.add(new Restaurant("jesusRestaurant", "bleh", "jesusCustomerRole"));
		restaurants.add(new Restaurant("alfredRestaurant", "bluh", "alfredCustomerRole"));
		restaurants.add(new Restaurant("anjaliRestaurant", "blih", "anjaliCustomerRole"));
		
		markets.add(new Market("market1", "market1CustomerRole"));
		markets.add(new Market("market2", "market2CustomerRole"));
		markets.add(new Market("market3", "market3CustomerRole"));
		
		banks.add(new Bank("bank1", "bank1DepositorRole"));
	}
	
	
	public String getName() {
		return name;
	}
	
	public int getMoney() {
		return money;
	}
	
	public String getJob() {
		return job.role;
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
	
	public void addRole(Role r, String n) {
		r.setPerson(this);
		roles.add(new MyRole(r, n));
	}
	
	public void addBusStop(BusStop b) {
		busStops.add(b);
	}
	
	/*private RestCustomer RestCustomerFactory(String roleName) {
		switch(roleName) {
			case "joshCustomerRole":
				return new JoshCustomerRole(name);
			default:
				break;
		}
		return null;
	}
	
	private MarketCustomer MarketCustomerFactory(String roleName) {
		switch(roleName) {
			case "market1CustomerRole":
				return new MarketCustomerRole(name);
			case "market2CustomerRole":
				return new MarketCustomerRole(name);
			case "market3CustomerRole":
				return new MarketCustomerRole(name);
			default:
				break;
		}
		return null;
	}
	
	private Depositor DepositorFactory(String roleName) {
		switch(roleName) {
			case "bank1DepositorRole":
				return new DepositorRole(name);
			default:
				break;
		}
		return null;
	}
	
	private Resident ResidentFactory(String roleName) {
		switch(roleName) {
			case "residentRole":
				return new ResidentRole(name);
			default:
				break;
		}
		return null;
	}*/
	
	private boolean haventEatenOutInAWhile() {
		return true;
	}
	
	private BusStop closestBusStop() {
		return busStops.get(0);
	}
	
	private boolean nearDestination(String destination) {
		return false;
	}
	
	private boolean takeBus(String destination) {
		if (job != null && destination.equals(job.location) && (time.plus(15)).greaterThanOrEqualTo(job.startShifts.get(time.getDay()))) {
			return true;
		}
		/*if (nearDestination(destination)) {
			return false;
		}*/
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
			if (destination.equals("alfredRestaurant")) {
				return restaurants.get(3);
			}
			if (destination.equals("anjaliRestaurant")) {
				return restaurants.get(4);
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
			if (destination.equals("market3")) {
				return markets.get(2);
			}
		}
		return markets.get(0);
	}
	
	private Bank chooseBank() {
		return banks.get(0);
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
	

	//Messages

	public void msgUpdateWatch(Day d, int h, int m) {
		log.add(new LoggedEvent("Received msgUpdateWatch"));
		time.day = d;
		time.hour = h;
		time.minute = m;
		stateChanged();
	}

	public void msgImHungry() {
		log.add(new LoggedEvent("Received msgImHungry"));
		state.ns = NourishmentState.gotHungry;
		stateChanged();
	}
	
	public void msgYoureHired(String l, String r, int p, Map<Day,Time> startShifts, Map<Day,Time> endShifts) {
		job = new Job(l, r, p, startShifts, endShifts);
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
		for (ItemOrder i : items) {
			foodNeeded.add(i);
		}
		stateChanged();
	}

	public void msgExpense(int cost) {
		log.add(new LoggedEvent("Received msgExpense"));
		money -= cost;
		stateChanged();
	}

	public void msgReceivedItems(List<ItemOrder> items) {
		for (ItemOrder i : items) {
			groceries.add(i);
		}
		stateChanged();
	}

	public void msgBoughtCar(Car c) {
		car = c;
		stateChanged();
	}

	public void msgIncome(int cash) {
		money += cash;
		stateChanged();
	}

	public void msgDoneEating() {
		state.ns = NourishmentState.normal;
		stateChanged();
	}

	public void msgEndShift() {
		state.ws = WorkingState.notWorking;
		money += job.payrate;
		stateChanged();
	}
	
	public void msgCreatedAccount() {
		haveBankAccount = true;
	}

	public void msgRentDue() {
		rentDue = true;
		stateChanged();
	}
	
	public void msgBusIsHere(Bus b) {
        bus = b;
        stateChanged();
	}
	
	public void msgAtDestination(String d) {
		state.ts = TransportationState.walkingFromVehicle;
		destination = d;
		stateChanged();
	}
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if (state.ts == TransportationState.walking || state.ts == TransportationState.walkingFromVehicle) { 
			if (job != null && state.ws == WorkingState.notWorking && job.startShifts.get(time.getDay()) != job.endShifts.get(time.getDay()) && (time.plus(30)).greaterThanOrEqualTo(job.startShifts.get(time.getDay())) && !time.greaterThanOrEqualTo(job.endShifts.get(time.getDay()))) { //if half an hour before your shift starts
				if (state.ls == LocationState.home) {
					leaveHouse();
					return true;
				} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
					goToWork();
					print("goToWork");
					return true;
				}
			}
			if (state.ws == WorkingState.working && time.greaterThanOrEqualTo(job.endShifts.get(time.getDay()) ) && !job.role.equals("landlord")) { //if your shift ends
				endShift();
				print("endShift");
				return true;
			} 
			if (state.ws == WorkingState.notWorking) {
				if (money <= minBalance) {
					if (state.ls == LocationState.home) {
						leaveHouse();
						return true;
					} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
						goToBank();
						print("goToBank");
						return true;
					} 
				} 
				if (rentDue) {
					if (state.ls == LocationState.home) {
						leaveHouse();
						return true;
					} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
						goToOwnerHouse();
						print("goToOwnerHouse");
						return true;
					} 
				} 
				if (!foodNeeded.isEmpty()) {
					if (state.ls == LocationState.home) {
						leaveHouse();
						return true;
					} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
						goToMarket();
						print("goToMarket");
						return true;
					} 
				} 
				if (state.ns == NourishmentState.gotHungry) {
					if (haventEatenOutInAWhile()) {
						if (state.ls == LocationState.home) {
							leaveHouse();
							return true;
						} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
							goToRestaurant();
							print("goToRestaurant");
							return true;
						} 
					} else {
						if (state.ls == LocationState.home) {
							goEat();
							print("goEat");
							return true;
						} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
							goHome();
							print("goHome");
							return true;
						} 
					} 
				} 
				if (!groceries.isEmpty()) {
					if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
						goHome();
						print("goHome");
						return true;
					} 
				} 
				if (money >= maxBalance) {
					if (state.ls == LocationState.home) {
						leaveHouse();
						return true;
					} else if (state.ls == LocationState.outside || state.ls == LocationState.atDestination) {
			        	goToBank();
			        	print("goToBank");
						return true;
					} 
				} 
			} 
		}
		
		if (state.ts == TransportationState.waitingForBus && bus != null) {
			boardBus();
			print("boardBus");
			return true;
		}
		
		boolean anytrue = false;
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.active) {
					anytrue = mr.r.pickAndExecuteAnAction();
					print("roleScheduler");
					return anytrue;
				}
			}
		}
		
		/*if (state.ts == TransportationState.walking && state.ls != LocationState.home && state.ls != LocationState.leavingHouse) {
			goHome(); //if nothing left to do, go home and do whatever
			return true;
		}*/
		
		return false;
	}

	//Actions

	private void endShift() {
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.name.equals(job.role)) {
					if (mr.r instanceof JobInterface) {
						JobInterface j = (JobInterface)(mr.r);
						j.msgEndShift();
						money += job.payrate;
						state.ws = WorkingState.notWorking;
					}
					return;
				}
			}
		}
	}
	
	private void boardBus() {
		bus.msgComingAboard(this, destination);
		bus = null;
		state.ts = TransportationState.ridingBus;
	}

	private void goHome() {
		Housing h = houses.get(0); //person's house
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(h.location))) {
			goToDestination(h.location);
		} else {
			/*if (!findRole(h.residentRole)) {
				ResidentRole r = (ResidentRole)ResidentFactory(h.residentRole);
				addRole(r, h.residentRole);
			}*/
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(h.residentRole)) {
						mr.active = true;
						state.ls = LocationState.home;
						if (mr.r instanceof Resident) {
							Resident r = (Resident)(mr.r);
							r.msgImHome();
							if (!groceries.isEmpty()) {
								r.msgGroceries(groceries);
								groceries.clear();
							} 
							if (state.ns == NourishmentState.gotHungry) {
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
						mr.active = true;
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
			goToDestination(h.location);
		} else {
			/*if (!findRole(h.residentRole)) {
				ResidentRole r = (ResidentRole)ResidentFactory(h.residentRole);
				addRole(r, h.residentRole);
			} */
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(h.residentRole)) {
						if (mr.r instanceof Resident) {
							Resident r = (Resident)(mr.r);
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
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(r.location))) {
			goToDestination(r.location);
		} else {
			/*if (!findRole(r.customerRole)) {
				RestCustomerRole c = (RestCustomerRole)RestCustomerFactory(r.customerRole);
				addRole(c, r.customerRole);
			} */
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(r.customerRole)) {
						if (mr.r instanceof RestCustomer) {
							RestCustomer c = (RestCustomer)(mr.r);
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
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(m.location))) {
			goToDestination(m.location);
		} else {
			/*if (!findRole(m.customerRole)) {
				MarketCustomerRole c = (MarketCustomerRole)RoleFactory(m.customerRole);
				addRole(c, m.customerRole);
			} */
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(m.customerRole)) {
						if (mr.r instanceof MarketCustomer) {
							MarketCustomer c = (MarketCustomer)(mr.r);
							mr.active = true;
							state.ls = LocationState.market;
							c.msgOrderItems(foodNeeded);
							foodNeeded.clear();
						}
						return;
					}
				}
			}
		}
	}

	private void goToBank() {
		Bank b = chooseBank();
		if (state.ls != LocationState.atDestination || (destination != null && !destination.equals(b.location))) {
			goToDestination(b.location);
		} else {
			/*if (!findRole(b.depositorRole)) {
				DepositorRole d = (DepositorRole)DepositorFactory(b.depositorRole);
				addRole(d, b.depositorRole);
			} */
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(b.depositorRole)) {
						if (mr.r instanceof Depositor) {
							Depositor d = (Depositor)(mr.r);
							mr.active = true;
							state.ls = LocationState.bank;
							if (money >= maxBalance) {
								d.msgMakeDeposit(money-minBalance-(maxBalance-minBalance)/2);
							}
							if (money <= minBalance) {
								d.msgMakeWithdrawal(minBalance+(maxBalance-minBalance)/3-money);
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
			goToDestination(job.location);
		} else {
			synchronized(roles) {
				for (MyRole mr : roles) {
					if (mr.name.equals(job.role)) {
						if (mr.r instanceof JobInterface) {
							JobInterface j = (JobInterface)(mr.r);
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
		if (car != null && !nearDestination(d) && state.ts != TransportationState.walkingFromVehicle) {
			//DoGoToCar();
			state.ts = TransportationState.inCar;
			destination = d;
			car.msgGoToDestination(this, d); //pass the destination to the car so it knows where to go
		} else {
			if (takeBus(d) && state.ts != TransportationState.walkingFromVehicle) {
				BusStop b = closestBusStop();
				//DoGoToBusStop(b);
				state.ts = TransportationState.waitingForBus;
				destination = d;
				b.msgWaitingForBus(this);
			} else {
				state.ts = TransportationState.walking;
				destination = d;
				//DoGoToDestination(locations.get(d)); //just walk there
				state.ls = LocationState.atDestination;
			}
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
		FinancialState fs;
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
		public FinancialState getFState() {
			return fs;
		}
		public PhysicalState getPState() {
			return ps;
		}
	}

	public class Job {
		Job(String l, String r, int p, Map<Day,Time> startShifts, Map<Day,Time> endShifts) {
			location = l;
			role = r;
			payrate = p;
			switch (location) {
				case "joshRestaurant": case "cherysRestaurant": case "alfredRestaurant": case "anjaliRestaurant": case "jesusRestaurant":
					jobLocation = LocationState.restaurant;
					break;
				case "market1": case "market2": case "market3":
					jobLocation = LocationState.market;
					break;
				case "ownerHouse":
					jobLocation = LocationState.ownerHouse;
					break;
				case "bank":
					jobLocation = LocationState.bank;
					break;
				default:
					break;
			}
			this.startShifts = startShifts;
			this.endShifts = endShifts;
			/*Day tempDay = Day.Sun;
			for (Time t : startShifts.values()) {
				this.startShifts.put(tempDay, t);
				tempDay = tempDay.next();
			}
			tempDay = Day.Sun;
			for (Time t : endShifts.values()) {
				this.endShifts.put(tempDay, t);
				tempDay = tempDay.next();
			}*/
		}
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
		}
		String location;
		String type;
		String customerRole;
	}

	public class Market {
		Market(String l, String r) {
			location = l;
			customerRole = r;
		}
		String customerRole;
		String location;
	}


	public class Bank {
		Bank(String l, String r) {
			location = l;
			depositorRole = r;
		}
		String depositorRole;
		String location;
	}
}
