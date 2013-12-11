package simcity.cherysrestaurant.gui; 

import simcity.CityDirectory;
import simcity.cherysrestaurant.CherysCustomerRole;
import simcity.cherysrestaurant.CherysNormalWaiterRole;
import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.gui.CityGui;
import simcity.joshrestaurant.gui.JoshRestaurantInputPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Main GUI class.
 * Contains the main frame and subsequent panels, along with the 'main' function
 */
public class CherysRestaurantGui extends BuildingGui
{
	JFrame animationFrame = new JFrame("Restaurant Animation");
	CherysRestaurantAnimationPanel animationPanel = new CherysRestaurantAnimationPanel();
	
    private CherysRestaurantInputPanel restPanel;
    
    private JPanel idPanel;
    private JLabel idLabel; //part of idPanel
    private ImageIcon image; //part of idPanel
    private JLabel imageLabel; //part of idPanel
    private JButton pauseButton; //part of idPanel

    /**
     * Constructor for RestaurantGui class. Sets up all the gui components.
     */
    public CherysRestaurantGui(String n, BuildingsGui bG, CityDirectory cD)
    {
    	super(n, bG, cD);
    	restPanel = new CherysRestaurantInputPanel(this, cD.getCherysHost(), cD.getCherysCook(), cD.getCherysCashier(), cD.getMarketCashiers());
    	
    	
        int WINDOWX = 650;
        int WINDOWY = 500;

        //main restaurant panel
		double inputFractionOfWindow = 150.0 / 650.0;
		Dimension restDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
        restPanel.setPreferredSize(restDim);
        restPanel.setMinimumSize(restDim);
        restPanel.setMaximumSize(restDim);
        restPanel.setVisible(false);
        bG.add(restPanel, BorderLayout.WEST);
        
        //animation panel
    	double animationFractionOfWindow = 500.0 / 650.0;
        Dimension animDim = new Dimension(WINDOWX, (int)(WINDOWY * animationFractionOfWindow));
        animationPanel.setPreferredSize(animDim);
        animationPanel.setMinimumSize(animDim);
        animationPanel.setMaximumSize(animDim);
        animationPanel.setVisible(false);
        bG.add(animationPanel, BorderLayout.CENTER);
        
//        //My section
//        double idPanelFractionOfWindow = .15;
//        int bufferBetweenLabels = 10;
//        int bufferAboveLabels = 0;
//        Dimension idDim = new Dimension(WINDOWX, (int)(WINDOWY * idPanelFractionOfWindow));
//        idPanel = new JPanel();
//        idPanel.setPreferredSize(idDim);
//        idPanel.setMinimumSize(idDim);
//        idPanel.setMaximumSize(idDim);
//        idPanel.setBorder(BorderFactory.createTitledBorder("Extras"));
//
//        idPanel.setLayout(new FlowLayout(FlowLayout.LEFT, bufferBetweenLabels, bufferAboveLabels));
//
//        image = new ImageIcon("me.jpg");
//        imageLabel = new JLabel(image);
//        idPanel.add(imageLabel);
//        
//        idLabel = new JLabel(); 
//        idLabel.setText("<html><pre><i>Layout edited by Cherys Fair\t\t\t\t\t\t\t\t\t\t\t\t\\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t</i></pre></html>");
//        idPanel.add(idLabel);
//        
//        add(idPanel, frameLayout.SOUTH);
        
    }
//    /**
//     * Update's the customer's internal hunger variable
//     * @param person reference to the person the information is about
//     * @param tf the value of the customer's 'hungry' check box
//     */
//    public void updatePerson(Object person, boolean tf) //* called in RestaurantPanel.addPerson/.updateInfo
//    {
//        currentPerson = person;
//        if(person instanceof CherysCustomerRole) 
//        {
//        	CherysCustomerRole customer = (CherysCustomerRole) person;
//        	if(tf)
//        	{
//        		customer.getGui().setHungry();
//        	}
//        }
//        if(person instanceof CherysWaiterRole)
//        {
//        	CherysWaiterRole waiter = (CherysWaiterRole) person;
//        	waiter.getGui().setTired(tf);
//        }
//    }
//    /**
//     * Message sent from the host agent when a customer leaves to enable
//     * that customer's "I'm hungry" checkbox.
//     * @param c reference to the customer
//     */
//    public void setCustomerEnabled(CherysCustomerRole c) //* called from CustomerGui.updatePosition
//    {
//    	restPanel.enableCheck("Customer", c.getName());
//    }
//    /**
//     * Changes the gui to let the user know that the waiter is serving
//     * their max number of customers
//     * @param w reference to the waiter agent
//     * @param tf the value the waiter's "busy" check box is being assigned
//     */
//    public void setWaiterEnabled(CherysWaiterRole w) //* called from ~HostAgent.assignCustomer
//    {
//    	restPanel.enableCheck("Waiter", w.getName());
//    }
//    public void setWaiterBusy(CherysWaiterRole w, boolean tf) //*called from WaiterGui
//    {
//    	restPanel.setCheck(w.getName(), tf);
//    	restPanel.enableCheck("Waiter", w.getName());
//    }
//    
	@Override
	public void changeView(boolean visible)
	{
    	animationPanel.setVisible(visible);
	}
	public void addCustomer(CherysCustomerRole c)
	{
    	restPanel.addCustomer(c);
		
	}
	public void addWaiter(CherysNormalWaiterRole w)
	{
    	restPanel.addWaiter(w);
	}
}