package simcity.housing.gui;

public class MoveBox
{
	private int indexX;
	private int indexY;
	private int x;
	private int y;
	private boolean open;
	private boolean hasVehicle;
	
	public MoveBox(int ix, int iy, int x, int y)
	{
		indexX = ix;
		indexY = iy;
		this.x = x;
		this.y = y;
		open = true;
		hasVehicle = false;
	}

	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getIndexX()
	{
		return indexX;
	}
	public int getIndexY()
	{
		return indexY;
	}
	public boolean getOpen()
	{
		return open;
	}
	public void setOpen(boolean tf)
	{
		open = tf;
	}
	public boolean getHasVehicle() 
	{
		return hasVehicle;
	}
	public void setHasVehicle(boolean tf)
	{
		hasVehicle = tf;
	}
}
