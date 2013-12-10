package simcity.anjalirestaurant.interfaces;




public interface AnjaliMarket{
	public abstract void msgOrderSupply(String food, AnjaliCook c, int reduceInventory, boolean canPay);
	
	public abstract void msgCheckInventory(String food, AnjaliCook c);
	
	public void msgHereIsMoney();

	public abstract String getName();

	
}
