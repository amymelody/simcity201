package simcity.test.mock;

import simcity.interfaces.Depositor;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

public class MockDepositorRole extends Role implements Depositor {
	String name;

	public MockDepositorRole(String name) {
		this.name = name;
	}
	
	public void msgMakeDeposit(int amount) {
		log.add(new LoggedEvent("Received msgMakeDeposit. Amount = $" + amount));
	}
	
	public void msgMakeWithdrawal(int amount) {
		log.add(new LoggedEvent("Received msgMakeWithdrawal. Amount = $" + amount));
	}
	
	public boolean pickAndExecuteAnAction() {
		if (log.containsString("Received msgOrderItems")) {
			person.msgExpense(16);
			person.msgLeftDestination(this);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "depositor " + getName();
	}

}
