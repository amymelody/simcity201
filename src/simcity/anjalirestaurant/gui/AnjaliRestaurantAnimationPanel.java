package simcity.anjalirestaurant.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnjaliRestaurantAnimationPanel extends JPanel implements ActionListener {

	static final int FILLRECTX = 200;
	static final int FILLRECTY = 250;
	static final int FILLRECT = 50;
    private final int WINDOWX = 300;
    private final int WINDOWY = 300;
    private Image bufferImage;
    private Dimension bufferSize;

    private List<AnjaliGui> guis = new ArrayList<AnjaliGui>();

    public AnjaliRestaurantAnimationPanel() {
    	setSize(WINDOWX, WINDOWY);
        setVisible(true);
        
        bufferSize = this.getSize();
 
    	Timer timer = new Timer(20, this );
    	timer.start();
    }

	public void actionPerformed(ActionEvent e) {
		repaint();  //Will have paintComponent called
	}

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Graphics2D g3 = (Graphics2D)g;
        Graphics2D g4 = (Graphics2D)g;
        Graphics2D cookArea = (Graphics2D)g;
        Graphics2D plateArea = (Graphics2D)g;
        Graphics2D fridge = (Graphics2D)g;
        //Clear the screen by painting a rectangle the size of the frame
        g2.setColor(getBackground());
        g2.fillRect(0, 0, WINDOWX, WINDOWY );
        g3.setColor(getBackground());
        g3.fillRect(0,0, WINDOWX, WINDOWY);
        g4.setColor(getBackground());
        g4.fillRect(0,0,WINDOWX, WINDOWY);
        
        cookArea.setColor(getBackground());
        cookArea.fillRect(0,0,WINDOWX, WINDOWY);
        plateArea.setColor(getBackground());
        plateArea.fillRect(0,0,WINDOWX, WINDOWY);
        fridge.setColor(getBackground());
        fridge.fillRect(0,0,WINDOWX, WINDOWY);
        //Here is the table
        g2.setColor(Color.ORANGE);
        g3.setColor(Color.YELLOW);
        g4.setColor(Color.BLUE);
        g2.fillRect(80, 50, FILLRECT, FILLRECT);//200 and 250 need to be table params
        g3.fillRect(160, 50, FILLRECT, FILLRECT);
        g4.fillRect(240, 50, FILLRECT, FILLRECT);
        cookArea.fillRect(10, 200, 50, 20);
        cookArea.drawString("Cook Area", 5, 200);
        
        plateArea.fillRect(110, 200, 50, 20);
        cookArea.drawString("Plate Area", 105, 200);
        
        fridge.fillRect(70, 250, 10, 20);
        fridge.drawString("Fridge", 70, 250);

        for(AnjaliGui gui : guis) {
            if (gui.isPresent()) {
                gui.updatePosition();
            }
        }

        for(AnjaliGui gui : guis) {
            if (gui.isPresent()) {
                gui.draw(g2);
                gui.draw(g3);
                gui.draw(g4);
                gui.draw(cookArea);
            }
        }
    }

    
    
    public void addGui(AnjaliCustomerGui gui) {
        guis.add(gui);
    }
    
    public void addGui(AnjaliWaiterGui gui){
    	guis.add(gui);
    	
    }

    public void addGui(AnjaliHostGui gui) {
        guis.add(gui);
    }
    public void addGui(AnjaliCookGui gui){
    	guis.add(gui);
    }
}
