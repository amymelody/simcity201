package simcity.gui;

import javax.swing.*;
import javax.swing.Timer;

import simcity.market.gui.MarketCustomerGui;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class CityAnimationPanel extends JPanel implements ActionListener
{
	static final int TIMERINCR = 10;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private Image bufferImage;
	private Dimension bufferSize;

	private List<Gui> guis = new ArrayList<Gui>();

	Image bg;
	
	public CityAnimationPanel(CityGui cG) {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		ImageIcon bgIcon = new ImageIcon(cG.getClass().getResource("images/simcity.png"));
		bg = bgIcon.getImage();
		
		bufferSize = this.getSize();

		Timer timer = new Timer(TIMERINCR, this );
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//background
		g2.drawImage(bg, 0, 0, null);
		
		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	public void addGui(PersonGui g) {
		guis.add(g);
	}
    
}
