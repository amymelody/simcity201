package simcity.cherysrestaurant.gui;


import simcity.cherysrestaurant.CherysCustomerRole;
import simcity.cherysrestaurant.CherysWaiterRole;
import simcity.cherysrestaurant.gui.CherysRestaurantAnimationPanel.Table;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CherysWaiterGui implements CherysGui
{
    private CherysWaiterRole agent = null;

    private int xPos = 50, yPos = 145;//default waiter position
    private int xDestination = 50, yDestination = 145;//default start position
    private int personDimensions = 20;
    private int xDesk = -20, yDesk = 0;
    private int xKitchen = 750, yKitchen = 0;
    private int xHome = xPos, yHome = yPos;
    private int xCustomer, yCustomer;
    
    private int tableDestination = 0;
    
    public static final int xTableInitial = 100;
    public static final int yTable = 75;
    private final int tableDimensions = 50;
    private final int tableBuffer = 50;

    private boolean newDestination = true;
    private boolean carryingOrder = false;
    private String orderType;
    
    private boolean tired = true;

    class Table
    {
    	int num;
    	int x;
    	int y;
    	Table(int n, int x, int y)
    	{
    		num = n;
    		this.x = x;
    		this.y = y;
    	}
    }
    private List<Table> tables = new ArrayList<Table>();
    static final int numTables = 5;
    
    CherysRestaurantGui gui;
    
    /**
     * Constructor for WaiterGui
     * @param agent reference to the waiter this gui represents
     */
    public CherysWaiterGui(CherysWaiterRole agent, CherysRestaurantGui gui, int number)
    {
        this.agent = agent;
    	for(int i = 0; i < numTables; i++)
    	{
	    	Table temp = new Table(i + 1, xTableInitial + (tableDimensions + tableBuffer)*i, yTable);
	    	tables.add(temp);
    	}

    	xPos = xHome + 30*number;
    	xHome = xPos;
    	xDestination = xPos;
    	
    	this.gui = gui;
    }

    public void updatePosition()
    {
    	if(newDestination)
    	{
	        if (xPos < xDestination)
	        {
	            xPos++;
	        }
	        else if (xPos > xDestination)
	        {
	            xPos--;
	        }
	
	        if (yPos < yDestination)
	        {
	            yPos++;
	        }
	        else if (yPos > yDestination)
	        {
	            yPos--;
	        }
	
	        if (xPos == xDestination && yPos == yDestination)
	        {
	        	newDestination = false;
	        	if(xDestination == xCustomer && yDestination == yCustomer)
	        	{
	        		agent.msgAtLobby();
	        	}
	        	if ((xDestination == tables.get(tableDestination).x + personDimensions) && (yDestination == tables.get(tableDestination).y - personDimensions))
	        	{
			        agent.msgAtTable();
			        carryingOrder = false;
	        	}
	        	if(xDestination == xKitchen && yDestination == yKitchen)
	        	{
	        		agent.msgAtKitchen();
	        	}
	        	if(xDestination == xHome && yDestination == yHome)
	        	{
	        		gui.setWaiterEnabled(agent);
	        	}
	        }
    	}
    }

    public void draw(Graphics2D g)
    {
        g.setColor(Color.MAGENTA);
        g.fillRect(xPos, yPos, personDimensions, personDimensions);
        if(carryingOrder)
        {
	        g.setColor(Color.YELLOW);
	        g.fillOval(xPos, yPos, personDimensions - 1, personDimensions - 1);
	        g.setColor(Color.BLACK);
	        g.drawString(orderType + "?", xPos + personDimensions/10, yPos + (personDimensions*3)/4);
        }
    }

    public boolean isPresent()
    {
        return true;
    }
    
    public void setTired(boolean tf)
    {
    	if(tf)
    	{
    		agent.msgGotTired();
    	}
    	else
    	{
    		if(xDestination == xHome && yDestination == yHome)
    		{
        		agent.msgOffBreak();
    		}
    	}
    	tired = tf;
    }
    public void doWorkThroughThePain()
    {
    	gui.setWaiterBusy(agent, false);
    }

    public void doGoToCustomer(CherysCustomerGui cg)
    {
    	newDestination = true;
    	xCustomer = cg.xDestination + personDimensions;
    	yCustomer = cg.yDestination - personDimensions;
        xDestination = xCustomer;
        yDestination = yCustomer;
    }
    public void doGoToTable(int t)
    {
    	newDestination = true;
    	for(int i = 0; i < tables.size(); i++)
    	{
    		if(tables.get(i).num == t)
    		{
    			tableDestination = i;
    		}
    	}
        xDestination = tables.get(tableDestination).x + personDimensions;
        yDestination = tables.get(tableDestination).y - personDimensions;
    }
    public void doSeatCustomer(CherysCustomerGui cg, int t)
    {
    	newDestination = true;
    	for(int i = 0; i < tables.size(); i++)
    	{
    		if(tables.get(i).num == t)
    		{
    			tableDestination = i;
    		}
    	}
        xDestination = tables.get(tableDestination).x + personDimensions;
        yDestination = tables.get(tableDestination).y - personDimensions;
        cg.msgFeetFollow(xDestination - personDimensions, yDestination + personDimensions);
    }
    public void doServeOrder(String ot, int t)
    {
    	newDestination = true;
    	for(int i = 0; i < tables.size(); i++)
    	{
    		if(tables.get(i).num == t)
    		{
    			tableDestination = i;
    		}
    	}
        xDestination = tables.get(tableDestination).x + personDimensions;
        yDestination = tables.get(tableDestination).y - personDimensions;
        orderType = ot;
        carryingOrder = true;
    }
    public void doGoToKitchen()
    {
    	newDestination = true;
        xDestination = xKitchen;
        yDestination = yKitchen;
    }
    public void doGoOnBreak()
    {
    	newDestination = true;
        xDestination = xHome;
        yDestination = yHome;
    }
    
    public int getXPos()
    {
        return xPos;
    }
    public int getYPos()
    {
        return yPos;
    }
}
