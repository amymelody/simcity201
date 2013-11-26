package simcity.bank.gui;

import javax.swing.*;

import simcity.gui.Gui;
import simcity.joshrestaurant.gui.JoshRestaurantAnimationPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class BankAnimationPanel extends JPanel implements ActionListener {
	static final int FILLRECTX = 200;
	static final int FILLRECTY = 250;
	static final int FILLRECT = 50;
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    static final int TABLEX = 150;
	static final int TABLEY = 250;
	static final int TABLEWIDTH = 50;
	static final int TABLEHEIGHT = 50;
	
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
        //Clear the screen by painting a rectangle the size of the frame
       
        
        managerArea.setColor(getBackground());
        managerArea.fillRect(0,0,WINDOWX, WINDOWY);
        tellerArea.setColor(getBackground());
        tellerArea.fillRect(0,0,WINDOWX, WINDOWY);
 
        //Here is the table
        
        managerArea.fillRect(10, 200, 50, 20);
        managerArea.drawString("Cook Area", 5, 200);
        
        tellerArea.fillRect(110, 200, 50, 20);
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