package simcity.cherysrestaurant.gui;

import simcity.agent.Agent;
import simcity.cherysrestaurant.CherysCashierRole;
import simcity.cherysrestaurant.CherysCookRole;
import simcity.cherysrestaurant.CherysCustomerRole;
import simcity.cherysrestaurant.CherysHostRole;
import simcity.cherysrestaurant.CherysMarketRole;
import simcity.cherysrestaurant.CherysWaiterRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * Sub panel of RestaurantGui.
 * Top panel in frame that contains all the restaurant information,
 * including agent list. Has the waiter and customer input panels
 * and the menu.
 */
public class CherysRestaurantPanel extends JPanel
{

    //Host, cook, waiters and customers
	private Vector<Agent> agents = new Vector<Agent>();
    private CherysHostRole host;
//    private HostGui hostGui;
    private CherysCookRole cook;
    private CherysMarketRole marketA;
    private CherysMarketRole marketB;
    private CherysMarketRole marketC;
    private CherysCashierRole cashier;

    private Vector<CherysCustomerRole> customers = new Vector<CherysCustomerRole>();
    private Vector<CherysWaiterRole> waiters = new Vector<CherysWaiterRole>();

    private JPanel restLabel = new JPanel();
    private CherysListPanel custPanel = new CherysListPanel(this, "Customer");
    private CherysListPanel waitPanel = new CherysListPanel(this, "Waiter");
    private JPanel custGroup = new JPanel();
    private JPanel waitGroup = new JPanel();

    private CherysRestaurantGui gui; //reference to main gui

    /**
     * Constructor for RestaurantPanel. Sets up the gui.
     * @param gui reference to the main gui class
     */
    public CherysRestaurantPanel(CherysRestaurantGui gui) //* called from RestaurantGui
    {
        this.gui = gui;
        
        host = new CherysHostRole("HOST", gui);
//        hostGui = new HostGui(host);
        cook = new CherysCookRole("COOK");
        marketA = new CherysMarketRole("MARKET A");
        marketB = new CherysMarketRole("MARKET B");
        marketC = new CherysMarketRole("MARKET C");
        cashier = new CherysCashierRole("CASHIER");
        
//        host.setGui(hostGui);
        agents.add(host);
        agents.add(cook);
        agents.add(marketA);
        agents.add(marketB);
        agents.add(marketC);
        agents.add(cashier);
		cook.setMarket(marketA);
		cook.setMarket(marketB);
		cook.setMarket(marketC);
		marketA.setCook(cook);
		marketA.setCashier(cashier);
		marketA.setAmount("Pizza", 0);
		marketB.setCook(cook);
		marketB.setCashier(cashier);
		marketB.setAmount("Pizza", 2);
		marketC.setCook(cook);
		marketC.setCashier(cashier);
		marketC.setAmount("Pizza", 6);
//        gui.animationPanel.addGui(hostGui);
        
        host.startThread();
        cook.startThread();
        marketA.startThread();
        marketB.startThread();
        marketC.startThread();
        cashier.startThread();


        int panelRows = 1;
        int panelColumns = 2;
        int panelBuffer = 7;
        int groupRows = 1;
        int groupColumns = 2;
        int groupBuffer = 10;
        setLayout(new GridLayout(panelRows, panelColumns, panelBuffer, panelBuffer));
        custGroup.setLayout(new GridLayout(groupRows, groupColumns, groupBuffer, groupBuffer));
        waitGroup.setLayout(new GridLayout(groupRows, groupColumns, groupBuffer, groupBuffer));

        custGroup.add(custPanel);
        waitGroup.add(waitPanel);

        initRestLabel();
        add(custGroup);
        add(restLabel);
        add(waitGroup);
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel()
    {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Host</u></h3><table><tr><td></td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * Updates a customer on their new state when their associated
     * checkbox is clicked.
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     * @param tf new value of the customer's 'hunger' checkbox
     */
    public void updateInfo(String type, String name, boolean tf) //* called from ListPanel.updatePerson
    {
        if (type.equals("Customer"))
        {
            for (int i = 0; i < customers.size(); i++)
            {
                CherysCustomerRole temp = customers.get(i);
                if(temp.getName().equals(name))
                {
                    gui.updatePerson(temp, tf);
                    //gui.updateInfoPanel(person);
                }
            }
        }
        if (type.equals("Waiter"))
        {
            for (int i = 0; i < waiters.size(); i++)
            {
                CherysWaiterRole temp = waiters.get(i);
                if(temp.getName().equals(name))
                {
                    gui.updatePerson(temp, tf);
                    //gui.updateInfoPanel(person);
                }
            }
        }
    }

    /**
     * Sets the check box of the customer to clickable again
     * @param n name of the agent
     */
    public void enableCheck(String type, String n) //* called from RestaurantGui.setCustomerEnabled
    {
    	//Self note: check restaurantgui
    	if(type.equals("Customer"))
    	{
    		custPanel.enableCheck(type, n);
    	}
    	else if(type.equals("Waiter"))
        {
    		waitPanel.enableCheck(type, n);
        }
    }
    /**
     * Sets the value of the check box (busy) of the waiter
     * @param n name of agent
     * @param tf the value the check boolean is being assigned
     */
    public void setCheck(String n, boolean tf) //* called from RestaurantGui.setWaiterBusy
    {
    	//Self note: check restaurantgui
    	waitPanel.setCheck(n, tf);
    }

    /**
     * Adds a customer or waiter to the appropriate list
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     * @param tf the value of the check box's (hungry/busy) checked status
     */
    public void addPerson(String type, String name, boolean tf) //* called from ListPanel.addPerson
    {
    	if (type.equals("Customer"))
    	{
    		CherysCustomerRole c = new CherysCustomerRole(name);	
    		CherysCustomerGui g = new CherysCustomerGui(c, gui, customers.size());

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setGui(g);
    		c.setCashier(cashier);
    		customers.add(c);
    	    agents.add(c);
    		c.startThread();
    		gui.updatePerson(c, tf);
    	}
    	if (type.equals("Waiter"))
    	{
    		CherysWaiterRole w = new CherysWaiterRole(name, host, cook, cashier);	
    		CherysWaiterGui g = new CherysWaiterGui(w, gui, waiters.size());

    		gui.animationPanel.addGui(g);
    		w.setGui(g);
    		waiters.add(w);
    	    agents.add(w);
    		w.startThread();
    		gui.updatePerson(w, tf);
    		
    		host.setWaiter(w);
    	}
    }
    
    /**
     * Pauses all agents in the restaurant
     * @param isPausing if the agent is to be paused
     */
    public void applyPause(boolean isPausing) //* called from RestaurantGui.actionPerformed
    {
    	for(Agent agent : agents)
    	{
    		agent.pause(isPausing);
    	}
    }
}