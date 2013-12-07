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
	private JPanel controlPanel = new JPanel();
	
    /* inputPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private JoshRestaurantInputPanel inputPanel;
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JTextField infoLabel; //part of infoPanel
    private JCheckBox stateCB;//part of infoLabel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public JoshRestaurantGui(String n, BuildingsGui bG, CityDirectory cD) {
    	super(n, bG, cD);
    	inputPanel = new JoshRestaurantInputPanel(this, cD.getJoshCashier(), cD.getJoshCook(), cD.getJoshHost(), cD.getMarketCashiers());
    	
        int WINDOWX = 650;
        int WINDOWY = 500;
    	
        //controlPanel.setLayout(new BorderLayout());

        double inputFractionOfWindow = 150.0 / 650.0;
        Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
        inputPanel.setPreferredSize(inputDim);
        inputPanel.setMinimumSize(inputDim);
        inputPanel.setMaximumSize(inputDim);
        //controlPanel.add(inputPanel, BorderLayout.CENTER);
        inputPanel.setVisible(false);
        bG.add(inputPanel, BorderLayout.WEST);
        
        double animFractionOfWindow = 500.0 / 650.0;
        Dimension animDim = new Dimension((int)(WINDOWX * animFractionOfWindow), WINDOWY);
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        animationPanel.setVisible(false);
        bG.add(animationPanel, BorderLayout.CENTER);
        
        // Now, setup the info panel
//        double infoFractionOfWindow = 100.0 / 500.0;
//        Dimension infoDim = new Dimension((int)(WINDOWX * controlFractionOfWindow), (int)(WINDOWY * infoFractionOfWindow));
//        infoPanel = new JPanel();
//        infoPanel.setPreferredSize(infoDim);
//        infoPanel.setMinimumSize(infoDim);
//        infoPanel.setMaximumSize(infoDim);
//        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

//        stateCB = new JCheckBox();
//        stateCB.setText("Hungry?");
//        stateCB.addActionListener(this);
//        stateCB.setEnabled(false);
//        stateCB.setVisible(false);
//
//        infoPanel.setLayout(new GridLayout(1, 3, 30, 0));
//        
//        infoLabel = new JTextField(); 
//
//        infoPanel.add(infoLabel);
//        infoPanel.add(stateCB);
       // controlPanel.add(infoPanel, BorderLayout.NORTH);
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

//        if (person instanceof JoshCustomerRole) {
//            JoshCustomerRole customer = (JoshCustomerRole) person;
//            stateCB.setVisible(true);
//            stateCB.setText("Hungry?");
//          //Should checkmark be there? 
//            stateCB.setSelected(customer.getGui().isHungry());
//          //Is customer hungry? Hack. Should ask customerGui
//            stateCB.setEnabled(!customer.getGui().isHungry());
//          // Hack. Should ask customerGui
//            infoLabel.setText(customer.getName());
//        } else 
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
    
        if (e.getSource() == stateCB) {
//        	if (stateCB.getText().equals("Hungry?")) {
//	            if (currentPerson instanceof JoshCustomerRole) {
//	                JoshCustomerRole c = (JoshCustomerRole) currentPerson;
//	                c.getGui().setHungry(inputPanel.getNumCustomers());
//	                inputPanel.addCustomer(c);
//	                stateCB.setEnabled(false);
//	            }
//        	}
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
    
    public void changeView(boolean visible) {
    	animationPanel.setVisible(visible);
		inputPanel.setVisible(visible);
	}
}
