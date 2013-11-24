package simcity.joshrestaurant.gui;

import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshHostRole;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.joshrestaurant.JoshCookRole;
import simcity.joshrestaurant.JoshCashierRole;
import simcity.joshrestaurant.JoshWaiterRole.CustomerState;
import simcity.market.MarketCashierRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import simcity.gui.BuildingGui;

public class JoshRestaurantInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel

    //Host, cook, waiters and customers
    private JoshHostRole host = new JoshHostRole("Sarah");
    private JoshCookRole cook = new JoshCookRole("John");
    private JoshCashierRole cashier = new JoshCashierRole("Jake");
    private Vector<MyCustomer> customers = new Vector<MyCustomer>();
    private Vector<JoshWaiterRole> waiters = new Vector<JoshWaiterRole>();
    private Vector<MarketCashierRole> markets = new Vector<MarketCashierRole>();
    private int numCustomers = 0;

    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private BuildingGui gui;
    private JoshRestaurantGui restGui; //reference to main gui
    private JoshCookGui cookGui;

    public JoshRestaurantInputPanel(BuildingGui g) {
        restGui = gui;
        this.gui = g;
		
		cook.addMarket(markets.get(0));
		cook.addMarket(markets.get(1));
		cook.addMarket(markets.get(2));
		cook.setHost(host);
        
		cookGui = new JoshCookGui(cook, gui);
		gui.animationPanel.addGui(cookGui);
		cook.setGui(cookGui);
	
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 3, 10, 10));

        group.add(customerPanel);
        group.add(waiterPanel);

        initRestLabel();
        add(restLabel);
        add(group);
    }
    
    /**
     * Returns the text from RestaurantGui's infoLabel
     */
    public String getInfoText() {
    	return restGui.getInfoLabelText();
    }
    
    public void addCustomer(JoshCustomerRole c) {
    	for (MyCustomer mc : customers) {
    		if (mc.cust == c) {
    			mc.waiting = true;
    		}
    	}
    }
    
    public void removeCustomer(JoshCustomerRole c) {
    	for (MyCustomer mc : customers) {
    		if (mc.cust == c) {
    			mc.waiting = false;
    		}
    	}
    	int total = 0;
    	for (MyCustomer mc : customers) {
    		if (mc.waiting) {
    			mc.cust.getGui().moveForwardInWait(total);
    			total++;
    		}
    	}
    }
    
    public int getNumCustomers() {
    	int total = 0;
    	for (MyCustomer mc : customers) {
    		if (mc.waiting) {
    			total++;
    		}
    	}
    	return total;
    }
    
    /*public void pauseAgents() {
    	host.pause();
    	cook.pause();
    	cashier.pause();
    	for (JoshWaiterRole w : waiters) {
    		w.pause();
    	}
    	for (MyCustomer mc : customers) {
    		mc.cust.pause();
    	}
    	for (JoshMarketRole m : markets) {
    		m.pause();
    	}
    }
    
    public void resumeAgents() {
    	host.resume();
    	cook.resume();
    	for (JoshWaiterRole w : waiters) {
    		w.resume();
    	}
    	for (MyCustomer mc : customers) {
    		mc.cust.resume();
    	}
    	for (JoshMarketRole m : markets) {
    		m.resume();
    	}
    }*/

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        //restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><table><tr><td>cook:</td><td>" + cook.getName() + "</td></tr></table><table><tr><td>cashier:</td><td>" + cashier.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$16.00</td></tr><tr><td>Chicken</td><td>$11.00</td></tr><tr><td>Salad</td><td>$6.00</td></tr><tr><td>Pizza</td><td>$9.00</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }

    /**
     * When a customer or waiter is clicked, this function calls
     * updatedInfoPanel() from the main gui so that person's information
     * will be shown
     *
     * @param type indicates whether the person is a customer or waiter
     * @param name name of person
     */
    public void showInfo(String type, String name) {

        if (type.equals("Customers")) {

            for (int i = 0; i < customers.size(); i++) {
                JoshCustomerRole temp = customers.get(i).cust;
                if (temp.getName() == name)
                    restGui.updateInfoPanel(temp);
            }
        }
        if (type.equals("Waiters")) {

            for (int i = 0; i < waiters.size(); i++) {
                JoshWaiterRole temp = waiters.get(i);
                if (temp.getName() == name)
                    restGui.updateInfoPanel(temp);
            }
        }
    }

    /**
     * Adds a customer or waiter to the appropriate list
     *
     * @param type indicates whether the person is a customer or waiter (later)
     * @param name name of person
     */
    public void addPerson(String type, String name) {

    	if (type.equals("Customers")) {
    		JoshCustomerRole c = new JoshCustomerRole(name);	
    		JoshCustomerGui g = new JoshCustomerGui(c, restGui);

    		restGui.animationPanel.addGui(g);
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		customers.add(new MyCustomer(c));
    		c.startThread();
    	}
    	if (type.equals("Waiters")) {
    		JoshWaiterRole w = new JoshWaiterRole(name);	
    		JoshWaiterGui g = new JoshWaiterGui(w, restGui, waiters.size()+1);

    		restGui.animationPanel.addGui(g);
     		w.setHost(host);
     		w.setCashier(cashier);
     		w.setCook(cook);
     		w.setGui(g);
     		w.setGui(cookGui);
     		waiters.add(w);
     		w.startThread();
     		host.addWaiter(w);
    	}
    }

    private class MyCustomer {
		JoshCustomerRole cust;
		boolean waiting;

		MyCustomer(JoshCustomerRole c) {
			cust = c;
			waiting = false;
		}
    }
}
