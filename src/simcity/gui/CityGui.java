package simcity.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import simcity.CityDirectory;
import simcity.market.gui.MarketGui;
import simcity.joshrestaurant.gui.JoshRestaurantGui;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.joshrestaurant.JoshNormalWaiterRole;
import simcity.joshrestaurant.JoshSharedDataWaiterRole;
import simcity.RestWaiterRole;
import simcity.RestCustomerRole;

/**
 * Main GUI class.
 * Contains the outer city animation and input panel
 */
public class CityGui extends JFrame implements MouseListener
{
	private JFrame animationFrame = new JFrame("City Animation");
	private CityAnimationPanel animationPanel;
	private CityInputPanel inputPanel;
	private static CityDirectory cityDirectory;
	List<BuildingGui> buildings = new ArrayList<BuildingGui>();
	BuildingGui currentBG = null;
	
	private final int WINDOWX = 650;
	private final int WINDOWY = 500;
	private final int BUFFERTOP = 50;
	private final int BUFFERSIDE = 15;

	/**
	 * Constructor
	 */
	public CityGui(CityDirectory cd)
	{
		// creation of buildings
		MarketGui market1 = new MarketGui("Market 1", this, cd);
		buildings.add(market1);
		MarketGui market2 = new MarketGui("Market 2", this, cd);
		buildings.add(market2);
		MarketGui market3 = new MarketGui("Market 3", this, cd);
		buildings.add(market3);
		JoshRestaurantGui joshRestaurant = new JoshRestaurantGui("Josh's Restaurant", this, cd);
		buildings.add(joshRestaurant);
		
		animationPanel = new CityAnimationPanel(this);
		inputPanel = new CityInputPanel(this, cd, null);
		
		setBounds(BUFFERSIDE, BUFFERTOP, WINDOWX, WINDOWY);
		BorderLayout frameLayout = new BorderLayout();
		setLayout(frameLayout);

		// input panel
		double inputFractionOfWindow = 150 / 650;
		Dimension inputDim = new Dimension((int)(WINDOWX * inputFractionOfWindow), WINDOWY);
		inputPanel.setPreferredSize(inputDim);
		inputPanel.setMinimumSize(inputDim);
		inputPanel.setMaximumSize(inputDim);
		add(inputPanel, frameLayout.WEST);

		// animation panel
		double animationFractionOfWindow = 500 / 650;
		Dimension animDim = new Dimension((int)(WINDOWX * animationFractionOfWindow), WINDOWY);
		animationPanel.setPreferredSize(animDim);
		animationPanel.setMinimumSize(animDim);
		animationPanel.setMaximumSize(animDim);
		add(animationPanel, frameLayout.CENTER);
		
		addMouseListener(this);
	}
	
	public void addGui(PersonGui g) {
		animationPanel.addGui(g);
	}
	
	public void addRestCustomer(RestCustomerRole c) {
		synchronized(buildings) {
			for(BuildingGui bG: buildings) {
				if(bG.getName().equals("Josh's Restaurant")) {
					JoshRestaurantGui g = (JoshRestaurantGui)bG;
					if (c instanceof JoshCustomerRole) {
						JoshCustomerRole jC = (JoshCustomerRole)(c);
						g.addCustomer(jC);
					}
				}
			}
		}
	}
	
	public void addRestWaiter(RestWaiterRole w) {
		synchronized(buildings) {
			for(BuildingGui bG: buildings) {
				if(bG.getName().equals("Josh's Restaurant")) {
					JoshRestaurantGui g = (JoshRestaurantGui)bG;
					if (w instanceof JoshNormalWaiterRole) {
						JoshNormalWaiterRole jW = (JoshNormalWaiterRole)(w);
						g.addWaiter(jW);
					}
					if (w instanceof JoshSharedDataWaiterRole) {
						JoshSharedDataWaiterRole jW = (JoshSharedDataWaiterRole)(w);
						g.addWaiter(jW);
					}
				}
			}
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		// TODO Auto-generated method stub
		int x = event.getX();
		int y = event.getY();

		if(animationPanel.isVisible()) {
			if(x > 80 && x < 144 && y > 145 && y < 209) {
				//House 1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("House 1")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;						}
					}
				}
			}
			if(x > 80 && x < 144 && y > 80 && y < 144) {
				//House 2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("House 2")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 145 && x < 209 && y > 80 && y < 144) {
				//House 3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("House 3")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;						}
					}
				}
			}
			if(x > 145 && x < 209 && y > 145 && y < 209) {
				//Bank
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Bank")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 290 && x < 354 && y > 80 && y < 209) {
				// Apartment 1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 355 && x < 419 && y > 80 && y < 209) {
				// Apartment 2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 290 && x < 354 && y > 290 && y < 354) {
				// Market 1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Market 1")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
				
			}
			if(x > 145 && x < 209 && y > 290 && y < 354) {
				// Market 2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Market 2")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 80 && x < 144 && y > 355 && y < 419) {
				// Market 3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Market 3")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 355 && x < 419 && y > 290 && y < 354) {
				// Josh's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Josh's Restaurant")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 290 && x < 354 && y > 355 && y < 419) {
				// Cherys's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Cherys's Restaurant")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 80 && x < 144 && y > 290 && y < 354) {
				// Anjali's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Anjali's Restaurant")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 145 && x < 209 && y > 355 && y < 419) {
				// Alfred's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Alfred's Restaurant")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 355 && x < 419 && y > 355 && y < 419) {
				// Jesus's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Jesus's Restaurant")) {
							animationPanel.setVisible(false);
							inputPanel.setVisible(false);
							bG.setVisible(true);
							currentBG = bG;
						}
					}
				}
			}
		}
		else {
			currentBG.setVisible(false);
			currentBG = null;
			animationPanel.setVisible(true);
			inputPanel.setVisible(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	/**
     * Main routine to get gui started
     */
    public static void main(String[] args) {
            CityGui gui = new CityGui(cityDirectory);
            gui.setTitle("SimCity201: City of the Blind");
            gui.setVisible(true);
            gui.setResizable(false);
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
	
}