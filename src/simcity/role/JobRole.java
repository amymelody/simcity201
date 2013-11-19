package simcity.role;

import simcity.interfaces.JobInterface;

public abstract class JobRole extends Role implements JobInterface {

	public JobRole() {
		// TODO Auto-generated constructor stub
	}

	public abstract void msgStartShift();
	
	public abstract void msgEndShift();
}
