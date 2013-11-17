package simcity.alfredrestaurant;

import java.util.Timer;
import java.util.TimerTask;

import agent.Agent;
import restaurant.interfaces.Market;

public class AlfredMarketRole extends Agent implements Market{

	private CookAgent cook;
	private CashierAgent cashier;
	
	public MarketAgent(CookAgent cook, CashierAgent cashier) {
		this.cook = cook;
		this.cashier = cashier;
		cook.addMarket(this);
	}

	public enum AgentState {

		DoingNothing, Ordering, RequestPayment
	};

	private Timer timer = new Timer();
	private AgentState state = AgentState.DoingNothing;// The start state
	private AgentEvent event = AgentEvent.none;
	
	public enum AgentEvent {

		none, gotOrder, orderArrived, HadPayment, HadNoPayment
	};
	
	private String item;
	private int quanity;
	
	public boolean isAvailable() {
		return state == AgentState.DoingNothing;
	}
	
	@Override
	protected boolean pickAndExecuteAnAction() {
		if (state == AgentState.DoingNothing && event == AgentEvent.gotOrder) {
			state = AgentState.Ordering;
			doOrder();
			return true;
		}else if (state == AgentState.Ordering 	&& event == AgentEvent.orderArrived) {
			state = AgentState.RequestPayment;
			cashier.msgMarketRequest(this, new Bill(item, 1.5 * quanity)); //Assume that price = 1.5			
			return true;
		}else if (state == AgentState.RequestPayment && event == AgentEvent.HadPayment) {
			state = AgentState.DoingNothing;
			return true;
		}else if (state == AgentState.RequestPayment && event == AgentEvent.HadNoPayment) {
			state = AgentState.DoingNothing;
			return true;
		}
		return false;
	}	
	//call from cook agent
	public void order(String item, int quantity){
		this.item = item;
		this.quanity = quantity;
		//set inorder to true
		cook.getInventory().orderingItem(item);
		event = AgentEvent.gotOrder;
		stateChanged();
	}

	public void hadPayment(){
		System.out.println("HadPayment");
		event = AgentEvent.HadPayment;
		stateChanged();
	}
	
	public void IOwnYou(){
		System.out.println("IOwnYou");
		event = AgentEvent.HadNoPayment;
		stateChanged();
	}
	
	private void doOrder(){
		timer.schedule(new TimerTask() {
			public void run() {
				print("market ordered =" + item);
				//random to got order or out of order
				int q = 0;
				if (Math.random() < 0.5){					
					q = quanity; 
				}
				//update inventory
				cook.getInventory().addItem(item, q);
				event = AgentEvent.orderArrived;				
				stateChanged();
			}
		}, 50000);// how long to wait before running task
	}
}
