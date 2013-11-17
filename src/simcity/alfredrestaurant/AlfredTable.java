/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simcity.alfredrestaurant;

import java.awt.Point;

/**
 *
 */
public class AlfredTable {

	volatile CustomerAgent occupiedBy;
	int tableNumber;
	Point position;

	Table(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	void setOccupant(CustomerAgent cust) {
		occupiedBy = cust;
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	CustomerAgent getOccupant() {
		return occupiedBy;
	}

	boolean isOccupied() {
		return occupiedBy != null;
	}

	public String toString() {
		return "table " + tableNumber;
	}

	public Point getPosition() {
		return position;
	}

	public void setLocation(Point point) {
		position = point;
	}
}