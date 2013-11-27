package simcity;

import java.awt.Point;
import java.util.*;

import simcity.housing.ResidentRole;
import simcity.role.JobRole;
import simcity.housing.LandlordRole;
import simcity.bank.BankManagerRole;
import simcity.bank.BankTellerRole;
import simcity.bank.BankDepositorRole;
import simcity.market.MarketEmployeeRole;
import simcity.market.MarketCashierRole;
import simcity.market.MarketDelivererRole;
import simcity.market.MarketCustomerRole;
import simcity.RestCustomerRole;
import simcity.joshrestaurant.JoshCashierRole;
import simcity.joshrestaurant.JoshHostRole;
import simcity.joshrestaurant.JoshCustomerRole;
import simcity.joshrestaurant.JoshCookRole;
import simcity.joshrestaurant.JoshWaiterRole;
import simcity.joshrestaurant.JoshNormalWaiterRole;
import simcity.joshrestaurant.JoshSharedDataWaiterRole;
/*import simcity.anjalirestaurant.AnjaliCashierRole;
import simcity.anjalirestaurant.AnjaliHostRole;
import simcity.anjalirestaurant.AnjaliCustomerRole;
import simcity.anjalirestaurant.AnjaliCookRole;
import simcity.anjalirestaurant.AnjaliWaiterRole;
import simcity.cherysrestaurant.CherysCashierRole;
import simcity.cherysrestaurant.CherysHostRole;
import simcity.cherysrestaurant.CherysCustomerRole;
import simcity.cherysrestaurant.CherysCookRole;
import simcity.cherysrestaurant.CherysWaiterRole;
import simcity.jesusrestaurant.JesusCashierRole;
import simcity.jesusrestaurant.JesusHostRole;
import simcity.jesusrestaurant.JesusCustomerRole;
import simcity.jesusrestaurant.JesusCookRole;
import simcity.jesusrestaurant.JesusWaiterRole;
import simcity.alfredrestaurant.AlfredCashierRole;
import simcity.alfredrestaurant.AlfredHostRole;
import simcity.alfredrestaurant.AlfredCustomerRole;
import simcity.alfredrestaurant.AlfredCookRole;
import simcity.alfredrestaurant.AlfredWaiterRole;*/

public class CityDirectory
{
	Map<Integer, PersonInfo> personMap = new HashMap<Integer, PersonInfo>();
	Map<String, BuildingInfo> buildingMap = new HashMap<String, BuildingInfo>();
	private List<TrafficNode> trafficNodes = new ArrayList<TrafficNode>();
	
	//All job roles in the city
	private Vector<JoshWaiterRole> joshWaiters = new Vector<JoshWaiterRole>();
	private Vector<MarketEmployeeRole> market1Employees = new Vector<MarketEmployeeRole>();
	private Vector<MarketEmployeeRole> market2Employees = new Vector<MarketEmployeeRole>();
	private Vector<MarketEmployeeRole> market3Employees = new Vector<MarketEmployeeRole>();
	private Vector<MarketDelivererRole> market1Deliverers = new Vector<MarketDelivererRole>();
	private Vector<MarketDelivererRole> market2Deliverers = new Vector<MarketDelivererRole>();
	private Vector<MarketDelivererRole> market3Deliverers = new Vector<MarketDelivererRole>();
	private Vector<BankTellerRole> bank1Tellers = new Vector<BankTellerRole>();
	private Vector<LandlordRole> landlords = new Vector<LandlordRole>();
	private MarketCashierRole market1Cashier = new MarketCashierRole();
	private MarketCashierRole market2Cashier = new MarketCashierRole();
	private MarketCashierRole market3Cashier = new MarketCashierRole();
	private BankManagerRole bank1Manager = new BankManagerRole();
	private JoshCashierRole joshCashier = new JoshCashierRole();
	private JoshCookRole joshCook = new JoshCookRole();
	private JoshHostRole joshHost = new JoshHostRole();
	
	private List<PersonAgent> renters = new ArrayList<PersonAgent>();
	
	private static final int house1X = 70;
	private static final int house1Y = 180;
	private static final int house2X = 100;
	private static final int house2Y = 70;
	private static final int house3X = 210;
	private static final int house3Y = 100;
	private static final int market1X = 280;
	private static final int market1Y = 280;
	private static final int market2X = 210;
	private static final int market2Y = 280;
	private static final int market3X = 70;
	private static final int market3Y = 420;
	private static final int bankX = 190;
	private static final int bankY = 210;
	private static final int apartment1X = 300;
	private static final int apartment1Y = 70;
	private static final int apartment2X = 400;
	private static final int apartment2Y = 70;
	private static final int joshRestaurantX = 420;
	private static final int joshRestaurantY = 280;
	
