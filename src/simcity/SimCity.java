package simcity;

import javax.swing.JFrame;

import simcity.gui.BuildingGui;
import simcity.gui.CityGui;

public class SimCity
{
    public static void main(String[] args)
    {
        CityGui cg = new CityGui();
        cg.setTitle("City of the Blind");
        cg.setVisible(true);
        cg.setResizable(true);
        cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BuildingGui bg = new BuildingGui();
        bg.setTitle("Interior");
        bg.setVisible(true);
        bg.setResizable(true);
        bg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        System.out.println("City");
    }
}
