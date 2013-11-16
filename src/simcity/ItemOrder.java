package simcity;

public class ItemOrder
{
	private String foodItem;
	private int amount;
	
	ItemOrder(String f, int a)
	{
		foodItem = f;
		amount = a;
	}
	
	public void setAmount(int a)
	{
		amount = a;
	}
	public int getAmount()
	{
		return amount;
	}
	public String getFoodItem()
	{
		return foodItem;
	}
	
}
