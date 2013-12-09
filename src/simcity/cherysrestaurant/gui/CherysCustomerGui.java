package simcity.Anjalirestaurant.gui;

import simcity.Anjalirestaurant.AnjaliCustomerRole;
import simcity.Anjalirestaurant.AnjaliHostRole;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class AnjaliCustomerGui implements AnjaliGui
{
	private Semaphore follow = new Semaphore(0, true);
	
	private AnjaliCustomerRole agent = null;
	private boolean isPresent = false;
	private boolean isHungry = false;

	//private HostAgent host;
	AnjaliRestaurantGui gui;

	private int xPos, yPos;
	public int xDestination, yDestination;
	private int dimensions;
	private int xCashier = 0, yCashier = 40;
	private int xExit = -40, yExit = 0;
	private int xTable = 0, yTable = 0;
	
	private String orderType;
	private boolean waitingForFood = false;
	private boolean hasFood = false;
	
	private enum Command
	{
		noCommand,
		GoToSeat,
		GoToCashier,
		LeaveRestaurant;
	}
	private Command command = Command.noCommand;

	public AnjaliCustomerGui(AnjaliCustomerRole c, AnjaliRestaurantGui gui, int number)
	{
		agent = c;
		xPos = 50 + 30*number;
		yPos = 30;
		xDestination = xPos;
		yDestination = yPos;
		dimensions = 20;
		this.gui = gui;
	}
	
	public void msgFeetFollow(int x, int y) //* called in WaiterAgent........
	{
		xTable = x;
		yTable = y;
		follow.release();
		doGoToTable();
	}

	public void updatePosition()
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
			if(command == Command.GoToSeat)
			{
				agent.msgAtTable();
				xTable = 0;
				yTable = 0;
			}
			else if(command == Command.GoToCashier)
			{
				agent.msgAtCashier();
			}
			else if(command == Command.LeaveRestaurant)
			{
				agent.msgLeftRestaurant();
				isHungry = false;
				gui.setCustomerEnabled(agent);
			}
			command = Command.noCommand;
		}
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.GREEN);
		g.fillRect(xPos, yPos, dimensions, dimensions);
        if(waitingForFood)
        {
	        g.setColor(Color.WHITE);
	        g.fillOval(xPos, yPos, dimensions - 1, dimensions - 1);
	        g.setColor(Color.BLACK);
	        g.drawString(orderType + "?", xPos, yPos + (dimensions*3)/4);
        }
        if(hasFood)
        {
	        g.setColor(Color.YELLOW);
	        g.fillOval(xPos, yPos, dimensions - 1, dimensions - 1);
	        g.setColor(Color.BLACK);
	        g.drawString(orderType, xPos + dimensions/10, yPos + (dimensions*3)/4);
        }
	}

	public boolean isPresent()
	{
		return isPresent;
	}
	public void setHungry()
	{
		isHungry = true;
		agent.gotHungry();
		setPresent(true);
	}
	public boolean isHungry()
	{
		return isHungry;
	}

	public void setPresent(boolean p)
	{
		isPresent = p;
	}

	public void doGoToTable()
	{
		if(xTable == 0 && yTable == 0)
    	{
			try
			{
				follow.acquire();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
    	}
		else
		{
	        xDestination = xTable;
	        yDestination = yTable;
			command = Command.GoToSeat;
		}
	}
	
	public void doWaitForFood(String ot)
	{
		orderType = ot;
		waitingForFood = true;
	}
	public void doGetFood(String ot)
	{
		orderType = ot;
		waitingForFood = false;
		hasFood = true;
	}
	public void doGoToCashier()
	{
		hasFood = false;
		xDestination = xCashier;
		yDestination = yCashier;
		command = Command.GoToCashier;
	}

	public void doExitRestaurant()
	{
		hasFood = false;
		xDestination = xExit;
		yDestination = yExit;
		command = Command.LeaveRestaurant;
	}
}
