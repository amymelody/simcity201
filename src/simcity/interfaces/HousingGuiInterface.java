package simcity.interfaces;

import java.util.List;

import simcity.housing.gui.MoveBox;
import simcity.housing.gui.ResidentGui;

public interface HousingGuiInterface
{
	public MoveBox[][] boxes = new MoveBox[25][25];
	
	public abstract void removeResidentGui(ResidentGui rg); //from ResidentGui
	public abstract MoveBox getBox(int y, int x); //from ResidentGui
	public abstract void setBox(List<MoveBox> borrowedBoxes); //from ResidentGui
	
}
