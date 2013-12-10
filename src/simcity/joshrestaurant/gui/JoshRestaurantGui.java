package simcity.joshrestaurant.gui;

import simcity.CityDirectory;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.gui.CityGui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class JoshRestaurantGui extends BuildingGui implements ActionListener {
	
	JoshRestaurantAnimationPanel animationPanel = new JoshRestaurantAnimationPanel();
	private JTabbedPane controlPanel = new JTabbedPane();
    private JoshRestaurantInputPanel inputPanel;
    private JPanel infoPanel;
    private JTextField infoLabel;
    private JCheckBox stateCB;
    private JPanel restLabel = new JPanel();
    private JPanel inventoryPanel = new JPanel();
    
    private ButtonGroup foodGroup = new ButtonGroup();
	private JRadioButtonMenuItem stButton = new JRadioButtonMenuItem("Steak");
	private JRadioButtonMenuItem chButton = new JRadioButtonMenuItem("Chicken");
	private JRadioButtonMenuItem piButton = new JRadioButtonMenuItem("Pizza");
	private JRadioButtonMenuItem saButton = new JRadioButtonMenuItem("Salad");
	
	private JLabel currentCash;
	private JLabel currentSteak;
	private JLabel currentChicken;
	private JLabel currentPizza;
	private JLabel currentSalad;
    
	private JButton cashButton = new JButton("Add");
	private JButton inventoryButton = new JButton("Add");
	private JTextField cashInput;
	private JTextField inventoryInput;
    
    private Timer timer;

    private Object currentPerson;

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public JoshRestaurantGui(String n, BuildingsGui bG, CityDirectory cD) {
    	super(n, bG, cD);
    	inputPanel = new JoshRestaurantInputPanel(this, cD.getJoshCashier(), cD.getJoshCook(), cD.getJoshHost(), cD.getMarketCashiers());
    	
        int WINDOWX = 650;
        int WINDOWY = 500;
    	
        double controlFractionOfWindow = 150.0 / 650.0;
        double infoFractionOfWindow = 100.0 / 500.0;
        Dimension infoDim = new Dimension((int)(WINDOWX * controlFractionOfWindow), (int)(WINDOWY * infoFractionOfWindow));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
        stateCB = new JCheckBox();
        stateCB.setText("Break?");
        stateCB.addActionListener(this);
        stateCB.setEnabled(false);
        stateCB.setVisible(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        infoLabel = new JTextField(); 
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        
        initRestLabel();

        Dimension controlDim = new Dimension((int)(WINDOWX * controlFractionOfWindow), WINDOWY);
        controlPanel.setPreferredSize(controlDim);
        controlPanel.setMinimumSize(controlDim);
        controlPanel.setMaximumSize(controlDim);
        controlPanel.setBounds(0, 0, 150, 500);
        
        JPanel waiters = new JPanel();
        waiters.setLayout(new BorderLayout());
        waiters.add(infoPanel, BorderLayout.NORTH);
        waiters.add(inputPanel, BorderLayout.CENTER);
        
        
        //Inventory Panel
        
        inventoryPanel.setLayout(new FlowLayout());
        JLabel addCash = new JLabel("<html><u>Add Cash</u></html>");
        JLabel addInventory = new JLabel("<html><u>   Add Food   </u></html>");
        JPanel cash = new JPanel();
        JPanel inventory = new JPanel();
        cash.setLayout(new GridLayout(1,2));
        inventory.setLayout(new GridLayout(1,2));
        
        foodGroup.add(stButton);
        foodGroup.add(chButton);
        foodGroup.add(piButton);
        foodGroup.add(saButton);
        cashInput = new JTextField("");
        inventoryInput = new JTextField("");
        
        cashButton.addActionListener(this);
        inventoryButton.addActionListener(this);
        cash.add(cashInput);
        cash.add(cashButton);
        inventory.add(inventoryInput);
        inventory.add(inventoryButton);
        
        inventoryPanel.add(addCash);
        inventoryPanel.add(cash);
        inventoryPanel.add(addInventory);
        inventoryPanel.add(stButton);
        inventoryPanel.add(chButton);
        inventoryPanel.add(piButton);
        inventoryPanel.add(saButton);
        inventoryPanel.add(inventory);
        
        JLabel space = new JLabel("                      ");
        currentCash = new JLabel("      Cash: $" + inputPanel.getCashier().getCash() + "      ");
        currentSteak =  new JLabel("         Steak: " + inputPanel.getCook().getFoodAmt("Steak") + "         ");
        currentChicken =  new JLabel("         Chicken: " + inputPanel.getCook().getFoodAmt("Chicken") + "         ");
        currentPizza =  new JLabel("         Pizza: " + inputPanel.getCook().getFoodAmt("Pizza") + "         ");
        currentSalad =  new JLabel("         Salad: " + inputPanel.getCook().getFoodAmt("Salad") + "         ");
        inventoryPanel.add(space);
        inventoryPanel.add(currentCash);
        inventoryPanel.add(currentSteak);
        inventoryPanel.add(currentChicken);
        inventoryPanel.add(currentPizza);
        inventoryPanel.add(currentSalad);
        
        
        controlPanel.addTab("Menu", restLabel);
        controlPanel.addTab("Waiters", waiters);
        controlPanel.addTab("Inventory", inventoryPanel);
        controlPanel.setVisible(false);
        bG.add(controlPanel, BorderLayout.WEST);
        
        double animFractionOfWindow = 500.0 / 650.0;
        Dimension animDim = new Dimension((int)(WINDOWX * animFractionOfWindow), WINDOWY);
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        animationPanel.setVisible(false);
        bG.add(animationPanel, BorderLayout.CENTER);
        
        timer = new Timer(6, this );
		timer.start();
    }
    
    /**
     * Returns the text from infoLabel
     */
    public String getInfoLabelText() {
    	return infoLabel.getText();
    }
    
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        currentPerson = person;
        if (person instanceof JoshWaiterRole) {
        	JoshWaiterRole waiter = (JoshWaiterRole) person;
        	stateCB.setVisible(true);
            stateCB.setText("Break?");
            //Should checkmark be there? 
            stateCB.setSelected(waiter.isOnBreak());
            stateCB.setEnabled(!waiter.isAboutToGoOnBreak());
        	infoLabel.setText(waiter.getName());
        } else {
        	stateCB.setVisible(false);
        }
        infoPanel.validate();
    }
    /**
     * Action listener method that reacts to the checkbox being clicked;
     * If it's the customer's checkbox, it will make him hungry
     * For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e) {
    	
    	currentCash.setText("      Cash: $" + inputPanel.getCashier().getCash() + "      ");
    	currentSteak.setText("         Steak: " + inputPanel.getCook().getFoodAmt("Steak") + "         ");
    	currentChicken.setText("         Chicken: " + inputPanel.getCook().getFoodAmt("Chicken") + "         ");
    	currentPizza.setText("         Pizza: " + inputPanel.getCook().getFoodAmt("Pizza") + "         ");
    	currentSalad.setText("         Salad: " + inputPanel.getCook().getFoodAmt("Salad") + "         ");
    	
    	if(e.getSource() == cashButton)
		{
			int num = 0;
			try {
				num = Integer.parseInt(cashInput.getText());
			} catch (NumberFormatException ex) {
			}

			inputPanel.getCashier().setCash(inputPanel.getCashier().getCash()+num);
			cashInput.setText("");
		}
    	
    	if(e.getSource() == inventoryButton)
		{
			if(stButton.isSelected())
			{
				int num = 0;
				try {
					num = Integer.parseInt(inventoryInput.getText());
				} catch (NumberFormatException ex) {
				}

				inputPanel.getCook().addFoodAmt("Steak", num);
				inventoryInput.setText("");
			}
			else if(chButton.isSelected())
			{
				int num = 0;
				try {
					num = Integer.parseInt(inventoryInput.getText());
				} catch (NumberFormatException ex) {
				}
				inputPanel.getCook().addFoodAmt("Chicken", num);
				inventoryInput.setText("");
			}
			else if(piButton.isSelected())
			{
				int num = 0;
				try {
					num = Integer.parseInt(inventoryInput.getText());
				} catch (NumberFormatException ex) {
				}
				inputPanel.getCook().addFoodAmt("Pizza", num);
				inventoryInput.setText("");
			}
			else if(saButton.isSelected())
			{
				int num = 0;
				try {
					num = Integer.parseInt(inventoryInput.getText());
				} catch (NumberFormatException ex) {
				}
				inputPanel.getCook().addFoodAmt("Salad", num);
				inventoryInput.setText("");
			}
		}
    	
        if (e.getSource() == stateCB) {
        	if (stateCB.getText().equals("Break?")) {
	            if (currentPerson instanceof JoshWaiterRole) {
	                JoshWaiterRole w = (JoshWaiterRole) currentPerson;
	                if (stateCB.isSelected()) {
		                w.msgWantToGoOnBreak();
		                stateCB.setEnabled(false);
	                } else {
	                	w.msgGoOffBreak();
	                }
	            }
        	}
        }

    }
    
    public void addCustomer(JoshCustomerRole c) {
    	inputPanel.addCustomer(c);
    }
    
    public void addWaiter(JoshWaiterRole w) {
    	inputPanel.addWaiter(w);
    }
    
    public void addPerson(String name) {
    	inputPanel.addPerson(name);
    }
    
    public void removePerson(String name) {
    	inputPanel.removePerson(name);
    }
    
    public void removeWaitingCustomer(JoshCustomerRole c) {
    	inputPanel.removeWaitingCustomer(c);
    }
    
    public void setWaiterEnabled(JoshWaiterRole w) {
        if (currentPerson instanceof JoshWaiterRole) {
            JoshWaiterRole waiter = (JoshWaiterRole) currentPerson;
            if (w.equals(waiter)) {
                stateCB.setEnabled(true);
                if (w.isOnBreak()) {
                	stateCB.setSelected(true);
                } else {
                	stateCB.setSelected(false);
                }
            }
        }
    }
    
    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Josh's Restaurant</u></h3><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$16.00</td></tr><tr><td>Chicken</td><td>$11.00</td></tr><tr><td>Salad</td><td>$6.00</td></tr><tr><td>Pizza</td><td>$9.00</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("  "), BorderLayout.EAST);
        restLabel.add(new JLabel("  "), BorderLayout.WEST);
    }
    
    public void changeView(boolean visible) {
    	animationPanel.setVisible(visible);
		controlPanel.setVisible(visible);
	}
}
