package simcity.alfredrestaurant.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import simcity.alfredrestaurant.AlfredCustomerRole;

public class AlfredRestaurantGUI extends JFrame{
	public AlfredRestaurantAnimationPanel animationPanel;
	public AlfredCustomerWaitingArea customerWaitingArea;
	public AlfredControlRestaurantPanel controlRestaurantPanel;
	public AlfredWaiterWaitingArea waiterWaitingArea;
	public AlfredCookingArea cookingArea;
	public AlfredPlatingArea platingArea;
	
	public AlfredRestaurantGUI(){
				
		getContentPane().setLayout(new GridLayout(1, 2, 10, 10));
		
		animationPanel = new AlfredRestaurantAnimationPanel(this);
		JPanel waitingArea = new JPanel();
		customerWaitingArea = new AlfredCustomerWaitingArea(this);
		controlRestaurantPanel = new AlfredControlRestaurantPanel(this);
		animationPanel.setHostAgent(controlRestaurantPanel.host);
		
		cookingArea = new AlfredCookingArea(this);
		platingArea = new AlfredPlatingArea(this);
		
		getContentPane().add(controlRestaurantPanel);
		
		waiterWaitingArea = new AlfredWaiterWaitingArea(this);
		waiterWaitingArea.setBackground(Color.GREEN);
		waitingArea.setLayout(new GridLayout(2,2));
		
		waitingArea.add(platingArea);
		waitingArea.add(cookingArea);
		waitingArea.add(waiterWaitingArea);
		waitingArea.add(customerWaitingArea);
		
		JPanel leftPanel = new JPanel();
		leftPanel.add(waitingArea);		
		leftPanel.add(animationPanel);
		
		platingArea.setPreferredSize(new Dimension(AlfredConfiguration.WINDOWX/4 - 10, (int)((double)AlfredConfiguration.WINDOWY*(2.0/10.0))));
		cookingArea.setPreferredSize(new Dimension(AlfredConfiguration.WINDOWX/4 - 10, (int)((double)AlfredConfiguration.WINDOWY*(2.0/10.0))));
		customerWaitingArea.setPreferredSize(new Dimension(AlfredConfiguration.WINDOWX/4 - 10, (int)((double)AlfredConfiguration.WINDOWY*(2.0/10.0))));
		waiterWaitingArea.setPreferredSize(new Dimension(AlfredConfiguration.WINDOWX/4 - 10, (int)((double)AlfredConfiguration.WINDOWY*(2.0/10.0))));
		animationPanel.setPreferredSize(new Dimension(AlfredConfiguration.WINDOWX/2 - 20, (int)((double)AlfredConfiguration.WINDOWY*(6.0/10.0)) - 40));
		
		getContentPane().add(leftPanel);
	}
	
	private Object currentPerson;/*
	 * Holds the agent that the info is about. Seems
	 * like a hack
	 */
	
	/**
	 * Message sent from a customer gui to enable that customer's "I'm hungry"
	 * checkbox.
	 * 
	 * @param c
	 *            reference to the customer
	 */
	public void setCustomerEnabled(AlfredCustomerRole c) {
		if (currentPerson instanceof AlfredCustomerRole) {
			AlfredCustomerRole cust = (AlfredCustomerRole) currentPerson;
			if (c.equals(cust)) {
				controlRestaurantPanel.stateCB.setEnabled(true);
				controlRestaurantPanel.stateCB.setSelected(false);
			}
		}
	}

	
	/**
	 * Main routine to get gui started
	 */
	public static void main(String[] args) {
		
		
		AlfredRestaurantGUI frame = new AlfredRestaurantGUI();
		
		frame.setTitle("csci201 Restaurant");
		frame.setSize(AlfredConfiguration.WINDOWX, AlfredConfiguration.WINDOWY);
		frame.setLocationRelativeTo(null);// center frame
		frame.setVisible(true);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