	class PersonInfo
	{
		int ID;
		PersonAgent person;
		String name;
		String job;
		String jobBuilding;
		String homeBuilding;
		PersonInfo(int i, PersonAgent p, String jobB, String homeB)
		{
			ID = i;
			person = p;
			name = p.getName();
			job = p.getJob();
			jobBuilding = jobB;
			homeBuilding = homeB;
			if(homeBuilding == "apartment10" || homeBuilding == "apartment11" || homeBuilding == "apartment12" || homeBuilding == "apartment13" || homeBuilding == "apartment14" || homeBuilding == "apartment15" || homeBuilding == "apartment16" || homeBuilding == "apartment17")
			{
				apartment1ResIDs.add(ID);
			}
			else if(homeBuilding == "apartment20" || homeBuilding == "apartment21" || homeBuilding == "apartment22" || homeBuilding == "apartment23" || homeBuilding == "apartment24" || homeBuilding == "apartment25" || homeBuilding == "apartment26" || homeBuilding == "apartment27")
			{
				apartment2ResIDs.add(ID);
			}
		}
	}
	
	class BuildingInfo
	{
		String name;
		String type;
		String orientation;
		Point loc; //the top right corner of the entrance
		Point parkingLoc; //the top right corner of the entrance to the garage
		HousingInfo residential = null;
		BuildingInfo(String n, String o, Point l)
		{
			name = n;
			if(n == "bank")
			{
				type = n;
			}
			else if(n == "house1")
			{
				type = "housing";
				residential = new HousingInfo("house", 1);
			}
			else if(n == "house2")
			{
				type = "housing";
				residential = new HousingInfo("house", 2);
			}
			else if(n == "house3")
			{
				type = "housing";
				residential = new HousingInfo("house", 3);
			}
			else if(n == "house4")
			{
				type = "housing";
				residential = new HousingInfo("house", 4);
			}
			else if(n == "house5")
			{
				type = "housing";
				residential = new HousingInfo("house", 5);
			}
			else if(n == "house6")
			{
				type = "housing";
				residential = new HousingInfo("house", 6);
			}
			else if(n == "apartment1" || n == "apartment10" || n == "apartment11" || n == "apartment12" || n == "apartment13" || n == "apartment14" || n == "apartment15" || n == "apartment16" || n == "apartment17")
			{
				type = "housing";
				residential = new HousingInfo("apartment", 1);
			}
			else if(n == "apartment2" || n == "apartment20" || n == "apartment21" || n == "apartment22" || n == "apartment23" || n == "apartment24" || n == "apartment25" || n == "apartment26" || n == "apartment27")
			{
				type = "housing";
				residential = new HousingInfo("apartment", 2);
			}
			else if(n == "market1" || n == "market2" || n == "market3")
			{
				type = "market";
			}
			else if(n == "anjaliRestaurant" || n == "alfredRestaurant" || n == "cherysRestaurant" || n == "jesusRestaurant" || n == "joshRestaurant")
			{
				type = "restaurant";
			}
			loc = l;
		}
	}
	class HousingInfo
	{
		String type;
		int number;
		int landlordID = -1;
		int residentID = -1;
		HousingInfo(String t, int n)
		{
			type = t;
			number = n;
		}
	}
	List<Integer> apartment1ResIDs;
	List<Integer> apartment2ResIDs;
	
	public ArrayList<MarketCashierRole> getMarketCashiers() {
		ArrayList<MarketCashierRole> cashiers = new ArrayList<MarketCashierRole>();
		cashiers.add(market1Cashier);
		cashiers.add(market2Cashier);
		cashiers.add(market3Cashier);
		return cashiers;
	}
	
	public BankManagerRole  getBankManager() {
		return bank1Manager;
	}
	
	public JoshCashierRole getJoshCashier() {
		return joshCashier;
	}
	
	public JoshCookRole getJoshCook() {
		return joshCook;
	}
	
	public JoshHostRole getJoshHost() {
		return joshHost;
	}
	
