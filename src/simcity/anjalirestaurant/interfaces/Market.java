package simcity.anjalirestaurant.interfaces;




public interface Market{
	public abstract void msgOrderSupply(String food, Cook c, int reduceInventory, boolean canPay);
	
	public abstract void msgCheckInventory(String food, Cook c);
	
	public void msgHereIsMoney();

	public abstract String getName();

	
}
