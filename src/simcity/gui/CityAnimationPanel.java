package simcity.gui;

import javax.swing.*;
import javax.swing.Timer;

import simcity.PersonAgent;
import simcity.RestCustomerRole;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.market.gui.MarketCustomerGui;
import simcity.joshrestaurant.gui.JoshRestaurantGui;
import simcity.RestWaiterRole;
import simcity.joshrestaurant.JoshNormalWaiterRole;
import simcity.joshrestaurant.JoshSharedDataWaiterRole;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CityAnimationPanel extends JPanel implements ActionListener, MouseListener, MouseMotionListener
{
	static final int TIMERINCR = 8;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private Image bufferImage;
	private Dimension bufferSize;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	private CityGui cityGui;
	List<BuildingGui> buildings = Collections.synchronizedList(new ArrayList<BuildingGui>());
	BuildingGui currentBG;
	
	Image bg;
	
	public CityAnimationPanel(CityGui cG) {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		cityGui = cG;
		
		ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("images/simcity.png"));
		bg = bgIcon.getImage();
		
		bufferSize = this.getSize();

		Timer timer = new Timer(TIMERINCR, this );
		timer.start();
		
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//background
		g2.drawImage(bg, 0, 0, null);
		
		synchronized(guis) {
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
	
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.draw(g2);
				}
			}
		}
	}

	public void addGui(PersonGui g) {
		guis.add(g);
	}
	
	public void addBuilding(BuildingGui bG) {
		buildings.add(bG);
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
		int x = event.getX();
		int y = event.getY();

		if(isVisible()) {
			if(x > 80 && x < 144 && y > 145 && y < 209) {
				//House 1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("House 1")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 80 && x < 144 && y > 80 && y < 144) {
				//House 2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("House 2")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 145 && x < 209 && y > 145 && y < 209) {
				//Bank
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Bank")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 110 && y < 120) {
				// Apartment 1-1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-1")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 110 && y < 120) {
				// Apartment 1-2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-2")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 130 && y < 140) {
				// Apartment 1-3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-3")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 130 && y < 140) {
				// Apartment 1-4
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-4")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 150 && y < 160) {
				// Apartment 1-5
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-5")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 150 && y < 160) {
				// Apartment 1-6
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-6")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 170 && y < 180) {
				// Apartment 1-7
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-7")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 170 && y < 180) {
				// Apartment 1-8
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 1-8")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 110 && y < 120) {
				// Apartment 2-1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-1")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 110 && y < 120) {
				// Apartment 2-2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-2")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 130 && y < 140) {
				// Apartment 2-3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-3")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 130 && y < 140) {
				// Apartment 2-4
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-4")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 150 && y < 160) {
				// Apartment 2-5
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-5")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 150 && y < 160) {
				// Apartment 2-6
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-6")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 170 && y < 180) {
				// Apartment 2-7
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-7")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 170 && y < 180) {
				// Apartment 2-8
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("Apartment 2-8")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
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
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
		}
	}

	public void mouseEntered(MouseEvent event) {

	}

	public void mouseExited(MouseEvent event) {

	}

	public void mousePressed(MouseEvent event) {

	}

	public void mouseReleased(MouseEvent event) {
	
	}
	public void mouseDragged(MouseEvent arg0) {
		
	}
	public void mouseMoved(MouseEvent event) {

	}
    
}
