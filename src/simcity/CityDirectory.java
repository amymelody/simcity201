package simcity;

import java.awt.Point;
import java.util.*;
import java.util.HashMap;
import java.util.Map;

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
import simcity.anjalirestaurant.AnjaliCashierRole;
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
import simcity.alfredrestaurant.AlfredWaiterRole;

public class CityDirectory
{
	Map<Integer, PersonInfo> personMap;
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
		case "anjaliCustomerRole":
			return new AnjaliCustomerRole();
		case "cherysCustomerRole":
			return new CherysCustomerRole();
		case "alfredCustomerRole":
			return new AlfredCustomerRole();
		case "jesusCustomerRole":
			return new JesusCustomerRole();
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
			return new LandlordRole();
		case "marketCashierRole":
			return new MarketCashierRole();
		case "marketDelivererRole":
			return new MarketDelivererRole();
		case "marketEmployeeRole":
			return new MarketEmployeeRole();
		case "bankTellerRole":
			return new BankTellerRole();
		case "bankManagerRole":
			return new BankManagerRole();
		case "restCashierRole":
			
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
}
