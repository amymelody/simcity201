package simcity.housing.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

import simcity.gui.Gui;

public class HousingAnimationPanel extends JPanel implements ActionListener
{
	static final int TIMERINCR = 10;
	private final int WINDOWX = 500;
	private final int WINDOWY = 500;
	private Image bufferImage;
	private Dimension bufferSize;

	private List<Gui> guis = new ArrayList<Gui>();

	//Image bg;
	
	public HousingAnimationPanel() {
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		//ImageIcon bgIcon = new ImageIcon("simcity/images/Housing_floor.png");
		//bg = bgIcon.getImage();
		
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
		//g2.drawImage(bg, 0, 0, null);
		
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

	public void addGui(HousingLandlordGui gui) {
		guis.add(gui);
	}

	public void addGui(HousingResidentGui gui) {
		guis.add(gui);
	}
}
