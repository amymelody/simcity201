package simcity.joshrestaurant.gui;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import simcity.gui.Gui;

public class JoshRestaurantAnimationPanel extends JPanel implements ActionListener {

	//Get rid of the "magic numbers"
	static final int TABLEX = 150;
	static final int TABLEY = 250;
	static final int TABLEWIDTH = 50;
	static final int TABLEHEIGHT = 50;
	
    private final int WINDOWX = 500;
    private final int WINDOWY = 500;
    private Image bufferImage;
    private Dimension bufferSize;
    private Timer timer;

    private List<Gui> guis = Collections.synchronizedList(new ArrayList<Gui>());

    public JoshRestaurantAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	timer = new Timer(5, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}
	
	public void pauseAnimation() {
		timer.stop();
	}
	
	public void resumeAnimation() {
		timer.start();
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );

        //Here are the tables
        g2.setColor(Color.ORANGE);
        g2.fillRect(TABLEX, TABLEY, TABLEWIDTH, TABLEHEIGHT);//200 and 250 need to be table params
        g2.fillRect(TABLEX+TABLEWIDTH*2, TABLEY, TABLEWIDTH, TABLEHEIGHT);
        g2.fillRect(TABLEX+TABLEWIDTH*4, TABLEY, TABLEWIDTH, TABLEHEIGHT);

        synchronized(guis) {
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
    }

    public void addGui(JoshCustomerGui gui) {
        guis.add(gui);
    }

    public void addGui(JoshWaiterGui gui) {
        guis.add(gui);
    }
    
    public void addGui(JoshCookGui gui) {
        guis.add(gui);
    }
}
