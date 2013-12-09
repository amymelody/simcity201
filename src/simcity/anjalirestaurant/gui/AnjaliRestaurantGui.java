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

import simcity.Anjalirestaurant.AnjaliCustomerRole;
import simcity.Anjalirestaurant.AnjaliWaiterRole;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels
 */
public class AnjaliRestaurantGui extends JFrame implements ActionListener {
    /* The GUI has two frames, the control frame (in variable gui) 
     * and the animation frame, (in variable animationFrame within gui)
     */
	JFrame animationFrame = new JFrame("Restaurant Animation");
	AnjaliRestaurantAnimationPanel animationPanel = new AnjaliRestaurantAnimationPanel();

    /* restPanel holds 2 panels
     * 1) the staff listing, menu, and lists of current customers all constructed
     *    in RestaurantPanel()
     * 2) the infoPanel about the clicked Customer (created just below)
     */    
    private AnjaliRestaurantPanel restPanel = new AnjaliRestaurantPanel(this);
    
    /* infoPanel holds information about the clicked customer, if there is one*/
    private JPanel infoPanel;
    private JLabel infoLabel; //part of infoPanel
    public JCheckBox stateCB;//part of infoLabel
    public JCheckBox stateWB;
    private JPanel waiterPanel;
    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class.
     * Sets up all the gui components.
     */
    public AnjaliRestaurantGui() {
        int WINDOWX = 1000;
        int WINDOWY = 1000;
       
        
        /*
        animationFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        animationFrame.setBounds(100+WINDOWX, 50 , WINDOWX+100, WINDOWY+100);
        animationFrame.setVisible(true);
    	animationFrame.add(animationPanel); 
    	*/
    	setBounds(50, 50, WINDOWX, WINDOWY);

        setLayout(new BoxLayout((Container) getContentPane(), 
        		BoxLayout.Y_AXIS));

        Dimension restDim = new Dimension(WINDOWX, (int) (WINDOWY * .3));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add((restPanel), BorderLayout.NORTH);
        
        // Now, setup the info panel
        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .1));
        infoPanel = new JPanel();
        infoPanel.setPreferredSize(infoDim);
        infoPanel.setMinimumSize(infoDim);
        infoPanel.setMaximumSize(infoDim);
        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));

        stateCB = new JCheckBox();
        stateCB.setVisible(false);
        stateCB.addActionListener(this);

        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
        
        infoLabel = new JLabel(); 
        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
        infoPanel.add(infoLabel);
        infoPanel.add(stateCB);
        add((infoPanel), BorderLayout.CENTER);
        
        Dimension animDim = new Dimension(500, (int) (WINDOWY * .6));
         animationPanel.setPreferredSize(animDim);
         animationPanel.setMinimumSize(animDim);
         animationPanel.setMaximumSize(animDim);
         add((animationPanel), BorderLayout.SOUTH);
        
         
         Dimension waiterDim = new Dimension(WINDOWX, WINDOWY);
         waiterPanel = new JPanel();
         waiterPanel.setPreferredSize(infoDim);
         waiterPanel.setMinimumSize(infoDim);
         waiterPanel.setMaximumSize(infoDim);
         waiterPanel.setBorder(BorderFactory.createTitledBorder("Waiters"));
         waiterPanel.setVisible(true);
         add((waiterPanel), BorderLayout.EAST);
         
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
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
        AnjaliRestaurantGui gui = new AnjaliRestaurantGui();
        gui.setTitle("csci201 Restaurant");
        gui.setVisible(true);
        gui.setLocationRelativeTo(null);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}