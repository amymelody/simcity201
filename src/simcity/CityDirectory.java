package simcity;

import java.awt.Point;
import java.util.*;

import simcity.housing.ResidentRole;
import simcity.role.JobRole;
import simcity.trace.AlertLog;
import simcity.trace.AlertTag;
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
import simcity.Anjalirestaurant.AnjaliCashierRole;
import simcity.Anjalirestaurant.AnjaliHostRole;
import simcity.Anjalirestaurant.AnjaliCustomerRole;
import simcity.Anjalirestaurant.AnjaliCookRole;
import simcity.Anjalirestaurant.AnjaliWaiterRole;
import simcity.jesusrestaurant.JesusCashierRole;
import simcity.jesusrestaurant.JesusHostRole;
import simcity.jesusrestaurant.JesusCustomerRole;
import simcity.jesusrestaurant.JesusCookRole;
import simcity.jesusrestaurant.JesusWaiterRole;*/

public class CityDirectory
{
	Map<Integer, PersonInfo> personMap = new HashMap<Integer, PersonInfo>();
	Map<String, BuildingInfo> buildingMap = new HashMap<String, BuildingInfo>();
	
	//All job roles in the city
	private Vector<JoshWaiterRole> joshWaiters = new Vector<JoshWaiterRole>();
	private Vector<MarketEmployeeRole> market1Employees = new Vector<MarketEmployeeRole>();
	private Vector<MarketEmployeeRole> market2Employees = new Vector<MarketEmployeeRole>();
	private Vector<MarketDelivererRole> market1Deliverers = new Vector<MarketDelivererRole>();
	private Vector<MarketDelivererRole> market2Deliverers = new Vector<MarketDelivererRole>();
	private Vector<BankTellerRole> bank1Tellers = new Vector<BankTellerRole>();
	private Vector<BankTellerRole> bank2Tellers = new Vector<BankTellerRole>();
	private Vector<LandlordRole> landlords = new Vector<LandlordRole>();
	private MarketCashierRole market1Cashier = new MarketCashierRole();
	private MarketCashierRole market2Cashier = new MarketCashierRole();
	private BankManagerRole bank1Manager = new BankManagerRole();
	private BankManagerRole bank2Manager = new BankManagerRole();
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
	private static final int house4X = 180;
	private static final int house4Y = 210;
	private static final int market1X = 370;
	private static final int market1Y = 280;
	private static final int market2X = 370;
	private static final int market2Y = 420;
	private static final int bank1X = 330;
	private static final int bank1Y = 280;
	private static final int bank2X = 330;
	private static final int bank2Y = 420;
	private static final int apartment1X = 300;
	private static final int apartment1Y = 70;
	private static final int apartment2X = 400;
	private static final int apartment2Y = 70;
	private static final int apartment3X = 80;
	private static final int apartment3Y = 280;
	private static final int apartment4X = 190;
	private static final int apartment4Y = 280;
	private static final int joshRestaurantX = 280;
	private static final int joshRestaurantY = 300;
	private static final int AnjaliRestaurantX = 420;
	private static final int AnjaliRestaurantY = 300;
	private static final int jesusRestaurantX = 280;
	private static final int jesusRestaurantY = 400;
	private static final int anjaliRestaurantX = 420;
	private static final int anjaliRestaurantY = 400;
	private static final int busStop1X = 70;
	private static final int busStop1Y = 80;
	private static final int busStop2X = 410;
	private static final int busStop2Y = 70;
	private static final int busStop3X = 420;
	private static final int busStop3Y = 410;
	private static final int busStop4X = 80;
	private static final int busStop4Y = 420;
	
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
			if(homeBuilding.equals("apartment10") || homeBuilding.equals("apartment11") || homeBuilding.equals("apartment12") || homeBuilding.equals("apartment13") || homeBuilding.equals("apartment14") || homeBuilding.equals("apartment15") || homeBuilding.equals("apartment16") || homeBuilding.equals("apartment17") || homeBuilding.equals("apartment18") || homeBuilding.equals("apartment19") || homeBuilding.equals("apartment110") || homeBuilding.equals("apartment111") || homeBuilding.equals("apartment112") || homeBuilding.equals("apartment113"))
			{
				apartment1ResIDs.add(ID);
			}
			else if(homeBuilding.equals("apartment20") || homeBuilding.equals("apartment21") || homeBuilding.equals("apartment22") || homeBuilding.equals("apartment23") || homeBuilding.equals("apartment24") || homeBuilding.equals("apartment25") || homeBuilding.equals("apartment26") || homeBuilding.equals("apartment27") || homeBuilding.equals("apartment28") || homeBuilding.equals("apartment29") || homeBuilding.equals("apartment210") || homeBuilding.equals("apartment211") || homeBuilding.equals("apartment212") || homeBuilding.equals("apartment213"))
			{
				apartment2ResIDs.add(ID);
			}
			else if(homeBuilding.equals("apartment30") || homeBuilding.equals("apartment31") || homeBuilding.equals("apartment32") || homeBuilding.equals("apartment33") || homeBuilding.equals("apartment34") || homeBuilding.equals("apartment35") || homeBuilding.equals("apartment36") || homeBuilding.equals("apartment37") || homeBuilding.equals("apartment38") || homeBuilding.equals("apartment39") || homeBuilding.equals("apartment310") || homeBuilding.equals("apartment311") || homeBuilding.equals("apartment312") || homeBuilding.equals("apartment313"))
			{
				apartment3ResIDs.add(ID);
			}
			else if(homeBuilding.equals("apartment40") || homeBuilding.equals("apartment41") || homeBuilding.equals("apartment42") || homeBuilding.equals("apartment43") || homeBuilding.equals("apartment44") || homeBuilding.equals("apartment45") || homeBuilding.equals("apartment46") || homeBuilding.equals("apartment47") || homeBuilding.equals("apartment48") || homeBuilding.equals("apartment49") || homeBuilding.equals("apartment410") || homeBuilding.equals("apartment411") || homeBuilding.equals("apartment412") || homeBuilding.equals("apartment413"))
			{
				apartment4ResIDs.add(ID);
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
			if(n == "bank1" || n == "bank2")
			{
				type = "bank";
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
			else if(n == "apartment1" || n == "apartment10" || n == "apartment11" || n == "apartment12" || n == "apartment13" || n == "apartment14" || n == "apartment15" || n == "apartment16" || n == "apartment17" || n == "apartment18" || n == "apartment19" || n == "apartment110" || n == "apartment111" || n == "apartment112" || n == "apartment113")
			{
				type = "housing";
				residential = new HousingInfo("apartment", 1);
			}
			else if(n == "apartment2" || n == "apartment20" || n == "apartment21" || n == "apartment22" || n == "apartment23" || n == "apartment24" || n == "apartment25" || n == "apartment26" || n == "apartment27" || n == "apartment28" || n == "apartment29" || n == "apartment210" || n == "apartment211" || n == "apartment212" || n == "apartment213")
			{
				type = "housing";
				residential = new HousingInfo("apartment", 2);
			}
			else if(n == "apartment3" || n == "apartment30" || n == "apartment31" || n == "apartment32" || n == "apartment33" || n == "apartment34" || n == "apartment35" || n == "apartment36" || n == "apartment37" || n == "apartment38" || n == "apartment39" || n == "apartment310" || n == "apartment311" || n == "apartment312" || n == "apartment313")
			{
				type = "housing";
				residential = new HousingInfo("apartment", 3);
			}
			else if(n == "apartment4" || n == "apartment40" || n == "apartment41" || n == "apartment42" || n == "apartment43" || n == "apartment44" || n == "apartment45" || n == "apartment46" || n == "apartment47" || n == "apartment48" || n == "apartment49" || n == "apartment410" || n == "apartment411" || n == "apartment412" || n == "apartment413")
			{
				type = "housing";
				residential = new HousingInfo("apartment", 4);
			}
			else if(n == "market1" || n == "market2")
			{
				type = "market";
			}
			else if(n == "anjaliRestaurant" || n == "AnjaliRestaurant" || n == "jesusRestaurant" || n == "joshRestaurant")
			{
				type = "restaurant";
			}
			loc = l;
			orientation = o;
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
	List<Integer> apartment3ResIDs;
	List<Integer> apartment4ResIDs;
	
	public ArrayList<MarketCashierRole> getMarketCashiers() {
		ArrayList<MarketCashierRole> cashiers = new ArrayList<MarketCashierRole>();
		cashiers.add(market1Cashier);
		cashiers.add(market2Cashier);
		return cashiers;
	}
	
	public ArrayList<BankManagerRole>  getBankManagers() {
		ArrayList<BankManagerRole> managers = new ArrayList<BankManagerRole>();
		managers.add(bank1Manager);
		managers.add(bank2Manager);
		return managers;
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
		apartment3ResIDs = new ArrayList<Integer>();
		apartment4ResIDs = new ArrayList<Integer>();
		
		market1Cashier.setJobLocation("market1");
		market2Cashier.setJobLocation("market2");
		bank1Manager.setJobLocation("bank1");
		bank2Manager.setJobLocation("bank2");
		joshCashier.setJobLocation("joshRestaurant");
		joshCook.setJobLocation("joshRestaurant");
		joshHost.setJobLocation("joshRestaurant");
		
		//add Buildings
		addBuilding("house1", "vertical", new Point(house1X, house1Y));
		addBuilding("house2", "horizontal", new Point(house2X, house2Y));
		addBuilding("house3", "vertical", new Point(house3X, house3Y));
		addBuilding("house4", "horizontal", new Point(house4X, house4Y));
		addBuilding("market1", "horizontal", new Point(market1X, market1Y));
		addBuilding("market2", "horizontal", new Point(market2X, market2Y));
		addBuilding("apartment1", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment10", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment11", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment12", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment13", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment14", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment15", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment16", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment17", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment18", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment19", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment110", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment111", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment112", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment113", "horizontal", new Point(apartment1X, apartment1Y));
		addBuilding("apartment2", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment20", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment21", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment22", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment23", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment24", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment25", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment26", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment27", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment28", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment29", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment210", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment211", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment212", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment213", "horizontal", new Point(apartment2X, apartment2Y));
		addBuilding("apartment3", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment30", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment31", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment32", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment33", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment34", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment35", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment36", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment37", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment38", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment39", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment310", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment311", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment312", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment313", "horizontal", new Point(apartment3X, apartment3Y));
		addBuilding("apartment4", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment40", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment41", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment42", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment43", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment44", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment45", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment46", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment47", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment48", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment49", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment410", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment411", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment412", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("apartment413", "horizontal", new Point(apartment4X, apartment4Y));
		addBuilding("bank1", "horizontal", new Point(bank1X, bank1Y));
		addBuilding("bank2", "horizontal", new Point(bank2X, bank2Y));
		addBuilding("joshRestaurant", "vertical", new Point(joshRestaurantX, joshRestaurantY));
		addBuilding("AnjaliRestaurant", "vertical", new Point(AnjaliRestaurantX, AnjaliRestaurantY));
		addBuilding("jesusRestaurant", "vertical", new Point(jesusRestaurantX, jesusRestaurantY));
		addBuilding("anjaliRestaurant", "vertical", new Point(anjaliRestaurantX, anjaliRestaurantY));
		addBuilding("busStop1", "vertical", new Point(busStop1X, busStop1Y));
		addBuilding("busStop2", "horizontal", new Point(busStop2X, busStop2Y));
		addBuilding("busStop3", "vertical", new Point(busStop3X, busStop3Y));
		addBuilding("busStop4", "horizontal", new Point(busStop4X, busStop4Y));
	
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
		case "AnjaliCustomerRole":
			return new AnjaliCustomerRole();
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
		default:
			return null;
		}
	}
	
	public BankDepositorRole BankDepositorFactory(String role) {
		switch(role) {
		case "bank1DepositorRole":
			return new BankDepositorRole();
		case "bank2DepositorRole":
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
			return la;
		}
	}
	
	public void addMarketDeliverer(MarketDelivererRole d) {
		int num1 = getNumPeople("marketDelivererRole","market1");
		int num2 = getNumPeople("marketDelivererRole","market2");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num == num1) {
			d.setJobLocation("market1");
			market1Deliverers.add(d);
		} else {
			d.setJobLocation("market2");
			market2Deliverers.add(d);
		}
	}
	
	public void addMarketEmployee(MarketEmployeeRole e) {
		int num1 = getNumPeople("marketEmployeeRole","market1");
		int num2 = getNumPeople("marketEmployeeRole","market2");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num == num1) {
			e.setJobLocation("market1");
			market1Employees.add(e);
		} else {
			e.setJobLocation("market2");
			market2Employees.add(e);
		}
	}
	
