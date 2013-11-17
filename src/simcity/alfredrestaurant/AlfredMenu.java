package simcity.alfredrestaurant;

public class AlfredMenu {
	
	//there are 4 foods in the restaurant
	public static int MAX_FOODS = 4;
	
	public static String[] FOODS = {"Steak", "Chicken", "Salad", "Pizza"};
	public static double[] PRICES = {15.99, 10.99, 5.99, 8.99};
	
	public String chooseFood(int number){
		return FOODS[number];
	}
	
}
