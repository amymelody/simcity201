package simcity.JesusRestaurant.gui;

import javax.swing.*;

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
	private final int WINDOWX = 440;
	private final int WINDOWY = 440;
	static final int NTABLES = JesusHostGui.getNTab();
	private Image bufferImage;
	private Dimension bufferSize;

	public static final Map<Integer, Point> tableLocations = new HashMap<Integer, Point>();
	static {
		tableLocations.put(1, new Point(40, 160));
		tableLocations.put(2, new Point(200, 160));
		tableLocations.put(3, new Point(40, 320));
		tableLocations.put(4, new Point(200, 320));
	}
	
	private List<JesusGui> guis = new ArrayList<JesusGui>();

	Image bg, t1Image, t2Image, t3Image, t4Image, plateArea, cookArea, cashierArea;
	
	public JesusRestaurantAnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		ImageIcon bgIcon = new ImageIcon(this.getClass().getResource("images/floor_texture.png"));
		bg = bgIcon.getImage();

		ImageIcon plateIcon = new ImageIcon(this.getClass().getResource("images/plate_area.png"));
		plateArea = plateIcon.getImage();
		ImageIcon cookIcon = new ImageIcon(this.getClass().getResource("images/cook_area.png"));
		cookArea = cookIcon.getImage();
		ImageIcon cashIcon = new ImageIcon(this.getClass().getResource("images/cashier_area.png"));
		cashierArea = cashIcon.getImage();
		
		ImageIcon t1Icon = new ImageIcon(this.getClass().getResource("images/ytable.png"));
		t1Image = t1Icon.getImage();
		ImageIcon t2Icon = new ImageIcon(this.getClass().getResource("images/btable.png"));
		t2Image = t2Icon.getImage();
		ImageIcon t3Icon = new ImageIcon(this.getClass().getResource("images/rtable.png"));
		t3Image = t3Icon.getImage();
		ImageIcon t4Icon = new ImageIcon(this.getClass().getResource("images/gtable.png"));
		t4Image = t4Icon.getImage();
		
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
		g2.drawImage(plateArea, 380, 320, null);
		g2.drawImage(cookArea, 320, 380, null);
		g2.drawImage(cashierArea, 0, 80, null);

		//Here are the tables
		int tableCnt = 0;
		for(Point p: tableLocations.values()) {
			switch(tableCnt % 4) { // randomly chooses a food choice
			case 0:
				g2.drawImage(t1Image, p.x, p.y, null);
				break;
			case 1:
				g2.drawImage(t2Image, p.x, p.y, null);
				break;
			case 2:
				g2.drawImage(t3Image, p.x, p.y, null);
				break;
			case 3:
				g2.drawImage(t4Image, p.x, p.y, null);
				break;
			}
			tableCnt++;
		}
		
		for(JesusGui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for(JesusGui gui : guis) {
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
	
	public void addGui(JesusMarketGui gui) {
		guis.add(gui);
	}
}
