package simcity.gui;

import simcity.CarAgent;
import simcity.PersonAgent;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CityInputPanel extends JPanel implements ActionListener
{
	private CityCreationPanel creationPanel = new CityCreationPanel(this);
    private JScrollPane personPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> personList = new ArrayList<JButton>();

	private Vector<PersonAgent> people = new Vector<PersonAgent>();
	private Vector<CarAgent> cars = new Vector<CarAgent>();

    private CityGui gui; //reference to main GUI
    
    
	public CityInputPanel(CityGui g)
	{
		gui = g;
		
        setLayout(new BoxLayout(this, 0));
        add(creationPanel);
        add(new JLabel(""));
        add(new JLabel("Citizens:"));
        personPane.setViewportView(view);
        add(personPane);
	}

    public void actionPerformed(ActionEvent e)
    {
    	for (JButton b : personList)
    	{
            if (e.getSource() == b)
            {
            	for(PersonAgent p : people)
            	{
            		if(p.getName() == b.getLabel())
            		{
            			creationPanel.setState(p);
            		}
            	}
            }
        }
    	
    }
    
    public void addPerson(String name, String job, int money, boolean car) //walk me through how to add all the variables to the person--job, $, car
    {
		PersonAgent p = new PersonAgent(name);
//		PersonGui g = new PersonGui(p, gui);
//		gui.animationPanel.addGui(g);
//		p.msgYoureHired(String nameOfBuilding (location), String nameOfRole (variable name style string), int salary); ************************
		p.msgIncome(money);
		if(car)
		{
//			CarAgent tempCar = new CarAgent(p)
//			cars.add(tempCar);
//			p.msgBoughtCar(tempCar);
		}
		people.add(p);
		p.startThread();
				
        JButton temp = new JButton(name);
        Dimension size = new Dimension(personPane.getSize().width - 20, (int)(personPane.getSize().height / 7));
        temp.setPreferredSize(size);
        temp.setMinimumSize(size);
        temp.setMaximumSize(size);
        temp.setBackground(Color.white);
        temp.addActionListener(this);
        personList.add(temp);
        view.add(temp);
        validate();
    }
}
