package simcity.joshrestaurant.test;

import java.util.ArrayList;
import java.util.List;

import simcity.ItemOrder;
import simcity.PersonAgent;
import simcity.joshrestaurant.JoshCashierRole.CheckState;
import simcity.joshrestaurant.JoshCookRole.FoodState;
import simcity.joshrestaurant.JoshCookRole;
import simcity.joshrestaurant.test.mock.MockMarketCashier;
import simcity.joshrestaurant.RevolvingStandMonitor;
import junit.framework.*;

public class CookTest extends TestCase
{
	//these are instantiated for each test separately via the setUp() method.
	PersonAgent cookP;
	JoshCookRole cook;
	MockMarketCashier cashier;
	RevolvingStandMonitor stand = new RevolvingStandMonitor();
	
	public void setUp() throws Exception{
		super.setUp();	
		cookP = new PersonAgent("cook");
		cook = new JoshCookRole();
		cook.setPerson(cookP);
		cashier = new MockMarketCashier("cashier");
		cook.addMarket(cashier);
		cook.setStand(stand);
		
		cook.msgStartShift();
	}	
	
	public void testNormativeScenario() {
		
		List<ItemOrder> orders = new ArrayList<ItemOrder>();
		orders.add(new ItemOrder("steak",2));
		orders.add(new ItemOrder("chicken",2));
		
		assertEquals("Cashier should have an empty event log. Instead, the Cashier's event log reads: "
				+ cashier.log.toString(), 0, cook.log.size());
		
		assertEquals("Cook should have an empty event log. Instead, the Cook's event log reads: "
				+ cook.log.toString(), 0, cook.log.size());
		
		assertEquals("Cook should have 0 item orders in it. It doesn't.", 0, cook.itemOrders.size());		
	
		assertTrue("Cook's scheduler should have returned true (it should call orderFoodFromMarket), but didn't.", cook.pickAndExecuteAnAction());
		
		assertTrue("Cook's steak should have state == Ordered. It doesn't.", cook.foods.get("steak").getState() == FoodState.Ordered);
		
		assertTrue("Cook's chicken should have state == Ordered. It doesn't.", cook.foods.get("chicken").getState() == FoodState.Ordered);
		
		assertTrue("Cashier should have logged \"Received msgIWantDelivery from cook. Location = joshRestaurant\" but didn't. His last event logged reads instead: " 
				+ cashier.log.getLastLoggedEvent().toString(), cashier.log.containsString("Received msgIWantDelivery from cook. Location = joshRestaurant"));
		
		assertEquals("Cook should have 0 item orders in it. It doesn't.", 0, cook.itemOrders.size());		
		
		cook.msgHereIsWhatICanFulfill(orders, true);
		
		assertTrue("Cook should have logged \"Received msgHereIsWhatICanFulfill\" but didn't. His last event logged reads instead: " 
				+ cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Received msgHereIsWhatICanFulfill"));
		
		assertTrue("Cook's steak should have state == WaitingForOrder. It doesn't.", cook.foods.get("steak").getState() == FoodState.WaitingForOrder);
		
		assertTrue("Cook's chicken should have state == WaitingForOrder. It doesn't.", cook.foods.get("chicken").getState() == FoodState.WaitingForOrder);
		
		assertFalse("Cook's scheduler should have returned false, but didn't.", cook.pickAndExecuteAnAction());
		
		cook.msgDelivery(orders);
		
		assertTrue("Cook should have logged \"Received msgDelivery\" but didn't. His last event logged reads instead: " 
				+ cook.log.getLastLoggedEvent().toString(), cook.log.containsString("Received msgDelivery"));
		
		assertTrue("Cook's steak should have state == ReceivedOrder. It doesn't.", cook.foods.get("steak").getState() == FoodState.ReceivedOrder);
		
		assertTrue("Cook's chicken should have state == ReceivedOrder. It doesn't.", cook.foods.get("chicken").getState() == FoodState.ReceivedOrder);
		
		assertEquals("Cook's steak should have an amount of 3. It doesn't.", 3, cook.foods.get("steak").getAmount());
		
		assertEquals("Cook's chicken should have an amount of 3. It doesn't.", 3, cook.foods.get("chicken").getAmount());
		
	}
}
