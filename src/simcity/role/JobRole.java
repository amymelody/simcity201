package simcity.role;

import simcity.interfaces.JobInterface;

public abstract class JobRole extends Role implements JobInterface {

	private String jobLocation;
	
	public JobRole() {
		// TODO Auto-generated constructor stub
	}
	
	public void setJobLocation(String l) {
		jobLocation = l;
	}
	
	public String getJobLocation() {
		return jobLocation;
	}

	public abstract void msgStartShift();
	
	public abstract void msgEndShift();
}
