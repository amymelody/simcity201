package simcity.joshrestaurant.gui;

import simcity.joshrestaurant.JoshSharedDataWaiterRole;
import simcity.joshrestaurant.RevolvingStandMonitor;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshHostRole;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.joshrestaurant.JoshCookRole;
import simcity.joshrestaurant.JoshCashierRole;
import simcity.market.MarketCashierRole;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

import simcity.gui.BuildingGui;

public class JoshRestaurantInputPanel extends JPanel 
{	
    //Host, cook, waiters and customers
    private JoshHostRole host;
    private JoshCookRole cook;
    private JoshCashierRole cashier;
    private RevolvingStandMonitor stand = new RevolvingStandMonitor();
    private Vector<MyCustomer> customers = new Vector<MyCustomer>();
    private Vector<JoshWaiterRole> waiters = new Vector<JoshWaiterRole>();
    private Vector<MarketCashierRole> markets = new Vector<MarketCashierRole>();
    private int numCustomers = 0;

    private ListPanel waiterPanel = new ListPanel(this, "Waiters");
    private JPanel group = new JPanel();

    private JoshRestaurantGui gui; //reference to main gui
    private JoshCookGui cookGui;

    public JoshRestaurantInputPanel(JoshRestaurantGui g, JoshCashierRole ca, JoshCookRole co, JoshHostRole h, ArrayList<MarketCashierRole> cashiers) {
    	gui = g;
    	host = h;
    	cashier = ca;
    	cook = co;
    	for (MarketCashierRole c : cashiers) {
    		markets.add(c);
    	}
		
    	for (MarketCashierRole c : markets) {
    		if (c.getJobLocation().equals("market1")) {
    			cook.addMarket(c,"market1");
    		} else {
    			cook.addMarket(c,"market2");
    		}
    	}
    	
		cook.setHost(host);
		cook.setCashier(cashier);
		cook.setStand(stand);
        
		cookGui = new JoshCookGui(cook);
		gui.animationPanel.addGui(cookGui);
		cook.setGui(cookGui);
	
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 3, 10, 10));

        group.add(waiterPanel);

        add(group);
    }
    
    /**
     * Returns the text from RestaurantGui's infoLabel
     */
    public String getInfoText() {
    	return gui.getInfoLabelText();
    }
    
    public void addMarketCashier(MarketCashierRole c) {
    	markets.add(c);
    }
    
    public void setCashier(JoshCashierRole c) {
    	cashier = c;
    }
    
    public void setCook(JoshCookRole c) {
    	cook = c;
    }
    
    public void setHost(JoshHostRole h) {
    	host = h;
    }
    
    public void addCustomer(JoshCustomerRole c) {
    	JoshCustomerGui g = new JoshCustomerGui(c, this);
		gui.animationPanel.addGui(g);
		c.setHost(host);
		c.setCashier(cashier);
		c.setGui(g);
		customers.add(new MyCustomer(c));
    }
    
    public void addWaiter(JoshWaiterRole w) {
    	if (w instanceof JoshSharedDataWaiterRole) {
    		JoshSharedDataWaiterRole waiter = (JoshSharedDataWaiterRole)w;
    		waiter.setStand(stand);
    	}
    	
		JoshWaiterGui g = new JoshWaiterGui(w, this, waiters.size()+1);
		gui.animationPanel.addGui(g);
 		w.setHost(host);
 		w.setCashier(cashier);
 		w.setCook(cook);
 		w.setGui(g);
 		w.setGui(cookGui);
 		w.setRestGui(gui);
 		waiters.add(w);
 		host.addWaiter(w);
    }
    
    public void addPerson(String name) {
    	waiterPanel.addPerson(name);
    }
    
    public void removePerson(String name) {
    	waiterPanel.removePerson(name);
    }
    
    public void addWaitingCustomer(JoshCustomerRole c) {
    	for (MyCustomer mc : customers) {
    		if (mc.cust == c) {
    			mc.waiting = true;
    		}
    	}
    }
    
    public void removeWaitingCustomer(JoshCustomerRole c) {
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

    public JoshHostRole getHost() {
    	return host;
    }
    
    public JoshCookRole getCook() {
    	return cook;
    }
    
    public JoshCashierRole getCashier() {
    	return cashier;
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

       if (type.equals("Waiters")) {
            for (int i = 0; i < waiters.size(); i++) {
                JoshWaiterRole temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
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
