package simcity.test.mock;

<<<<<<< HEAD
import simcity.interfaces.BankDepositor;
=======
import simcity.bank.interfaces.BankDepositor;
>>>>>>> anjali
import simcity.interfaces.BankTeller;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

public class MockBankDepositorRole extends Role implements BankDepositor {
	String name;
	int deposit;
	int withdrawal;

	public MockBankDepositorRole(String name) {
		this.name = name;
	}
	
	public void msgTransactionComplete() {
	}
	
	public void msgGoToTellerDesk() {
		
	}
	
	public void msgCannotMakeTransaction() {};
	
	public void msgMakeRequest(BankTeller t) {};
	
	public void msgImARobber() {};
	
	public void msgLoanDenied() {};
	
	public void msgLeaveMyBank() {};
	
	public void msgYoureDead() {};
	
	public void msgBusinessDeposit(int amt) {};
	
	public void msgMakeDeposit(int amount) {
		log.add(new LoggedEvent("Received msgMakeDeposit. Amount = $" + amount));
		deposit = amount;
	}
	
	public void msgMakeWithdrawal(int amount) {
		log.add(new LoggedEvent("Received msgMakeWithdrawal. Amount = $" + amount));
		withdrawal = amount;
	}
	
	public boolean pickAndExecuteAnAction() {
		if (log.containsString("Received msgMakeDeposit")) {
			person.msgCreatedAccount();
			person.msgExpense(deposit);
			person.msgLeftDestination(this);
			return true;
		}
		if (log.containsString("Received msgMakeWithdrawal")) {
			person.msgCreatedAccount();
			person.msgIncome(withdrawal);
			person.msgLeftDestination(this);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "depositor " + getName();
	}

}
