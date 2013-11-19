package simcity.test.mock;

import simcity.interfaces.JobInterface;
import simcity.mock.LoggedEvent;
import simcity.role.Role;

public class MockJobRole extends Role implements JobInterface {
	String name;

	public MockJobRole(String name) {
		this.name = name;
	}
	
	public void msgStartShift() {
		log.add(new LoggedEvent("Received msgStartShift"));
	}
	
	public void msgEndShift() {
		log.add(new LoggedEvent("Received msgEndShift"));
	}
	
	public boolean pickAndExecuteAnAction() {
		if (log.size() > 0) {
			person.msgLeftDestination(this);
			return true;
		}
		return false;
	}
	
	public String toString() {
		return "employee " + getName();
	}

}
