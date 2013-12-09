package simcity.housing.gui;

import simcity.gui.CityGui;
import simcity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class HousingAnimationPanel extends JPanel implements ActionListener
{
	static final int TIMERINCR = 10;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private Dimension bufferSize;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());
	private Image floor;
	
	private HousingGui superGui;
	
	public HousingAnimationPanel()
	{
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		ImageIcon floorIcon = new ImageIcon(this.getClass().getResource("images/HousingFloor.png"));
		floor = floorIcon.getImage();
		
		bufferSize = this.getSize();

		Timer timer = new Timer(TIMERINCR, this);
		timer.start();
	}
	
	public void setHousingGui(HousingGui gui)
	{
		superGui = gui;
	}

	public void actionPerformed(ActionEvent e)
	{
		synchronized(guis)
		{
			for(Gui gui : guis)
			{
				if(gui instanceof ResidentGui)
				{
					ResidentGui rg = (ResidentGui)gui;
					if(rg.getCurrentHouse() == this.superGui)
					{
						if (gui.isPresent())
						{
							gui.updatePosition();
						}
					}
				}
				else
				{
					if (gui.isPresent())
					{
						gui.updatePosition();
					}
				}
			}
		}
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(floor, 0, 0, null);

		synchronized(guis)
		{
			for(Gui gui : guis)
			{
				if(gui instanceof ResidentGui)
				{
					ResidentGui rg = (ResidentGui)gui;
					if(rg.getCurrentHouse() == this.superGui)
					{
						if (gui.isPresent())
						{
							gui.updatePosition();
						}
					}
				}
				else
				{
					if (gui.isPresent())
					{
						gui.updatePosition();
					}
				}
			}
		}

		synchronized(guis)
		{
			for(Gui gui : guis)
			{
				if(gui instanceof ResidentGui)
				{
					ResidentGui rg = (ResidentGui)gui;
					if(rg.getCurrentHouse() == this.superGui)
					{
						if (gui.isPresent())
						{
							gui.draw(g2);
						}
					}
				}
				else
				{
					if (gui.isPresent())
					{
						gui.draw(g2);
					}
				}
			}
		}
	}

	public void addGui(ResidentGui gui)
	{
		guis.add(gui);
	}

	public void addGui(LandlordGui gui)
	{
		guis.add(gui);
	}
}
