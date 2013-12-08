package simcity.gui;

import java.awt.*;

import simcity.CityDirectory;
import simcity.BusAgent;
import simcity.interfaces.BusStop;


public class BusGui implements Gui {
	private BusAgent agent = null;
	private CityGui gui;
	private CityDirectory city;

	private static final int width = 20;
	private static final int height = 20;
	private int xPos = 40, yPos = 80;
	private int xGoal = xPos, yGoal = yPos;
	private String destination;
	
	private enum Command {noCommand, GoToDestination};
	private Command command=Command.noCommand;
	
	public BusGui(BusAgent b, CityGui g, CityDirectory c) {
		agent = b;
		gui = g;
		city = c;
		
		if (agent.getCurrentStop().getName().equals("busStop1")) {
			xPos = 40;
			yPos = 80;
		} else if (agent.getCurrentStop().getName().equals("busStop2")) {
			xPos = 400;
			yPos = 40;
		} else if (agent.getCurrentStop().getName().equals("busStop3")) {
			xPos = 440;
			yPos = 400;
		} else if (agent.getCurrentStop().getName().equals("busStop4")) {
			xPos = 80;
			yPos = 440;
		}
		xGoal = xPos;
		yGoal = yPos;
	}

	public void updatePosition() {
		
		if (destination != null) {
			if (destination.equals("busStop1")) {
				if (xPos > xGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos-20, yPos).getOpen() && gui.getMoveBox(xPos-10, yPos).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+10).getOpen() && gui.getMoveBox(xPos-10, yPos+10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+20).getOpen() && gui.getMoveBox(xPos-10, yPos+20).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+30).getOpen() && gui.getMoveBox(xPos-10, yPos+30).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						xPos-=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				} else if (yPos > yGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos, yPos-20).getOpen() && gui.getMoveBox(xPos, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+10, yPos-20).getOpen() && gui.getMoveBox(xPos+10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-10, yPos-20).getOpen() && gui.getMoveBox(xPos-10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos-20).getOpen() && gui.getMoveBox(xPos-20, yPos-10).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						yPos-=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				} 
			}
			if (destination.equals("busStop2")) {
				if (yPos > yGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos, yPos-20).getOpen() && gui.getMoveBox(xPos, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+10, yPos-20).getOpen() && gui.getMoveBox(xPos+10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-10, yPos-20).getOpen() && gui.getMoveBox(xPos-10, yPos-10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos-20).getOpen() && gui.getMoveBox(xPos-20, yPos-10).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						yPos-=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				} else if (xPos < xGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos+20, yPos).getOpen() && gui.getMoveBox(xPos+30, yPos).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+10).getOpen() && gui.getMoveBox(xPos+30, yPos+10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-10).getOpen() && gui.getMoveBox(xPos+30, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-20).getOpen() && gui.getMoveBox(xPos+30, yPos-20).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						xPos+=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				} 
			}
			if (destination.equals("busStop3")) {
				if (xPos < xGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos+20, yPos).getOpen() && gui.getMoveBox(xPos+30, yPos).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+10).getOpen() && gui.getMoveBox(xPos+30, yPos+10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-10).getOpen() && gui.getMoveBox(xPos+30, yPos-10).getOpen()
							&& gui.getMoveBox(xPos+20, yPos-20).getOpen() && gui.getMoveBox(xPos+30, yPos-20).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						xPos+=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				} else if (yPos < yGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos, yPos+20).getOpen() && gui.getMoveBox(xPos, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+10, yPos+20).getOpen() && gui.getMoveBox(xPos+10, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+20).getOpen() && gui.getMoveBox(xPos+20, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+30, yPos+20).getOpen() && gui.getMoveBox(xPos+30, yPos+30).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						yPos+=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				}
			}
			if (destination.equals("busStop4")) {
				if (yPos < yGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos, yPos+20).getOpen() && gui.getMoveBox(xPos, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+10, yPos+20).getOpen() && gui.getMoveBox(xPos+10, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+20, yPos+20).getOpen() && gui.getMoveBox(xPos+20, yPos+30).getOpen()
							&& gui.getMoveBox(xPos+30, yPos+20).getOpen() && gui.getMoveBox(xPos+30, yPos+30).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						yPos+=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				} else if (xPos > xGoal) {
					if (agent.getNonNorm() || (gui.getMoveBox(xPos-20, yPos).getOpen() && gui.getMoveBox(xPos-10, yPos).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+10).getOpen() && gui.getMoveBox(xPos-10, yPos+10).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+20).getOpen() && gui.getMoveBox(xPos-10, yPos+20).getOpen()
							&& gui.getMoveBox(xPos-20, yPos+30).getOpen() && gui.getMoveBox(xPos-10, yPos+30).getOpen())) {
						gui.getMoveBox(xPos, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(false);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(false);
						xPos-=20;
						gui.getMoveBox(xPos, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos, yPos+10).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos).setHasVehicle(true);
						gui.getMoveBox(xPos+10, yPos+10).setHasVehicle(true);
					}
				}
			}
		}
		
		if (xPos == xGoal && yPos == yGoal) {
        	if (command == Command.GoToDestination) {
        		agent.msgAtStop();
        	}
        	command = Command.noCommand;
        }
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		g.fillRect(xPos, yPos, width, height);
	}
	
	public void DoGoToStop(BusStop b) {
		destination = b.getName();
		if (destination.equals("busStop1")) {
			xGoal = city.getBuildingEntrance(b.getName()).x-30;
			yGoal = city.getBuildingEntrance(b.getName()).y;
		} else if (destination.equals("busStop2")) {
			xGoal = city.getBuildingEntrance(b.getName()).x-10;
			yGoal = city.getBuildingEntrance(b.getName()).y-30;
		} else if (destination.equals("busStop3")) {
			xGoal = city.getBuildingEntrance(b.getName()).x+20;
			yGoal = city.getBuildingEntrance(b.getName()).y-10;
		} else {
			xGoal = city.getBuildingEntrance(b.getName()).x;
			yGoal = city.getBuildingEntrance(b.getName()).y+20;
		}
 		command = Command.GoToDestination;
	}

	public boolean isPresent() {
		return true;
	}

	public int getXPos() {
		return xPos;
	}

	public int getYPos() {
		return yPos;
	}
}