	public CityDirectory()
	{
		apartment1ResIDs = new ArrayList<Integer>();
		apartment2ResIDs = new ArrayList<Integer>();
		
		market1Cashier.setJobLocation("market1");
		market2Cashier.setJobLocation("market2");
		market3Cashier.setJobLocation("market3");
		bank1Manager.setJobLocation("bank1");
		joshCashier.setJobLocation("joshRestaurant");
		joshCook.setJobLocation("joshRestaurant");
		joshHost.setJobLocation("joshRestaurant");
		
		//add Buildings
		addBuilding("house1", "vertical", new Point(house1X, house1Y));
		addBuilding("house2", "horizontal", new Point(house2X, house2Y));
		addBuilding("house3", "vertical", new Point(house3X, house3Y));
		addBuilding("market1", "vertical", new Point(market1X, market1Y));
		addBuilding("market2", "vertical", new Point(market2X, market2Y));
		addBuilding("market3", "vertical", new Point(market3X, market3Y));
		addBuilding("apartment1", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment10", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment11", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment12", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment13", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment14", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment15", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment16", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment17", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment2", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment20", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment21", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment22", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment23", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment24", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment25", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment26", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment27", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("bank1", "horizontal", new Point(bankX, bankY));
		addBuilding("joshRestaurant", "horizontal", new Point(joshRestaurantX, joshRestaurantY));
	
		//add Traffic Nodes
		trafficNodes.add(new TrafficNode(0,0));
		trafficNodes.add(new TrafficNode(70,0));
		trafficNodes.add(new TrafficNode(210,0));
		trafficNodes.add(new TrafficNode(280,0));
		trafficNodes.add(new TrafficNode(420,0));
		trafficNodes.add(new TrafficNode(480,0));
		trafficNodes.add(new TrafficNode(0,70));
		trafficNodes.add(new TrafficNode(70,70));
		trafficNodes.add(new TrafficNode(210,70));
		trafficNodes.add(new TrafficNode(280,70));
		trafficNodes.add(new TrafficNode(420,70));
		trafficNodes.add(new TrafficNode(480,70));
		trafficNodes.add(new TrafficNode(0,210));
		trafficNodes.add(new TrafficNode(70,210));
		trafficNodes.add(new TrafficNode(210,210));
		trafficNodes.add(new TrafficNode(280,210));
		trafficNodes.add(new TrafficNode(420,210));
		trafficNodes.add(new TrafficNode(480,210));
		trafficNodes.add(new TrafficNode(0,280));
		trafficNodes.add(new TrafficNode(70,280));
		trafficNodes.add(new TrafficNode(210,280));
		trafficNodes.add(new TrafficNode(280,280));
		trafficNodes.add(new TrafficNode(420,280));
		trafficNodes.add(new TrafficNode(480,280));
		trafficNodes.add(new TrafficNode(0,420));
		trafficNodes.add(new TrafficNode(70,420));
		trafficNodes.add(new TrafficNode(210,420));
		trafficNodes.add(new TrafficNode(280,420));
		trafficNodes.add(new TrafficNode(420,420));
		trafficNodes.add(new TrafficNode(480,420));
		trafficNodes.add(new TrafficNode(0,480));
		trafficNodes.add(new TrafficNode(70,480));
		trafficNodes.add(new TrafficNode(210,480));
		trafficNodes.add(new TrafficNode(280,480));
		trafficNodes.add(new TrafficNode(420,480));
		trafficNodes.add(new TrafficNode(480,480));
		
		/*for (TrafficNode node : trafficNodes) {
			for (TrafficNode n : trafficNodes) {
				if (n.x == node.x && n.y != node.y) {
					if (n.)
				}
				if (n.y == node.y && n.x != node.x) {
					
				}
			}
		}*/
	}
	
	public ResidentRole ResidentFactory(String role) {
		switch(role) {
		case "residentRole":
			return new ResidentRole();
		default:
			return null;
		}
	}
	
	public RestCustomerRole RestCustomerFactory(String role) {
		switch(role) {
		case "joshCustomerRole":
			return new JoshCustomerRole();
		/*case "anjaliCustomerRole":
			return new AnjaliCustomerRole();
		case "cherysCustomerRole":
			return new CherysCustomerRole();
		case "alfredCustomerRole":
			return new AlfredCustomerRole();
		case "jesusCustomerRole":
			return new JesusCustomerRole();*/
		default:
			return null;
		}
	}
	
	public MarketCustomerRole MarketCustomerFactory(String role) {
		switch(role) {
		case "market1CustomerRole":
			return new MarketCustomerRole();
		case "market2CustomerRole":
			return new MarketCustomerRole();
		case "market3CustomerRole":
			return new MarketCustomerRole();
		default:
			return null;
		}
	}
	
	public BankDepositorRole BankDepositorFactory(String role) {
		switch(role) {
		case "bank1DepositorRole":
			return new BankDepositorRole();
		default:
			return null;
		}
	}
	
	public JobRole JobFactory(String role) {
		switch(role) {
		case "landlordRole":
			LandlordRole l = new LandlordRole();
			l.setJobLocation("home");
			landlords.add(l);
			return l;
		case "marketCashierRole":
			return pickMarketCashier();
		case "marketDelivererRole":
			MarketDelivererRole d = new MarketDelivererRole();
			addMarketDeliverer(d);
			return d;
		case "marketEmployeeRole":
			MarketEmployeeRole e  = new MarketEmployeeRole();
			addMarketEmployee(e);
			return e;
		case "bankTellerRole":
			BankTellerRole t = new BankTellerRole();
			addBankTeller(t);
			return t;
		case "bankManagerRole":
			return pickBankManager();
		case "restCashierRole":
			return pickRestCashier();
		case "restCookRole":
			return pickRestCook();
		case "restHostRole":
			return pickRestHost();
		case "restWaiter1Role": case "restWaiter2Role":
			return addRestWaiter(role);
		default:
			LandlordRole la = new LandlordRole();
			la.setJobLocation("home");
			landlords.add(la);
			return la;
		}
	}
	
	public void addMarketDeliverer(MarketDelivererRole d) {
		int num1 = getNumPeople("marketDelivererRole","market1");
		int num2 = getNumPeople("marketDelivererRole","market2");
		int num3 = getNumPeople("marketDelivererRole","market3");
	//	System.out.println(num1);
	//	System.out.println(num2);
	//	System.out.println(num3);
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num > num3) {
			num = num3;
		}
		if (num == num1) {
			d.setJobLocation("market1");
			market1Deliverers.add(d);
		} else if (num == num2) {
			d.setJobLocation("market2");
			market2Deliverers.add(d);
		} else {
			d.setJobLocation("market3");
			market3Deliverers.add(d);
		}
	}
	
