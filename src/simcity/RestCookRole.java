package simcity;

import simcity.role.JobRole;
import simcity.interfaces.RestCook;
import java.util.*;

public abstract class RestCookRole extends JobRole implements RestCook {

	public RestCookRole() {
		// TODO Auto-generated constructor stub
	}

	public abstract void msgDelivery(List<ItemOrder> order);
	
	public abstract void msgHereIsWhatICanFulfill(List<ItemOrder> items, boolean canFulfill);
}
