package simcity.alfredrestaurant;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import simcity.alfredrestaurant.gui.AlfredCookerGui;
import simcity.agent.Agent;

public class AlfredCookRole extends Agent {
	Timer timer = new Timer();
	private AlfredHostRole hostAgent;
	private AlfredInventory inventory = new AlfredInventory();
	private final int ORDER_QUANTITY = 10;
	private final int INVENTORY_SLEEPING = 5000;
	private AlfredCookerGui cookGui;
	
	
	//references to MarketAgent objects, they are created in Restaurant Panel
	private Vector<AlfredMarketRole> markets = new Vector<AlfredMarketRole>();

	public enum AgentState {

		DoingNothing, GoingToReferigerator, GettingFood, GoingToCookingArea, Cooking, GoingToPlatingArea
		, GoingToDoNothingArea, GoingToCookingAreaToGetReadyFood
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {

		none, gotOrder, comeReferigerator, gotFood, gotNoFood, comeCookingArea, doneCooking, comePlatingArea, 
		comeDoNothingArea, comeCookingAreaToGetFood
		
	};

	AgentEvent event = AgentEvent.none;
	
	public CookAgent(AlfredHostRole hostAgent) {
		this.hostAgent = hostAgent;
		InventoryThread inventoryThread = new InventoryThread();
		inventoryThread.start();
	}
	
	public List<QueueItem> orderQueue = Collections.synchronizedList(new LinkedList<AlfredCookRole.QueueItem>());
	public List<QueueItem> cookingQueue = new LinkedList<AlfredCookRole.QueueItem>();
	public List<QueueItem> readyQueue = new LinkedList<AlfredCookRole.QueueItem>();
	public List<QueueItem> platingQueue = new LinkedList<AlfredCookRole.QueueItem>();
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if (state == AgentState.DoingNothing && event == AgentEvent.gotOrder){
			state = AgentState.GoingToReferigerator;
			cookGui.msgGoToRefrigerator();
			return true;
		}
		if (state == AgentState.GoingToReferigerator && event == AgentEvent.comeReferigerator){
			state = AgentState.GettingFood;
			getFoodFromRefrigerator();
			return true;
		}
		if (state == AgentState.GettingFood && event == AgentEvent.gotFood){
			
			state = AgentState.GoingToCookingArea;
			cookGui.msgGoToCookingArea();	
			return true;
		}
		if (state == AgentState.GoingToCookingArea && event == AgentEvent.comeCookingArea){
			if (!orderQueue.isEmpty()){
				cookingQueue.add(orderQueue.remove(0));
				state = AgentState.Cooking;
				cooking();
			}			
			state = AgentState.DoingNothing; //don't know what to do, need to call try to do cooking method
			tryToDoCooking();
			return true;
		}
		
		if (state == AgentState.GoingToCookingAreaToGetReadyFood && event == AgentEvent.comeCookingArea){
			if (!readyQueue.isEmpty()){
				platingQueue.add(readyQueue.remove(0));
				state = AgentState.GoingToPlatingArea;
				cookGui.msgGoToPlatingArea();
			}
			
			return true;
		}
		//DONE COOKING
		if (event == AgentEvent.doneCooking){
			if (!cookingQueue.isEmpty()){
				readyQueue.add(cookingQueue.remove(0));
				
				if (state == AgentState.DoingNothing){
					platingQueue.add(readyQueue.remove(0));
					state = AgentState.GoingToPlatingArea;
					cookGui.msgGoToPlatingArea();	
				}
			}
			
			return true;
		}
		if (state == AgentState.GoingToPlatingArea && event == AgentEvent.comePlatingArea){
			//put food for waiter to get it
			if (!platingQueue.isEmpty()){
				QueueItem item = platingQueue.remove(0);
				item.waiter.msgOrderIsReady();				
			}
			state = AgentState.DoingNothing; //don't know what to do, need to call try to do cooking method
			tryToDoCooking();
			return true;
		}
		if (state == AgentState.GoingToDoNothingArea && event == AgentEvent.comeDoNothingArea){
			//go to refrigerator if having order or go to do nothing area 
			state = AgentState.DoingNothing;
			tryToDoCooking();//don't know what to do, need to call try to do cooking method
			return true;
		}
		return false;
	}
	
