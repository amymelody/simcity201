package simcity.test;

import simcity.PersonAgent;
import simcity.Time;
import simcity.Day;
import simcity.ItemOrder;
import simcity.PersonAgent.LocationState;
import simcity.PersonAgent.NourishmentState;
import simcity.PersonAgent.TransportationState;
import simcity.PersonAgent.PhysicalState;
import simcity.PersonAgent.WorkingState;
import simcity.test.mock.MockRestCustomerRole;
import simcity.test.mock.MockBankDepositorRole;
import simcity.test.mock.MockJobRole;
import simcity.test.mock.MockMarketCustomerRole;
import simcity.test.mock.MockResidentRole;
import simcity.test.mock.MockBusStop;
import simcity.test.mock.MockBus;
import simcity.test.mock.MockCar;
import junit.framework.*;

import java.util.*;

public class PersonTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent person;
	MockResidentRole resident;
	MockRestCustomerRole customer;
	MockMarketCustomerRole marketCust;
	MockBankDepositorRole depositor;
	MockJobRole waiter;
	MockBusStop busStop;
	MockBus bus;
	MockCar car;
	
	public void setUp() throws Exception{
		super.setUp();		
		person = new PersonAgent("person");
		resident = new MockResidentRole("resident");
		customer = new MockRestCustomerRole("joshCustomer");
		marketCust = new MockMarketCustomerRole("market1Customer");
		depositor = new MockBankDepositorRole("depositor");
		waiter = new MockJobRole("waiter");
		busStop = new MockBusStop("busStop");
		bus = new MockBus("bus");
		car = new MockCar("car");
		person.msgIncome(400);
	}	
	
	public void testRestaurantCustomer()
	{
		person.addRole(customer, "joshCustomerRole");
		
		assertEquals("Person should have 1 role. It doesn't.", 1, person.roles.size());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Person should have an empty event log before the first message. Instead, the Person's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertEquals("Person's nourishment state should be normal. It isn't", NourishmentState.normal, person.state.getNState());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		person.msgImHungry();
		
		assertEquals("Person's nourishment state should be gotHungry. It isn't", NourishmentState.gotHungry, person.state.getNState());
		
		assertTrue("Person should have logged \"Received msgImHungry\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgImHungry"));
		
		assertTrue("Person's scheduler should have returned true (it should call goToRestaurant, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToRestaurant), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have 1 role. It doesn't.", person.roles.size(), 1);
		
		assertEquals("First role in roles should be active. It isn't.", true, person.roles.get(0).isActive());
		
		assertTrue("RestCustomer role should have logged \"Received gotHungry\" but didn't. His last event logged reads instead: " 
				+ customer.log.getLastLoggedEvent().toString(), customer.log.containsString("Received gotHungry"));
		
		assertEquals("Person's nourishment state should be hungry. It isn't", NourishmentState.hungry, person.state.getNState());
		
		assertEquals("Person's location state should be restaurant. It isn't", LocationState.restaurant, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call customer role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's nourishment state should be normal. It isn't", NourishmentState.normal, person.state.getNState());
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Person should have $384. It doesn't.", 384, person.getMoney());
		
		assertFalse("Person's scheduler should have returned false, but didn't.", person.pickAndExecuteAnAction());
		
	}
	
	public void testDepositor()
	{
		person.addRole(depositor, "bank1DepositorRole");
		
		assertEquals("Person should have 1 role. It doesn't.", 1, person.roles.size());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Person should have an empty event log before the first message. Instead, the Person's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		person.msgExpense(300);
		
		assertEquals("Person should have $100. It doesn't.", 100, person.getMoney());
		
		assertTrue("Person should have logged \"Received msgExpense\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgExpense"));
		
		assertTrue("Person's scheduler should have returned true (it should call goToBank, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToBank), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have 1 role. It doesn't.", person.roles.size(), 1);
		
		assertEquals("First role in roles should be active. It isn't.", true, person.roles.get(0).isActive());
		
		assertTrue("Depositor role should have logged \"Received msgMakeWithdrawal\" but didn't. His last event logged reads instead: " 
				+ depositor.log.getLastLoggedEvent().toString(), depositor.log.containsString("Received msgMakeWithdrawal"));
		
		assertEquals("Person's location state should be bank. It isn't", LocationState.bank, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call depositor role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertFalse("Person's scheduler should have returned false, but didn't.", person.pickAndExecuteAnAction());
		
	}
	
	public void testGoToWorkByBus()
	{
		person.addRole(waiter, "restWaiterRole");
		person.setPState(PhysicalState.lazy);
		person.addBusStop(busStop);
		
		Map<Day, Time> startShifts = new HashMap<Day, Time>();
		startShifts.put(Day.Mon, new Time(Day.Mon, 9, 0));
		
		Map<Day, Time> endShifts = new HashMap<Day, Time>();
		endShifts.put(Day.Mon, new Time(Day.Mon, 17, 0));
		
		person.msgYoureHired("joshRestaurant", "restWaiterRole", 80, startShifts, endShifts);
		
		assertEquals("Person should have 1 role. It doesn't.", 1, person.roles.size());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Person should have an empty event log before the first message. Instead, the Person's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("Person's transportation state should be walking. It isn't", TransportationState.walking, person.state.getTState());
		
		person.msgUpdateWatch(Day.Mon, 8, 30);
		
		assertTrue("Person should have logged \"Received msgUpdateWatch\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgUpdateWatch"));
		
		assertTrue("Person's scheduler should have returned true (it should call goToWork, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's transportation state should be waitingForBus. It isn't", TransportationState.waitingForBus, person.state.getTState());
		
		assertTrue("Person's destination should be joshRestaurant. Instead it is " + person.getDestination(), person.getDestination().equals("joshRestaurant"));
		
		assertTrue("BusStop should have logged \"Received msgWaitingForBus\" but didn't. Its last event logged reads instead: " 
				+ busStop.log.getLastLoggedEvent().toString(), busStop.log.containsString("Received msgWaitingForBus"));
		
		assertFalse("Person's scheduler should have returned false, but didn't.", person.pickAndExecuteAnAction());
		
		person.msgBusIsHere(bus);
		
		assertTrue("Person's scheduler should have returned true (it should call boardBus), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's transportation state should be ridingBus. It isn't", TransportationState.ridingBus, person.state.getTState());
		
		assertTrue("Bus should have logged \"Received msgComingAboard. Destination = joshRestaurant\" but didn't. Its last event logged reads instead: " 
				+ bus.log.getLastLoggedEvent().toString(), bus.log.containsString("Received msgComingAboard. Destination = joshRestaurant"));
		
		assertFalse("Person's scheduler should have returned false, but didn't.", person.pickAndExecuteAnAction());
		
		person.msgAtDestination("joshRestaurant");
		
		assertTrue("Person's destination should be joshRestaurant. Instead it is " + person.getDestination(), person.getDestination().equals("joshRestaurant"));
		
		assertEquals("Person's transportation state should be walkingFromVehicle. It isn't", TransportationState.walkingFromVehicle, person.state.getTState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToWork, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToWork), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have 1 role. It doesn't.", person.roles.size(), 1);
		
		assertEquals("First role in roles should be active. It isn't.", true, person.roles.get(0).isActive());
		
		assertTrue("Waiter role should have logged \"Received msgStartShift\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgStartShift"));
		
		assertEquals("Person's location state should be restaurant. It isn't", LocationState.restaurant, person.state.getLState());
		
		assertEquals("Person's working state should be working. It isn't", WorkingState.working, person.state.getWState());
		
		assertEquals("First role in roles should be active. It isn't.", true, person.roles.get(0).isActive());
		
		assertTrue("Person's scheduler should have returned true (it should call waiter role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertTrue("Waiter role should have logged \"Ran scheduler\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Ran scheduler"));
		
		person.msgUpdateWatch(Day.Mon, 17, 0);
		
		assertTrue("Person should have logged \"Received msgUpdateWatch\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgUpdateWatch"));
		
		assertTrue("Person's scheduler should have returned true (it should call endShift), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have $480. It doesn't.", 480, person.getMoney());
		
		assertEquals("Person's working state should be notWorking. It isn't", WorkingState.notWorking, person.state.getWState());
		
		assertTrue("Waiter role should have logged \"Received msgEndShift\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgEndShift"));
		
		assertTrue("Person's scheduler should have returned true (it should call waiter role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertTrue("Waiter role should have logged \"Ran scheduler\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Ran scheduler"));
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("First role in roles should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertFalse("Person's scheduler should have returned false, but didn't.", person.pickAndExecuteAnAction());
		
	}
	
	public void testDriveToMarket()
	{
		person.addRole(resident, "residentRole");
		person.addRole(marketCust, "market1CustomerRole");
		person.msgBoughtCar(car);
		person.setLState(LocationState.home);
		List<ItemOrder> foods = new ArrayList<ItemOrder>();
		foods.add(new ItemOrder("steak", 2));
		foods.add(new ItemOrder("chicken", 3));
		
		assertEquals("Person should have 2 roles. It doesn't.", 2, person.roles.size());
		
		assertEquals("Person's foodNeeded should be empty. It isn't.", 0, person.foodNeeded.size());
		
		assertEquals("Person's groceries should be empty. It isn't.", 0, person.groceries.size());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("MarketCust role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Person should have an empty event log before the first message. Instead, the Person's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertEquals("Person's location state should be home. It isn't", LocationState.home, person.state.getLState());
		
		assertEquals("Person's transportation state should be walking. It isn't", TransportationState.walking, person.state.getTState());
		
		person.msgFoodLow(foods);
		
		assertTrue("Person should have logged \"Received msgFoodLow\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgFoodLow"));
		
		assertEquals("Person's foodNeeded should have 2 items. It doesn't.", 2, person.foodNeeded.size());
		
		assertTrue("Person's scheduler should have returned true (it should call leaveHouse), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be leavingHouse. It isn't", LocationState.leavingHouse, person.state.getLState());
		
		assertEquals("Resident role should be active. It isn't.", true, person.roles.get(0).isActive());

		assertEquals("MarketCust role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertTrue("Resident role should have logged \"Received msgLeave\" but didn't. His last event logged reads instead: " 
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgLeave"));
		
		assertTrue("Person's scheduler should have returned true (it should call resident role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("MarketCust role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToMarket, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's transportation state should be inCar. It isn't", TransportationState.inCar, person.state.getTState());
		
		assertTrue("Person's destination should be market1. Instead it is " + person.getDestination(), person.getDestination().equals("market1"));
		
		assertTrue("Car should have logged \"Received msgGoToDestination. Destination = market1\" but didn't. Its last event logged reads instead: " 
				+ car.log.getLastLoggedEvent().toString(), car.log.containsString("Received msgGoToDestination. Destination = market1"));
		
		person.msgAtDestination("market1");
		
		assertTrue("Person's destination should be market1. Instead it is " + person.getDestination(), person.getDestination().equals("market1"));
		
		assertEquals("Person's transportation state should be walkingFromVehicle. It isn't", TransportationState.walkingFromVehicle, person.state.getTState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToMarket, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToMarket), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("MarketCust role should be active. It isn't.", true, person.roles.get(1).isActive());
		
		assertEquals("Person's location state should be market. It isn't", LocationState.market, person.state.getLState());
		
		assertEquals("Person's foodNeeded should be empty. It isn't.", 0, person.foodNeeded.size());
		
		assertTrue("MarketCust role should have logged \"Received msgOrderItems\" but didn't. His last event logged reads instead: " 
				+ marketCust.log.getLastLoggedEvent().toString(), marketCust.log.containsString("Received msgOrderItems"));
		
		assertTrue("Person's scheduler should have returned true (it should call marketCust role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertTrue("Person should have logged \"Received msgReceivedItems\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgReceivedItems"));
		
		assertTrue("Person should have logged \"Received msgExpense\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgExpense"));
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Person's groceries should have 2 items. It doesn't.", 2, person.groceries.size());
		
		assertEquals("Person should have $370. It doesn't.", 370, person.getMoney());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("MarketCust role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goHome, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's transportation state should be inCar. It isn't", TransportationState.inCar, person.state.getTState());
		
		assertTrue("Person's destination should be home. Instead it is " + person.getDestination(), person.getDestination().equals("home"));
		
		assertTrue("Car should have logged \"Received msgGoToDestination. Destination = market1\" but didn't. Its last event logged reads instead: " 
				+ car.log.getLastLoggedEvent().toString(), car.log.containsString("Received msgGoToDestination. Destination = home"));
		
		person.msgAtDestination("home");
		
		assertTrue("Person's destination should be home. Instead it is " + person.getDestination(), person.getDestination().equals("home"));
		
		assertEquals("Person's transportation state should be walkingFromVehicle. It isn't", TransportationState.walkingFromVehicle, person.state.getTState());
		
		assertTrue("Person's scheduler should have returned true (it should call goHome, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goHome), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Resident role should be active. It isn't.", true, person.roles.get(0).isActive());
		
		assertEquals("MarketCust role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Person's location state should be home. It isn't", LocationState.home, person.state.getLState());
		
		assertEquals("Person's groceries should be empty. It isn't.", 0, person.groceries.size());
		
		assertTrue("Resident role should have logged \"Received msgGroceries\" but didn't. His last event logged reads instead: " 
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgGroceries"));
	}
	
	public void testGoToBankRentDueAndGoToWork()
	{
		person.addRole(resident, "residentRole");
		person.addRole(depositor, "bank1DepositorRole");
		person.addRole(waiter, "restWaiterRole");
		
		Map<Day, Time> startShifts = new HashMap<Day, Time>();
		startShifts.put(Day.Mon, new Time(Day.Mon, 9, 0));
		
		Map<Day, Time> endShifts = new HashMap<Day, Time>();
		endShifts.put(Day.Mon, new Time(Day.Mon, 17, 0));
		
		person.msgYoureHired("joshRestaurant", "restWaiterRole", 80, startShifts, endShifts);
		
		assertEquals("Person should have 3 roles. It doesn't.", 3, person.roles.size());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be inactive. It isn't.", false, person.roles.get(2).isActive());
		
		assertEquals("Person should have an empty event log before the first message. Instead, the Person's event log reads: "
				+ person.log.toString(), 0, person.log.size());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		person.msgExpense(350);
		
		assertEquals("Person should have $50. It doesn't.", 50, person.getMoney());
		
		assertTrue("Person should have logged \"Received msgExpense\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgExpense"));
		
		assertTrue("Person's scheduler should have returned true (it should call goToBank, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		person.setRentDue(true);
		
		assertTrue("Person's scheduler should have returned true (it should call goToBank), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be active. It isn't.", true, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be inactive. It isn't.", false, person.roles.get(2).isActive());
		
		assertTrue("Depositor role should have logged \"Received msgMakeWithdrawal\" but didn't. His last event logged reads instead: " 
				+ depositor.log.getLastLoggedEvent().toString(), depositor.log.containsString("Received msgMakeWithdrawal"));
		
		assertEquals("Person's location state should be bank. It isn't", LocationState.bank, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call depositor role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have $400. It doesn't.", 400, person.getMoney());
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be inactive. It isn't.", false, person.roles.get(2).isActive());
		
		assertTrue("Person's scheduler should have returned true (it should call goToOwnerHouse, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		person.msgUpdateWatch(Day.Mon, 8, 30);
		
		assertTrue("Person should have logged \"Received msgUpdateWatch\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgUpdateWatch"));
		
		assertTrue("Person's scheduler should have returned true (it should call goToWork, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToWork), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be active. It isn't.", true, person.roles.get(2).isActive());
		
		assertTrue("Waiter role should have logged \"Received msgStartShift\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgStartShift"));
		
		assertEquals("Person's location state should be restaurant. It isn't", LocationState.restaurant, person.state.getLState());
		
		assertEquals("Person's working state should be working. It isn't", WorkingState.working, person.state.getWState());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be active. It isn't.", true, person.roles.get(2).isActive());
		
		assertTrue("Person's scheduler should have returned true (it should call waiter role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertTrue("Waiter role should have logged \"Ran scheduler\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Ran scheduler"));
		
		person.msgUpdateWatch(Day.Mon, 17, 0);
		
		assertTrue("Person should have logged \"Received msgUpdateWatch\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgUpdateWatch"));
		
		assertTrue("Person's scheduler should have returned true (it should call endShift), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have $480. It doesn't.", 480, person.getMoney());
		
		assertEquals("Person's working state should be notWorking. It isn't", WorkingState.notWorking, person.state.getWState());
		
		assertTrue("Waiter role should have logged \"Received msgEndShift\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Received msgEndShift"));
		
		assertTrue("Person's scheduler should have returned true (it should call waiter role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertTrue("Waiter role should have logged \"Ran scheduler\" but didn't. His last event logged reads instead: " 
				+ waiter.log.getLastLoggedEvent().toString(), waiter.log.containsString("Ran scheduler"));
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be inactive. It isn't.", false, person.roles.get(2).isActive());
		
		assertTrue("Person's scheduler should have returned true (it should call goToOwnerHouse, which then calls goToDestination), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person's location state should be atDestination. It isn't", LocationState.atDestination, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call goToOwnerHouse), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Resident role should be active. It isn't.", true, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be inactive. It isn't.", false, person.roles.get(2).isActive());
		
		assertTrue("Resident role should have logged \"Received msgAtLandlord\" but didn't. His last event logged reads instead: " 
				+ resident.log.getLastLoggedEvent().toString(), resident.log.containsString("Received msgAtLandlord"));
		
		assertEquals("Person's location state should be ownerHouse. It isn't", LocationState.ownerHouse, person.state.getLState());
		
		assertTrue("Person's scheduler should have returned true (it should call resident role's scheduler), but didn't.", person.pickAndExecuteAnAction());
		
		assertEquals("Person should have $380. It doesn't.", 380, person.getMoney());
		
		assertTrue("Person should have logged \"Received msgLeftDestination\" but didn't. His last event logged reads instead: " 
				+ person.log.getLastLoggedEvent().toString(), person.log.containsString("Received msgLeftDestination"));
		
		assertEquals("Person's location state should be outside. It isn't", LocationState.outside, person.state.getLState());
		
		assertEquals("Resident role should be inactive. It isn't.", false, person.roles.get(0).isActive());
		
		assertEquals("Depositor role should be inactive. It isn't.", false, person.roles.get(1).isActive());
		
		assertEquals("Waiter role should be inactive. It isn't.", false, person.roles.get(2).isActive());
		
		person.setRentDue(false);
		
		assertFalse("Person's scheduler should have returned false, but didn't.", person.pickAndExecuteAnAction());
	}
}
