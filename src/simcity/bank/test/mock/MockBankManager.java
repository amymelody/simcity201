package simcity.bank.test.mock;

import java.util.Map;

import simcity.bank.interfaces.BankDepositor;
import simcity.bank.interfaces.BankManager;
import simcity.bank.interfaces.BankTeller;


//Works for normative and nonnormative scenarios


public class MockBankManager extends Mock implements BankManager {
	
	//public Cashier cashier;
	
	
	
	public MockBankManager(String name){
		super(name);
	
		
	}
	
	// Messages
	@Override
	
	
	public void msgHired(BankTeller t){
		log.add(new LoggedEvent("Hired a bank teller"));
	}
	
	public void msgStartShift(){
		log.add(new LoggedEvent("Starting shift of tellers and manager"));

	}
	public void msgEndShift(){
		log.add(new LoggedEvent("Ending shift of tellers and manager"));

	}
	public void msgDoneForTheDay(){
		log.add(new LoggedEvent("Received message to close bank"));
	}
	
	public void msgWereOpen(){
		log.add(new LoggedEvent("Received message that bank is open"));
	}
	
	public void msgTransaction(BankDepositor c){
		log.add(new LoggedEvent("Received message from customer that he wants to make a transaction"));

	}
	public void msgProcessTransaction(BankTeller t){
		log.add(new LoggedEvent("Received message from teller to process the transaction."));
		
	}

	@Override
	public void msgHired(BankTeller t, int salary) {
		// TODO Auto-generated method stub
		
	}
	
	
}