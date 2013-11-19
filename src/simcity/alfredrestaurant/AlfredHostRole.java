package simcity.alfredrestaurant;

import simcity.agent.Agent;

import java.awt.Point;

import simcity.alfredrestaurant.gui.AlfredHostGui;
import simcity.alfredrestaurant.gui.AlfredControlRestaurantPanel;
import simcity.alfredrestaurant.gui.AlfredRestaurantGUI;
import simcity.alfredrestaurant.gui.AlfredRestaurantPanel;

import java.util.*;
import java.util.concurrent.Semaphore;

/**
 * Restaurant Host Agent
 */
// We only have 2 types of agents in this prototype. A customer and an agent
// that
// does all the rest. Rather than calling the other agent a waiter, we called
// him
// the HostAgent. A Host is the manager of a restaurant who sees that all
// is proceeded as he wishes.
public class AlfredHostRole extends Agent {

	private static final int TABLE_VIEW_WIDTH = 300;
	private static final int TABLE_VIEW_HEIGHT = 350;
	private static final int TABLE_OFFSET = 60;
	public int NTABLES = 1;// a global for the number of tables.
	public int NWAITERS = 1;

	// Notice that we implement waitingCustomers using ArrayList, but type it
	// with List semantics.
	private List<AlfredCustomerRole> waitingCustomers = Collections.synchronizedList(new ArrayList<AlfredCustomerRole>());
	public Collection<AlfredTable> tables;
	// note that tables is typed with Collection semantics.
	// Later we will see how it is implemented
	private String name;

	public AlfredHostGui hostGui = null;
		
	public AlfredCashierRole cashierAgent = new AlfredCashierRole("cashier"); 

	//try catch ConcurrentModificationException instead of synchonized
	private List<AlfredWaiterRole> waiters = new ArrayList<AlfredWaiterRole>();

	private AlfredCookRole cook = new AlfredCookRole(this);

	public AlfredMenu getMenu() {
		return menu;
	}

	public AlfredCookRole getCook() {
		return cook;
	}
	
	public void clickOn(int x, int y){
		for (AlfredWaiterRole waiter : waiters){
			waiter.clickOn(x, y);
		}
	}

	private AlfredMenu menu = new AlfredMenu();

	public HostAgent(String name) {
		super();

		this.name = name;
		// make some tables
		tables = Collections.synchronizedList(new ArrayList<AlfredTable>());
		for (int ix = 1; ix <= NTABLES; ix++) {
			AlfredTable table = new AlfredTable(ix);
			table.position = findTablePosition();
			tables.add(table);// how you add to a collections

		}

		for (int ix = 0; ix < NWAITERS; ix++) {
			waiters.add(new AlfredWaiterRole(this, ix + 1));
		}
		cook.startThread();
		
		(new WaiterBreakThread()).start();
		cashierAgent.startThread();
	}

	// get table [i] position
	public Point getTablePosition(int tableNumber) {
		Iterator<AlfredTable> iter = tables.iterator();
		while (iter.hasNext()) {
			AlfredTable table = iter.next();
			if (table.tableNumber == tableNumber) {
				return table.position;
			}
		}
		return null;
	}

	// get table by number
	public AlfredTable getTable(int tableNumber) {
		Iterator<AlfredTable> iter = tables.iterator();
		while (iter.hasNext()) {
			AlfredTable table = iter.next();
			if (table.tableNumber == tableNumber) {
				return table;
			}
		}
		return null;
	}

	// get position of new table, not overlap
	public Point findTablePosition() {
		Random random = new Random();
		int xPos = 0;
		int yPos = 0;
		boolean overlap;
		int tryNumber = 0;
		int maxTry = 10000;
		do {
			tryNumber++;
			if (tryNumber > maxTry)
				break;
			overlap = false;
			xPos = random.nextInt(TABLE_VIEW_WIDTH - TABLE_OFFSET)
					+ TABLE_OFFSET;
			yPos = random.nextInt(TABLE_VIEW_HEIGHT - TABLE_OFFSET)
					+ TABLE_OFFSET;
			Iterator<AlfredTable> iter = tables.iterator();
			while (iter.hasNext()) {
				Point p = iter.next().position;
				if (Math.sqrt((xPos - p.x) * (xPos - p.x) + (yPos - p.y)
						* (yPos - p.y)) < 2 * AlfredHostGui.sizeTable) {
					overlap = true;
					break;
				}
			}
		} while (overlap);

		return new Point(xPos, yPos);
	}

	// increase the number of tables
	public void increaseTable() {
		NTABLES++;
		AlfredTable table = new AlfredTable(NTABLES);
		table.position = findTablePosition();
		synchronized (tables) {
			tables.add(table);
			tables.notifyAll();
		}
		
	}

	// increase the number of waiter
	public void increaseWaiter() {
		NWAITERS++;

		AlfredWaiterRole waiter = new AlfredWaiterRole(this, NWAITERS);
		
		synchronized (waiters) {
			waiters.add(waiter);
			waiters.notifyAll();
		}		
		
		restaurantPanel.addWaiter(waiter);
		stateChanged();
	}
	public void waiterComeback(){
		stateChanged();
	}
	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

//	public List<CustomerAgent> getWaitingCustomers() {
//		return waitingCustomers;
//	}

//	public Collection<Table> getTables() {
//		return tables;
//	}
	
