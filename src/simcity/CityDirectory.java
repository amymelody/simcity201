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
	Map<Integer, PersonInfo> personMap;
	
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
	/*private MarketCashierRole market1Cashier = new MarketCashierRole();
	private MarketCashierRole market2Cashier = new MarketCashierRole();
	private MarketCashierRole market3Cashier = new MarketCashierRole();*/
	//private BankManagerRole bank1Manager = new BankManagerRole();
	private JoshCashierRole joshCashier = new JoshCashierRole();
	private JoshCookRole joshCook = new JoshCookRole();
	private JoshHostRole joshHost = new JoshHostRole();
	
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
			job = p.getJob(); //not actual function yet
			jobBuilding = jobB;
			homeBuilding = homeB;
			if(homeBuilding == "apartment10" || homeBuilding == "apartment11" || homeBuilding == "apartment12" || homeBuilding == "apartment13" || homeBuilding == "apartment14" || homeBuilding == "apartment15")
			{
				apartment1ResIDs.add(ID);
			}
			else if(homeBuilding == "apartment20" || homeBuilding == "apartment21" || homeBuilding == "apartment22" || homeBuilding == "apartment23" || homeBuilding == "apartment24" || homeBuilding == "apartment25")
			{
				apartment2ResIDs.add(ID);
			}
		}
	}
	Map<String, BuildingInfo> buildingMap;
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
			else if(n == "apartment10" || n == "apartment11" || n == "apartment12" || n == "apartment13" || n == "apartment14" || n == "apartment15")
			{
				type = "housing";
				residential = new HousingInfo("apartment", 1);
			}
			else if(n == "apartment20" || n == "apartment21" || n == "apartment22" || n == "apartment23" || n == "apartment24" || n == "apartment25")
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
		int residentID;
		HousingInfo(String t, int n)
		{
			type = t;
			number = n;
		}
	}
	List<Integer> apartment1ResIDs;
	List<Integer> apartment2ResIDs;
	
	/*public ArrayList<MarketCashierRole> getMarketCashiers() {
		ArrayList<MarketCashierRole> cashiers = new ArrayList<MarketCashierRole>();
		cashiers.add(market1Cashier);
		cashiers.add(market2Cashier);
		cashiers.add(market3Cashier);
		return cashiers;
	}*/
	
	/*public BankManagerRole  getBankManager() {
		return bank1Manager;
	}*/
	
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
		personMap = new HashMap<Integer, PersonInfo>();
		buildingMap = new HashMap<String, BuildingInfo>();
		apartment1ResIDs = new ArrayList<Integer>();
		apartment2ResIDs = new ArrayList<Integer>();
		
		/*market1Cashier.setJobLocation("market1");
		market2Cashier.setJobLocation("market2");
		market3Cashier.setJobLocation("market3");*/
		//bank1Manager.setJobLocation("bank1");
		joshCashier.setJobLocation("joshRestaurant");
		joshCook.setJobLocation("joshRestaurant");
		joshHost.setJobLocation("joshRestaurant");
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
		//case "marketCashierRole":
			//return pickMarketCashier();
		case "marketDelivererRole":
			MarketDelivererRole d = new MarketDelivererRole();
			addMarketDeliverer(d);
			return d;
		case "marketEmployeeRole":
			MarketEmployeeRole e  = new MarketEmployeeRole();
			addMarketEmployee(e);
			return e;
		/*case "bankTellerRole":
			BankTellerRole t = new BankTellerRole();
			addBankTeller(t);
			return t;
		case "bankManagerRole":
			return pickBankManager();*/
		case "restCashierRole":
			return pickRestCashier();
		case "restCookRole":
			return pickRestCook();
		case "restHostRole":
			return pickRestHost();
		case "restWaiterRole":
			return addRestWaiter();
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
	
	public RestWaiterRole addRestWaiter() {
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
			JoshWaiterRole w = new JoshWaiterRole();
			w.setJobLocation("joshRestaurant");
			joshWaiters.add(w);
			return w;
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
	
	/*public MarketCashierRole pickMarketCashier() {
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
	}*/
	
	/*public BankManagerRole pickBankManager() {
		return bank1Manager;
	}*/
	
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
		personMap.put(personMap.size(), new PersonInfo(personMap.size(), p, jobB, homeB));
	}
	
	public int getNumPeople(String role, String building)
	{
		if(role.equals("residentRole"))
		{
			if(buildingMap.get(building).residential.type.equals("apartment"))
			{
				if(buildingMap.get(building).residential.number == 1)
				{
					return apartment1ResIDs.size();
				}
				if(buildingMap.get(building).residential.number == 2)
				{
					return apartment2ResIDs.size();
				}
			}
		}
		if(role.equals(market)) //in progress
		return 0;
	}
	
	public Point getBuildingEntrance(String building) {
		return buildingMap.get(building).loc;
	}
}
