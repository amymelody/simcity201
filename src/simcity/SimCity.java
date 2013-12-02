package simcity;

import javax.swing.JFrame;

import simcity.gui.BuildingGui;
import simcity.gui.BuildingsGui;
import simcity.gui.CityGui;

public class SimCity
{
    public static void main(String[] args)
    {
    	CityDirectory cityDirectory = new CityDirectory();
        
        CityGui cg = new CityGui(cityDirectory);
        cg.setTitle("City of the Blind");
        cg.setVisible(true);
        cg.setResizable(true);
        cg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BuildingsGui bg = new BuildingsGui(cityDirectory, cg);
        bg.setTitle("Interior");
        bg.setVisible(true);
        bg.setResizable(true);
        bg.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        System.out.println("City");
    }
}
