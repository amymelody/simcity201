package simcity.alfredrestaurant;

import java.util.Timer;
import java.util.TimerTask;

import restaurant.CustomerAgent.AgentEvent;
import restaurant.gui.WaiterGui;
import agent.Agent;

public class AlfredWaiterRole extends Agent {

	Timer timer = new Timer();
	public enum AgentState {

		DoingNothing, SitCustomer, WaitForReadyOrder, WaitForAnswer, GoToPlatingArea, RequestCooking, BringToCustomer,  
		BringNoFoodToCustomer, WaitForDone, WaitForFullfillCheckRequest, SendingCheckToCustomer
	};

	private AgentState state = AgentState.DoingNothing;// The start state

	public enum AgentEvent {

		none, haveCustomer, seated, readyToOrder, leaveDuetoExpensive, ComeToPlatingArea, hadOrder, hadFood, hadNoFood, doneBringFood, doneBringNoFood, 
		eatingDone, leavingDone, doneCheckRequest, sendingCheckDone
	};
	
	private String food; //serving food

	AgentEvent event = AgentEvent.none;

	private HostAgent hostAgent;

	private WaiterGui waiterGui;
	
	private int index;

	public WaiterAgent(HostAgent hostAgent, int index) {
		this.hostAgent = hostAgent;
		this.index = index;
	}
	
	public void clickOn(int x, int y){
		int hostXPos = waiterGui.getXPos();
		int hostYPos = waiterGui.getYPos();
		
		if (x >= hostXPos && x <= hostXPos + WaiterGui.sizeHost &&
				y >= hostYPos && y <= hostYPos + WaiterGui.sizeHost ){
			wantingToGoOnBreak = true;
		}
	}

	private CustomerAgent currentCustomer;
	private Table table;

	public boolean isAvailable() {
		return currentCustomer == null && beingOnBreak == false;
	}
	
	public boolean wantingToGoOnBreak = false;
	public boolean beingOnBreak = false;
	
	public void wantToGoOnBreak(){
		wantingToGoOnBreak = true;
	}

	/**
	 * @param waiterGui
	 *            the waiterGui to set
	 */
	public void setWaiterGui(WaiterGui waiterGui) {
		this.waiterGui = waiterGui;
	}

	@Override
	protected boolean pickAndExecuteAnAction() {
		if (hostAgent.restaurantPanel.restauranGUI.waiterWaitingArea != null)
			hostAgent.restaurantPanel.restauranGUI.waiterWaitingArea.repaint();
		if (state == AgentState.DoingNothing
				&& event == AgentEvent.haveCustomer) {
			System.out.print("");
			sitCustomer();
			state = AgentState.SitCustomer;
			return true;
		}
		if (state == AgentState.SitCustomer && event == AgentEvent.seated) {
			state = AgentState.WaitForReadyOrder;
			return true;
		}
		if (state == AgentState.WaitForReadyOrder && event == AgentEvent.readyToOrder) {
			currentCustomer.msgWhatWouldYouLike(new Menu());
			state = AgentState.WaitForAnswer;
			return true;
		}
		if (state == AgentState.WaitForReadyOrder && event == AgentEvent.leaveDuetoExpensive) {
			waiterGui.DoLeaving();
			state = AgentState.WaitForDone;
			return true;
		}
		if (state == AgentState.WaitForAnswer && event == AgentEvent.hadOrder) {
			waiterGui.DoLeaveCustomer();			
			state = AgentState.GoToPlatingArea;
			waiterGui.DoGoToPlatingArea();
			return true;
		}
		if (state == AgentState.GoToPlatingArea && event == AgentEvent.ComeToPlatingArea) {
			state = AgentState.RequestCooking;
			hostAgent.getCook().msgOrder(this, food);			
			return true;
		}
		if (state == AgentState.RequestCooking && event == AgentEvent.hadFood) {
			state = AgentState.BringToCustomer;
			waiterGui.DoBringToTable(currentCustomer, table);
			return true;
		}
		
		if (state == AgentState.RequestCooking && event == AgentEvent.hadNoFood) {
			state = AgentState.BringNoFoodToCustomer;
			waiterGui.DoBringNoFoodToTable(currentCustomer, table);
			return true;
		}
		
		
		if (state == AgentState.BringToCustomer
				&& event == AgentEvent.doneBringFood) {
			currentCustomer.msgHereIsYourFood();
			
			//////////<-------------leave customer after bringing food
			waiterGui.DoLeaving();
			
			state = AgentState.WaitForDone;
			
			return true;
		}
		
		if (state == AgentState.BringNoFoodToCustomer
				&& event == AgentEvent.doneBringNoFood) {
			currentCustomer.msgHereIsYourNoFood();
			state = AgentState.WaitForReadyOrder;
			return true;
		}		
		
//		if (state == AgentState.WaitForDone && event == AgentEvent.eatingDone) {
//			hostAgent.cashierAgent.msgCheckRequest(this);
//			state = AgentState.WaitForFullfillCheckRequest;
//			return true;
//		}
//		
//		if (state == AgentState.WaitForFullfillCheckRequest && event == AgentEvent.doneCheckRequest) {
//			//send to customer, customer will send payment request to cashier
//			state = AgentState.SendingCheckToCustomer;
//			currentCustomer.msgHereYourBill();
//			return true;
//		}
//		
//		if (state == AgentState.SendingCheckToCustomer && event == AgentEvent.sendingCheckDone) {
//			state = AgentState.WaitForDone;
//			return true;
//		}
//		
		if (state == AgentState.WaitForDone && event == AgentEvent.leavingDone) {
			//TODO --> table will be freed by customer
//			hostAgent.msgTableIsFree(currentCustomer.getTable());
			state = AgentState.DoingNothing;
			currentCustomer = null;
			table = null;
			return true;
		}
		return false;
	}
	
