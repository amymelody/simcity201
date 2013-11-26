package simcity.housing.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import simcity.gui.Gui;

public class ResidentGui implements Gui
{
	private boolean isPresent = false;
	
	HousingGui gui;

	private int xPos, yPos;
	public int xDestination, yDestination;
	private int xGoal, yGoal;
	private int dimensions;
	private int boxY, boxX;
	private MoveBox currentBox;

	public ResidentGui(HousingGui gui)
	{
		xPos = 500;
		yPos = 220;
		xDestination = xPos;
		yDestination = yPos;
		xGoal = xPos;
		yGoal = yPos;
		boxY = 11;
		boxX = 25;
		dimensions = 20;
		this.gui = gui;
	}

	public void updatePosition()
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
			//xtra fun stuff
		}
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, dimensions, dimensions);
	}

	public void setPresent(boolean p)
	{
		isPresent = p;
	}
	@Override
	public boolean isPresent()
	{
		return isPresent;
	}

	public void doGoToLocation(Point p)
	{
		xGoal = (int) p.getX();
		yGoal = (int) p.getY();
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
