package simcity.interfaces;

import simcity.ItemOrder;
import java.util.*;

public interface MarketCustomer {

	public abstract void msgOrderItems(List<ItemOrder> i);
	
	public abstract void msgIWantCar();

}