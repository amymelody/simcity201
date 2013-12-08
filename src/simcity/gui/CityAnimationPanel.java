package simcity.gui;

import javax.swing.*;
import javax.swing.Timer;

import simcity.PersonAgent;
import simcity.RestCustomerRole;
import simcity.bank.BankDepositorRole;
import simcity.bank.BankTellerRole;
import simcity.bank.gui.BankDepositorGui;
import simcity.bank.gui.BankGui;
import simcity.housing.LandlordRole;
import simcity.housing.ResidentRole;
import simcity.housing.gui.HousingGui;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.market.gui.MarketCustomerGui;
import simcity.market.gui.MarketGui;
import simcity.market.gui.MarketEmployeeGui;
import simcity.market.gui.MarketDelivererGui;
import simcity.market.gui.MarketCustomerGui;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketDelivererRole;
import simcity.market.MarketCustomerRole;
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
	static final int TIMERINCR = 140;
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
		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//background
		g2.drawImage(bg, 0, 0, null);
		
		synchronized(guis) {	
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
	
	public void addGui(BusGui g) {
		guis.add(g);
	}
	
	public void addBuilding(BuildingGui bG) {
		buildings.add(bG);
	}
	
	public void addRestCustomer(RestCustomerRole c) {
		synchronized(buildings) {
			for(BuildingGui bG: buildings) {
				if(bG.getName().equals("joshRestaurant")) {
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
				if(bG.getName().equals("joshRestaurant")) {
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
	public void addBankDepositor(BankDepositorRole d) {
		synchronized(buildings){
			for(BuildingGui bG : buildings){
				if(bG.getName().equals("bank1")){
					BankGui g = (BankGui)bG;
					g.addBankDepositor(d);
				}
			}
		}
	}
	
	public void addBankTeller(BankTellerRole t){
		synchronized(buildings){
			for(BuildingGui bG : buildings){
				if(bG.getName().equals("bank1")){
					BankGui g = (BankGui)bG;
					g.addBankTeller(t);
				}
			}
		}
	}
	
	public void addMarketEmployee(MarketEmployeeRole e){
		synchronized(buildings){
			for(BuildingGui bG : buildings){
				if(bG.getName().equals(e.getJobLocation())){
					MarketGui g = (MarketGui)bG;
					g.addMarketEmployee(e);
				}
			}
		}
	}
	
	public void addMarketDeliverer(MarketDelivererRole d){
		synchronized(buildings){
			for(BuildingGui bG : buildings){
				if(bG.getName().equals(d.getJobLocation())){
					MarketGui g = (MarketGui)bG;
					g.addMarketDeliverer(d);
				}
			}
		}
	}
	
	public void addMarketCustomer(MarketCustomerRole c, String location){
		synchronized(buildings){
			for(BuildingGui bG : buildings){
				if(bG.getName().equals(location)){
					MarketGui g = (MarketGui)bG;
					g.addMarketCustomer(c);
				}
			}
		}
	}
	
	public void addResident(ResidentRole r, String homeName, String ownerHomeName) {
		synchronized(buildings) {
			for(BuildingGui bG: buildings) {
				if(bG.getName().equals(homeName)) {
					HousingGui g = (HousingGui)bG;
					r.setHomeGui(g);
					g.addResidentGui(r.getGui());
				}
				if(bG.getName().equals(ownerHomeName)) {
					HousingGui g = (HousingGui)bG;
					r.setLandlordGui(g);
					g.addResidentGui(r.getGui());
				}
			}
		}
	}
	public void addLandlord(LandlordRole l, String buildingName) {
		System.out.println(buildingName + "----------------");
		synchronized(buildings) {
			for(BuildingGui bG: buildings) {
				if(bG.getName().equals(buildingName)) {
					System.out.println("LALALALALALALALALALAL");
					HousingGui g = (HousingGui)bG;
					l.setGui(g);
					g.addLandlordGui(l.getGui());

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
						if(bG.getName().equals("house1")) {
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
						if(bG.getName().equals("house2")) {
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
						if(bG.getName().equals("house3")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 145 && x < 209 && y > 145 && y < 209) {
				// House 4
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("house4")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 110 && y < 119) {
				// Apartment 1-1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment10")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 110 && y < 119) {
				// Apartment 1-2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment11")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 120 && y < 129) {
				// Apartment 1-3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment12")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 120 && y < 129) {
				// Apartment 1-4
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment13")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 130 && y < 139) {
				// Apartment 1-5
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment14")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 130 && y < 139) {
				// Apartment 1-6
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment15")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 140 && y < 149) {
				// Apartment 1-7
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment16")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 140 && y < 149) {
				// Apartment 1-8
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment17")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 150 && y < 159) {
				// Apartment 1-9
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment18")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 150 && y < 159) {
				// Apartment 1-10
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment19")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 160 && y < 169) {
				// Apartment 1-11
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment110")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 160 && y < 169) {
				// Apartment 1-12
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment111")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 300 && x < 310 && y > 170 && y < 179) {
				// Apartment 1-13
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment112")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 330 && y > 170 && y < 179) {
				// Apartment 1-14
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment113")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 110 && y < 119) {
				// Apartment 2-1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment20")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 110 && y < 119) {
				// Apartment 2-2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment21")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 120 && y < 129) {
				// Apartment 2-3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment22")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 120 && y < 129) {
				// Apartment 2-4
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment23")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 130 && y < 139) {
				// Apartment 2-5
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment24")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 130 && y < 139) {
				// Apartment 2-6
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment25")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 140 && y < 149) {
				// Apartment 2-7
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment26")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 140 && y < 149) {
				// Apartment 2-8
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment27")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 150 && y < 159) {
				// Apartment 2-9
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment28")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 150 && y < 159) {
				// Apartment 2-10
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment29")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 160 && y < 169) {
				// Apartment 2-11
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment210")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 160 && y < 169) {
				// Apartment 2-12
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment211")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 380 && x < 390 && y > 170 && y < 179) {
				// Apartment 2-13
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment212")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 400 && x < 410 && y > 170 && y < 179) {
				// Apartment 2-14
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment213")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 90 && x < 100 && y > 320 && y < 329) {
				// Apartment 3-1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment30")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 110 && x < 120 && y > 320 && y < 329) {
				// Apartment 3-2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment31")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 90 && x < 100 && y > 330 && y < 339) {
				// Apartment 3-3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment32")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 110 && x < 120 && y > 330 && y < 339) {
				// Apartment 3-4
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment33")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 90 && x < 100 && y > 340 && y < 349) {
				// Apartment 3-5
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment34")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 110 && x < 120 && y > 340 && y < 349) {
				// Apartment 3-6
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment35")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 90 && x < 100 && y > 350 && y < 359) {
				// Apartment 3-7
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment36")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 110 && x < 120 && y > 350 && y < 359) {
				// Apartment 3-8
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment37")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 90 && x < 100 && y > 360 && y < 369) {
				// Apartment 3-9
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment38")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 110 && x < 120 && y > 360 && y < 369) {
				// Apartment 3-10
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment39")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 90 && x < 100 && y > 370 && y < 379) {
				// Apartment 3-11
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment310")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 110 && x < 120 && y > 370 && y < 379) {
				// Apartment 3-12
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment311")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 90 && x < 100 && y > 380 && y < 389) {
				// Apartment 3-13
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment312")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 110 && x < 120 && y > 380 && y < 389) {
				// Apartment 3-14
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment313")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 170 && x < 180 && y > 320 && y < 329) {
				// Apartment 4-1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment40")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 190 && x < 200 && y > 320 && y < 329) {
				// Apartment 4-2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment41")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 170 && x < 180 && y > 330 && y < 339) {
				// Apartment 4-3
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment42")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 190 && x < 200 && y > 330 && y < 339) {
				// Apartment 4-4
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment43")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 170 && x < 180 && y > 340 && y < 349) {
				// Apartment 4-5
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment44")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 190 && x < 200 && y > 340 && y < 349) {
				// Apartment 4-6
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment45")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 170 && x < 180 && y > 350 && y < 359) {
				// Apartment 4-7
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment46")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 190 && x < 200 && y > 350 && y < 359) {
				// Apartment 4-8
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment47")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 170 && x < 180 && y > 360 && y < 369) {
				// Apartment 4-9
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment48")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 190 && x < 200 && y > 360 && y < 369) {
				// Apartment 4-10
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment49")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 170 && x < 180 && y > 370 && y < 379) {
				// Apartment 4-11
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment410")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 190 && x < 200 && y > 370 && y < 379) {
				// Apartment 4-12
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment411")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 170 && x < 180 && y > 380 && y < 389) {
				// Apartment 4-13
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment412")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 190 && x < 200 && y > 380 && y < 389) {
				// Apartment 4-14
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("apartment413")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 355 && x < 389 && y > 290 && y < 340) {
				// Market 1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("market1")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
				
			}
			if(x > 355 && x < 389 && y > 370 && y < 420) {
				// Market 2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("market2")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 320 && x < 354 && y > 290 && y < 340) {
				// Bank 1
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("bank1")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
				
			}
			if(x > 320 && x < 354 && y > 370 && y < 420) {
				// Bank 2
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("bank2")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 290 && x < 319 && y > 290 && y < 340) {
				// Josh's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("joshRestaurant")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 390 && x < 419 && y > 290 && y < 340) {
				// Cherys's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("cherysRestaurant")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 390 && x < 419 && y > 370 && y < 420) {
				// Anjali's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("anjaliRestaurant")) {
							if(currentBG != null)
								currentBG.changeView(false);
							bG.changeView(true);
							currentBG = bG;
						}
					}
				}
			}
			if(x > 290 && x < 319 && y > 370 && y < 420) {
				// Jesus's Restaurant
				synchronized(buildings) {
					for(BuildingGui bG: buildings) {
						if(bG.getName().equals("jesusRestaurant")) {
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
