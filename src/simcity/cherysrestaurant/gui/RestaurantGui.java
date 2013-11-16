package simcity.cherysrestaurant.gui;

import simcity.cherysrestaurant.CustomerAgent;
import simcity.cherysrestaurant.WaiterAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels, along with the 'main' function
 */
public class RestaurantGui extends JFrame implements ActionListener
{
	JFrame animationFrame = new JFrame("Restaurant Animation");
	CherysRestaurantAnimationPanel animationPanel = new CherysRestaurantAnimationPanel();
	
    private RestaurantPanel restPanel = new RestaurantPanel(this);
    
//    /* infoPanel holds information about the clicked customer, if there is one*/
//    private JPanel infoPanel;
//    private JLabel infoLabel; //part of infoPanel
//    private JCheckBox stateCB;//part of infoLabel
    
    private JPanel idPanel;
    private JLabel idLabel; //part of idPanel
    private ImageIcon image; //part of idPanel
    private JLabel imageLabel; //part of idPanel
    private JButton pauseButton; //part of idPanel

    private Object currentPerson;/* Holds the agent that the info is about.
    								Seems like a hack */

    /**
     * Constructor for RestaurantGui class. Sets up all the gui components.
     */
    public RestaurantGui()
    {
        int WINDOWX = 750;
        int WINDOWY = 500;
        int bufferFromTopOfScreen = 50;
        int bufferFromSideOfScreen = 50;

    	setBounds(bufferFromSideOfScreen, bufferFromTopOfScreen, WINDOWX, WINDOWY);

    	BorderLayout frameLayout = new BorderLayout();
    	setLayout(frameLayout);

        //main restaurant panel
    	double restaurantFractionOfWindow = .45;
        Dimension restDim = new Dimension(WINDOWX, (int)(WINDOWY * restaurantFractionOfWindow));
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        add(restPanel, frameLayout.NORTH);
        
        //animation panel
    	double animationFractionOfWindow = .4;
        Dimension animDim = new Dimension(WINDOWX, (int)(WINDOWY * animationFractionOfWindow));
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        add(animationPanel, frameLayout.CENTER);
        
        // Now, setup the info panel                                                      //DELETE
//        Dimension infoDim = new Dimension(WINDOWX, (int) (WINDOWY * .125));
//        infoPanel = new JPanel();
//        infoPanel.setPreferredSize(infoDim);
//        infoPanel.setMinimumSize(infoDim);
//        infoPanel.setMaximumSize(infoDim);
//        infoPanel.setBorder(BorderFactory.createTitledBorder("Information"));
//
//        stateCB = new JCheckBox();
//        stateCB.setVisible(false);
//        stateCB.addActionListener(this);
//
//        infoPanel.setLayout(new GridLayout(1, 2, 30, 0));
//        
//        infoLabel = new JLabel(); 
//        infoLabel.setText("<html><pre><i>Click Add to make customers</i></pre></html>");
//        infoPanel.add(infoLabel);
//        infoPanel.add(stateCB);
//        add(infoPanel, frameLayout.NORTH);
        
        //My section
        double idPanelFractionOfWindow = .15;
        int bufferBetweenLabels = 10;
        int bufferAboveLabels = 0;
        Dimension idDim = new Dimension(WINDOWX, (int)(WINDOWY * idPanelFractionOfWindow));
        idPanel = new JPanel();
        idPanel.setPreferredSize(idDim);
        idPanel.setMinimumSize(idDim);
        idPanel.setMaximumSize(idDim);
        idPanel.setBorder(BorderFactory.createTitledBorder("Extras"));

        idPanel.setLayout(new FlowLayout(FlowLayout.LEFT, bufferBetweenLabels, bufferAboveLabels));

        image = new ImageIcon("me.jpg");
        imageLabel = new JLabel(image);
        idPanel.add(imageLabel);
        
        idLabel = new JLabel(); 
        idLabel.setText("<html><pre><i>Layout edited by Cherys Fair\t\t\t\t\t\t\t\t\t\t\t\t\\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</i></pre></html>");
        idPanel.add(idLabel);
        
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(this);
        idPanel.add(pauseButton);
        
        add(idPanel, frameLayout.SOUTH);
        
    }
//    /**
//     * updateInfoPanel() takes the given customer (or, for v3, Host) object and
//     * changes the information panel to hold that person's info.
//     *
//     * @param person customer (or waiter) object
//     */
//    public void updateInfoPanel(Object person)
//    {
//        stateCB.setVisible(true);
//        currentPerson = person;
//
//        if (person instanceof CustomerAgent)
//        {
//            CustomerAgent customer = (CustomerAgent) person;
//            stateCB.setText("Hungry?");
//          //Should checkmark be there? 
//            stateCB.setSelected(customer.getGui().isHungry());
//          //Is customer hungry? Hack. Should ask customerGui
//            stateCB.setEnabled(!customer.getGui().isHungry());
//          // Hack. Should ask customerGui
//            infoLabel.setText(
//               "<html><pre>     Name: " + customer.getName() + " </pre></html>");
//        }
//        infoPanel.validate();
//    }
    /**
     * Update's the customer's internal hunger variable
     * @param person reference to the person the information is about
     * @param tf the value of the customer's 'hungry' check box
     */
    public void updatePerson(Object person, boolean tf) //* called in RestaurantPanel.addPerson/.updateInfo
    {
        currentPerson = person;
        if(person instanceof CustomerAgent) 
        {
        	CustomerAgent customer = (CustomerAgent) person;
        	if(tf)
        	{
        		customer.getGui().setHungry();
        	}
        }
        if(person instanceof WaiterAgent)
        {
        	WaiterAgent waiter = (WaiterAgent) person;
        	waiter.getGui().setTired(tf);
        }
    }
    /**
     * Action listener method that reacts to the pause button being
     * clicked. For v3, it will propose a break for the waiter.
     */
    public void actionPerformed(ActionEvent e)
    {
//        if (e.getSource() == stateCB)
//        {
//            if (currentPerson instanceof CustomerAgent)
//            {
//                CustomerAgent c = (CustomerAgent) currentPerson;
//                c.getGui().setHungry();
//                stateCB.setEnabled(false);
//            }
//        }
        if(e.getSource() == pauseButton)
        {
        	if(pauseButton.getText().equals("Pause"))
        	{
	        	restPanel.applyPause(true);
	        	//System.out.print("pause applied");
	        	pauseButton.setText("Restart");
        	}
        	else if(pauseButton.getText().equals("Restart"))
        	{
	        	restPanel.applyPause(false);
	        	pauseButton.setText("Pause");
        	}
        }
    }
//    public void setHungry()
//    {
//    	if (currentPerson instanceof CustomerAgent)
//    	{
//    		CustomerAgent c = (CustomerAgent) currentPerson;
//    		c.getGui().setHungry();
//    	}
//    }
    /**
     * Message sent from the host agent when a customer leaves to enable
     * that customer's "I'm hungry" checkbox.
     * @param c reference to the customer
     */
    public void setCustomerEnabled(CustomerAgent c) //* called from CustomerGui.updatePosition
    {
    	restPanel.enableCheck("Customer", c.getName());
//        if (currentPerson instanceof CustomerAgent)
//        {
//            CustomerAgent cust = (CustomerAgent) currentPerson;
//            if (c.equals(cust))
//            {
//                //restPanel.enableCheck();
//            }
//        }
    }
    /**
     * Changes the gui to let the user know that the waiter is serving
     * their max number of customers
     * @param w reference to the waiter agent
     * @param tf the value the waiter's "busy" check box is being assigned
     */
    public void setWaiterEnabled(WaiterAgent w) //* called from ~HostAgent.assignCustomer
    {
    	restPanel.enableCheck("Waiter", w.getName());
    }
    public void setWaiterBusy(WaiterAgent w, boolean tf) //*called from WaiterGui
    {
    	restPanel.setCheck(w.getName(), tf);
    	restPanel.enableCheck("Waiter", w.getName());
    }
    
    
    /**
     * Main routine to get gui started
     */
    public static void main(String[] args)
    {
        RestaurantGui gui = new RestaurantGui();
        gui.setTitle("Cherys' Restaurant");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}