package simcity.gui;

import simcity.Day;
import simcity.PersonAgent;
import simcity.BusStopAgent;
import simcity.BusAgent;
import simcity.interfaces.BusStop;
import simcity.Time;
import simcity.CityDirectory;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CityInputPanel extends JPanel implements ActionListener
{
	static final int TIMERINCR = 1500;
	
	private Vector<PersonAgent> people = new Vector<PersonAgent>();
	private List<BusStop> busStops = new ArrayList<BusStop>();
	BusAgent bus;
	
	private CityDirectory cityDirectory;
	private BuildingGui buildingGui;
//    private JScrollPane personPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JPanel view = new JPanel();
    private List<JButton> personList = new ArrayList<JButton>();
    private Timer timer;
    private Time time;

    private CityGui gui;
    private CityCreationPanel creationPanel;

	private Vector<JRadioButtonMenuItem> configs = new Vector<JRadioButtonMenuItem>();
	private ButtonGroup configGroup = new ButtonGroup();
	private JLabel scenario = new JLabel("CHOOSE A SCENARIO");
	private JRadioButtonMenuItem button1 = new JRadioButtonMenuItem("Restaurant Config");
	private JRadioButtonMenuItem button2 = new JRadioButtonMenuItem("Market Config");
	private JRadioButtonMenuItem button3 = new JRadioButtonMenuItem("Jobs Config");
	private JButton goButton = new JButton("Run Scenario");
    
//    @Override
//    public void paint(Graphics g){
//    	super.paint(g);
//    	//System.out.println(this.getSize().width + "," + this.getSize().height);
//    	Graphics2D g2d  = (Graphics2D) g;
//    	g2d.setColor(Color.black);
//    	g2d.fillRect(0, 0, this.getSize().width, this.getSize().height);
//    }
	public CityInputPanel(CityGui g, CityDirectory cd, BuildingGui bg)
	{
		gui = g;
		cityDirectory = cd;
		buildingGui = bg;
		creationPanel = new CityCreationPanel(this, cityDirectory);
		gui = g;
		
		bus = new BusAgent("bus");
		BusGui busGui = new BusGui(bus, gui, cityDirectory);
		bus.setGui(busGui);
		gui.addGui(busGui);
		
		BusStopAgent stop1 = new BusStopAgent("busStop1");
		BusStopAgent stop2 = new BusStopAgent("busStop2");
		BusStopAgent stop3 = new BusStopAgent("busStop3");
		BusStopAgent stop4 = new BusStopAgent("busStop4");
		busStops.add(stop1);
		busStops.add(stop2);
		busStops.add(stop3);
		busStops.add(stop4);
		bus.addBusStop(stop1, true);
		bus.addBusStop(stop2, false);
		bus.addBusStop(stop3, false);
		bus.addBusStop(stop4, false);
		bus.setCityDirectory(cityDirectory);
		
		stop1.startThread();
		stop2.startThread();
		stop3.startThread();
		stop4.startThread();
		
//        setLayout(new BoxLayout(this, 0));
		
		int rows = 5;
		int columns = 1;
		int buffer = 10;
		view.setLayout(new GridLayout(rows, columns, buffer, buffer)); //view. maybe ought to be deleted

		add(scenario);
		configGroup.add(button1);
		add(button1);
		configGroup.add(button2);
		add(button2);
		configGroup.add(button3);
		add(button3);
		goButton.addActionListener(this);
		add(goButton);
//        add(creationPanel);
//        add(new JLabel(""));
//        add(new JLabel("Citizens:"));
//        personPane.setViewportView(view);
//        add(personPane);
        
        time = new Time(Day.Sun, 4, 0);
        timer = new Timer(TIMERINCR, this );
    	timer.start();
        
//        final CityInputPanel me = this;
//        (new Thread(new Runnable(){
//        	@Override
//        	public void run(){
//        		while(true){
//        			me.repaint();
//        			
//        		}
//        	}
//        })).start();
	}

    public void actionPerformed(ActionEvent e)
    {
    	time = time.plus(30);
    	for (PersonAgent p : people) {
    		p.msgUpdateWatch(time.getDay(), time.getHour(), time.getMinute());
    	}
    	
		if(e.getSource() == goButton)
		{
			if(button1.isSelected())
			{
				creationPanel.readConfig("../restaurantConfig.properties");
			}
			else if(button2.isSelected())
			{
				creationPanel.readConfig("../marketConfig.properties");
			}
			else if(button3.isSelected())
			{
			creationPanel.readConfig("../jobsConfig.properties");
			}
		}
//    	for (JButton b : personList)
//    	{
//            if (e.getSource() == b)
//            {
//            	for(PersonAgent p : people)
//            	{
//            		if(p.getName() == b.getText())
//            		{
//            			creationPanel.setState(p);
//            		}
//            	}
//            }
//        }
    	
    }
    
    public void startBus() {
    	bus.startThread();
    }
    
    public void addPerson(String name, String job, int pay, int startShift, int endShift, String eco, String physical, String housing, boolean car, CityDirectory c) 
    {
		PersonAgent p = new PersonAgent(name);
		PersonGui g = new PersonGui(p, gui, c);
		p.setCityDirectory(c);
		p.setCityGui(gui);
		p.addBusStops(busStops);
		p.setGui(g);
		gui.addGui(g);
		
		Map<Day, Time> startShifts = new HashMap<Day, Time>();
		startShifts.put(Day.Sun, new Time(Day.Sun, startShift, 0));
		startShifts.put(Day.Mon, new Time(Day.Mon, startShift, 0));
		startShifts.put(Day.Tue, new Time(Day.Tue, startShift, 0));
		startShifts.put(Day.Wed, new Time(Day.Wed, startShift, 0));
		startShifts.put(Day.Thu, new Time(Day.Thu, startShift, 0));
		startShifts.put(Day.Fri, new Time(Day.Fri, startShift, 0));
		startShifts.put(Day.Sat, new Time(Day.Sat, startShift, 0));
		
		Map<Day, Time> endShifts = new HashMap<Day, Time>();
		endShifts.put(Day.Sun, new Time(Day.Sun, endShift, 0));
		endShifts.put(Day.Mon, new Time(Day.Mon, endShift, 0));
		endShifts.put(Day.Tue, new Time(Day.Tue, endShift, 0));
		endShifts.put(Day.Wed, new Time(Day.Wed, endShift, 0));
		endShifts.put(Day.Thu, new Time(Day.Thu, endShift, 0));
		endShifts.put(Day.Fri, new Time(Day.Fri, endShift, 0));
		endShifts.put(Day.Sat, new Time(Day.Sat, endShift, 0));
		
		p.msgYoureHired(job, pay, startShifts, endShifts);
		p.setEState(eco);
		p.setPState(physical);
		if(car)
		{
//			CarAgent tempCar = new CarAgent(p)
//			cars.add(tempCar);
//			p.msgBoughtCar(tempCar);
		}
		people.add(p);
		cityDirectory.addPerson(p, p.getJobLocation(), housing);
		p.startThread();
				
//        JButton temp = new JButton(name);
//        Dimension size = new Dimension(personPane.getSize().width - 20, (int)(personPane.getSize().height / 7));
//        temp.setPreferredSize(size);
//        temp.setMinimumSize(size);
//        temp.setMaximumSize(size);
//        temp.setBackground(Color.white);
//        temp.addActionListener(this);
//        personList.add(temp);
//        view.add(temp);
//        validate();
    }
}
