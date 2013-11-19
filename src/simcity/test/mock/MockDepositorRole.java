package simcity.test.mock;

import simcity.interfaces.Depositor;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

public class MockDepositorRole extends Role implements Depositor {
	String name;
	int deposit;
	int withdrawal;

	public MockDepositorRole(String name) {
		this.name = name;
	}
	
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
