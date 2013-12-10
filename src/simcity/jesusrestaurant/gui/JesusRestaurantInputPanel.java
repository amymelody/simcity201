package simcity.jesusrestaurant.gui;

//import simcity.jesusrestaurant.JesusSharedDataWaiterRole;
//import simcity.jesusrestaurant.RevolvingStandMonitor;
import simcity.jesusrestaurant.JesusCustomerRole;
import simcity.jesusrestaurant.JesusHostRole;
import simcity.jesusrestaurant.JesusWaiterRole;
import simcity.jesusrestaurant.JesusCookRole;
import simcity.jesusrestaurant.JesusCashierRole;
import simcity.jesusrestaurant.gui.JesusCookGui;
import simcity.jesusrestaurant.gui.JesusRestaurantGui;
import simcity.market.MarketCashierRole;

import javax.swing.*;

import java.util.*;
import java.awt.*;
import java.util.Vector;

public class JesusRestaurantInputPanel extends JPanel 
{	
    //Host, cook, waiters and customers
    private JesusHostRole host;
    private JesusCookRole cook;
    private JesusCashierRole cashier;
    //private RevolvingStandMonitor stand = new RevolvingStandMonitor();
    private Vector<JesusCustomerRole> customers = new Vector<JesusCustomerRole>();
    private Vector<JesusWaiterRole> waiters = new Vector<JesusWaiterRole>();
    private Vector<MarketCashierRole> markets = new Vector<MarketCashierRole>();

    private JPanel restLabel = new JPanel();

    private JesusRestaurantGui gui; //reference to main gui
    private JesusCashierGui cashierGui;
    private JesusCookGui cookGui;

    public JesusRestaurantInputPanel(JesusRestaurantGui g, JesusCashierRole jesusCashierRole, JesusCookRole jesusCookRole, JesusHostRole jesusHostRole, ArrayList<MarketCashierRole> cashiers) {
    	setBounds(0, 0, 150, 500);
    	setLayout(new GridLayout(1, 2, 20, 20));
    	setSize(150, 500);
		setVisible(false);
    	
    	gui = g;
    	cashier = jesusCashierRole;
    	cook = jesusCookRole;
    	host = jesusHostRole;
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
		//cook.setStand(stand);
        
		cookGui = new JesusCookGui(cook);
		gui.animationPanel.addGui(cookGui);
		cook.setGui(cookGui);

        initRestLabel();
        add(restLabel);
    }
    
    /**
     * Returns the text from RestaurantGui's infoLabel
     */
   /* public String getInfoText() {
    	return gui.getInfoLabelText();
    }*/
    
    public void addMarketCashier(MarketCashierRole c) {
    	markets.add(c);
    }
    
    public void addCustomer(JesusCustomerRole c) {
    	customers.add(c);
    }
    
    public void addWaiter(JesusWaiterRole w) {
    	waiters.add(w);
    }
    
    public void setCashier(JesusCashierRole c) {
    	cashier = c;
    }
    
    public void setCook(JesusCookRole c) {
    	cook = c;
    }
    
    public void setHost(JesusHostRole h) {
    	host = h;
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        JLabel label = new JLabel();
        restLabel.setLayout(new BoxLayout((Container)restLabel, BoxLayout.Y_AXIS));
        restLabel.setLayout(new BorderLayout());
        label.setText(
                "<html><h3><u>Tonight's Staff</u></h3><table><tr><td>host:</td><td>" + host.getName() + "</td></tr></table><table><tr><td>cook:</td><td>" + cook.getName() + "</td></tr></table><table><tr><td>cashier:</td><td>" + cashier.getName() + "</td></tr></table><h3><u> Menu</u></h3><table><tr><td>Steak</td><td>$16.00</td></tr><tr><td>Chicken</td><td>$11.00</td></tr><tr><td>Salad</td><td>$6.00</td></tr><tr><td>Pizza</td><td>$9.00</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createRaisedBevelBorder());
        restLabel.add(label, BorderLayout.CENTER);
        restLabel.add(new JLabel("               "), BorderLayout.EAST);
        restLabel.add(new JLabel("               "), BorderLayout.WEST);
    }
}