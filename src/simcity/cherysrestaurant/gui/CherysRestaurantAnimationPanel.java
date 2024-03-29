package simcity.cherysrestaurant.gui; 

import javax.swing.*;

import simcity.gui.Gui;
import simcity.housing.gui.ResidentGui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

/**
 * Sub panel of RestaurantGui.
 * Shows all of the animation.
 */
public class CherysRestaurantAnimationPanel extends JPanel implements ActionListener
{
	private final int WINDOWLOCATIONX = 0;
	private final int WINDOWLOCATIONY = 0;
    private final int WINDOWX = 750;
    private final int WINDOWY = 200;
    private final int timerInterval = 10;
	private final int xTableInitial = 100;
	private final int yTable = 75;
    private final int tableDimensions = 50;
    private final int tableBuffer = 50;
    private Image bufferImage;
    private Dimension bufferSize;
    private int buff = 1;

    private List<CherysGui> guis = Collections.synchronizedList(new ArrayList<CherysGui>());
    
    class Table
    {
    	int num;
    	int x;
    	int y;
    	Table(int n, int x, int y)
    	{
    		num = n;
    		this.x = x;
    		this.y = y;
    	}
    }
    private List<Table> tables = new ArrayList<Table>();
    static final int NTABLES = 5;

    /**
     * Constructor for AnimationPanel
     */
    public CherysRestaurantAnimationPanel()
    {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(timerInterval, this );
    	timer.start();
    	
    	for(int i = 0; i < NTABLES; i++)
    	{
	    	Table temp = new Table(i + 1, xTableInitial + (tableDimensions + tableBuffer)*i, yTable);
	    	tables.add(temp);
    	}
    }

    /**
     * Action listener method that updates the panel
     */
	public void actionPerformed(ActionEvent e)
	{
		synchronized(guis)
		{
			for(CherysGui gui : guis)
			{
//				if(gui instanceof CherysCustomerGui)
//				{
//					System.out.println("Size: " + guis.size());
//				}
				if (gui.isPresent())
				{
					gui.updatePosition();
				}
			}
		}
		repaint();  //Will have paintComponent called
	}
	/**
	 * Draws graphics
	 */
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(WINDOWLOCATIONX, WINDOWLOCATIONX, WINDOWX, WINDOWY);

        //Here is the table
        for(int i = 0; i < tables.size(); i++)
        {
	        g2.setColor(Color.ORANGE);
	        g2.fillRect(tables.get(i).x, tables.get(i).y, tableDimensions, tableDimensions);//200 and 250 need to be table params
        }

        for(CherysGui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.updatePosition();
            }
        }

        for(CherysGui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.draw(g2);
            }
        }
    }

    public void addGui(CherysCustomerGui gui)
    {
        guis.add(gui);
    }
    public void addGui(CherysWaiterGui gui)
    {
        guis.add(gui);
    }
}
