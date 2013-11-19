package simcity.alfredrestaurant.gui;

import simcity.alfredrestaurant.AlfredCustomerRole;
import simcity.alfredrestaurant.AlfredHostRole;
import simcity.alfredrestaurant.AlfredMarketRole;
import simcity.alfredrestaurant.AlfredWaiterRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.Vector;

/**
 * Panel in frame that contains all the restaurant information, including host,
 * cook, waiters, and customers.
 */
public class AlfredRestaurantPanel extends JPanel {

	// Host, cook, waiters and customers
	private AlfredHostRole host;
	private AlfredHostGui hostGui;
	private AlfredCookerGui cookerGui;
	private java.util.List<AlfredCustomerRole> customers = Collections.synchronizedList(new java.util.ArrayList<AlfredCustomerRole>());
	private Vector<AlfredMarketRole> markets = new Vector<AlfredMarketRole>();

	private JPanel restLabel = new JPanel();
	private AlfredListPanel customerPanel;
	private JPanel group = new JPanel();

	public AlfredRestaurantGUI restauranGUI; // reference to main gui
	
	public AlfredRestaurantPanel(AlfredRestaurantGUI gui, AlfredHostRole hostAgent) {
		this.restauranGUI = gui;
		customerPanel = new AlfredListPanel(gui, this,"Customers");
		hostGui = new AlfredHostGui(hostAgent);
		host = hostAgent;
		host.setGui(hostGui);
		hostAgent.setRestaurantPanel(this);
//		gui.animationPanel.addGui(hostGui);
		host.startThread();
		cookerGui = new AlfredCookerGui();
		
		//create some markets
		for (int i = 0; i < 10; i++){
			AlfredMarketRole ma = new AlfredMarketRole(host.getCook(), host.cashierAgent);
			ma.startThread();
			markets.add(ma);
		}

		// add waiters to customer
		for (AlfredWaiterRole waiter : host.getWaiters()) {
			AlfredWaiterGui waiterGui = new AlfredWaiterGui(waiter);
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

	public void addWaiter(AlfredWaiterRole waiter) {
		AlfredWaiterGui waiterGui = new AlfredWaiterGui(waiter);
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
				AlfredCustomerRole temp = customers.get(i);
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
			AlfredCustomerRole c = new AlfredCustomerRole(name);
			AlfredCustomerGui g = new AlfredCustomerGui(c, restauranGUI);
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

	public AlfredHostRole getHost() {
		return host;
	}

	public java.util.List<AlfredCustomerRole> getCustomers() {
		return customers;
	}

	/**
	 * @return the cookerGui
	 */
	public AlfredCookerGui getCookerGui() {
		return cookerGui;
	}
	
}
