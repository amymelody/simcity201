package simcity.anjalirestaurant.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import simcity.anjalirestaurant.AnjaliCashierRole;
import simcity.anjalirestaurant.AnjaliCookRole;
import simcity.anjalirestaurant.AnjaliCustomerRole;
import simcity.anjalirestaurant.AnjaliHostRole;
import simcity.anjalirestaurant.AnjaliWaiterRole;
import simcity.anjalirestaurant.RevolvingStandMonitor;
import simcity.gui.BuildingGui;
import simcity.joshrestaurant.JoshCashierRole;
import simcity.joshrestaurant.JoshCookRole;
import simcity.joshrestaurant.JoshHostRole;
import simcity.joshrestaurant.JoshSharedDataWaiterRole;
import simcity.market.MarketCashierRole;

public class AnjaliRestaurantInputPanel extends JPanel 
{
	//Super container for all gui panels other than animation--closest to the restaurant panel
	
	BuildingGui bgui;
	
	
		
	    //Host, cook, waiters and customers
	    private AnjaliHostRole host;
	    private AnjaliCookRole cook;
	    private AnjaliCashierRole cashier;
	   
	    private RevolvingStandMonitor stand = new RevolvingStandMonitor();
	    private Vector<AnjaliCustomerRole> customers = new Vector<AnjaliCustomerRole>();
	    private Vector<AnjaliWaiterRole> waiters = new Vector<AnjaliWaiterRole>();
	    private Vector<MarketCashierRole> markets = new Vector<MarketCashierRole>();
	    
	    private JPanel restLabel = new JPanel();
	    private JPanel group = new JPanel();
	   
	    private JPanel waiterPanel = new AnjaliWaiterPanel(this,"Waiters");
	    
	    private AnjaliRestaurantGui gui; //reference to main gui
	   private AnjaliCookGui cg;
	    
	    public AnjaliRestaurantInputPanel(AnjaliRestaurantGui gui, AnjaliCookRole co, AnjaliCashierRole ca, AnjaliHostRole h, ArrayList<MarketCashierRole> cashiers) {
	        this.gui = gui;
	        host = h;
	        cashier = ca;
	        cook = co;
	        for(MarketCashierRole c : cashiers){
	        	markets.add(c);
	        }
	        for (MarketCashierRole c : markets) {
	    		if (c.getJobLocation().equals("market1")) {
	    			cook.addMarket(c,"market1");
	    		} else {
	    			cook.addMarket(c,"market2");
	    		}
	    	}
	       
	        
	        cook.setStand(stand);
	        
	        cg= new AnjaliCookGui(cook, gui);
	    	gui.animationPanel.addGui(cg);
	    	cook.setGui(cg);
	      
	    	
	        
	        
	        
	    	 setLayout(new GridLayout(1, 2, 20, 20));
	         group.setLayout(new GridLayout(1, 3, 10, 10));

	         group.add(waiterPanel);

	         add(group);
	    }
	    public void addMarketCashier(MarketCashierRole c) {
	    	markets.add(c);
	    }
	    
	    public void setCashier(AnjaliCashierRole c) {
	    	cashier = c;
	    }
	    
	    public void setCook(AnjaliCookRole c) {
	    	cook = c;
	    }
	    
	    public void setHost(AnjaliHostRole h) {
	    	host = h;
	    }
	    
	    public void showInfo(String type, String name) {

	        if (type.equals("Customers")) {

	            for (int i = 0; i < customers.size(); i++) {
	                AnjaliCustomerRole temp = customers.get(i);
	                if (temp.getName() == name)
	                    gui.updateInfoPanel(temp);
	            }
	        }
	        if(type.equals("Waiters")){
	        	for(int i = 0; i < waiters.size(); i++){
	        		AnjaliWaiterRole temp = waiters.get(i);
	        		if(temp.getName() == name){
	        			gui.updateInfoPanel(temp);
	        		}
	        	}
	        }
	    }

	    /**
	     * Adds a customer or waiter to the appropriate list
	     *
	     * @param type indicates whether the person is a customer or waiter (later)
	     * @param name name of person
	     */
	   
	    
	    public void addCustomer(AnjaliCustomerRole c) {
	    	
	    	
	    		int xPos = 0;
	    		customers.add(c);
	    		xPos = (50*customers.size());
	    		AnjaliCustomerGui g = new AnjaliCustomerGui(c, gui, xPos);

	    		gui.animationPanel.addGui(g);// dw
	    		c.setHost(host);
	    		c.setCashier(cashier);
	    		c.setGui(g);
	    		System.out.println(waiters.size());
	    		g.setHungry();		
	    	
	    }
	    

	    public void addWaiter(AnjaliWaiterRole w){
	    	int yPos = 0;
	    	/*
	    	if (w instanceof AnjaliSharedDataWaiterRole) {
	    		AnjaliSharedDataWaiterRole waiter = (AnjaliSharedDataWaiterRole)w;
	    		waiter.setStand(stand);
	    	}
	    	*/
	    		AnjaliWaiterGui wg = new AnjaliWaiterGui(w, this, yPos);
	    		waiters.add(w);
	    		yPos = (50*waiters.size());
	    		gui.animationPanel.addGui(wg);
	    		w.setGui(wg);
	    		w.setCook(cook);
	    		w.setHost(host);
	    		w.setCashier(cashier);
	    		host.addWaiter(w);
	    		
	    	
	    	
	    	}
	    
	public AnjaliHostRole getHost(){
		return host;
	}
	
	public AnjaliCookRole getCook(){
		return cook;
	}
	
	public AnjaliCashierRole getCashier(){
		return cashier;
	}
	
	    //waiter has wrong table number stored

	    public AnjaliRestaurantGui getGui()
	    {
	    	return gui;
	    }
	    public AnjaliRestaurantInputPanel(BuildingGui g)
		{
			bgui = g;
		}
		 
	    
	}


