package simcity.market.gui;

import javax.swing.*;

import simcity.gui.CityGui;
import simcity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;

public class MarketAnimationPanel extends JPanel implements ActionListener {
	static final int TIMERINCR = 100;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private Image bufferImage;
	private Dimension bufferSize;

	private List<Gui> guis = new ArrayList<Gui>();

	Image bg;
	
	public MarketAnimationPanel() {
		setBounds(150, 0, 500, 500);
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("images/market.png"));
		bg = bgIcon.getImage();
		
		bufferSize = this.getSize();

		Timer timer = new Timer(TIMERINCR, this );
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		synchronized(guis) {
			for(Gui gui : guis) {
				if (gui.isPresent()) {
					gui.updatePosition();
				}
			}
		}
		repaint();  //Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;

		//background
		g2.drawImage(bg, 0, 0, null);

		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
	}

	public void addGui(MarketCustomerGui gui) {
		guis.add(gui);
	}

	public void addGui(MarketCashierGui gui) {
		guis.add(gui);
	}
	
	public void addGui(MarketEmployeeGui gui) {
		guis.add(gui);
	}
	
	public void addGui(MarketDelivererGui gui) {
		guis.add(gui);
	}
	
}
