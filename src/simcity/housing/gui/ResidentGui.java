package simcity.housing.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import simcity.gui.Gui;
import simcity.interfaces.HousingGuiInterface;
import simcity.interfaces.Resident;
import simcity.mock.EventLog;
import simcity.mock.LoggedEvent;

public class ResidentGui implements Gui
{
	Resident resident;
	HousingGuiInterface gui;

	public int xPos;
	public int yPos;
	public int xDestination, yDestination;
	public int xGoal;
	public int yGoal;
	private int dimensions;
	public int boxY;
	public int boxX;
	public List<MoveBox> pastBoxes = new ArrayList<MoveBox>();
	public MoveBox currentBox;
	private boolean exiting;
	private boolean arrived;
	private boolean error;

	public enum Command
	{
		putFood,
		getFood,
		cook,
		eat,
		exit,
		noCommand;
	}
	public Command pastCommand = Command.noCommand;
	public Command command = Command.noCommand;

	public EventLog log = new EventLog();
	
	public ResidentGui(Resident r)
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
		error = false;
	}
	public void setGui(HousingGuiInterface gui)
	{
		this.gui = gui;
	}

	public void updatePosition()
	{
		log.add(new LoggedEvent("Received updatePosition"));
		
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
				currentBox.setOpen(true);
				List<MoveBox> boxTemp = new ArrayList<MoveBox>();
				boxTemp.add(currentBox);
				gui.setBox(boxTemp);
				boxTemp.clear();
				currentBox = null;
				error = false;
				
				resident.msgAtLocation();
			}
		}
		else
		{
			if(!error)
			{
				if(xPos == xDestination && yPos == yDestination && (xDestination != xGoal || yDestination != yGoal))
				{
					findMoveBox();
				}
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
				pastBoxes.clear();
				if(command == Command.exit)
				{
					exiting = true; 
				}
				else
				{
					if(!arrived)
					{
						if(!(xPos == 500 && yPos == 220))
						{
							resident.msgAtLocation();
							arrived = true;
						}
					}
				}
			}
		}
	}

	public void draw(Graphics2D g)
	{
		g.setColor(Color.BLUE);
		g.fillRect(xPos, yPos, dimensions, dimensions);
        if((pastCommand == Command.getFood || command == Command.putFood) && !arrived)
        {
	        g.setColor(Color.WHITE);
	        g.fillOval(xPos, yPos, dimensions - 1, dimensions - 1);
        }
        else if((pastCommand == Command.getFood || command == Command.putFood) && arrived)
        {
	        g.setColor(Color.WHITE);
	        g.fillOval(xPos - 20, yPos, dimensions - 1, dimensions - 1);
        }
        if((pastCommand == Command.cook) && !arrived)
        {
	        g.setColor(Color.YELLOW);
	        g.fillOval(xPos, yPos, dimensions - 1, dimensions - 1);
        }
        else if((command == Command.eat) && arrived)
        {
	        g.setColor(Color.YELLOW);
	        g.fillOval(xPos + 20, yPos, dimensions - 1, dimensions - 1);
        }
	}
	
	public HousingGuiInterface getCurrentHouse()
	{
		return gui;
	}

	@Override
	public boolean isPresent()
	{
		return true;
	}

	public void doGoToLocation(Point p, String purpose)
	{
		log.add(new LoggedEvent("Received doGoToLocation. String purpose = " + purpose));
		
		arrived = false;
		xGoal = (int) p.getX();
		yGoal = (int) p.getY();
		//Specifically just for move hack
		if(error)
		{
			xDestination = xGoal;
			yDestination = yGoal;
		}
		
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
		if(purpose.equals(""))
		{
			command = Command.noCommand;
		}
	}
	
	public void findMoveBox()
	{
		log.add(new LoggedEvent("Received findMoveBox"));
		
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
		if(boxX < 25)
		{
			if (yPos > yGoal)
			{
				if((boxY - 1) >= 0)
				{
					boxesToCheck.add(gui.getBox(boxY - 1, boxX));
				}
			}
			else if (yPos < yGoal)
			{
				if((boxY + 1) < 25)
				{
					boxesToCheck.add(gui.getBox(boxY + 1, boxX));
				}
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
		if (yPos == yGoal && boxX < 25)
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
			pastBoxes.add(currentBox);
			currentBox = null;
		}
		List<MoveBox> untouchedBoxes = new ArrayList<MoveBox>();
		for(MoveBox b : boxesToCheck)
		{
			boolean untouched = true;
			for(MoveBox pb : pastBoxes)
			{
				if(b == pb)
				{
					untouched = false;
				}
			}
			if(untouched)
			{
				untouchedBoxes.add(b);
			}
		}
		for(MoveBox b : untouchedBoxes)
		{
			if(b.getOpen())
			{
				currentBox = b;
				b.setOpen(false);
				boxX = b.getIndexX();
				boxY = b.getIndexY();
				break;
			}
		}
		if(currentBox == null)
		{
			gui.setBox(boxesToCheck);
			boxesToCheck.clear();

			if(boxX < 25)
			{
				if (yPos < yGoal)
				{
					if((boxY - 1) >= 0)
					{
						boxesToCheck.add(gui.getBox(boxY - 1, boxX));
					}
				}
				else if (yPos > yGoal)
				{
					if((boxY + 1) < 25)
					{
						boxesToCheck.add(gui.getBox(boxY + 1, boxX));
					}
				}
			}
			if (xPos < xGoal)
			{
				if((boxX - 1) >= 0)
				{
					boxesToCheck.add(gui.getBox(boxY, boxX - 1));
				}
			}
			else if (xPos > xGoal)
			{
				if((boxX + 1) < 25)
				{
					boxesToCheck.add(gui.getBox(boxY, boxX + 1));
				}
			}
			
			untouchedBoxes.clear();
			for(MoveBox b : boxesToCheck)
			{
				boolean untouched = true;
				for(MoveBox pb : pastBoxes)
				{
					if(b == pb)
					{
						untouched = false;
					}
				}
				if(untouched)
				{
					untouchedBoxes.add(b);
				}
			}
			for(MoveBox b : untouchedBoxes)
			{
				if(b.getOpen())
				{
					b.setOpen(false);
					boxX = b.getIndexX();
					boxY = b.getIndexY();
					currentBox = b;
					break;
				}
			}
		}
		boxesToCheck.remove(currentBox);
		gui.setBox(boxesToCheck);
		if(currentBox == null)
		{
			error = true;
			xDestination = xGoal;
			yDestination = yGoal;
			
		}
		else
		{
			xDestination = currentBox.getX();
			yDestination = currentBox.getY();
		}
	}
}
