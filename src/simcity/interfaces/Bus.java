package simcity.interfaces;

import simcity.PersonAgent;

public interface Bus {

	public abstract void msgComingAboard(PersonAgent person, String destination);

}