	public void addBankTeller(BankTellerRole t) {
		int num1 = getNumPeople("bankTellerRole","bank1");
		int num2 = getNumPeople("bankTellerRole","bank2");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num == num1) {
			t.setJobLocation("bank1");
			bank1Tellers.add(t);
		} else {
			t.setJobLocation("bank2");
			bank2Tellers.add(t);
		}
	}
	
	public RestWaiterRole addRestWaiter(String role) {
		/*int num1 = getNumPeople("restWaiterRole","joshRestaurant");
		int num2 = getNumPeople("restWaiterRole","AnjaliRestaurant");
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
			AnjaliWaiterRole w = new AnjaliWaiterRole();
			AnjaliWaiters.add(w);
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
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num == num1) {
			return market1Cashier;
		} else {
			return market2Cashier;
		} 
	}
	
	public BankManagerRole pickBankManager() {
		int num1 = getNumPeople("bankManagerRole","bank1");
		int num2 = getNumPeople("bankManagerRole","bank2");
		int num = num1;
		if (num > num2) {
			num = num2;
		}
		if (num == num1) {
			return bank1Manager;
		} else {
			return bank2Manager;
		} 
	}
	
	public RestCashierRole pickRestCashier() {
		/*int num1 = getNumPeople("restCashierRole","joshRestaurant");
		int num2 = getNumPeople("restCashierRole","AnjaliRestaurant");
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
			return AnjaliCashier;
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
		int num2 = getNumPeople("restCookRole","AnjaliRestaurant");
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
			return AnjaliCook;
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
		int num2 = getNumPeople("restHostRole","AnjaliRestaurant");
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
			return AnjaliHost;
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
			int num4 = getNumPeople("residentRole", "house4");
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
			if (num == num1) {
				housing = "house1";
			}
			else if (num == num2) {
				housing = "house2";
			}
			else if (num == num3) {
				housing = "house3";
			}
			else {
				housing = "house4";
			}
		} else {
			renters.add(p);
			int num1 = getNumPeople("residentRole", "apartment1");
			int num2 = getNumPeople("residentRole", "apartment2");
			int num3 = getNumPeople("residentRole", "apartment3");
			int num4 = getNumPeople("residentRole", "apartment4");
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
			
			if (num == num1) {
				housing = "apartment1" + num;
			} else if (num == num2) {
				housing = "apartment2" + num;
			} else if (num == num3) {
				housing = "apartment3" + num;
			} else {
				housing = "apartment4" + num;
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
				if(building.equals("apartment3"))
				{
					return apartment3ResIDs.size();
				}
				if(building.equals("apartment4"))
				{
					return apartment4ResIDs.size();
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
		}
		if(role.equals("marketDelivererRole")) {
			if (building.equals("market1")) {
				return market1Deliverers.size();
			}
			if (building.equals("market2")) {
				return market2Deliverers.size();
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
		}
		if(role.equals("bankTellerRole")) {
			if (building.equals("bank1")) {
				return bank1Tellers.size();
			}
			if (building.equals("bank2")) {
				return bank2Tellers.size();
			}
		}
		if(role.equals("bankManagerRole")) {
			if (building.equals("bank1")) {
				int num = 0;
				for (PersonInfo p : personMap.values()) {
					if (p.job.equals(role) && p.jobBuilding.equals(building)) {
						num++;
					}
				}
				return num;
			}
			if (building.equals("bank2")) {
				int num = 0;
				for (PersonInfo p : personMap.values()) {
					if (p.job.equals(role) && p.jobBuilding.equals(building)) {
						num++;
					}
				}
				return num;
			}
		}
		if(role.equals("bankTellerRole")) {
			if (building.equals("bank1")) {
				return bank1Tellers.size();
			}
			if (building.equals("bank2")) {
				return bank2Tellers.size();
			}
		}
		if(role.equals("restWaiterRole")) {
			if (building.equals("joshRestaurant")) {
				return joshWaiters.size();
			}
//			if (building.equals("cherysRestaurant")) {
//				return cherysWaiters.size();
//			}
//			if (building.equals("anjaliRestaurant")) {
//				return anjaliWaiters.size();
//			}
//			if (building.equals("jesusRestaurant")) {
//				return jesusWaiters.size();
//			}
		}
		if(role.equals("restHostRole")) {
			if (building.equals("joshRestaurant") || building.equals("cherysRestaurant") || building.equals("anjaliRestaurant") || building.equals("jesusRestaurant")) {
				int num = 0;
				for (PersonInfo p : personMap.values()) {
					if (p.job.equals(role) && p.jobBuilding.equals(building)) {
						num++;
					}
				}
				return num;
			}
		}
		if(role.equals("restCashierRole")) {
			if (building.equals("joshRestaurant") || building.equals("cherysRestaurant") || building.equals("anjaliRestaurant") || building.equals("jesusRestaurant")) {
				int num = 0;
				for (PersonInfo p : personMap.values()) {
					if (p.job.equals(role) && p.jobBuilding.equals(building)) {
						num++;
					}
				}
				return num;
			}
		}
		if(role.equals("restCookRole")) {
			if (building.equals("joshRestaurant") || building.equals("cherysRestaurant") || building.equals("anjaliRestaurant") || building.equals("jesusRestaurant")) {
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
	
	public String getBuildingOrientation(String building) {
		return buildingMap.get(building).orientation;
	}
	
	public void assignLandlord()
	{
		int i = 0;
		for(PersonAgent p : renters)
		{
			p.setOwnerHome(landlords.get(i).getPersonAgent().getHome());
			landlords.get(i).addRenter(p.getResident());
			i++;
			if(i >= landlords.size())
			{
				i = 0;
			}
		}
	}
}
