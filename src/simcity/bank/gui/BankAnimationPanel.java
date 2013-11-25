package simcity.bank.gui;

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

public class BankAnimationPanel extends JPanel implements ActionListener {
	private final int WINDOWX = 400;
	private final int WINDOWY = 400;
	private Image bufferImage;
	private Dimension bufferSize;


	private List<Gui> guis = new ArrayList<Gui>();
	
	public BankAnimationPanel(){
		setSize(WINDOWX, WINDOWY);
		setVisible(true);
		
		bufferSize = this.getSize();
		Timer timer = new Timer(20,this);
		timer.start();
	}

	public void actionPerformed(ActionEvent e){
		repaint();
	}
	
	public void paintComponent(Graphics g){
		Graphics2D managerArea = (Graphics2D)g;
		Graphics2D tellerArea = (Graphics2D)g;
		
		managerArea.setColor(getBackground());
		managerArea.fillRect(10,200,50,20);
		managerArea.drawString("Manager Desk", 5, 20);
		tellerArea.setColor(getBackground());
		tellerArea.fillRect(10,200,50,20);
		tellerArea.drawString("Teller Desk", 105, 200);
		
	
	
	for(Gui gui : guis){
		if(gui.isPresent()){
			gui.updatePosition();
		}
	}
	
	for(Gui gui : guis){
		if(gui.isPresent()){
			gui.draw(managerArea);
			gui.draw(tellerArea);
			}
		}
	}
	
	public void addGui(BankDepositorGui gui){
		guis.add(gui);
	}
	public void addGui(BankManagerGui gui){
		guis.add(gui);
	}
	public void addGui(BankTellerGui gui){
		guis.add(gui);
	}
}