	//call from customer
	public void msgSendingBillDone(){
		System.out.println("msgSendingBillDone");
		event = AgentEvent.sendingCheckDone;
		stateChanged();
	}
	
	//call from cashier
//	public void msgCheckRequestDone(){
//		System.out.println("msgCheckRequestDone");
//		event = AgentEvent.doneCheckRequest;
//		stateChanged();
//	}
	//host agent will call this
	public void doOnBreak(){
		beingOnBreak = true;
		wantingToGoOnBreak = false;
	}
	
	public void backToWork(){
		if (beingOnBreak = true){
			print("Back to work");
			beingOnBreak = false;
			hostAgent.waiterComeback();
		}		
	}

	/**
	 * @return the currentCustomer
	 */
	public CustomerAgent getCurrentCustomer() {
		return currentCustomer;
	}

	public void msgAtTable() {// from animation
		// print("msgAtTable() called");
		// hostAgent.getSemaphore(table.tableNumber).release();// = true;
		// stateChanged();
	}

	private void sitCustomer() {
		print("Seating " + currentCustomer + " at " + table);
		waiterGui.sitCustomerToTable(currentCustomer, table);
		currentCustomer.setTable(table);
		currentCustomer.msgFollowMeToTable(hostAgent.getMenu());
		System.out.println("sitCustomer");
		currentCustomer.setTable(table);
	}

	// call from GUI
	public void msgSeated() {
		System.out.println(state);
		event = AgentEvent.seated;
		stateChanged();
	}

	public void hasCustomer(CustomerAgent customer, Table table) {
		currentCustomer = customer;
		this.table = table;
		event = AgentEvent.haveCustomer;
		stateChanged();
	}
	public void messeageleaveDuetoExpensive(CustomerAgent customer){
		print("Got messeageleaveDuetoExpensive");
		event = AgentEvent.leaveDuetoExpensive;
		System.out.println("state: " + state);
		stateChanged();
	}
	public void messeageReadyToOrder(CustomerAgent customer) {
		print("Got messeageReadyToOrder");
		event = AgentEvent.readyToOrder;
		System.out.println("state: " + state);
		stateChanged();
	}

	public void msgMyChoice(String food) {
		// order and get order
		this.food = food;
		event = AgentEvent.hadOrder;
		stateChanged();
	}
	
	public void msgComeToPlatingArea() {
		event = AgentEvent.ComeToPlatingArea;
		stateChanged();
	}

	//cook said
	public void msgOrderIsReady() {
		event = AgentEvent.hadFood;
		stateChanged();
	}
	
	//cook said
	public void msgOrderIsOutOfStock() {
		event = AgentEvent.hadNoFood;
		stateChanged();
	}

	public void msgDoneBring() {
		event = AgentEvent.doneBringFood;
		stateChanged();
	}
	
	public void msgLeavingDone() {
		event = AgentEvent.doneBringFood;
		stateChanged();
	}
	
	public void askForNewOrder() {
		event = AgentEvent.doneBringNoFood;
		stateChanged();
	}

	public void msgDoneEating() {
		event = AgentEvent.eatingDone;
		stateChanged();
	}

	/**
	 * @return the hostAgent
	 */
	public HostAgent getHostAgent() {
		return hostAgent;
	}

	public void msgDoneLeaving() {
		event = AgentEvent.leavingDone;
		stateChanged();		
	}

	public int getIndex() {
		return index;
	}

	public boolean isWaiting() {
		return state == AgentState.DoingNothing;
	}
	
	public boolean isWaitingCooking() {
		return state == AgentState.RequestCooking;
	}

	/**
	 * @return the waiterGui
	 */
	public WaiterGui getWaiterGui() {
		return waiterGui;
	}
	
	
	
}
