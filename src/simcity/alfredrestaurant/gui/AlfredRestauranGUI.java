package restaurant.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import restaurant.CustomerAgent;

public class RestauranGUI extends JFrame{
	public AnimationPanel animationPanel;
	public CustomerWaitingArea customerWaitingArea;
	public ControlRestaurantPanel controlRestaurantPanel;
	public WaiterWaitingArea waiterWaitingArea;
	public CookingArea cookingArea;
	public PlatingArea platingArea;
	
	public RestauranGUI(){
				
		getContentPane().setLayout(new GridLayout(1, 2, 10, 10));
		
		animationPanel = new AnimationPanel(this);
		JPanel waitingArea = new JPanel();
		customerWaitingArea = new CustomerWaitingArea(this);
		controlRestaurantPanel = new ControlRestaurantPanel(this);
		animationPanel.setHostAgent(controlRestaurantPanel.host);
		
		cookingArea = new CookingArea(this);
		platingArea = new PlatingArea(this);
		
		getContentPane().add(controlRestaurantPanel);
		
		waiterWaitingArea = new WaiterWaitingArea(this);
		waiterWaitingArea.setBackground(Color.GREEN);
		waitingArea.setLayout(new GridLayout(2,2));
		
		waitingArea.add(platingArea);
		waitingArea.add(cookingArea);
		waitingArea.add(waiterWaitingArea);
		waitingArea.add(customerWaitingArea);
		
		JPanel leftPanel = new JPanel();
		leftPanel.add(waitingArea);		
		leftPanel.add(animationPanel);
		
		platingArea.setPreferredSize(new Dimension(Configuration.WINDOWX/4 - 10, (int)((double)Configuration.WINDOWY*(2.0/10.0))));
		cookingArea.setPreferredSize(new Dimension(Configuration.WINDOWX/4 - 10, (int)((double)Configuration.WINDOWY*(2.0/10.0))));
		customerWaitingArea.setPreferredSize(new Dimension(Configuration.WINDOWX/4 - 10, (int)((double)Configuration.WINDOWY*(2.0/10.0))));
		waiterWaitingArea.setPreferredSize(new Dimension(Configuration.WINDOWX/4 - 10, (int)((double)Configuration.WINDOWY*(2.0/10.0))));
		animationPanel.setPreferredSize(new Dimension(Configuration.WINDOWX/2 - 20, (int)((double)Configuration.WINDOWY*(6.0/10.0)) - 40));
		
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
	public void setCustomerEnabled(CherysCustomerRole c) {
		if (currentPerson instanceof CherysCustomerRole) {
			CherysCustomerRole cust = (CherysCustomerRole) currentPerson;
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
		
		
		RestauranGUI frame = new RestauranGUI();
		
		frame.setTitle("csci201 Restaurant");
		frame.setSize(Configuration.WINDOWX, Configuration.WINDOWY);
		frame.setLocationRelativeTo(null);// center frame
		frame.setVisible(true);
		//frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