	private void tryToDoCooking(){
		//go to cooking area to get food, go to refrigerator if having order or go to do nothing area 
		if (!readyQueue.isEmpty()){
			state = AgentState.GoingToCookingAreaToGetReadyFood;
			cookGui.msgGoToCookingArea();	
		}else if (!orderQueue.isEmpty()){
			state = AgentState.GoingToReferigerator;
			cookGui.msgGoToRefrigerator();
		}
		else if (state == AgentState.DoingNothing){
			if (!cookGui.isAtDoNothingArea() && readyQueue.isEmpty() && cookingQueue.isEmpty() && orderQueue.isEmpty()){
				state = AgentState.GoingToDoNothingArea;
				cookGui.msgGoToNoCommandArea();
			}
		}
		else{
			if (!cookGui.isAtCookingArea()){
				//go to cooking area to wait there
				cookGui.msgGoToCookingArea();
			}			
		}
	}
	
	private void cooking(){
		System.out.println("cooking bill..");
		timer.schedule(new TimerTask() {
			public void run() {
				event = AgentEvent.doneCooking;
				stateChanged();
			}
		}, 10000);	//take 10 seconds to cook
		
	}
	private void getFoodFromRefrigerator(){
		
		event = AgentEvent.gotFood;
		stateChanged();
	}
	
	public void msgOrder(AlfredWaiterRole waiter, String food) {
		boolean hasFood = inventory.getItem(food);
		if (hasFood){
			synchronized (orderQueue) {
				orderQueue.add(new QueueItem(waiter, food));
				orderQueue.notifyAll();
			}
			
			event = AgentEvent.gotOrder;
			stateChanged();
		}else{
			waiter.msgOrderIsOutOfStock();
		}
		
	}	
	
	public void msgComeRefrigerator(){
		event = AgentEvent.comeReferigerator;
		stateChanged();
	}
	public void msgComeCookingArea(){
		event = AgentEvent.comeCookingArea;
		stateChanged();
	}
	public void msgComePlatingArea(){
		event = AgentEvent.comePlatingArea;
		stateChanged();
	}
	public void msgComeDoNothingArea(){
		event = AgentEvent.comeDoNothingArea;
		stateChanged();
	}
	public void addMarket(AlfredMarketRole m){
		markets.add(m);
	}
	
	class QueueItem{
		AlfredWaiterRole waiter;
		String food;
		QueueItem(AlfredWaiterRole waiter, String food){
			this.waiter = waiter;
			this.food = food;
		}
	}
	
	//call by inventory thread
	private void marketOrderItem(String item, int quantity){
		synchronized (markets) {
			for (AlfredMarketRole m: markets){
				if (m.isAvailable()){
					m.order(item, quantity);					
				}
			}
			markets.notifyAll();
		}
	}
	
	class InventoryThread extends Thread{
		public InventoryThread(){
			
		}
		@Override
		public void run(){
			System.out.println("Inventory thread started");
			//run forever
			while(true){
				try {
					sleep(INVENTORY_SLEEPING); //sleep some seconds
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("Inventory thread check low inventory");
				Vector<String> itemsToOrder;
				synchronized (inventory) {
					itemsToOrder = inventory.getItemsToOrder();
					inventory.notifyAll();
				}
				synchronized (inventory) {
					for (String item: itemsToOrder){
						marketOrderItem(item, ORDER_QUANTITY);
						System.out.println("Cooker trying to order item.....");
					}
					inventory.notifyAll();
				}
			}
		}
	}

	/**
	 * @return the inventory
	 */
	public AlfredInventory getInventory() {
		return inventory;
	}

	/**
	 * @param cookGui the cookGui to set
	 */
	public void setCookGui(AlfredCookerGui cookGui) {
		this.cookGui = cookGui;
	}
	
}
