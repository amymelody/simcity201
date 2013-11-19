package restaurant.gui;

import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.MarketAgent;
import restaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information, including host,
 * cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

	// Host, cook, waiters and customers
	private CherysHostRole host;
	private HostGui hostGui;
	private CookerGui cookerGui;
	private java.util.List<CherysCustomerRole> customers = Collections.synchronizedList(new java.util.ArrayList<CherysCustomerRole>());
	private Vector<CherysMarketRole> markets = new Vector<CherysMarketRole>();

	private JPanel restLabel = new JPanel();
	private CherysListPanel customerPanel;
	private JPanel group = new JPanel();

	public RestauranGUI restauranGUI; // reference to main gui
	
	public RestaurantPanel(RestauranGUI gui, CherysHostRole hostAgent) {
		this.restauranGUI = gui;
		customerPanel = new CherysListPanel(gui, this,"Customers");
		hostGui = new HostGui(hostAgent);
		host = hostAgent;
		host.setGui(hostGui);
		hostAgent.setRestaurantPanel(this);
//		gui.animationPanel.addGui(hostGui);
		host.startThread();
		cookerGui = new CookerGui();
		
		//create some markets
		for (int i = 0; i < 10; i++){
			CherysMarketRole ma = new CherysMarketRole(host.getCook(), host.cashierAgent);
			ma.startThread();
			markets.add(ma);
		}

		// add waiters to customer
		for (CherysWaiterRole waiter : host.getWaiters()) {
			CherysWaiterGui waiterGui = new CherysWaiterGui(waiter);
			gui.animationPanel.addGui(waiterGui);
			waiter.setWaiterGui(waiterGui);
			waiter.startThread();
		}

		setLayout(new GridLayout(1, 2, 20, 20));
		group.setLayout(new GridLayout(1, 2, 10, 10));

		group.add(customerPanel);

		initRestLabel();
		add(restLabel);
		add(group);
	}

	public void addWaiter(CherysWaiterRole waiter) {
		CherysWaiterGui waiterGui = new CherysWaiterGui(waiter);
		restauranGUI.animationPanel.addGui(waiterGui);
		waiter.setWaiterGui(waiterGui);
		waiter.startThread();
	}

	/**
	 * Sets up the restaurant label that includes the menu, and host and cook
	 * information
	 */
	private void initRestLabel() {
		JLabel label = new JLabel();
		// restLabel.setLayout(new BoxLayout((Container)restLabel,
		// BoxLayout.Y_AXIS));
		restLabel.setLayout(new BorderLayout());
		label.setText("<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>"
				+ host.getName()
				+ "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

		restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
		restLabel.add(label, BorderLayout.CENTER);
		restLabel.add(new JLabel("               "), BorderLayout.EAST);
		restLabel.add(new JLabel("               "), BorderLayout.WEST);
	}

	/**
	 * When a customer or waiter is clicked, this function calls
	 * updatedInfoPanel() from the main gui so that person's information will be
	 * shown
	 * 
	 * @param type
	 *            indicates whether the person is a customer or waiter
	 * @param name
	 *            name of person
	 */
	public void showInfo(String type, String name) {

		if (type.equals("Customers")) {

			for (int i = 0; i < customers.size(); i++) {
				CherysCustomerRole temp = customers.get(i);
				if (temp.getName() == name)
					restauranGUI.controlRestaurantPanel.updateInfoPanel(temp);
			}
		}
	}

	/**
	 * Adds a customer or waiter to the appropriate list
	 * 
	 * @param type
	 *            indicates whether the person is a customer or waiter (later)
	 * @param name
	 *            name of person
	 */
	public void addPerson(String type, String name, boolean hungry) {

		if (type.equals("Customers")) {
			CherysCustomerRole c = new CherysCustomerRole(name);
			CherysCustomerGui g = new CherysCustomerGui(c, restauranGUI);
			if (hungry) {
				g.setHungry();
			}
			restauranGUI.animationPanel.addGui(g);// dw
			c.setHost(host);
			c.setGui(g);
			customers.add(c);
			c.startThread();
			
			host.checkAction();
		}
	}

	public CherysHostRole getHost() {
		return host;
	}

	public java.util.List<CherysCustomerRole> getCustomers() {
		return customers;
	}

	/**
	 * @return the cookerGui
	 */
	public CookerGui getCookerGui() {
		return cookerGui;
	}
	
}
