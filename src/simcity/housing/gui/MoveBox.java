package simcity.housing.gui;

public class MoveBox
{
	private int x;
	private int y;
	private boolean open;
	
	public MoveBox(int x, int y)
	{
		this.x = x;
		this.y = y;
		open = false;
	}
	
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public boolean getOpen()
	{
		return open;
	}
	public void setOpen(boolean tf)
	{
		open = tf;
	}
}
