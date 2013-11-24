package simcity.gui;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

public class CityAnimationPanel extends JPanel implements ActionListener
{
	private final int windowLocationX = 0;
	private final int windowLocationY = 0;
    private final int windowX = 500;
    private final int windowY = 500;
    private final int timerInterval = 20;
    private Dimension bufferSize;

    private List<Gui> guis = new ArrayList<Gui>();
    
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

    /**
     * Constructor
     */
    public CityAnimationPanel()
    {
    	setSize(windowX, windowY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(timerInterval, this );
    	timer.start();
    }

    /**
     * Action listener method that updates the panel
     */
	public void actionPerformed(ActionEvent e)
	{
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
        g2.fillRect(windowLocationX, windowLocationY, windowX, windowY);

        for(Gui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.updatePosition();
            }
        }
        for(Gui gui : guis)
        {
            if (gui.isPresent())
            {
                gui.draw(g2);
            }
        }
    }

	public void addGui(PersonGui gui)
	 {
	     guis.add(gui);
	 }
}
