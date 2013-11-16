package simcity;

import javax.swing.JFrame;
import simcity.gui.CityGui;

public class SimCity
{
    public static void main(String[] args)
    {
        CityGui gui = new CityGui();
        gui.setTitle("City of the Blind");
        gui.setVisible(true);
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
