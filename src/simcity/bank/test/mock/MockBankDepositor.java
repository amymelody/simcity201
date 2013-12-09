package simcity.bank.test.mock;

import java.util.Map;

<<<<<<< HEAD

import simcity.interfaces.BankDepositor;
import simcity.interfaces.BankTeller;

=======
import simcity.interfaces.BankDepositor;
import simcity.interfaces.BankTeller;
import simcity.interfaces.BankManager;
>>>>>>> anjali

//Works for normative and nonnormative scenarios


public class MockBankDepositor extends Mock implements BankDepositor {
	
	//public Cashier cashier;
	
	
	
	public MockBankDepositor(String name){
		super(name);
	
		
	}
	
	// Messages
	@Override
	
	
	public void msgMakeDeposit(int cash){
		log.add(new LoggedEvent("Received message from person agent to take "
				+ "on role of depositor"));
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

	@Override
	public void msgGoToTellerDesk() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgBusinessDeposit(int cash) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLoanDenied() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgYoureDead() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void msgLeaveMyBank() {
		// TODO Auto-generated method stub
		
	}
	
	
}