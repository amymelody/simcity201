package simcity.anjalirestaurant.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import restaurant.CashierAgent;
import restaurant.CookAgent;
import restaurant.CustomerAgent;
import restaurant.HostAgent;
import restaurant.MarketAgent;
import restaurant.WaiterAgent;
import simcity.anjalirestaurant.interfaces.Customer;
import simcity.anjalirestaurant.interfaces.Waiter;


/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private CherysHostRole host = new CherysHostRole("Sarah");
    private HostGui hostGui = new HostGui(host);
    
   
  //private  Waiter waiter = new Waiter("Jack");
 //  private WaiterGui waiterGui = new WaiterGui("waiter");
   
    private CherysCookRole cook = new CherysCookRole("Cook");
   
	
    private CherysCashierRole cashier = new CherysCashierRole("Cashier");
    
    private CherysMarketRole market1 = new CherysMarketRole("Market 1");
    private CherysMarketRole market2 = new CherysMarketRole("Market 2");
    private CherysMarketRole market3 = new CherysMarketRole("Market 3");

    
    private Vector<CherysCustomerRole> customers = new Vector<CherysCustomerRole>();
    private Vector<CherysWaiter> waiters = new Vector<CherysWaiter>();
    private Vector<CherysMarketRole> markets = new Vector<CherysMarketRole>();
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();
    private JPanel waiterPanel = new WaiterPanel(this,"Waiters");
    private RestaurantGui gui; //reference to main gui
    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        AnjaliCookGui cg= new AnjaliCookGui(cook, gui);
    	gui.animationPanel.addGui(cg);
    	cook.setGui(cg);
       // waiter.setGui(waiterGui);
        
        market1.startThread();
        cook.addMarket(market1);
        
        market1.setCashier(cashier);
        
        market2.startThread();
        cook.addMarket(market2);
        market2.setCashier(cashier);
        
        market3.startThread();
        cook.addMarket(market3);
        market3.setCashier(cashier);
        
        
        gui.animationPanel.addGui(hostGui);
        //gui.animationPanel.addGui(waiterGui);
        host.startThread();
        cashier.startThread();
        //waiter.startThread();
        cook.startThread();
        setLayout(new GridLayout(1, 2, 20, 20));
        group.setLayout(new GridLayout(1, 2, 10, 10));

        group.add(customerPanel);
        add(waiterPanel);
        initRestLabel();
        add(restLabel);
        add(group);
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
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$15.99</td></tr><tr><td>Chicken</td><td>$10.99</td></tr><tr><td>Salad</td><td>$5.99</td></tr><tr><td>Pizza</td><td>$8.99</td></tr></table><br></html>");

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
                CherysCustomer temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if(type.equals("Waiters")){
        	for(int i = 0; i < waiters.size(); i++){
        		CherysWaiter temp = waiters.get(i);
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
    public void addCustomer(String type, String name, boolean shouldBeHungry) {
    	
    	if (type.equals("Customers")) {
    		int xPos = 0;
    		CherysCustomerRole c = new CherysCustomerRole(name);	
    		customers.add(c);
    		xPos = (50*customers.size());
    		AnjaliCustomerGui g = new AnjaliCustomerGui(c, gui, xPos);

    		gui.animationPanel.addGui(g);// dw
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		System.out.println(waiters.size());
    		
    		if(shouldBeHungry == true){
    				g.setHungry();
    		
    		
    		c.startThread();
    		
    		}
    	}
    }
    

    public void addWaiter(String type, String name){
    	int yPos = 0;
    	if(type.equals("Waiters")){
    		CherysWaiterRole w = new CherysWaiterRole(name);
    		waiters.add(w);
    		yPos = (50*waiters.size());
    		WaiterGui wg= new WaiterGui(w, gui, yPos);
    		gui.animationPanel.addGui(wg);
    		w.setGui(wg);
    		w.setCook(cook);
    		w.setHost(host);
    		w.setCashier(cashier);
    		

    		w.startThread();
    		host.addWaiter(w);
    		System.out.println("waiter added to list");
    		
    	}
    	
    	}
    
 
    //waiter has wrong table number stored

    public RestaurantGui getGui()
    {
    	return gui;
    }
    
}
