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

import simcity.bank.gui.BankManagerGui;
import simcity.gui.DelivererGui;
import simcity.market.MarketDelivererRole;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketCustomerRole;
import simcity.market.MarketCashierRole;

public class MarketInputPanel extends JPanel
{
	static final int LSPACE = 10;
	static final int GROWS = 3;
	static final int GCOLUMNS = 1;
	
	public String name;
	public MarketGui marketGui;
	public JButton goBack;
	private JPanel marketLabel = new JPanel();
    JLabel marketInfo = new JLabel();
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
        
        setLayout(new GridLayout(LSPACE, LSPACE));
        setBorder(BorderFactory.createBevelBorder(0));
        
        initMarketLabel();
        add(marketLabel);
	}
	
	private void initMarketLabel() {
        marketLabel.setLayout(new BorderLayout());
        marketInfo.setText(
                "<html><br/>"
                + "<h3>Welcome to " + name + "!</h3>"
                		+ "<h4><u> Menu</u></h4><table>"
                		+ "<tr><td>Car</td><td>$1,000.00</td></tr>"
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

        //marketLabel.setBorder(BorderFactory.createBevelBorder(0));
        marketLabel.add(marketInfo, BorderLayout.CENTER);
        marketLabel.add(new JLabel("      "), BorderLayout.EAST);
        marketLabel.add(new JLabel("      "), BorderLayout.WEST);
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
