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
import restaurant.interfaces.Customer;
import restaurant.interfaces.Waiter;


/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class RestaurantPanel extends JPanel {

    //Host, cook, waiters and customers
    private HostAgent host = new HostAgent("Sarah");
    private HostGui hostGui = new HostGui(host);
    
   
  //private  Waiter waiter = new Waiter("Jack");
 //  private WaiterGui waiterGui = new WaiterGui("waiter");
   
    private CookAgent cook = new CookAgent("Cook");
   
	
    private CashierAgent cashier = new CashierAgent("Cashier");
    
    private MarketAgent market1 = new MarketAgent("Market 1");
    private MarketAgent market2 = new MarketAgent("Market 2");
    private MarketAgent market3 = new MarketAgent("Market 3");

    
    private Vector<CustomerAgent> customers = new Vector<CustomerAgent>();
    private Vector<Waiter> waiters = new Vector<Waiter>();
    private Vector<MarketAgent> markets = new Vector<MarketAgent>();
    private JPanel restLabel = new JPanel();
    private ListPanel customerPanel = new ListPanel(this, "Customers");
    private JPanel group = new JPanel();
    private JPanel waiterPanel = new WaiterPanel(this,"Waiters");
    private RestaurantGui gui; //reference to main gui
    
    public RestaurantPanel(RestaurantGui gui) {
        this.gui = gui;
        CookGui cg= new CookGui(cook, gui);
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
                Customer temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        if(type.equals("Waiters")){
        	for(int i = 0; i < waiters.size(); i++){
        		Waiter temp = waiters.get(i);
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
    		CustomerAgent c = new CustomerAgent(name);	
    		customers.add(c);
    		xPos = (50*customers.size());
    		CustomerGui g = new CustomerGui(c, gui, xPos);

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
    		WaiterAgent w = new WaiterAgent(name);
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
