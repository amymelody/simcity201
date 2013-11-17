package simcity.alfredrestaurant;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class AlfredInventory {
	
	private int MINIMUM_QUANTITY = 5;
	private int DEFAULT_QUANTITY = 6;
	private int ORDER_TIME_MILLI = 60*1000;//60 seconds
	public Inventory(){
		//default add some foods
		items.add(new Item("Steak", DEFAULT_QUANTITY));
		items.add(new Item("Chicken", DEFAULT_QUANTITY));
		items.add(new Item("Salad", DEFAULT_QUANTITY));
		items.add(new Item("Pizza", DEFAULT_QUANTITY));
	}
	private Vector<Item> items = new Vector<Item>();
	
	class Item{
		String name;
		int quanity;
		boolean inOrder;
		Item(String n, int q){
			name = n;
			quanity = q;
			inOrder = false;
		}
		Item(String n, int q, boolean o){
			name = n;
			quanity = q;
			inOrder = false;
			inOrder = o;
		}
	}
	//call from Cook Agent
	public boolean getItem(String name){
		synchronized (items) {
			for (Item item: items){
				if (item.name.equals(name)){
					if (item.quanity > 0){
						item.quanity--;
						return true;
					}
				}
			}
			items.notifyAll();
		}	
		return false;
	}
	
	//called from market agent
	public void addItem(String name, int quantity){
		synchronized (items) {
			for (Item item: items){
				if (item.name.equals(name)){
					item.quanity += quantity;
					item.inOrder = false;
					return;
				}
			}
			//new item
			items.add(new Item(name, quantity));
			items.notifyAll();
		}		
	}
	//set inorder of ordering item to true
	public void orderingItem(String name){
		synchronized (items) {
			for (Item item: items){
				if (item.name.equals(name)){
					item.inOrder = true;
					break;
				}
			}
			items.notifyAll();
		}		
	}
//	//call form Cook Agent
//	public void orderItem(String name, int quantity){
//		synchronized (items) {
//			boolean found = false;
//			for (Item item: items){
//				if (item.equals(name)){
//					if (item.inOrder == false){
//						(new OrderItem(item, quantity)).start();
//					}
//					found = true;
//					break;
//				}
//			}
//			//new item
//			if (!found){
//				Item item = new Item(name, 0, true);
//				items.add(new Item(name, 0, true));
//				(new OrderItem(item,quantity)).start();
//			}
//			items.notifyAll();
//		}		
//	}
	//call from cook agent (order thread of cook agent)
	public Vector<String> getItemsToOrder(){
		Vector<String> itemsToOrder = new Vector<String>();
		synchronized (items) {
			
			for (Item item: items){
				//item is not in order and quantity is low
				if (item.inOrder == false &&  item.quanity <= MINIMUM_QUANTITY){
					itemsToOrder.add(item.name);
				}
			}
			items.notifyAll();
		}	
		return itemsToOrder;
				
	}	
	
//	class OrderItem extends Thread{
//		Timer timer = new Timer();
//		Item item;
//		int quantity;
//		OrderItem(Item item, int quantity){
//			this.item = item;
//			this.quantity = quantity;
//		}
//		public void run(){
//			synchronized (items) {
//				item.inOrder = true;
//				items.notifyAll();
//			}
//			timer.schedule(new TimerTask() {
//				public void run() {
//					System.out.println("Ordered item " +  item.name + " arrived. Add to inventory.....");
//					addItem(item.name, quantity);
//				}
//			}, ORDER_TIME_MILLI);// how long to wait before running task
//		}
//	}
}

