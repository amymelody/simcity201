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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class JesusRestaurantInputPanel extends JPanel implements ActionListener
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
    private JLabel restInfo = new JLabel();
    //private JTabbedPane group = new JTabbedPane();

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
    
    public void actionPerformed(ActionEvent e) {
    	initRestLabel();
    }
    
    private void initRestLabel() {
        restLabel.setLayout(new BorderLayout());
        restInfo.setText(
                "<html><br/><h3>Welcome to La Cocina!</h3><h3 align=center color=red>" + host.openPanel() + "</h3><h4><u>Tonight's Staff</u></h4><table><tr><td>Host:</td><td>" + host.getName() + "</td></tr><tr><td>Cashier:</td><td>" + cashier.getName() + "</td></tr><tr><td>Cook:</td><td>" + cook.getName() + "</td></tr></table><h4><u> Menu</u></h4><table><tr><td>Steak</td><td>$15.00</td></tr><tr><td>Salad</td><td>$5.00</td></tr><tr><td>Pizza</td><td>$8.00</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createBevelBorder(0));
        restLabel.add(restInfo, BorderLayout.CENTER);
        restLabel.add(new JLabel("      "), BorderLayout.EAST);
        restLabel.add(new JLabel("      "), BorderLayout.WEST);
    }
    
    public void addMarketCashier(MarketCashierRole c) {
    	markets.add(c);
    }
    
    public void addCustomer(JesusCustomerRole c) {
    	JesusCustomerGui g = new JesusCustomerGui(c, gui, 0, 0);
    	
    	customers.add(c);
    }
    
    public void addWaiter(JesusWaiterRole w) {
    	JesusWaiterGui g = new JesusWaiterGui(w, gui, 0, 0);
    	
    	waiters.add(w);
    }
    
    public void setCashier(JesusCashierRole c) {
    	JesusCashierGui g = new JesusCashierGui(c);
    	
    	cashier = c;
    }
    
    public void setCook(JesusCookRole c) {
    	JesusCookGui g = new JesusCookGui(c);
    	
    	cook = c;
    }
    
    public void setHost(JesusHostRole h) {
    	JesusHostGui g = new JesusHostGui(h);
    	
    	host = h;
    }
}