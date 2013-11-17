package simcity.gui;

import javax.swing.*;

import simcity.PersonAgent;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Vector;

public class CityCreationPanel extends JPanel implements ActionListener
{
	private enum State
	{
		creating,
		editing;
	}
	private State currentState = State.creating;
	private PersonAgent currentPerson;
	
	private JPanel view = new JPanel();
	private JLabel title = new JLabel("<html><pre><u> Create a Citizen </u><br></pre></html>");
	private JButton addButton = new JButton("Add Citizen");
	private JTextField name = new JTextField(null);
	private Vector<JRadioButtonMenuItem> jobs = new Vector<JRadioButtonMenuItem>();
	private ButtonGroup jobGroup = new ButtonGroup();
	private JRadioButtonMenuItem landlord = new JRadioButtonMenuItem("Landlord");
	private JRadioButtonMenuItem hostR = new JRadioButtonMenuItem("Restaurant Host");
	private JRadioButtonMenuItem cookR = new JRadioButtonMenuItem("Restaurant Cook");
	private JRadioButtonMenuItem waiterR = new JRadioButtonMenuItem("Restaurant Waiter");
	private JRadioButtonMenuItem chashierR = new JRadioButtonMenuItem("Restaurant Cashier");
	private JRadioButtonMenuItem employeeM = new JRadioButtonMenuItem("Market Employee");
	private JRadioButtonMenuItem delivererM = new JRadioButtonMenuItem("Market Deliverer");
	private JRadioButtonMenuItem chashierM = new JRadioButtonMenuItem("Market Cashier");
	private JRadioButtonMenuItem ownerB = new JRadioButtonMenuItem("Bank Owner");
	private JRadioButtonMenuItem tellerB = new JRadioButtonMenuItem("Bank Teller");
	private JRadioButtonMenuItem unemployeed = new JRadioButtonMenuItem("Unemployeed");
	private Vector<JRadioButtonMenuItem> ecos = new Vector<JRadioButtonMenuItem>();
	private ButtonGroup ecoGroup = new ButtonGroup();
	private JLabel ecoStatus = new JLabel("$ Status");
	private JRadioButtonMenuItem rich = new JRadioButtonMenuItem("Rich ($500)");
	private JRadioButtonMenuItem middle = new JRadioButtonMenuItem("Middle Class ($20)");
	private JRadioButtonMenuItem poor = new JRadioButtonMenuItem("Poor ($50)");
	private JCheckBox car = new JCheckBox("Yes");

	private CityInputPanel inputPanel;

	/**
	 * Constructor
	 */
	public CityCreationPanel(CityInputPanel ip) //* called from the RestaurantPanel
	{
		inputPanel = ip;

		int rows = 21;
		int columns = 2;
		int buffer = 10;
		view.setLayout(new GridLayout(rows, columns, buffer, buffer)); //view. maybe ought to be deleted

		add(title);
		addButton.addActionListener(this);
		add(addButton);

		add(new JLabel(""));
		add(new JLabel(""));

		add(new JLabel("Name: "));
		add(name);

		add(new JLabel(""));
		add(new JLabel(""));

		add(new JLabel("Job: "));
		jobGroup.add(landlord);
		add(landlord);
		add(new JLabel(""));
		jobGroup.add(hostR);
		add(hostR);
		add(new JLabel(""));
		jobGroup.add(cookR);
		add(cookR);
		add(new JLabel(""));
		jobGroup.add(waiterR);
		add(waiterR);
		add(new JLabel(""));
		jobGroup.add(chashierR);
		add(chashierR);
		add(new JLabel(""));
		jobGroup.add(employeeM);
		add(employeeM);
		add(new JLabel(""));
		jobGroup.add(delivererM);
		add(delivererM);
		add(new JLabel(""));
		jobGroup.add(chashierM);
		add(chashierM);
		add(new JLabel(""));
		jobGroup.add(ownerB);
		add(ownerB);
		add(new JLabel(""));
		jobGroup.add(tellerB);
		add(tellerB);
		add(new JLabel(""));
		jobGroup.add(unemployeed);
		add(unemployeed);

		add(new JLabel(""));
		add(new JLabel(""));

		add(ecoStatus);
		jobGroup.add(rich);
		add(rich);
		add(new JLabel(""));
		jobGroup.add(middle);
		add(middle);
		add(new JLabel(""));
		jobGroup.add(poor);
		add(poor);

		add(new JLabel(""));
		add(new JLabel(""));

		add(new JLabel("Car: "));
		add(car);

	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == addButton && currentState == State.creating)
		{
			if(name.getText() != null && jobGroup.getSelection() != null && ecoGroup != null)
			{
				String jobString = "";
				int money = 0;
				for(JRadioButtonMenuItem b : jobs)
				{
					if(b.isSelected())
					{
						jobString = b.getLabel();
					}
				}
				for(JRadioButtonMenuItem b : ecos)
				{
					if(b.isSelected())
					{
						if(b == rich)
						{
							money = 500;
						}
						else if(b == middle)
						{
							money = 250;
						}
						else if(b == poor)
						{
							money = 50;
						}
					}
				}
				inputPanel.addPerson(name.getText(), jobString, money, car.isSelected());
				creatingPanel();
			}
		}
		else if(e.getSource() == addButton && currentState == State.editing)
		{

			String jobString = "";
			for(JRadioButtonMenuItem b : jobs)
			{
				if(b.isSelected())
				{
					jobString = b.getLabel();
				}
			}
			for(JRadioButtonMenuItem b : ecos)
			{
				if(b.isSelected())
				{
					if(b == rich)
					{
						currentPerson.msgIncome(500);
					}
					else if(b == middle)
					{
						currentPerson.msgIncome(250);
					}
					else if(b == poor)
					{
						currentPerson.msgIncome(50);
					}
				}
			}
//			currentPerson.changeJob(jobString);
			creatingPanel();
		}
	}
	
	private void creatingPanel()
	{
		title.setText("<html><pre><u> Create a Citizen </u><br></pre></html>");
		addButton.setLabel("Add Citizen");
		name.setEnabled(true);
		name.setText(null);
		jobGroup.clearSelection();
		ecoStatus.setText("$ Status: ");
		ecoGroup.clearSelection();
		car.setEnabled(true);
		car.setSelected(false);
	}
	private void editingPanel()
	{
		title.setText("<html><pre><u> Edit " + currentPerson.getName() + " </u><br></pre></html>");
		addButton.setLabel("Save Changes");
		name.setEnabled(false);
		ecoStatus.setText("Current balance = $" + currentPerson.getMoney() + " : ");
		
//		for(JRadioButtonMenuItem b : jobs)
//		{
//			if(b.getLabel() == "Name of Job")
//			{
//				b.setSelected(true);
//			}
//		}
		rich.setLabel("+ $500");
		middle.setLabel("+ $250");
		poor.setLabel("+ $50");
//		if they have a car, car.setEnabled(false) and car.setSelected(true), else car.setSelected(false)
	}
	
	public void setState(PersonAgent p)
	{
		if(p == null)
		{
			currentState = State.creating;
			creatingPanel();
		}
		else
		{
			currentPerson = p;
			currentState = State.editing;
			editingPanel();
		}
	}
}