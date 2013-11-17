package simcity.alfredrestaurant;

import java.util.ArrayList;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurant.interfaces.Cashier;
import restaurant.interfaces.Customer;
import restaurant.interfaces.Market;
import simcity.agent.Agent;

public class AlfredCashierRole extends Agent implements AlfredCashier{
	Timer timer = new Timer();
	public enum AgentState {
		DoingNothing, Working
	}
	
	private String name;
	
	
	
	public AgentState state = AgentState.DoingNothing;// The start state
	
	public List<BillState> bills = new ArrayList<BillState>();
	public List<BillState> processingBill = new ArrayList<BillState>();
	public enum AgentEvent {

		none, hasRequest, processedRequest
	};

	public AgentEvent event = AgentEvent.none;
	
	public CashierAgent(String name) {
		this.name = name;
	}

	@Override
	public boolean pickAndExecuteAnAction() {
		if ((state == AgentState.DoingNothing || state == AgentState.Working) && event == AgentEvent.hasRequest){
			state = AgentState.Working;			
			processRequest();
		}else if (state == AgentState.Working && event == AgentEvent.processedRequest){
			System.out.println("Cashier proccessed request - pick and execute.");
			if (processingBill.size() > 0){
				BillState bill =  processingBill.remove(0);
				if (bill.agent instanceof Customer){
					((Customer)bill.agent).msgCheckRequestDone();
				}else if (bill.agent instanceof Market){
					System.out.println("Market....");
					if (Math.random() < 0.2){ //not enough money
						((Market)bill.agent).IOwnYou();
					}else{
						((Market)bill.agent).hadPayment();
					}
				}
				return true;
			}			
			
			return false;
		}
		return false;
	}

//	//send from waiter
//	public void msgCheckRequest(Agent agent){
//		System.out.println("msgCheckRequest");
//		currentAgent = agent;
//		event = AgentEvent.hasRequest;
//		stateChanged();
//	}
	
	//send from customer
	public void msgPaymentRequest(Customer agent, Bill bill){
		event = AgentEvent.hasRequest;
		bills.add(new BillState(bill, agent)); //add to tail
		stateChanged();
	}
	
	//send message to ask for paying
	public void msgMarketRequest(Market agent, Bill bill){
		event = AgentEvent.hasRequest;
		bills.add(new BillState(bill, agent)); //add to tail
		stateChanged();
	}
	
	private boolean processRequest(){
		System.out.println("processRequest");
		if (bills.size() == 0){
			return false;
		}
		BillState bill =  bills.remove(0);
		processingBill.add(bill);
		timer.schedule(new TimerTask() {
			public void run() {
				System.out.println("Cashier proccessed request.");
				event = AgentEvent.processedRequest;						
				stateChanged();
			}
		}, 200);
			
		return false;
	}
	class BillState{
		Bill bill;
		Object agent;
		
		public BillState(Bill bill, Object agent){
			this.bill = bill;
			this.agent = agent;
		}
	}
}
