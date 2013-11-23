package simcity.jesusrestaurant.gui;

import simcity.jesusrestaurant.JesusCashierRole;
import simcity.jesusrestaurant.JesusCustomerRole;
import simcity.jesusrestaurant.JesusHostRole;
import simcity.jesusrestaurant.JesusMarketRole;
import simcity.jesusrestaurant.JesusWaiterRole;
import simcity.jesusrestaurant.JesusCookRole;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 * Panel in frame that contains all the restaurant information,
 * including host, cook, waiters, and customers.
 */
public class JesusRestaurantPanel extends JPanel implements ActionListener, MouseMotionListener{
	//static declarations
	static final int LSPACE = 10;
	static final int GROWS = 3;
	static final int GCOLUMNS = 1;
	static int gridX = 22;
	static int gridY = 22;
	
	Semaphore[][] grid = new Semaphore[gridX][gridY];
	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(40, 160));
		tableLocations.put(2, new Point(200, 160));
		tableLocations.put(3, new Point(40, 320));
		tableLocations.put(4, new Point(200, 320));
	}
	
    //Host, cook, waiters and customers
    private JesusHostRole host = new JesusHostRole("Jesus");
    private JesusHostGui jesusHostGui = new JesusHostGui(host);
    
    private JesusCookRole cook = new JesusCookRole();
    private JesusCookGui jesusCookGui = new JesusCookGui(cook);
    
    private JesusCashierRole cashier = new JesusCashierRole();
    private JesusCashierGui jesusCashierGui = new JesusCashierGui(cashier);
    
    private Vector<JesusCustomerRole> customers = new Vector<JesusCustomerRole>();
    private Vector<JesusWaiterRole> waiters = new Vector<JesusWaiterRole>();
    private Vector<JesusMarketRole> markets = new Vector<JesusMarketRole>();
    
    int custxloc = 2, custyloc = 0;
    int waiterxloc = 21, waiteryloc = 0;
    
    public int getWaiterCnt() {
    	return waiters.size();
    }

    private JPanel mainPanel = new JPanel();
    private JPanel restLabel = new JPanel();
    JLabel restInfo = new JLabel();
    private JesusListPanel customerPanel = new JesusListPanel(this, "Customer");
    private JesusListPanel waiterPanel = new JesusListPanel(this, "Waiter");
    private JesusListPanel marketPanel = new JesusListPanel(this, "Market");
    private JTabbedPane group = new JTabbedPane();
    JesusAnimationPanel jesusAnimationPanel = new JesusAnimationPanel();
    
    private JButton pauseB;
    
    private JButton updateInvB;//for cook
    
    private JesusRestaurantGui gui; //reference to main gui

	public JesusRestaurantPanel(JesusRestaurantGui gui) {
        this.gui = gui;
        
        //initialize the semaphore grid
        for (int i=0; i<gridX; i++)
            for (int j=0; j<gridY; j++)
                grid[i][j] = new Semaphore(1,true);
        //build the animation areas
        try {
        	for(int i=0; i<3; i++)
        		grid[i][4].acquire();
        	for(int i=19; i<22; i++)
        		grid[i][16].acquire();
        	for(int j=19; j<22; j++)
        		grid[16][j].acquire();
        	for(int k=1; k<5; k++) {
        		for(int i=tableLocations.get(k).x/20; i<tableLocations.get(k).x/20 + 4; i++) {
        			for(int j=tableLocations.get(k).y/20; j<tableLocations.get(k).y/20 + 4; j++) {
        				grid[i][j].acquire();
        			}
        		}
        	}
        }catch (Exception e) {
            System.out.println("Unexpected exception caught in during setup:"+ e);
        }
        
        host.setGui(jesusHostGui);
        cook.setGui(jesusCookGui);
        cashier.setGui(jesusCashierGui);
        cook.setHost(host);
        
        pauseB = new JButton("Pause");
        pauseB.setPreferredSize(new Dimension(10, 20));
        pauseB.addActionListener(this);
        
        updateInvB = new JButton("Cook Inv.");
        updateInvB.setPreferredSize(new Dimension(10, 30));
        updateInvB.addActionListener(this);
        
        jesusAnimationPanel.addGui(jesusHostGui);
        jesusAnimationPanel.addGui(jesusCookGui);
        jesusAnimationPanel.addGui(jesusCashierGui);

        setLayout(new BorderLayout(LSPACE, LSPACE));
        //group.setLayout(new GridLayout(GROWS, GCOLUMNS, LSPACE, LSPACE));
        mainPanel.setLayout(new BorderLayout(LSPACE, LSPACE));
        
        group.addTab("Customers", null, customerPanel, "Customers");
        group.addTab("Waiters", null, waiterPanel, "Waiters");
        group.addTab("Markets", null, marketPanel, "Markets");
        group.setSelectedIndex(0);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 5, 5));
        mainPanel.add(restLabel);
        panel.add(pauseB);
        panel.add(updateInvB);
        mainPanel.add(panel, BorderLayout.SOUTH);
        
        Dimension animationDim = new Dimension((int) (440), (int) (440));
        jesusAnimationPanel.setPreferredSize(animationDim);
        jesusAnimationPanel.setMinimumSize(animationDim);
        jesusAnimationPanel.setMaximumSize(animationDim);
        add(jesusAnimationPanel, BorderLayout.EAST); 
        
        initRestLabel();
        addMouseMotionListener(this);
        
        add(mainPanel, BorderLayout.WEST);
        add(group, BorderLayout.CENTER);
        cook.getGui().checkInventory();
    }

    /**
     * Sets up the restaurant label that includes the menu,
     * and host and cook information
     */
    private void initRestLabel() {
        restLabel.setLayout(new BorderLayout());
        restInfo.setText(
                "<html><br/><h3>Welcome to La Cocina!</h3><h3 align=center color=red>" + host.openPanel() + "</h3><h4><u>Tonight's Staff</u></h4><table><tr><td>Host:</td><td>" + host.getName() + "</td></tr><tr><td>Cashier:</td><td>" + cashier.getName() + "</td></tr><tr><td>Cook:</td><td>" + cook.getName() + "</td></tr></table><h4><u> Menu</u></h4><table><tr><td>Steak</td><td>$15.00</td></tr><tr><td>Salad</td><td>$5.00</td></tr><tr><td>Pizza</td><td>$8.00</td></tr></table><br></html>");

        restLabel.setBorder(BorderFactory.createBevelBorder(0));
        restLabel.add(restInfo, BorderLayout.CENTER);
        restLabel.add(new JLabel("      "), BorderLayout.EAST);
        restLabel.add(new JLabel("      "), BorderLayout.WEST);
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

        if (type.equals("Customer")) {

            for (int i = 0; i < customers.size(); i++) {
                JesusCustomerRole temp = customers.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        else if (type.equals("Waiter")) {
        	for (int i = 0; i < waiters.size(); i++) {
                JesusWaiterRole temp = waiters.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
        else if (type.equals("Market")) {
        	for (int i = 0; i < markets.size(); i++) {
                JesusMarketRole temp = markets.get(i);
                if (temp.getName() == name)
                    gui.updateInfoPanel(temp);
            }
        }
    }
    
    /**
	 * Method from the ActionListener interface.
	 * Handles the event of the add button being pressed
	 */
    public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateInvB) {
			JTextField steak = new JTextField();
			JTextField salad = new JTextField();
			JTextField pizza = new JTextField();
			Object[] invUp = {
					"Steak:", steak,
					"Salad:", salad,
					"Pizza:", pizza
			};
			int option = JOptionPane.showConfirmDialog(null, invUp, "Update Inventory", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION) {
				if (steak.getText().length() > 0 && salad.getText().length() > 0 && pizza.getText().length() > 0) {
					boolean number;
					try{
						if(Integer.parseInt(steak.getText()) >= 0 && Integer.parseInt(salad.getText()) >= 0 
								&& Integer.parseInt(pizza.getText()) >= 0)
							number = true;
						else
							number = false;
					}
					catch(NumberFormatException ex) {
						number = false;
					}
					if(number) {
						cook.getGui().updateInventory(Integer.parseInt(steak.getText()), Integer.parseInt(salad.getText()), 
								Integer.parseInt(pizza.getText()));
						steak.setText("");
						salad.setText("");
						pizza.setText("");
					}
					else {
						JOptionPane.showMessageDialog(this, "Please enter a positive integer for each food item.");
						steak.setText("");
						salad.setText("");
						pizza.setText("");
					}
				}
				else {
					JOptionPane.showMessageDialog(this, "Please enter an integer for each food item.");
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
    public void addPerson(String type, String name, int hungry) {
    	if (type.equals("Customer")) {
    		JesusCustomerRole c = new JesusCustomerRole(name);	
    		JesusCustomerGui g = new JesusCustomerGui(c, gui, custxloc*20, custyloc*20);
    		if(custxloc == 9)
    			custxloc = 2;
    		else
    			custxloc++;

    		jesusAnimationPanel.addGui(g);
    		c.setHost(host);
    		c.setCashier(cashier);
    		c.setGui(g);
    		c.setHungerLevel(hungry);
    		customers.add(c);
    	}
    	if (type.equals("Waiter")) {
    		JesusWaiterRole w = new JesusWaiterRole(name);
    		JesusWaiterGui g = new JesusWaiterGui(w, gui, waiterxloc*20, waiteryloc*20-2);
    		if(waiteryloc == 5) {
    			waiteryloc = 0;
    			waiterxloc++;
    		}
    		else
    			waiteryloc++;
    		
    		jesusAnimationPanel.addGui(g);
    		w.setHost(host);
    		w.setCook(cook);
    		w.setCashier(cashier);
    		w.setGui(g);
    		host.setWaiters(w);
    		waiters.add(w);
    	}
    	if (type.equals("Market")) {
    		JesusMarketRole m = new JesusMarketRole(name);
    		JesusMarketGui g = new JesusMarketGui(m);
    		cook.addMarket(m);
    		
    		jesusAnimationPanel.addGui(g);
    		m.setGui(g);
    		m.setCook(cook);
    		m.setCashier(cashier);
    		markets.add(m);
    	}
    }

	public void mouseDragged(MouseEvent e) {
	
	}
	public void mouseMoved(MouseEvent e) {
	    initRestLabel();
	}
}