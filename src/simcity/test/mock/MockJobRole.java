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
		log.add(new LoggedEvent("Ran scheduler"));
		if (log.containsString("Received msgEndShift")) {
			person.msgLeftDestination(this);
			return true;
		}
		return true;
	}
	
	public String toString() {
		return "employee " + getName();
	}

}
