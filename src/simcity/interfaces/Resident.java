package simcity.interfaces;

import simcity.ItemOrder;
import java.util.*;

public interface Resident {

	public abstract void msgLeave();
	
	public abstract void msgAtLandlord();
	
	public abstract void msgEat();
	
	public abstract void msgGroceries(List<ItemOrder> groceries);

	public abstract void msgImHome();
}