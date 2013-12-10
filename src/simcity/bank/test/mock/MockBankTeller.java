package simcity.bank.test.mock;

import java.util.Map;

import simcity.interfaces.BankDepositor;
import simcity.interfaces.BankTeller;
import simcity.interfaces.BankManager;
import simcity.mock.LoggedEvent;
import simcity.mock.Mock;



//Works for normative and nonnormative scenarios


public class MockBankTeller extends Mock implements BankTeller {
	
	//public Cashier cashier;
	
	
	
	public MockBankTeller(String name){
		super(name);
	
		
	}
	
	// Messages
	
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
	

	public void msgMakeRequest(BankDepositor c, int transactionAmount) {
		log.add(new LoggedEvent("Received message from customer with his request"));
		
	}


	public void msgTransactionDenied(BankDepositor c) {
		log.add(new LoggedEvent("msg transaction denied"));
		
	}


	public void msgMakeLoanRequest(BankDepositor c) {
		log.add(new LoggedEvent("msg make loan request"));

		
	}


	public void msgLoanApproved(BankDepositor c, int newBalance) {
		log.add(new LoggedEvent("msg loan approved"));

		
	}

	
	public void msgLoanDenied(BankDepositor c) {
		log.add(new LoggedEvent("msg loan denied"));

		
	}
	
	
	
	
}