	public void addMarketEmployee(MarketEmployeeRole e) {
		int num1 = getNumPeople("marketEmployeeRole","market1");
		int num2 = getNumPeople("marketEmployeeRole","market2");
		int num3 = getNumPeople("marketEmployeeRole","market3");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num > num3) {
			num = num3;
		}
		if (num == num1) {
			e.setJobLocation("market1");
			market1Employees.add(e);
		} else if (num == num2) {
			e.setJobLocation("market2");
			market2Employees.add(e);
		} else {
			e.setJobLocation("market3");
			market3Employees.add(e);
		}
	}
	
	public void addBankTeller(BankTellerRole t) {
		t.setJobLocation("bank1");
		bank1Tellers.add(t);
	}
	
	public RestWaiterRole addRestWaiter(String role) {
		/*int num1 = getNumPeople("restWaiterRole","joshRestaurant");
		int num2 = getNumPeople("restWaiterRole","cherysRestaurant");
		int num3 = getNumPeople("restWaiterRole","anjaliRestaurant");
		int num4 = getNumPeople("restWaiterRole","alfredRestaurant");
		int num5 = getNumPeople("restWaiterRole","jesusRestaurant");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num > num3) {
			num = num3;
		}
		if (num > num4) {
			num = num4;
		}
		if (num > num5) {
			num = num5;
		}*/
		//if (num == num1) {
			if (role.equals("restWaiter1Role")) {
				JoshNormalWaiterRole w = new JoshNormalWaiterRole();
				w.setJobLocation("joshRestaurant");
				joshWaiters.add(w);
				return w;
			} else {
				JoshSharedDataWaiterRole w = new JoshSharedDataWaiterRole();
				w.setJobLocation("joshRestaurant");
				joshWaiters.add(w);
				return w;
			}
		/*} else if (num == num2) {
			CherysWaiterRole w = new CherysWaiterRole();
			cherysWaiters.add(w);
			return w;
		} else if (num == num3) {
			AnjaliWaiterRole w = new AnjaliWaiterRole();
			anjaliWaiters.add(w);
			return w;
		} else if (num == num4) {
			AlfredWaiterRole w = new AlfredWaiterRole();
			alfredWaiters.add(w);
			return w;
		} else {
			JesusWaiterRole w = new JesusWaiterRole();
			jesusWaiters.add(w);
			return w;
		}*/
	}
	
	public MarketCashierRole pickMarketCashier() {
		int num1 = getNumPeople("marketCashierRole","market1");
		int num2 = getNumPeople("marketCashierRole","market2");
		int num3 = getNumPeople("marketCashierRole","market3");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num > num3) {
			num = num3;
		}
		if (num == num1) {
			return market1Cashier;
		} else if (num == num2) {
			return market2Cashier;
		} else {
			return market3Cashier;
		}
	}
	
	public BankManagerRole pickBankManager() {
		return bank1Manager;
	}
	
	public RestCashierRole pickRestCashier() {
		/*int num1 = getNumPeople("restCashierRole","joshRestaurant");
		int num2 = getNumPeople("restCashierRole","cherysRestaurant");
		int num3 = getNumPeople("restCashierRole","anjaliRestaurant");
		int num4 = getNumPeople("restCashierRole","alfredRestaurant");
		int num5 = getNumPeople("restCashierRole","jesusRestaurant");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num > num3) {
			num = num3;
		}
		if (num > num4) {
			num = num4;
		}
		if (num > num5) {
			num = num5;
		}*/
		//if (num == num1) {
			return joshCashier;
		/*} else if (num == num2) {
			return cherysCashier;
		} else if (num == num3) {
			return anjaliCashier;
		} else if (num == num4) {
			return alfredCashier;
		} else {
			return jesusCashier;
		}*/
	}
	
	public RestCookRole pickRestCook() {
		/*int num1 = getNumPeople("restCookRole","joshRestaurant");
		int num2 = getNumPeople("restCookRole","cherysRestaurant");
		int num3 = getNumPeople("restCookRole","anjaliRestaurant");
		int num4 = getNumPeople("restCookRole","alfredRestaurant");
		int num5 = getNumPeople("restCookRole","jesusRestaurant");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num > num3) {
			num = num3;
		}
		if (num > num4) {
			num = num4;
		}
		if (num > num5) {
			num = num5;
		}*/
		//if (num == num1) {
			return joshCook;
		/*} else if (num == num2) {
			return cherysCook;
		} else if (num == num3) {
			return anjaliCook;
		} else if (num == num4) {
			return alfredCook;
		} else {
			return jesusCook;
		}*/
	}
	
	public RestHostRole pickRestHost() {
		/*int num1 = getNumPeople("restHostRole","joshRestaurant");
		int num2 = getNumPeople("restHostRole","cherysRestaurant");
		int num3 = getNumPeople("restHostRole","anjaliRestaurant");
		int num4 = getNumPeople("restHostRole","alfredRestaurant");
		int num5 = getNumPeople("restHostRole","jesusRestaurant");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num > num3) {
			num = num3;
		}
		if (num > num4) {
			num = num4;
		}
		if (num > num5) {
			num = num5;
		}*/
		//if (num == num1) {
			return joshHost;
		/*} else if (num == num2) {
			return cherysHost;
		} else if (num == num3) {
			return anjaliHost;
		} else if (num == num4) {
			return alfredHost;
		} else {
			return jesusHost;
		}*/
	}
	
	public void addBuilding(String name, String orientation, Point loc)
	{
		buildingMap.put(name, new BuildingInfo(name, orientation, loc));
	}
	public void addPerson(PersonAgent p, String jobB, String homeB)
	{
		String housing;
		if (homeB.equals("owner")) {
			int num1 = getNumPeople("residentRole", "house1");
			int num2 = getNumPeople("residentRole", "house2");
			int num3 = getNumPeople("residentRole", "house3");
			int num = num1;
			if (num > num2) {
				num = num2;
			}
			if (num > num3) {
				num = num3;
			}
			if (num == num1) {
				housing = "house1";
			}
			else if (num == num2) {
				housing = "house2";
			}
			else {
				housing = "house3";
			}
		} else {
			renters.add(p);
			int num1 = getNumPeople("residentRole", "apartment10");
			int num3 = getNumPeople("residentRole", "apartment11");
			int num5 = getNumPeople("residentRole", "apartment12");
			int num7 = getNumPeople("residentRole", "apartment13");
			int num9 = getNumPeople("residentRole", "apartment14");
			int num11 = getNumPeople("residentRole", "apartment15");
			int num13 = getNumPeople("residentRole", "apartment16");
			int num15 = getNumPeople("residentRole", "apartment17");
			int num2 = getNumPeople("residentRole", "apartment20");
			int num4 = getNumPeople("residentRole", "apartment21");
			int num6 = getNumPeople("residentRole", "apartment22");
			int num8 = getNumPeople("residentRole", "apartment23");
			int num10 = getNumPeople("residentRole", "apartment24");
			int num12 = getNumPeople("residentRole", "apartment25");
			int num14 = getNumPeople("residentRole", "apartment26");
			int num16 = getNumPeople("residentRole", "apartment27");
			int num = num1;
			if (num > num2) {
				num = num2;
			}
			if (num > num3) {
				num = num3;
			}
			if (num > num4) {
				num = num4;
			}
			if (num > num5) {
				num = num5;
			}
			if (num > num6) {
				num = num6;
			}
			if (num > num7) {
				num = num7;
			}
			if (num > num8) {
				num = num8;
			}
			if (num > num9) {
				num = num9;
			}
			if (num > num10) {
				num = num10;
			}
			if (num > num11) {
				num = num11;
			}
			if (num > num12) {
				num = num12;
			}
			if (num > num13) {
				num = num13;
			}
			if (num > num14) {
				num = num14;
			}
			if (num > num15) {
				num = num15;
			}
			if (num > num16) {
				num = num16;
			}
			
			if (num == num1) {
				housing = "apartment10";
			}
			else if (num == num2) {
				housing = "apartment20";
			}
			else if (num == num3) {
				housing = "apartment11";
			}
			else if (num == num4) {
				housing = "apartment21";
			}
			else if (num == num5) {
				housing = "apartment12";
			}
			else if (num == num6) {
				housing = "apartment22";
			}
			else if (num == num7) {
				housing = "apartment13";
			}
			else if (num == num8) {
				housing = "apartment23";
			}
			else if (num == num9) {
				housing = "apartment14";
			}
			else if (num == num10) {
				housing = "apartment24";
			}
			else if (num == num11) {
				housing = "apartment15";
			}
			else if (num == num12) {
				housing = "apartment25";
			}
			else if (num == num13) {
				housing = "apartment16";
			}
			else if (num == num14) {
				housing = "apartment26";
			}
			else if (num == num15) {
				housing = "apartment17";
			}
			else {
				housing = "apartment27";
			}
		}
		int iD = getNumPeople("residentRole", housing);
		buildingMap.get(housing).residential.residentID = iD;
		p.setHome(housing);
		personMap.put(personMap.size(), new PersonInfo(personMap.size(), p, jobB, housing));
	}
	
	public int getNumPeople(String role, String building)
	{
		if(role.equals("residentRole"))
		{
			if(buildingMap.get(building).residential.type.equals("apartment"))
			{
				if(building.equals("apartment1"))
				{
					return apartment1ResIDs.size();
				}
				if(building.equals("apartment2"))
				{
					return apartment2ResIDs.size();
				}
				if (buildingMap.get(building).residential.residentID == -1) {
					return 0;
				}
				return 1;
			}
			if(buildingMap.get(building).residential.type.equals("house"))
			{
				if (buildingMap.get(building).residential.residentID == -1) {
					return 0;
				}
				return 1;
			}
		}
		if(role.equals("marketEmployeeRole")) {
			if (building.equals("market1")) {
				return market1Employees.size();
			}
			if (building.equals("market2")) {
				return market2Employees.size();
			}
			if (building.equals("market3")) {
				return market3Employees.size();
			}
		}
		if(role.equals("marketDelivererRole")) {
			if (building.equals("market1")) {
				return market1Deliverers.size();
			}
			if (building.equals("market2")) {
				return market2Deliverers.size();
			}
			if (building.equals("market3")) {
				return market3Deliverers.size();
			}
		}
		if(role.equals("marketCashierRole")) {
			if (building.equals("market1")) {
				int num = 0;
				for (PersonInfo p : personMap.values()) {
					if (p.job.equals(role) && p.jobBuilding.equals(building)) {
						num++;
					}
				}
				return num;
			}
			if (building.equals("market2")) {
				int num = 0;
				for (PersonInfo p : personMap.values()) {
					if (p.job.equals(role) && p.jobBuilding.equals(building)) {
						num++;
					}
				}
				return num;
			}
			if (building.equals("market3")) {
				int num = 0;
				for (PersonInfo p : personMap.values()) {
					if (p.job.equals(role) && p.jobBuilding.equals(building)) {
						num++;
					}
				}
				return num;
			}
		}
		return 0;
	}
	
	public Point getBuildingEntrance(String building) {
		return buildingMap.get(building).loc;
	}
	
	public void assignLandlord()
	{
		for(PersonAgent p : renters)
		{
			p.setOwnerHome(landlords.get(0).getPersonAgent().getHome());
		}
	}
}
