package simcity.interfaces;

import java.util.List;
import java.util.Map;

import simcity.Day;
import simcity.ItemOrder;
import simcity.RestWaiterRole;
import simcity.Time;
import simcity.PersonAgent.EconomicState;
import simcity.PersonAgent.Job;
import simcity.PersonAgent.LocationState;
import simcity.PersonAgent.MyRole;
import simcity.PersonAgent.NourishmentState;
import simcity.PersonAgent.PhysicalState;
import simcity.PersonAgent.TransportationState;
import simcity.PersonAgent.WorkingState;
import simcity.mock.LoggedEvent;
import simcity.role.JobRole;
import simcity.role.Role;

public interface Person {

	public abstract void msgAtDestination();
	
	public abstract void msgUpdateWatch(Day d, int h, int m);

	public abstract void msgImHungry();
	
	public abstract void msgYoureHired(String role, int payrate, Map<Day,Time> startShifts, Map<Day,Time> endShifts);
	
	public abstract void msgYoureHired(String jobLocation, String role, int payrate, Map<Day,Time> startShifts, Map<Day,Time> endShifts);

	public abstract void msgLeftDestination(Role r);

	public abstract void msgFoodLow(List<ItemOrder> items);

	public abstract void msgExpense(int cost);

	public abstract void msgReceivedItems(List<ItemOrder> items);

	public abstract void msgIncome(int cash);

	public abstract void msgDoneEating();

	public abstract void msgEndShift();
	
	public abstract void msgCreatedAccount();
	
	public abstract void msgGoodGuyAgain();
	
	public abstract void msgBusIsHere(Bus b);
	
	public abstract void msgAtDestination(String d);
	
	public abstract void setRentDue(boolean b);
	
	public abstract void setPState(PhysicalState ps);
	
	public abstract void setLState(LocationState ls);
	
	public abstract void setEState(String es);
	
	public abstract void setPState(String ps);
	
	public abstract void addRole(Role r, String n);
	
	public abstract String getName();
	
	public abstract int getMoney();
	
	public abstract String getJob();
	
	public abstract String getHome();
	
	public abstract int getSalary();
	
	public abstract String getDestination();
	
	public abstract boolean businessOpen(String b);
	
	public abstract void businessIsClosed(String b, boolean c);
	
	public abstract void stateChanged();
}