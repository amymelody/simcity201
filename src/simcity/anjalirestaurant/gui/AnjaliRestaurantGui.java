	package simcity.anjalirestaurant.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import simcity.CityDirectory;
import simcity.anjalirestaurant.AnjaliCustomerRole;
import simcity.anjalirestaurant.AnjaliWaiterRole;
import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.joshrestaurant.gui.JoshRestaurantInputPanel;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class AnjaliRestaurantGui extends BuildingGui implements ActionListener {

	AnjaliRestaurantAnimationPanel animationPanel = new AnjaliRestaurantAnimationPanel();

     
    private AnjaliRestaurantInputPanel inputPanel;
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JTabbedPane controlPanel = new JTabbedPane();
    private JLabel infoLabel; //part of infoPanel
    public JCheckBox stateCB;//part of infoLabel
    public JCheckBox stateWB;
    private JPanel waiterPanel;
    private Object currentPerson;
    private JPanel restLabel = new JPanel();
    private JPanel inventoryPanel = new JPanel();
    
   /* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public AnjaliRestaurantGui(String n, BuildingsGui bG, CityDirectory cD) {
       super(n, bG, cD);
       inputPanel = new AnjaliRestaurantInputPanel(this, cD.getAnjaliCashier(), cD.getAnjaliCook(), cD.getAnjaliHost(), cD.getMarketCashiers());
       
    	int WINDOWX = 650;
        int WINDOWY = 500;
       
        double controlFractionOfWindow = 150.0 / 650.0;
        double infoFractionOfWindow = 100.0 / 500.0;
        Dimension infoDim = new Dimension((int)(WINDOWX * controlFractionOfWindow), (int)(WINDOWY * infoFractionOfWindow));
        
       
    	
        
        // Now, setup the info panel
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.PAGE_AXIS));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);

        Dimension controlDim = new Dimension((int)(WINDOWX * controlFractionOfWindow), WINDOWY);
        controlPanel.setPreferredSize(controlDim);
        controlPanel.setMinimumSize(controlDim);
        controlPanel.setMaximumSize(controlDim);
       
         JPanel waiters = new JPanel();
         waiters.setLayout(new BorderLayout());
         waiters.add(infoPanel, BorderLayout.NORTH);
         waiters.add(inputPanel, BorderLayout.CENTER);
         
       
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
         
    }
    /**
     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
     * changes the information panel to hold that person's info.
     *
     * @param person customer (or waiter) object
     */
    public void updateInfoPanel(Object person) {
        stateCB.setVisible(true);
        //stateWB.setVisible(true);
        currentPerson = person;

        if (person instanceof AnjaliCustomerRole) {
            AnjaliCustomerRole customer = (AnjaliCustomerRole) person;
            stateCB.setText("Hungry?");
          //Should checkmark be there? 
            stateCB.setSelected(customer.getGui().isHungry());
          //Is customer hungry? Hack. Should ask customerGui
            stateCB.setEnabled(!customer.getGui().isHungry());
          // Hack. Should ask customerGui
            infoLabel.setText(
               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
        }
        
       if(person instanceof AnjaliWaiterRole){
    	   AnjaliWaiterRole waiter = (AnjaliWaiterRole) person;
    	   stateCB.setText("Wants break?");
    	   //Setting checkmark false because waiter doesn't want break immediately
    	   stateCB.setSelected(false);
    	   stateCB.setEnabled(true);
    	   infoLabel.setText(
                   "<html><pre>     Name: " + waiter.getName() + " </pre></html>");
    			  
    	   
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
            if (currentPerson instanceof AnjaliCustomerRole) {
                AnjaliCustomerRole c = (AnjaliCustomerRole) currentPerson;
                c.getGui().setHungry();
                stateCB.setEnabled(false);
            }
        }
        if(e.getSource() == stateCB){
        	if(currentPerson instanceof AnjaliWaiterRole){
        		AnjaliWaiterRole w = (AnjaliWaiterRole) currentPerson;
        		System.out.println("Waiter wants break button selected");
        		w.msgWantsBreak();
        		stateCB.setEnabled(false);
        		
        	}
        }
    }
    /**
     * Message sent from a customer gui to enable that customer's
     * "I'm hungry" checkbox.
     *
     * @param c reference to the CustomerAgent
     */
    public void setCustomerEnabled(AnjaliCustomerRole c) {
        if (currentPerson instanceof AnjaliCustomerRole) {
            AnjaliCustomerRole cust = (AnjaliCustomerRole) currentPerson;
            if (c.equals(cust)) {
                stateCB.setEnabled(true);
                stateCB.setSelected(false);
            }
        }
    }
    
    public void addCustomer(AnjaliCustomerRole c) {
    	inputPanel.addCustomer(c);
    }
    
    public void addWaiter(AnjaliWaiterRole w) {
    	inputPanel.addWaiter(w);
    }
    
    public void addPerson(String name) {
    	inputPanel.addPerson(name);
    }
    
    public void removePerson(String name) {
    	inputPanel.removePerson(name);
    }
    /**
     * Main routine to get gui started
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Anjali's Restaurant</u></h3><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$16.00</td></tr><tr><td>Chicken</td><td>$11.00</td></tr><tr><td>Salad</td><td>$6.00</td></tr><tr><td>Pizza</td><td>$9.00</td></tr></table><br></html>");

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