package simcity.market.test.mock;

import java.util.List;
import java.util.Map;

import simcity.Day;
import simcity.ItemOrder;
import simcity.PersonAgent.LocationState;
import simcity.PersonAgent.PhysicalState;
import simcity.Time;
import simcity.interfaces.Bus;
import simcity.interfaces.Car;
import simcity.interfaces.Person;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;
import simcity.role.Role;

public class MockPerson extends Mock implements Person {

	/**
	 * References for unit testing
	 */
	public List<ItemOrder> items;
	public String name;

	public MockPerson(String n) {
		super(n);

		name = n;
	}

	public void setItems(List<ItemOrder> i) {
		items = i;
	}

	@Override
	public void msgAtDestination() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("At destination"));
	}

	@Override
	public void msgUpdateWatch(Day d, int h, int m) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Watch updated"));
	}

	@Override
	public void msgImHungry() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("I'm hungry"));
	}

	@Override
	public void msgYoureHired(String role, int payrate,
			Map<Day, Time> startShifts, Map<Day, Time> endShifts) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Hired"));
	}
	
	@Override
	public void msgGoodGuyAgain()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYoureHired(String jobLocation, String role, int payrate,
			Map<Day, Time> startShifts, Map<Day, Time> endShifts) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Hired"));
	}

	@Override
	public void msgLeftDestination(Role r) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Left destination"));
	}

	@Override
	public void msgFoodLow(List<ItemOrder> items) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Food low"));
	}

	@Override
	public void msgExpense(int cost) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Spending money"));
	}

	@Override
	public void msgReceivedItems(List<ItemOrder> items) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Got items"));
	}

	@Override
	public void msgIncome(int cash) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("More money for me"));
	}

	@Override
	public void msgDoneEating() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Bloated"));
	}

	@Override
	public void msgEndShift() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Shift is done"));
	}

	@Override
	public void msgCreatedAccount() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Account created"));
	}

	@Override
	public void msgBusIsHere(Bus b) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Bus is here"));
	}

	@Override
	public void msgAtDestination(String d) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("At destination"));
	}

	@Override
	public void setRentDue(boolean b) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Rent due sent"));
	}

	@Override
	public void setPState(PhysicalState ps) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Physical state set"));
	}

	@Override
	public void setLState(LocationState ls) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Location state set"));
	}

	@Override
	public void setEState(String es) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("E state set"));
	}

	@Override
	public void setPState(String ps) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("P state set"));
	}

	@Override
	public void addRole(Role r, String n) {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("Role added"));
	}

	@Override
	public int getMoney() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getJob() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDestination() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean businessOpen(String b)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void businessIsClosed(String b, boolean c)
	{
	}

	@Override
	public void stateChanged() {
		// TODO Auto-generated method stub
		log.add(new LoggedEvent("State changed"));
	}

	@Override
	public String getHome() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSalary() {
		// TODO Auto-generated method stub
		return 0;
	}

}