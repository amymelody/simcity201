package simcity.jesusrestaurant.gui;

import javax.swing.*;

import simcity.gui.Gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class JesusRestaurantAnimationPanel extends JPanel implements ActionListener {
	static final int TIMERINCR = 10;
	static final int TABLESIZE = 50;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	static final int NTABLES = JesusHostGui.getNTab();

	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(40, 160));
		tableLocations.put(2, new Point(200, 160));
		tableLocations.put(3, new Point(40, 320));
		tableLocations.put(4, new Point(200, 320));
	}
	
	private List<Gui> guis = new ArrayList<Gui>();

	Image bg;
	
	public JesusRestaurantAnimationPanel() {
		setBounds(150, 0, 500, 500);
		setSize(WINDOWX, WINDOWY);
		setVisible(false);
		
		ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("images/jesus_floor.png"));
		bg = bgIcon.getImage();

		Timer timer = new Timer(TIMERINCR, this );
		timer.start();
	}

	public void actionPerformed(ActionEvent e) {
		for(Gui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
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

	public void addGui(JesusCustomerGui gui) {
		guis.add(gui);
	}

	public void addGui(JesusHostGui gui) {
		guis.add(gui);
	}
	
	public void addGui(JesusWaiterGui gui) {
		guis.add(gui);
	}
	
	public void addGui(JesusCookGui gui) {
		guis.add(gui);
	}
	
	public void addGui(JesusCashierGui gui) {
		guis.add(gui);
	}
}
