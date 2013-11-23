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
	private MarketCashierRole market1Cashier = new MarketCashierRole();
	private MarketCashierRole market2Cashier = new MarketCashierRole();
	private MarketCashierRole market3Cashier = new MarketCashierRole();
	private BankManagerRole bank1Manager = new BankManagerRole();
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
	
	public CityDirectory()
	{
		personMap = new HashMap<Integer, PersonInfo>();
		buildingMap = new HashMap<String, BuildingInfo>();
		apartment1ResIDs = new ArrayList<Integer>();
		apartment2ResIDs = new ArrayList<Integer>();
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
		case "restWaiterRole":
			return addRestWaiter();
		}
	}
	
	public void addMarketDeliverer(MarketDelivererRole d) {
		int num = getNumPeople("marketDelivererRole","market1");
		if (num > getNumPeople("marketDelivererRole","market2")) {
			num = getNumPeople("marketDelivererRole","market2");
		}
		if (num > getNumPeople("marketDelivererRole","market3")) {
			num = getNumPeople("marketDelivererRole","market3");
		}
		switch(num) {
		case getNumPeople("marketDelivererRole","market1"):
			market1Deliverers.add(d);
			break;
		case getNumPeople("marketDelivererRole","market2"):
			market2Deliverers.add(d);
			break;
		case getNumPeople("marketDelivererRole","market3"):
			market3Deliverers.add(d);
			break;
		default:
			market1Deliverers.add(d);
			break;
		}
	}
	
	public void addMarketEmployee(MarketEmployeeRole e) {
		int num = getNumPeople("marketEmployeeRole","market1");
		if (num > getNumPeople("marketEmployeeRole","market2")) {
			num = getNumPeople("marketEmployeeRole","market2");
		}
		if (num > getNumPeople("marketEmployeeRole","market3")) {
			num = getNumPeople("marketEmployeeRole","market3");
		}
		switch(num) {
		case getNumPeople("marketEmployeeRole","market1"):
			market1Employees.add(e);
			break;
		case getNumPeople("marketEmployeeRole","market2"):
			market2Employees.add(e);
			break;
		case getNumPeople("marketEmployeeRole","market3"):
			market3Employees.add(e);
			break;
		default:
			market1Employees.add(e);
			break;
		}
	}
	
	public void addBankTeller(BankTellerRole t) {
		bank1Tellers.add(t);
	}
	
	public RestWaiterRole addRestWaiter() {
		int num = getNumPeople("restWaiterRole","joshRestaurant");
		/*if (num > getNumPeople("restWaiterRole","cherysRestaurant")) {
			num = getNumPeople("restWaiterRole","cherysRestaurant");
		}
		if (num > getNumPeople("restWaiterRole","anjaliRestaurant")) {
			num = getNumPeople("restWaiterRole","anjaliRestaurant");
		}
		if (num > getNumPeople("restWaiterRole","alfredRestaurant")) {
			num = getNumPeople("restWaiterRole","alfredRestaurant");
		}
		if (num > getNumPeople("restWaiterRole","jesusRestaurant")) {
			num = getNumPeople("restWaiterRole","jesusRestaurant");
		}*/
		switch(num) {
		case getNumPeople("restWaiterRole","joshRestaurant"):
			JoshWaiterRole w = new JoshWaiterRole();
			joshWaiters.add(w);
			return w;
		/*case getNumPeople("restWaiterRole","cherysRestaurant"):
			CherysWaiterRole w = new CherysWaiterRole();
			cherysWaiters.add(w);
			return w;
		case getNumPeople("restWaiterRole","anjaliRestaurant"):
			AnjaliWaiterRole w = new AnjaliWaiterRole();
			anjaliWaiters.add(w);
			return w;
		case getNumPeople("restWaiterRole","alfredRestaurant"):
			AlfredWaiterRole w = new AlfredWaiterRole();
			alfredWaiters.add(w);
			return w;
		case getNumPeople("restWaiterRole","jesusRestaurant"):
			JesusWaiterRole w = new JesusWaiterRole();
			jesusWaiters.add(w);
			return w;*/
		default:
			JoshWaiterRole w = new JoshWaiterRole();
			joshWaiters.add(w);
			return w;
		}
	}
	
	public MarketCashierRole pickMarketCashier() {
		int num = getNumPeople("marketCashierRole","market1");
		if (num > getNumPeople("marketCashierRole","market2")) {
			num = getNumPeople("marketCashierRole","market2");
		}
		if (num > getNumPeople("marketCashierRole","market3")) {
			num = getNumPeople("marketCashierRole","market3");
		}
		switch(num) {
		case getNumPeople("marketCashierRole","market1"):
			return market1Cashier;
		case getNumPeople("marketCashierRole","market2"):
			return market2Cashier;
		case getNumPeople("marketCashierRole","market3"):
			return market3Cashier;
		default:
			return market1Cashier;
		}
	}
	
	public BankManagerRole pickBankManager() {
		return bank1Manager;
	}
	
	public RestCashierRole pickRestCashier() {
		int num = getNumPeople("restCashierRole","joshRestaurant");
		/*if (num > getNumPeople("restCashierRole","cherysRestaurant")) {
			num = getNumPeople("restCashierRole","cherysRestaurant");
		}
		if (num > getNumPeople("restCashierRole","anjaliRestaurant")) {
			num = getNumPeople("restCashierRole","anjaliRestaurant");
		}
		if (num > getNumPeople("restCashierRole","alfredRestaurant")) {
			num = getNumPeople("restCashierRole","alfredRestaurant");
		}
		if (num > getNumPeople("restCashierRole","jesusRestaurant")) {
			num = getNumPeople("restCashierRole","jesusRestaurant");
		}*/
		switch(num) {
		case getNumPeople("restCashierRole","joshRestaurant"):
			return joshCashier;
		/*case getNumPeople("restCashierRole","cherysRestaurant"):
			return cherysCashier;
		case getNumPeople("restCashierRole","anjaliRestaurant"):
			return anjaliCashier;
		case getNumPeople("restCashierRole","alfredRestaurant"):
			return alfredCashier;
		case getNumPeople("restCashierRole","jesusRestaurant"):
			return jesusCashier;*/
		default:
			return joshCashier;
		}
	}
	
	public RestCookRole pickRestCook() {
		int num = getNumPeople("restCookRole","joshRestaurant");
		/*if (num > getNumPeople("restCookRole","cherysRestaurant")) {
			num = getNumPeople("restCookRole","cherysRestaurant");
		}
		if (num > getNumPeople("restCookRole","anjaliRestaurant")) {
			num = getNumPeople("restCookRole","anjaliRestaurant");
		}
		if (num > getNumPeople("restCookRole","alfredRestaurant")) {
			num = getNumPeople("restCookRole","alfredRestaurant");
		}
		if (num > getNumPeople("restCookRole","jesusRestaurant")) {
			num = getNumPeople("restCookRole","jesusRestaurant");
		}*/
		switch(num) {
		case getNumPeople("restCookRole","joshRestaurant"):
			return joshCook;
		/*case getNumPeople("restCookRole","cherysRestaurant"):
			return cherysCook;
		case getNumPeople("restCookRole","anjaliRestaurant"):
			return anjaliCook;
		case getNumPeople("restCookRole","alfredRestaurant"):
			return alfredCook;
		case getNumPeople("restCookRole","jesusRestaurant"):
			return jesusCook;*/
		default:
			return joshCook;
		}
	}
	
	public RestHostRole pickRestHost() {
		int num = getNumPeople("restHostRole","joshRestaurant");
		/*if (num > getNumPeople("restHostRole","cherysRestaurant")) {
			num = getNumPeople("restHostRole","cherysRestaurant");
		}
		if (num > getNumPeople("restHostRole","anjaliRestaurant")) {
			num = getNumPeople("restHostRole","anjaliRestaurant");
		}
		if (num > getNumPeople("restHostRole","alfredRestaurant")) {
			num = getNumPeople("restHostRole","alfredRestaurant");
		}
		if (num > getNumPeople("restHostRole","jesusRestaurant")) {
			num = getNumPeople("restHostRole","jesusRestaurant");
		}*/
		switch(num) {
		case getNumPeople("restHostRole","joshRestaurant"):
			return joshHost;
		/*case getNumPeople("restHostRole","cherysRestaurant"):
			return cherysHost;
		case getNumPeople("restHostRole","anjaliRestaurant"):
			return anjaliHost;
		case getNumPeople("restHostRole","alfredRestaurant"):
			return alfredHost;
		case getNumPeople("restHostRole","jesusRestaurant"):
			return jesusHost;*/
		default:
			return joshHost;
		}
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
