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
	
	public void msgHelpCustomer(BankDepositor c, int cash){
		
		log.add(new LoggedEvent("Received message from manager to work with customer"));
	}
	
	public void msgMakeWithdrawal(BankDepositor c, int cash){
		log.add(new LoggedEvent("Received message from Bank customer to make a withdrawal"));

	}
	public void msgMakeDeposit(BankDepositor c, int cash){
		log.add(new LoggedEvent("Received message from Bank customer to make a deposit"));

	}
	
	public void msgTransactionComplete(BankDepositor c){
			log.add(new LoggedEvent("Received message from manager that transaction was successful"));

		
	}
	
	
}