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

	volatile AlfredCustomerRole occupiedBy;
	int tableNumber;
	Point position;

	AlfredTable(int tableNumber) {
		this.tableNumber = tableNumber;
	}

	void setOccupant(AlfredCustomerRole cust) {
		occupiedBy = cust;
	}

	void setUnoccupied() {
		occupiedBy = null;
	}

	AlfredCustomerRole getOccupant() {
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