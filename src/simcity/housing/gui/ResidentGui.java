package simcity.housing.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import simcity.gui.Gui;
import simcity.housing.ResidentRole;

public class ResidentGui implements Gui
{
	ResidentRole resident;
	HousingGui gui;

	private int xPos, yPos;
	public int xDestination, yDestination;
	private int xGoal, yGoal;
	private int dimensions;
	private int boxY, boxX;
	private MoveBox currentBox;
	private boolean exiting;

	private enum Command
	{
		putFood,
		getFood,
		cook,
		eat,
		exit,
		noCommand;
	}
	private Command pastCommand = Command.noCommand;
	private Command command = Command.noCommand;
	
	public ResidentGui(ResidentRole r, HousingGui gui)
	{
		resident = r;
		xPos = 500;
		yPos = 220;
		xDestination = xPos;
		yDestination = yPos;
		xGoal = xPos;
		yGoal = yPos;
		boxY = 11;
		boxX = 25;
		dimensions = 20;
		exiting = false;
		pastCommand = Command.noCommand;
		command = Command.noCommand;
		this.gui = gui;
	}

	public void updatePosition()
	{
		if(exiting)
		{
			if (xPos < 500)
			{
				xPos++;
			}
			else
			{
				xPos = 500;
				yPos = 220;
				xDestination = xPos;
				yDestination = yPos;
				xGoal = xPos;
				yGoal = yPos;
				boxY = 11;
				boxX = 25;
				exiting = false;
				pastCommand = Command.noCommand;
				command = Command.noCommand;
				currentBox = null;
				
				resident.msgAtLocation();
			}
		}
		else
		{
			if(xPos == xDestination && yPos == yDestination && (xPos != xGoal || yPos != yGoal))
			{
				findMoveBox();
			}
			
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
			
			if (xPos == xGoal && yPos == yGoal)
			{
				if(command == Command.exit)
				{
					exiting = true; 
				}
				else
				{
					resident.msgAtLocation();
				}
			}
		}
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, dimensions, dimensions);
        if(pastCommand == Command.getFood || command == Command.putFood)
        {
	        g.setColor(Color.WHITE);
	        g.fillOval(xPos, yPos, dimensions - 1, dimensions - 1);
        }
        if(pastCommand == Command.cook)
        {
	        g.setColor(Color.YELLOW);
	        g.fillOval(xPos, yPos, dimensions - 1, dimensions - 1);
        }
        if(pastCommand == Command.eat)
        {
	        g.setColor(Color.GREEN);
	        g.fillOval(xPos, yPos, dimensions + 19, dimensions - 1);
        }
	}

	@Override
	public boolean isPresent()
	{
		return true;
	}

	public void doGoToLocation(Point p, String purpose)
	{
		xGoal = (int) p.getX();
		yGoal = (int) p.getY();

		pastCommand = command;
		if(purpose.equals("Put food"))
		{
			command = Command.putFood;
		}
		if(purpose.equals("Get food"))
		{
			command = Command.getFood;
		}
		if(purpose.equals("Cook"))
		{
			command = Command.cook;
		}
		if(purpose.equals("Eat"))
		{
			command = Command.eat;
		}
		if(purpose.equals("Exit"))
		{
			command = Command.exit;
		}
		else
		{
			command = Command.noCommand;
		}
	}
	
	public void findMoveBox()
	{
		List<MoveBox> boxesToCheck = new ArrayList<MoveBox>();

		if (xPos < xGoal)
		{
			if((boxX + 1) < 25)
			{
				boxesToCheck.add(gui.getBox(boxY, boxX + 1));
			}
		}
		else if (xPos > xGoal)
		{
			if((boxX - 1) >= 0)
			{
				boxesToCheck.add(gui.getBox(boxY, boxX - 1));
			}
		}
		if (yPos < yGoal)
		{
			if((boxY + 1) < 25)
			{
				boxesToCheck.add(gui.getBox(boxY + 1, boxX));
			}
		}
		else if (yPos > yGoal)
		{
			if((boxY - 1) >= 0)
			{
				boxesToCheck.add(gui.getBox(boxY - 1, boxX));
			}
		}

		if (xPos == xGoal)
		{
			if((boxX + 1) < 25)
			{
				boxesToCheck.add(gui.getBox(boxY, boxX + 1));
			}
			if((boxX - 1) >= 0)
			{
				boxesToCheck.add(gui.getBox(boxY, boxX - 1));
			}
		}
		if (yPos == yGoal)
		{
			if((boxY + 1) < 25)
			{
				boxesToCheck.add(gui.getBox(boxY + 1, boxX));
			}
			if((boxY - 1) >= 0)
			{
				boxesToCheck.add(gui.getBox(boxY - 1, boxX));
			}
		}
		if(currentBox != null)
		{
			currentBox.setOpen(true);
			boxesToCheck.add(currentBox);
		}
		for(MoveBox b : boxesToCheck)
		{
			if(b.getOpen())
			{
				b.setOpen(false);
				boxX = b.getIndexX();
				boxY = b.getIndexY();
				currentBox = b;
			}
		}
		boxesToCheck.remove(currentBox);
		gui.setBox(boxesToCheck);
		xDestination = currentBox.getX();
		yDestination = currentBox.getY();
	}
}
