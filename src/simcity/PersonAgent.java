package simcity;

import java.util.ArrayList;
import java.util.Collections;
import java.awt.Point;

import agent.Agent;
import role.Role;
import role.JobRole;

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

	public List<BusStopAgent> busStops;
	public List<ItemOrder> foodNeeded = Collections.synchronizedList(new ArrayList<ItemOrder>());
	public List<ItemOrder> groceries = Collections.synchronizedList(new ArrayList<ItemOrder>());

	public List<MyRole> roles = Collections.synchronizedList(new ArrayList<MyRole>());
	public List<Restaurant> restaurants = Collections.synchronizedList(new ArrayList<Restaurant>());
	public List<Market> markets = Collections.synchronizedList(new ArrayList<Market>());
	public List<Bank> banks = Collections.synchronizedList(new ArrayList<Bank>());
	public List<Housing> houses = Collections.synchronizedList(new ArrayList<Housing>());
	public Map<String, List> places;

	private Job job;
	private Day day;
	private Time time;

	public enum NourishmentState {unknown, normal, gotHungry, hungry, full};
	public enum LocationState {unknown, outside, leavingHouse, home, ownerHouse, restaurant, market, bank};
	public enum WorkingState {unknown, notWorking, working};
	public enum Day {Sun, Mon, Wed, Tue, Thu, Fri, Sat};

	PersonState state;

	//utilities
	public class MyRole {
		MyRole(Role role) {
			r = role;
			active = false;
		}
		Role r;
		boolean active;
	}

	public class Time {
		int hour;
		int minute;
	}

	public class PersonState {
		NourishmentState ns;
		LocationState ls;
		WorkingState ws;
	}

	public class Job {
		String role;
		Point location;
		Map<Day, Time> startShifts;
		Map<Day, Time> endShifts;
		int paycheck;
	}

	public class Housing {
		String residentRole;
		Point location;
	}

	public class Restaurant {
		Point location;
		Menu m;
		String type;
		String customerRole;
	}

	public class Market {
		String customerRole;
		Point location;
	}


	public class Bank {
		String depositorRole;
		Point location;
	}

	//Messages

	public void msgUpdateWatch(Day d, int h, int m) {
		day = d;
		time.hour = h;
		time.minute = m;
		stateChanged();
	}

	public void msgImHungry() {
		state.ns = NourishmentState.gotHungry;
		stateChanged();
	}

	public void msgLeftDestination(Role r) {
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == r) {
						mr.active = false;
						state.ls = LocationState.outside;
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
		money += job.paycheck;
		stateChanged();
	}
	
	public void msgCreatedAccount() {
		haveBankAccount = true;
	}

	public void msgRentDue() {
		rentDue = true;
		stateChanged();
	}
	
	//Scheduler
	
	public boolean pickAndExecuteAnAction() {
		if (job != null & state.ws == WorkingState.notWorking & time+30 >= job.startShifts.get(day)) { //if half an hour before your shift starts
			if (state.ls == LocationState.home) {
				leaveHouse();
				return true;
			} else if (state.ls == LocationState.outside) {
				goToWork();
				return true;
			}
		}
		if (state.ws == WorkingState.working & time >= job.endShifts.get(day)) { //if your shift ends
			endShift();
			return true;
		} 
		if (state.ws == WorkingState.notWorking) {
			if (rentDue) {
				if (state.ls == LocationState.home) {
					leaveHouse();
					return true;
				} else if (state.ls == LocationState.outside) {
					goToOwnerHouse();
					return true;
				} 
			} 
			if (money <= minBalance & haveBankAccount) {
				if (state.ls == LocationState.home) {
					leaveHouse();
					return true;
				} else if (state.ls == LocationState.outside) {
					goToBank();
					return true;
				} 
			} 
			if (state.ns == NourishmentState.gotHungry) {
				if (haventEatenOutInAWhile()) {
					if (state.ls == LocationState.home) {
						leaveHouse();
						return true;
					} else if (state.ls == LocationState.outside) {
						goToRestaurant();
						return true;
					} 
				} else {
					if (state.ls == LocationState.home) {
						goEat();
						return true;
					} else if (state.ls == LocationState.outside) {
						goHome();
						return true;
					} 
				} 
			} 
			if (!foodNeeded.isEmpty()) {
				if (state.ls == LocationState.home) {
					leaveHouse();
					return true;
				} else if (state.ls == LocationState.outside) {
					goToMarket();
					return true;
				} 
			} 
			if (!groceries.isEmpty()) {
				if (state.ls == LocationState.outside) {
					goHome();
					return true;
				} 
			} 
			if (money >= maxBalance) {
				if (state.ls == LocationState.home) {
					leaveHouse();
					return true;
				} else if (state.ls == LocationState.outside) {
		        	goToBank();
					return true;
				} 
			} 
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
		
		if (state.ls != LocationState.home) {
			goHome(); //if nothing left to do, go home and do whatever
			return true;
		}
	}

	//Actions

	private void addRole(Role r) {
		r.setPerson(this);
		roles.add(new MyRole(r));
	}

	private void endShift() {
		money += job.paycheck;
		state.ws = WorkingState.notWorking;
	}

	private void goHome() {
		Housing h = houses.get(0); //person's house
		goToDestination(h.location);
		ResidentRole r = RoleFactory(h.residentRole);
		if (!findResidentRole(r)) {
			addRole(r);
		}
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == r) {
					mr.active = true;
					state.ls = LocationState.home;
					r.msgImHome();
					if (!groceries.isEmpty()) {
						r.msgGroceries(groceries);
						groceries.clear();
					} 
					if (state.ns == NourishmentState.gotHungry) {
						r.msgEat();
						state.ns = NourishmentState.hungry;
					} 
					return;
				}
			}
		}
	}
	
	private void goEat() {
		Housing h = houses.get(0); //person's house
		ResidentRole r = RoleFactory(h.residentRole);
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == r) {
					mr.active = true;
	                r.msgEat();
	                state.ns = NourishmentState.hungry;
					return;
				}
			}
		}       
	}

	private void leaveHouse() {
		Housing h = houses.get(0); //person's house
		ResidentRole r = RoleFactory(h.residentRole);
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == r) {
					r.msgLeave();
					state.ls = LocationState.leavingHouse;
					return;
				}
			}
		} 
	}

	private void goToOwnerHouse() {
		Housing h = houses.get(0); //owner's house
		goToDestination(h.location);
		ResidentRole r = RoleFactory(h.residentRole);
		if (!findResidentRole(r)) {
			addRole(r);
		} 
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == r) {
					mr.active = true;
					state.ls = LocationState.ownerHouse;
					r.msgAtLandlord();
					return;
				}
			}
		} 
	}

	private void goToRestaurant() {
		Restaurant r = restaurants.chooseOne();
		goToDestination(r.location);
		RestCustomerRole c = RoleFactory(r.customerRole);
		if (!findRestCustomerRole(c)) {
			addRole(c);
		} 
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == c) {
					mr.active = true;
					state.ls = LocationState.restaurant;
					c.msgGotHungry();
					state.ns = NourishmentState.hungry;
					return;
				}
			}
		}	
	}

	private void goToMarket() {
		Market m = markets.chooseOne();
		goToDestination(m.location);
		MarketCustomerRole c = RoleFactory(m.customerRole);
		if (!findMarketCustomerRole(c)) {
			addRole(c);
		} 
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == c) {
					mr.active = true;
					state.ls = LocationState.market;
					c.msgOrderItems(foodNeeded);
					foodNeeded.clear();
					return;
				}
			}
		}
	}

	private void goToBank() {
		Bank b = banks.chooseOne();
		goToDestination(b.location);
		DepositorRole d = RoleFactory(b.depositorRole);
		if (!findDepositorRole(d)) {
			addRole(d);
		} 
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == d) {
					mr.active = true;
					state.ls = LocationState.bank;
					if (money >= maxBalance) {
						d.msgMakeDeposit();
					}
					if (money <= minBalance) {
						d.msgMakeWithdrawal();
					}
					return;
				}
			}
		}
	}

	private void goToWork() {
		goToDestination(job.location);
		JobRole r = RoleFactory(job.role);
		synchronized(roles) {
			for (MyRole mr : roles) {
				if (mr.r == r) {
					mr.active = true;
					r.msgStartShift();
					state.ws = WorkingState.working;
					return;
				}
			}
		}
	}

	private void goToDestination(Point p) {
		if (car != null & !nearDestination(p)) {
			DoGoToCar();
			car.gotoDestination(p); //pass the destination to the car so it knows where to go
			DoGoToDestination(p); //after car has reached destination
		} else {
			if (!nearDestination(p)) {
				BusStop b = closestBusStop();
				DoGoToBusStop(b);
				b.msgWaitingForBus(this);
				DoGoToDestination(p); //after getting off the bus
			} else {
				DoGoToDestination(p); //just walk there
			}
		}
	}
}
