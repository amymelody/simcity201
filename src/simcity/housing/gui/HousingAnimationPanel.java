package simcity.housing.gui;

import simcity.gui.CityGui;
import simcity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import javax.swing.Timer;

public class HousingAnimationPanel extends JPanel implements ActionListener
{
	static final int TIMERINCR = 10;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private Dimension bufferSize;

	private List<Gui> guis = new ArrayList<Gui>();
	
	public HousingAnimationPanel(CityGui cG)
	{
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();

		Timer timer = new Timer(TIMERINCR, this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e)
	{
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
    	g2.setColor(Color.black);
    	g2.fillRect(0, 0, this.getSize().width, this.getSize().height);
		
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

//	public void addGui(ResidentGui gui)
//	{
//		guis.add(gui);
//	}
//
//	public void addGui(LandlordGui gui)
//	{
//		guis.add(gui);
//	}
}
