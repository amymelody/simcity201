package simcity.alfredrestaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.ArrayList;

import simcity.alfredrestaurant.AlfredHostRole;

public class AlfredRestaurantAnimationPanel extends JPanel implements ActionListener, MouseListener {

	// private final int WINDOWX = 450;
	// private final int WINDOWY = 350;
	private final int TIMER_TICK = 20;
	private Image bufferImage;
	private Dimension bufferSize;

	private List<AlfredGui> guis = new ArrayList<AlfredGui>();

	private AlfredHostRole agent;
	private AlfredRestaurantGUI restaurantGUI;
	
	public void setHostAgent(AlfredHostRole agent){
		this.agent = agent;
	}
	public AlfredRestaurantAnimationPanel(AlfredRestaurantGUI restaurantGUI) {
		// setSize(WINDOWX, WINDOWY);
		// setVisible(true);
		this.restaurantGUI = restaurantGUI;
		bufferSize = this.getSize();
		setBorder(BorderFactory.createTitledBorder(""));
		Timer timer = new Timer(TIMER_TICK, this);
		timer.start();
		addMouseListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		repaint(); // Will have paintComponent called
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		// Clear the screen by painting a rectangle the size of the frame
		g2.setColor(getBackground());
		g2.fillRect(0, 0, getWidth(), getHeight());

		// Here is the table
		g2.setColor(Color.ORANGE);

		// draw table
		for (int i = 1; i <= agent.NTABLES; i++) {
			g2.fillRect(agent.getTablePosition(i).x,
					agent.getTablePosition(i).y, AlfredHostGui.sizeTable,
					AlfredHostGui.sizeTable);// 200 and 250 need to be table params
		}

		for (AlfredGui gui : guis) {
			if (gui.isPresent()) {
				gui.updatePosition();
			}
		}

		for (AlfredGui gui : guis) {
			if (gui.isPresent()) {
				gui.draw(g2);
			}
		}
		restaurantGUI.customerWaitingArea.repaint();
		restaurantGUI.platingArea.repaint();
		restaurantGUI.cookingArea.repaint();
	}

	public void addGui(AlfredGui gui) {
		guis.add(gui);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//Not clicking on animation
//		agent.clickOn(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
