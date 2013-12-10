package simcity.anjalirestaurant.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

import simcity.gui.Gui;
import simcity.anjalirestaurant.gui.AnjaliCookGui;
import simcity.anjalirestaurant.gui.AnjaliCustomerGui;
import simcity.anjalirestaurant.gui.AnjaliWaiterGui;

public class AnjaliRestaurantAnimationPanel extends JPanel implements ActionListener {

	static final int FILLRECTX = 200;
	static final int FILLRECTY = 250;
	static final int FILLRECT = 50;
    private final int WINDOWX = 300;
    private final int WINDOWY = 300;
    private Image bufferImage;
    private Dimension bufferSize;

	private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

    public AnjaliRestaurantAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(5, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
		synchronized(guis){
			for(Gui gui: guis){
				if(gui.isPresent()){
					gui.updatePosition();
				}
			}
		}
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Graphics2D g3 = (Graphics2D)g;
        Graphics2D g4 = (Graphics2D)g;
        Graphics2D cookArea = (Graphics2D)g;
        Graphics2D plateArea = (Graphics2D)g;
        Graphics2D fridge = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        g3.setColor(Color.WHITE);
        g3.fillRect(0,0, WINDOWX, WINDOWY);
        g4.setColor(Color.WHITE);
        g4.fillRect(0,0,WINDOWX, WINDOWY);
        
       
        //Here is the table
        g2.setColor(Color.ORANGE);
        g3.setColor(Color.YELLOW);
        g4.setColor(Color.BLUE);
        g2.fillRect(80, 50, FILLRECT, FILLRECT);//200 and 250 need to be table params
        g3.fillRect(160, 50, FILLRECT, FILLRECT);
        g4.fillRect(240, 50, FILLRECT, FILLRECT);
      

      
        for(Gui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
                gui.draw(g3);
                gui.draw(g4);
            }
        }
    }

    
    
    public void addGui(AnjaliCustomerGui gui) {
		guis.add(gui);
	}

	public void addGui(AnjaliWaiterGui gui) {
		guis.add(gui);
	}

	public void addGui(AnjaliCookGui gui) {
		guis.add(gui);
	}
}
