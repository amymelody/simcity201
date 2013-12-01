package simcity.interfaces;

import simcity.interfaces.Person;
import java.util.*;

public interface Bus {

	public abstract void msgComingAboard(Person person, String destination);

	public abstract void msgHereArePassengers(List<Person> passengers);
	
}