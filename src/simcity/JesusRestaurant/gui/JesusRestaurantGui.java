package simcity.JesusRestaurant.gui;

import restaurant.CustomerAgent;
import restaurant.MarketAgent;
import restaurant.WaiterAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class JesusRestaurantGui extends JFrame implements ActionListener, MouseMotionListener {
	//static declarations
	static final int LSPACE = 10;
	static final int WINDOWX = 900;
	static final int WINDOWY = 580;
	static final int BUTTONX = 40;
	static final int BUTTONY = 20;
	static final int INFOROWS = 1;
	static final int INFOCOLUMNS = 2; 

	/* The GUI has two frames, the control frame (in variable gui) 
	 * and the animation frame, (in variable animationFrame within gui)
	 */
	//JFrame animationFrame = new JFrame("Restaurant Animation");


	/* restPanel holds 2 panels
	 * 1) the staff listing, menu, and lists of current customers all constructed
	 *    in RestaurantPanel()
	 * 2) the infoPanel about the clicked Customer (created just below)
	 */    
	private JesusRestaurantPanel restPanel = new JesusRestaurantPanel(this);

	/* infoPanel holds information about the clicked customer, if there is one*/
	private JPanel infoPanel;
	private JLabel infoLabel; //part of infoPanel for all
	private JCheckBox stateCB;//part of infoLabel for customer
	private JButton moneyB;//part of infoLabel for customer
	private JButton breakB;//part of infoLabel for waiter
	private JButton invB;//part of infoLabel for market
	private JPanel layers;


	private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

	private JPanel about;

	/**
	 * Constructor for RestaurantGui class.
	 * Sets up all the gui components.
	 */
	public JesusRestaurantGui() {
		setBounds(100, 0, WINDOWX, WINDOWY);

		setLayout(new BorderLayout());

		Dimension restDim = new Dimension((int) (WINDOWX * .5), (int) (WINDOWY * 0.75862068965517241379310344827586));
		restPanel.setPreferredSize(restDim);
		restPanel.setMinimumSize(restDim);
		restPanel.setMaximumSize(restDim);
		restPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
		add(restPanel, BorderLayout.NORTH);

		// Now, setup the info panel
		Dimension infoDim = new Dimension((int) (WINDOWX * .5), (int) (WINDOWY * .15));
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(infoDim);
		infoPanel.setMinimumSize(infoDim);
		infoPanel.setMaximumSize(infoDim);
		infoPanel.setLayout(new GridLayout(1, 2));
		infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

		layers = new JPanel();
		layers.setLayout(new GridBagLayout());

		stateCB = new JCheckBox();
		stateCB.setVisible(false);
		stateCB.addActionListener(this);

		breakB = new JButton("Go On Break");
		breakB.setSize(new Dimension(BUTTONX, BUTTONY));
		breakB.setVisible(false);
		breakB.addActionListener(this);

		invB = new JButton("Update Inventory");
		invB.setSize(new Dimension(BUTTONX, BUTTONY));
		invB.setVisible(false);
		invB.addActionListener(this);

		moneyB = new JButton("Update $");
		moneyB.setSize(new Dimension(BUTTONX, BUTTONY));
		moneyB.setVisible(false);
		moneyB.addActionListener(this);

		infoPanel.setLayout(new GridLayout(INFOROWS, INFOCOLUMNS, LSPACE, LSPACE));

		infoLabel = new JLabel(); 
		infoLabel.setText("<html><pre><i>Start Inviting Customers!</i></pre></html>");
		infoPanel.add(infoLabel, BorderLayout.CENTER);
		addMouseMotionListener(this);
		layers.add(stateCB);
		layers.add(breakB);
		layers.add(invB);
		layers.add(moneyB);
		infoPanel.add(layers);
		add(infoPanel, BorderLayout.CENTER);

		about = new JPanel();
		about.add(new JLabel("Jesus Garcia (Email: jesusega@usc.edu)"));
		add(about, BorderLayout.AFTER_LAST_LINE);
	}
	/**
	 * updateInfoPanel() takes the given customer (or, for v3, Host) object and
	 * changes the information panel to hold that person's info.
	 *
	 * @param person customer (or waiter) object
	 */
	public void updateInfoPanel(Object person) {
		currentPerson = person;

		if (person instanceof CustomerAgent) {
			stateCB.setVisible(true);
			breakB.setVisible(false);
			invB.setVisible(false);
			moneyB.setVisible(true);
			CustomerAgent customer = (CustomerAgent) person;
			stateCB.setText("Hungry?");
			//Should checkmark be there? 
			stateCB.setSelected(customer.getGui().isHungry());
			//Is customer hungry? Hack. Should ask customerGui
			stateCB.setEnabled(!customer.getGui().isHungry());
			// Hack. Should ask customerGui
			infoLabel.setText(
					"<html><pre>     Name: " + customer.getName() + "      Type: Customer </pre><pre>       Hunger Lvl: " + customer.getHungerLevel() + "       Money: $" + customer.getMoney() + "</pre></html>");
		}
		else if (person instanceof WaiterAgent) {
			stateCB.setVisible(false);
			breakB.setVisible(true);
			invB.setVisible(false);
			moneyB.setVisible(false);
			WaiterAgent waiter = (WaiterAgent) person;
			breakB.setEnabled(!waiter.getGui().breakDeciding);
			if(waiter.getGui().onBreak) {
				breakB.setText("Return to Work");
			}
			else {
				breakB.setText("Go On Break");
			}
			infoLabel.setText(
					"<html><pre>     Name: " + waiter.getName() + "      Type: Waiter </pre><pre>      Status: " + waiter.getGui().getOnBreak() + " </pre></html>");
		}
		else if (person instanceof MarketAgent) {
			stateCB.setVisible(false);
			breakB.setVisible(false);
			invB.setVisible(true);
			moneyB.setVisible(false);
			MarketAgent market = (MarketAgent) person;
			infoLabel.setText(
					"<html><pre>     Name: " + market.getName() + "      Type: Market </pre><pre>       Inventory:</pre><pre>            Steak: " + market.getInventory("Steak") + "       Salad: " + market.getInventory("Salad") + "       Pizza: " + market.getInventory("Pizza") + "</pre></html>");
		}
		infoPanel.validate();
	}
	/**
	 * Action listener method that reacts to the checkbox being clicked;
	 * If it's the customer's checkbox, it will make him hungry
	 * For v3, it will propose a break for the waiter.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == stateCB) {
			if (restPanel.getWaiterCnt() <= 0) {
				JOptionPane.showMessageDialog(this, "Please add a waiter first.");
				stateCB.setSelected(false);
			}
			else if (currentPerson instanceof CustomerAgent) {
				CustomerAgent c = (CustomerAgent) currentPerson;
				c.getGui().setHungry();
				stateCB.setEnabled(false);
			}
		}
		else if(e.getSource() == breakB) {
			if(currentPerson instanceof WaiterAgent) {
				WaiterAgent w = (WaiterAgent) currentPerson;
				w.getGui().goOnBreak();
				updateInfoPanel(w);
			}
		}
		else if(e.getSource() == invB) {
			if(currentPerson instanceof MarketAgent) {
				MarketAgent m = (MarketAgent) currentPerson;
				JTextField steak = new JTextField();
				JTextField salad = new JTextField();
				JTextField pizza = new JTextField();
				Object[] invUp = {
						"Steak:", steak,
						"Salad:", salad,
						"Pizza:", pizza
				};
				int option = JOptionPane.showConfirmDialog(null, invUp, "Update Inventory", JOptionPane.OK_CANCEL_OPTION);
				if (option == JOptionPane.OK_OPTION) {
					if (steak.getText().length() > 0 && salad.getText().length() > 0 && pizza.getText().length() > 0) {
						boolean number;
						try{
							if(Integer.parseInt(steak.getText()) >= 0 && Integer.parseInt(salad.getText()) >= 0 
									&& Integer.parseInt(pizza.getText()) >= 0)
								number = true;
							else
								number = false;
						}
						catch(NumberFormatException ex) {
							number = false;
						}
						if(number) {
							m.getGui().updateInventory(Integer.parseInt(steak.getText()), Integer.parseInt(salad.getText()), 
									Integer.parseInt(pizza.getText()));
							steak.setText("");
							salad.setText("");
							pizza.setText("");
							updateInfoPanel(m);
						}
						else {
							JOptionPane.showMessageDialog(this, "Please enter a positive integer for each food item.");
							steak.setText("");
							salad.setText("");
							pizza.setText("");
						}
					}
					else {
						JOptionPane.showMessageDialog(this, "Please enter an integer for each food item.");
					}
				}
			}
		}
		else if(e.getSource() == moneyB) {
			if(currentPerson instanceof CustomerAgent) {
				CustomerAgent c = (CustomerAgent) currentPerson;
				String money = JOptionPane.showInputDialog("Please input a money amount");
				if (money.length() > 0) {
					boolean number;
					try{
						if(Double.parseDouble(money) >= 0)
							number = true;
						else
							number = false;
					}
					catch(NumberFormatException ex) {
						number = false;
					}
					if(number) {
						c.getGui().updateMoney(Double.parseDouble(money));
						updateInfoPanel(c);
					}
					else {
						JOptionPane.showMessageDialog(this, "Please enter a positive number for money amount.");
					}
				}
			}
		}
	}
	public void mouseDragged(MouseEvent e) {

	}	

	public void mouseMoved(MouseEvent e) {
		updateInfoPanel(currentPerson);
	}
	/**
	 * Message sent from a customer gui to enable that customer's
	 * "I'm hungry" checkbox.
	 *
	 * @param c reference to the customer
	 */
	public void setCustomerEnabled(CustomerAgent c) {
		if (currentPerson instanceof CustomerAgent) {
			CustomerAgent cust = (CustomerAgent) currentPerson;
			if (c.equals(cust)) {
				stateCB.setEnabled(true);
				stateCB.setSelected(false);
			}
		}
	}

	/**
	 * Main routine to get gui started
	 */
	public static void main(String[] args) {
		JesusRestaurantGui gui = new JesusRestaurantGui();
		gui.setTitle("CSCI201L Restaurant: La Cocina");
		gui.setVisible(true);
		gui.setResizable(false);
		gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}