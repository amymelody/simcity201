package simcity;

import javax.swing.JFrame;

import simcity.gui.BuildingGui;
import simcity.gui.CityGui;

public class SimCity
{
    public static void main(String[] args)
    {
    	CityDirectory cityDirectory = new CityDirectory();
        
        BuildingGui bg = new BuildingGui(cityDirectory);
        bg.setTitle("Interior");
        bg.setVisible(true);
        bg.setResizable(true);
        bg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        CityGui cg = new CityGui(cityDirectory, bg);
        cg.setTitle("City of the Blind");
        cg.setVisible(true);
        cg.setResizable(true);
        cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        System.out.println("City");
    }
}
