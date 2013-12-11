package simcity.market.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import simcity.bank.gui.BankManagerGui;
import simcity.gui.DelivererGui;
import simcity.market.MarketDelivererRole;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketCustomerRole;
import simcity.market.MarketCashierRole;

public class MarketInputPanel extends JPanel implements ActionListener
{
	static final int LSPACE = 10;
	static final int GROWS = 3;
	static final int GCOLUMNS = 1;
	
	public String name;
	public MarketGui marketGui;
	public JButton goBack;
	private JPanel marketLabel = new JPanel();
	private JPanel inventoryLabel = new JPanel();
	private JTabbedPane group = new JTabbedPane();
    JLabel marketInfo = new JLabel();
    JLabel inventoryInfo = new JLabel();
    private MarketCashierRole cashier;
    private MarketCashierGui cashierGui;
	private Vector<MarketEmployeeRole> employees = new Vector<MarketEmployeeRole>();
	private Vector<MarketDelivererRole> deliverers = new Vector<MarketDelivererRole>();
	private Vector<MarketCustomerRole> customers = new Vector<MarketCustomerRole>();
	
	public MarketInputPanel(MarketGui mG, String n, MarketCashierRole c)
	{
		name = n;
		marketGui = mG;
		cashier = c;
		
		cashierGui = new MarketCashierGui(cashier);
		marketGui.animationPanel.addGui(cashierGui);
		cashier.setGui(cashierGui);
        
		setBounds(0, 0, 150, 500);
        setLayout(new BorderLayout(LSPACE, LSPACE));
        setBorder(BorderFactory.createBevelBorder(0));
        
        initMarketLabel();
        group.addTab("Menu", null, marketLabel, "Menu");
        initMarketInv();
        group.addTab("Inventory", null, inventoryLabel, "Inventory");
        group.setSelectedIndex(0);
        
        add(group, BorderLayout.CENTER);
	}
	
	public void actionPerformed(ActionEvent e) {
		initMarketInv();
	}
	private String getMName() {
		if(name == "market1")
			return "Market 1";
		else if(name == "market2")
			return "Market 2";
		return null;
	}
	private String getColor() {
		if(name == "market1")
			return "green";
		else if(name == "market2")
			return "blue";
		return null;
	}
	private void initMarketInv() {
		inventoryLabel.setLayout(new BorderLayout());
        inventoryInfo.setText(
                "<html>"
                + "<h3 color=" + getColor() + ">Welcome to " + getMName() + "!</h3>"
                		+ "<h4><u> Inventory</u></h4><table cellspacing=0 color=" + getColor() + ">"
                		+ "<tr><td>Salad</td><td>" + cashier.inventory.getAmount("Salad") + "</td></tr>"
                		+ "<tr><td>Steak</td><td>" + cashier.inventory.getAmount("Steak") + "</td></tr>"
                		+ "<tr><td>Pizza</td><td>" + cashier.inventory.getAmount("Pizza") + "</td></tr>"
                		+ "<tr><td>Chicken</td><td>" + cashier.inventory.getAmount("Chicken") + "</td></tr>"
                		+ "<tr><td>Spaghetti</td><td>" + cashier.inventory.getAmount("Spaghetti") + "</td></tr>"
                		+ "<tr><td>Lasagna</td><td>" + cashier.inventory.getAmount("Lasagna") + "</td></tr>"
                		+ "<tr><td>Garlic Bread</td><td>" + cashier.inventory.getAmount("Garlic Bread") + "</td></tr>"
                		+ "<tr><td>Ribs</td><td>" + cashier.inventory.getAmount("Ribs") + "</td></tr>"
                		+ "<tr><td>Burger</td><td>" + cashier.inventory.getAmount("Burger") + "</td></tr>"
                		+ "<tr><td>Enchiladas</td><td>" + cashier.inventory.getAmount("Enchiladas") + "</td></tr>"
                		+ "<tr><td>Tacos</td><td>" + cashier.inventory.getAmount("Tacos") + "</td></tr>"
                		+ "<tr><td>Pozole</td><td>" + cashier.inventory.getAmount("Pozole") + "</td></tr>"
                		+ "<tr><td>Horchata</td><td>" + cashier.inventory.getAmount("Horchata") + "</td></tr>"
                		+ "</table><br></html>");

        inventoryLabel.setBorder(BorderFactory.createBevelBorder(0));
        inventoryLabel.add(inventoryInfo, BorderLayout.CENTER);
        inventoryLabel.add(new JLabel(" "), BorderLayout.EAST);
        inventoryLabel.add(new JLabel(" "), BorderLayout.WEST);
	}
	private void initMarketLabel() {
        marketLabel.setLayout(new BorderLayout());
        marketInfo.setText(
                "<html>"
                + "<h3 color=" + getColor() + ">Welcome to " + getMName() + "!</h3>"
                		+ "<h4><u> Menu</u></h4><table cellspacing=0 color=" + getColor() + ">"
                		+ "<tr><td>Salad</td><td>$3.00</td></tr>"
                		+ "<tr><td>Steak</td><td>$10.00</td></tr>"
                		+ "<tr><td>Pizza</td><td>$5.00</td></tr>"
                		+ "<tr><td>Chicken</td><td>$10.00</td></tr>"
                		+ "<tr><td>Spaghetti</td><td>$8.00</td></tr>"
                		+ "<tr><td>Lasagna</td><td>$10.00</td></tr>"
                		+ "<tr><td>Garlic Bread</td><td>$5.00</td></tr>"
                		+ "<tr><td>Ribs</td><td>$10.00</td></tr>"
                		+ "<tr><td>Burger</td><td>$8.00</td></tr>"
                		+ "<tr><td>Enchiladas</td><td>$10.00</td></tr>"
                		+ "<tr><td>Tacos</td><td>$5.00</td></tr>"
                		+ "<tr><td>Pozole</td><td>$8.00</td></tr>"
                		+ "<tr><td>Horchata</td><td>$3.00</td></tr>"
                		+ "</table><br></html>");

        marketLabel.setBorder(BorderFactory.createBevelBorder(0));
        marketLabel.add(marketInfo, BorderLayout.CENTER);
        marketLabel.add(new JLabel(" "), BorderLayout.EAST);
        marketLabel.add(new JLabel(" "), BorderLayout.WEST);
   
	}
	
	public void setCashier(MarketCashierRole cashier) {
		this.cashier = cashier;
	}
	
	public void addEmployee(MarketEmployeeRole e) {
		MarketEmployeeGui g = new MarketEmployeeGui(e);
		marketGui.animationPanel.addGui(g);
		e.setGui(g);
		e.setCashier(cashier);
		employees.add(e);
	}
	
	public void addDeliverer(MarketDelivererRole d) {
		MarketDelivererGui g = new MarketDelivererGui(d);
		DelivererGui dG = new DelivererGui(g, name);
		g.setDelivererGui(dG);
		marketGui.animationPanel.addGui(g);
		marketGui.addDelivererGui(dG);
		d.setGui(g);
		d.setCashier(cashier);
		deliverers.add(d);
	}
	
	public void addCustomer(MarketCustomerRole c) {
		MarketCustomerGui g = new MarketCustomerGui(c);
		marketGui.animationPanel.addGui(g);
		c.setCashier(cashier);
		c.setGui(g);
		customers.add(c);
	}
}