	public int getWaitingCustomersSize(){
		int size = 0;
		synchronized (waitingCustomers) {
			size = waitingCustomers.size();
			waitingCustomers.notifyAll();
		}
		return size;
	}

	// Messages

	public void msgIWantFood(AlfredCustomerRole cust) {
		System.out.println(cust.getCustomerName() + " want food");
		synchronized (waitingCustomers) {
			waitingCustomers.add(cust);
			waitingCustomers.notifyAll();
		}
		stateChanged();
	}
	
	public void checkAction(){
		stateChanged();
	}

//	public void msgLeavingTable(CustomerAgent cust) {
//		for (Table table : tables) {
//			if (table.getOccupant() == cust) {
//				print(cust + " leaving " + table);
//				table.setUnoccupied();
//				stateChanged();
//			}
//		}
//	}

	/**
	 * Scheduler. Determine what action is called for, and do it.
	 */
	protected boolean pickAndExecuteAnAction() {
		boolean doneCalling = false;
		/*
		 * Think of this next rule as: Does there exist a table and customer, so
		 * that table is unoccupied and customer is waiting. If so seat him at
		 * the table.
		 */
		synchronized (tables) {
			for (AlfredTable table : tables) {
				if (!table.isOccupied()) {
					synchronized (waitingCustomers) {
						if (!waitingCustomers.isEmpty()) {
							// find waiters
							for (AlfredWaiterRole waiter : waiters) {
								if (waiter.isAvailable()) {
									System.out.println(waitingCustomers.get(0).getCustomerName() + ": host seat customer" + " table = " + table.tableNumber);
									seatCustomer(waitingCustomers.get(0), table, waiter);// the  action									
									doneCalling = true;
									break;
									//return true;// return true to the abstract agent to
									//// reinvoke the scheduler.
								}
							}
	
						}
					waitingCustomers.notifyAll();
					}	
				}
				if (doneCalling){
					break;
				}
			}
			tables.notifyAll();
		}	
		
//		if (!waitingCustomers.isEmpty()) {
//			return true;
//		}
		//return doneCalling;
		return true;
		// we have tried all our rules and found
		// nothing to do. So return false to main loop of abstract agent
		// and wait.
	}

	// Actions
	private void seatCustomer(AlfredCustomerRole customer, AlfredTable table,
			AlfredWaiterRole waiter) {
		// customer.setTable(table);
		// customer.msgSitAtTable();
		// DoSeatCustomer(customer, table);
		// try {
		// getSemaphore(table.tableNumber).acquire();
		// } catch (InterruptedException e) {
		// //e.printStackTrace();
		// return;
		// }
		// table.setOccupant(customer);
		// customer.setTable(table);
		// waitingCustomers.remove(customer);
		// hostGui.DoLeaveCustomer();
		customer.setWaiter(waiter);
		table.setOccupant(customer);
		synchronized (waitingCustomers) {
			waitingCustomers.remove(customer);
			waitingCustomers.notifyAll();
		}			
		
		waiter.hasCustomer(customer, table);
	}

	// // The animation DoXYZ() routines
	// private void DoSeatCustomer(CustomerAgent customer, Table table) {
	// //Notice how we print "customer" directly. It's toString method will do
	// it.
	// //Same with "table"
	// print("Seating " + customer + " at " + table);
	// hostGui.DoBringToTable(customer, table.tableNumber);
	// }

	// utilities
	public void setGui(AlfredHostGui gui) {
		hostGui = gui;
	}

	public AlfredHostGui getGui() {
		return hostGui;
	}

	public void msgTableIsFree(AlfredTable table) {
		table.setUnoccupied();
		stateChanged();
	}

	/**
	 * @return the waiters
	 */
	public List<AlfredWaiterRole> getWaiters() {
		return waiters;
	}

	public AlfredRestaurantPanel restaurantPanel;

	public void setRestaurantPanel(AlfredRestaurantPanel restaurantPanel) {
		this.restaurantPanel = restaurantPanel;
	}

	class WaiterBreakThread extends Thread{
		public void run(){
			while (true){
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				synchronized (waiters) {
					//allow break only if there are more than 1 waiter
					int count = 0;
					for (AlfredWaiterRole waiter: waiters){
						if (!waiter.beingOnBreak){
							count++;
						}
					}
					if (count > 1){
						for (AlfredWaiterRole waiter: waiters){
							if (waiter.isAvailable() && waiter.wantingToGoOnBreak){
								waiter.doOnBreak();
								System.out.println("Allow waiter to break....");
								break; //one waiter a times only
							}
						}
					}
					
					waiters.notifyAll();
				}
			}
		}
	}
	private AlfredControlRestaurantPanel restaurantGui;

	public void setRestaurantGUI(AlfredControlRestaurantPanel restaurantGui) {
		this.restaurantGui = restaurantGui;		
	}
}
