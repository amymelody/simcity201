package simcity.bank.test.mock;

import java.util.Map;


import simcity.bank.interfaces.BankDepositor;
import simcity.bank.interfaces.BankTeller;


//Works for normative and nonnormative scenarios


public class MockBankTeller extends Mock implements BankTeller {
	
	//public Cashier cashier;
	
	
	
	public MockBankTeller(String name){
		super(name);
	
		
	}
	
	// Messages
	@Override
	
	public void msgStartShift(){
		log.add(new LoggedEvent("Received message from person agent to start working as bank teller"));

	}
	public void msgEndShift(){
		log.add(new LoggedEvent("Received message from person agent to end working as bank teller."));

	}
	public void msgPay(){
		log.add(new LoggedEvent("Received salary, ending shift"));

	}
	
	public void msgHelpCustomer(BankDepositor c){
		
		log.add(new LoggedEvent("Received message from manager to work with customer"));
	}
	
	public void msgTransactionComplete(BankDepositor c, int cashInBank){
			log.add(new LoggedEvent("Received message from manager that transaction was successful"));

		
	}

	
	
	@Override
	public void msgMakeRequest(BankDepositor c, int transactionAmount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgTransactionDenied(BankDepositor c) {
		// TODO Auto-generated method stub
		
	}
	
	
}