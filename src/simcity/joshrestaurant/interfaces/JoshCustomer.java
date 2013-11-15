package simcity.joshrestaurant.interfaces;

public interface JoshCustomer {
	
	public abstract String getName();
	
	public abstract int getCharge();

	public abstract void msgChange(int change);

}