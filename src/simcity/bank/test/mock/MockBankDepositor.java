package simcity.bank.test.mock;

import java.util.Map;



import simcity.interfaces.BankDepositor;
import simcity.interfaces.BankTeller;


import simcity.interfaces.BankManager;


//Works for normative and nonnormative scenarios


public class MockBankDepositor extends Mock implements BankDepositor {
	
	//public Cashier cashier;
	
	
	
	public MockBankDepositor(String name){
		super(name);
	
		
	}
	
	// Messages
	@Override
	
	
	public void msgMakeDeposit(int cash){
		log.add(new LoggedEvent("msg make deposit"));
	}
	
	public void msgMakeWithdrawal(int cash){
		log.add(new LoggedEvent("Received message from person agent to take on role of withdrawer"));

	}
	
	public void msgMakeRequest(BankTeller t){
		log.add(new LoggedEvent("Received message from teller to make a request"));
	}
	
	public void msgCannotMakeTransaction(){
		log.add(new LoggedEvent("Received message from teller that there are not sufficient funds to make request"));

	}
	public void msgTransactionComplete(){
		log.add(new LoggedEvent("Received message from teller that transaction was successful."));
		
	}


	public void msgGoToTellerDesk() {
		log.add(new LoggedEvent("Msg go to teller desk"));
		
	}


	public void msgBusinessDeposit(int cash) {
		log.add(new LoggedEvent("msg business deposit"));
		
	}


	public void msgLoanDenied() {
		log.add(new LoggedEvent("msg loan denied"));
		
	}


	public void msgYoureDead() {
		log.add(new LoggedEvent("msg you're dead"));
		
	}


	public void msgLeaveMyBank() {
		log.add(new LoggedEvent("msg leave my bank"));
		
	}

	public void msgImARobber() {
		log.add(new LoggedEvent("msg im a robber"));
		
	}
	
	
}