package simcity.gui;

import javax.swing.*;

import simcity.PersonAgent;
import simcity.CityDirectory;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CityCreationPanel extends JPanel //implements ActionListener
{
	private PersonAgent currentPerson;
	private CityDirectory cityDirectory;

	
//	private enum State
//	{
//		creating,
//		editing;
//	}
//	private State currentState = State.creating;
//	
//	private JPanel view = new JPanel();
//	private JLabel title = new JLabel("<html><pre><u> Create a Citizen </u><br></pre></html>");
//	private JButton addButton = new JButton("Add Citizen");
//	private JTextField name = new JTextField(null);
//	private Vector<JRadioButtonMenuItem> jobs = new Vector<JRadioButtonMenuItem>();
//	private ButtonGroup jobGroup = new ButtonGroup();
//	private JRadioButtonMenuItem landlord = new JRadioButtonMenuItem("Landlord");
//	private JRadioButtonMenuItem hostR = new JRadioButtonMenuItem("Restaurant Host");
//	private JRadioButtonMenuItem cookR = new JRadioButtonMenuItem("Restaurant Cook");
//	private JRadioButtonMenuItem waiterR = new JRadioButtonMenuItem("Restaurant Waiter");
//	private JRadioButtonMenuItem cashierR = new JRadioButtonMenuItem("Restaurant Cashier");
//	private JRadioButtonMenuItem employeeM = new JRadioButtonMenuItem("Market Employee");
//	private JRadioButtonMenuItem delivererM = new JRadioButtonMenuItem("Market Deliverer");
//	private JRadioButtonMenuItem chashierM = new JRadioButtonMenuItem("Market Cashier");
//	private JRadioButtonMenuItem managerB = new JRadioButtonMenuItem("Bank Manager");
//	private JRadioButtonMenuItem tellerB = new JRadioButtonMenuItem("Bank Teller");
//	private JRadioButtonMenuItem unemployeed = new JRadioButtonMenuItem("Unemployeed");
//	private Vector<JRadioButtonMenuItem> ecos = new Vector<JRadioButtonMenuItem>();
//	private ButtonGroup ecoGroup = new ButtonGroup();
//	private JLabel ecoStatus = new JLabel("$ Status");
//	private JRadioButtonMenuItem rich = new JRadioButtonMenuItem("Rich ($500)");
//	private JRadioButtonMenuItem middle = new JRadioButtonMenuItem("Middle Class ($20)");
//	private JRadioButtonMenuItem poor = new JRadioButtonMenuItem("Poor ($50)");
//	private JCheckBox car = new JCheckBox("Yes");

	private CityInputPanel inputPanel;

	/**
	 * Constructor
	 */
	public CityCreationPanel(CityInputPanel ip, CityDirectory cd) //* called from the RestaurantPanel
	{
		inputPanel = ip;
		cityDirectory = cd;
		
		
		
		
		
//		int rows = 21;
//		int columns = 2;
//		int buffer = 10;
//		view.setLayout(new GridLayout(rows, columns, buffer, buffer)); //view. maybe ought to be deleted
//
//		add(title);
//		addButton.addActionListener(this);
//		add(addButton);
//
//		add(new JLabel(""));
//		add(new JLabel(""));
//
//		add(new JLabel("Name: "));
//		add(name);
//
//		add(new JLabel(""));
//		add(new JLabel(""));
//
//		add(new JLabel("Job: "));
//		jobGroup.add(landlord);
//		add(landlord);
//		add(new JLabel(""));
//		jobGroup.add(hostR);
//		add(hostR);
//		add(new JLabel(""));
//		jobGroup.add(cookR);
//		add(cookR);
//		add(new JLabel(""));
//		jobGroup.add(waiterR);
//		add(waiterR);
//		add(new JLabel(""));
//		jobGroup.add(cashierR);
//		add(cashierR);
//		add(new JLabel(""));
//		jobGroup.add(employeeM);
//		add(employeeM);
//		add(new JLabel(""));
//		jobGroup.add(delivererM);
//		add(delivererM);
//		add(new JLabel(""));
//		jobGroup.add(chashierM);
//		add(chashierM);
//		add(new JLabel(""));
//		jobGroup.add(managerB);
//		add(managerB);
//		add(new JLabel(""));
//		jobGroup.add(tellerB);
//		add(tellerB);
//		add(new JLabel(""));
//		jobGroup.add(unemployeed);
//		add(unemployeed);
//
//		add(new JLabel(""));
//		add(new JLabel(""));
//
//		add(ecoStatus);
//		jobGroup.add(rich);
//		add(rich);
//		add(new JLabel(""));
//		jobGroup.add(middle);
//		add(middle);
//		add(new JLabel(""));
//		jobGroup.add(poor);
//		add(poor);
//
//		add(new JLabel(""));
//		add(new JLabel(""));
//
//		add(new JLabel("Car: "));
//		add(car);
	}

//	public void actionPerformed(ActionEvent e)
//	{
//		if(e.getSource() == addButton && currentState == State.creating)
//		{
//			if(name.getText() != null && jobGroup.getSelection() != null && ecoGroup != null)
//			{
//				String jobString = "";
//				int money = 0;
//				for(JRadioButtonMenuItem b : jobs)
//				{
//					if(b.isSelected())
//					{
//						jobString = b.getText();
//					}
//				}
//				for(JRadioButtonMenuItem b : ecos)
//				{
//					if(b.isSelected())
//					{
//						if(b == rich)
//						{
//							money = 500;
//						}
//						else if(b == middle)
//						{
//							money = 250;
//						}
//						else if(b == poor)
//						{
//							money = 50;
//						}
//					}
//				}
//				inputPanel.addPerson(name.getText(), jobString, money, car.isSelected());
//				creatingPanel();
//			}
//		}
//		else if(e.getSource() == addButton && currentState == State.editing)
//		{
//			String jobString = "";
//			for(JRadioButtonMenuItem b : jobs)
//			{
//				if(b.isSelected())
//				{
//					jobString = b.getText();
//				}
//			}
//			for(JRadioButtonMenuItem b : ecos)
//			{
//				if(b.isSelected())
//				{
//					if(b == rich)
//					{
//						currentPerson.msgIncome(500);
//					}
//					else if(b == middle)
//					{
//						currentPerson.msgIncome(250);
//					}
//					else if(b == poor)
//					{
//						currentPerson.msgIncome(50);
//					}
//				}
//			}
//			currentPerson.changeJob(jobString);
//			creatingPanel();
//		}
//	}
//	
//	private void creatingPanel()
//	{
//		title.setText("<html><pre><u> Create a Citizen </u><br></pre></html>");
//		addButton.setText("Add Citizen");
//		name.setEnabled(true);
//		name.setText(null);
//		jobGroup.clearSelection();
//		ecoStatus.setText("$ Status: ");
//		ecoGroup.clearSelection();
//		car.setEnabled(true);
//		car.setSelected(false);
//	}
//	private void editingPanel()
//	{
//		title.setText("<html><pre><u> Edit " + currentPerson.getName() + " </u><br></pre></html>");
//		addButton.setText("Save Changes");
//		name.setEnabled(false);
//		ecoStatus.setText("Current balance = $" + currentPerson.getMoney() + " : ");
		
//		for(JRadioButtonMenuItem b : jobs)
//		{
//			if(b.getLabel() == "Name of Job")
//			{
//				b.setSelected(true);
//			}
//		}
//		rich.setText("+ $500");
//		middle.setText("+ $250");
//		poor.setText("+ $50");
//	}
//	
//	public void setState(PersonAgent p)
//	{
//		if(p == null)
//		{
//			currentState = State.creating;
//			creatingPanel();
//		}
//		else
//		{
//			currentPerson = p;
//			currentState = State.editing;
//			editingPanel();
//		}
//	}
	
	public void readConfig(String file) {
		Properties cityConfig = new Properties();
		
		try {
		    cityConfig.load(this.getClass().getResourceAsStream(file));
		} catch(IOException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException iae) {
			  iae.printStackTrace();
		}
		
		List<PersonInfo> info = new ArrayList<PersonInfo>();
		int numPeople = 9;
		int index = 0;
		boolean testingAnimation = true;
		for(String key : cityConfig.stringPropertyNames()) {
			if (key.equals("testingAnimation")) {
				if (cityConfig.getProperty(key).equals("true")) {
					testingAnimation = true;
				} else if (cityConfig.getProperty(key).equals("false")) {
					testingAnimation = false;
				}
			}
			if (key.contains("name")) {
				numPeople++;
			}
	    }
		for(String key : cityConfig.stringPropertyNames()) {
			if (key.contains("name")) {
				for(int i=10; i<=numPeople; i++) {
					if (key.contains(Integer.toString(i))) {
						index = i;
					}
				}
				info.add(new PersonInfo(cityConfig.getProperty(key),index));
			}
	    }
		for(String key : cityConfig.stringPropertyNames()) {
			if (key.contains("job")) {
				for (PersonInfo pI : info) {
					if (key.contains(Integer.toString(pI.id))) {
						pI.job = cityConfig.getProperty(key);
						break;
					}
				}
			}
			if (key.contains("eco")) {
				for (PersonInfo pI : info) {
					if (key.contains(Integer.toString(pI.id))) {
						pI.eco = cityConfig.getProperty(key);
						break;
					}
				}
			}
			if (key.contains("physical")) {
				for (PersonInfo pI : info) {
					if (key.contains(Integer.toString(pI.id))) {
						pI.physical = cityConfig.getProperty(key);
						break;
					}
				}
			}
			if (key.contains("housing")) {
				for (PersonInfo pI : info) {
					if (key.contains(Integer.toString(pI.id))) {
						pI.housing = cityConfig.getProperty(key);
						break;
					}
				}
			}
			if (key.contains("end")) {
				for (PersonInfo pI : info) {
					if (key.contains(Integer.toString(pI.id))) {
						pI.end = Integer.parseInt(cityConfig.getProperty(key));
						break;
					}
				}
			}
			if (key.contains("start")) {
				for (PersonInfo pI : info) {
					if (key.contains(Integer.toString(pI.id))) {
						pI.start = Integer.parseInt(cityConfig.getProperty(key));
						break;
					}
				}
			}
			if (key.contains("pay")) {
				for (PersonInfo pI : info) {
					if (key.contains(Integer.toString(pI.id))) {
						pI.pay = Integer.parseInt(cityConfig.getProperty(key));
						break;
					}
				}
			}
	    }
		
		int money;
		boolean car;
		for (PersonInfo pI : info) {
			/*System.out.println(pI.name);
			System.out.println(pI.job);
			System.out.println(pI.pay);
			System.out.println(pI.start);
			System.out.println(pI.end);
			System.out.println(pI.eco);
			System.out.println(pI.physical);
			System.out.println(pI.housing);
			System.out.println(pI.car);*/
			inputPanel.addPerson(pI.name, pI.job, pI.pay, pI.start, pI.end, pI.eco, pI.physical, pI.housing, cityDirectory, testingAnimation);
		}
		cityDirectory.assignLandlord();
		inputPanel.startBus();
	}
	
	private class PersonInfo {
		public String name;
		public int id;
		public String job;
		public String eco;
		public String physical;
		public String housing;
		public int start;
		public int end;
		public int pay;
		public String car;
		PersonInfo(String n, int i) {
			name = n;
			id = i;
			job = null;
			eco = null;
			physical = null;
			housing = null;
			start = -1;
			end = -1;
			pay = -1;
			car = null;
		}
	}
}