package simcity.gui;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import simcity.gui.BusGui;
import simcity.gui.CarGui;

public class Lane {
	Rectangle2D.Double rectangle;
	ArrayList<Line2D.Double> sides;
	int xVelocity;
	int yVelocity;
	boolean redLight;
	int xOrigin;
	int yOrigin;
	int width;
	int height;
	boolean isHorizontal;
	boolean startAtOrigin;
	Color laneColor;
	Color sideColor;
	ArrayList<CarGui> cars;
	ArrayList<BusGui> buses;
	
	public Lane(int xo, int yo, int w, int h, int xv, int yv, boolean ish, Color lc, Color sc ) {
		redLight = false;
		width = w;
		height = h;
		xVelocity = xv;
		yVelocity = yv;
		xOrigin = xo;
		yOrigin = yo;
		isHorizontal = ish;
		laneColor = lc;
		sideColor = sc;
		
		//Make the lane surface
		rectangle = new Rectangle2D.Double( xOrigin, yOrigin, width, height );
		
		//Make the edges to the lane surface
		sides = new ArrayList<Line2D.Double>();
		if ( isHorizontal ) {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin+width, yOrigin ) );
			sides.add( new Line2D.Double( xOrigin, yOrigin+height, xOrigin+width, yOrigin+height ) );
		} else {
			sides.add( new Line2D.Double( xOrigin, yOrigin, xOrigin, yOrigin+height ) );
			sides.add( new Line2D.Double( xOrigin+width, yOrigin, xOrigin+width, yOrigin+height ) );
		}
		
		cars = new ArrayList<CarGui>();
		buses = new ArrayList<BusGui>();
	}
	
	public void addCar( CarGui c ) {
		//We need to set the proper origin for this new vehicle, given the lane starting geometry constraints
		//The +2 is due to my lanes being 20 pixels "wide" and vehicles being 16 pixels "wide". 
		if ( xVelocity > 0 ) {
			c.setRect( xOrigin, yOrigin+2, c.getWidth(), c.getHeight() ); 
		} else if ( yVelocity > 0 ) {
			c.setRect( xOrigin+2, yOrigin, c.getWidth(), c.getHeight() ); 
		} else {
			if ( isHorizontal ) {
				c.setRect( xOrigin + width - c.getWidth(), yOrigin + 2, c.getWidth(), c.getHeight() );
			} else {
				c.setRect( xOrigin + 2, yOrigin + height - c.getHeight(), c.getWidth(), c.getHeight() ) ;
			}
		}
		
		cars.add( c );
	}
	
	public void addBus( BusGui b ) {
		//We need to set the proper origin for this new vehicle, given the lane starting geometry constraints
		//The +2 is due to my lanes being 20 pixels "wide" and vehicles being 16 pixels "wide". 
		if ( xVelocity > 0 ) {
			b.setRect( xOrigin, yOrigin+2, b.getWidth(), b.getHeight() ); 
		} else if ( yVelocity > 0 ) {
			b.setRect( xOrigin+2, yOrigin, b.getWidth(), b.getHeight() ); 
		} else {
			if ( isHorizontal ) {
				b.setRect( xOrigin + width - b.getWidth(), yOrigin + 2, b.getWidth(), b.getHeight() );
			} else {
				b.setRect( xOrigin + 2, yOrigin + height - b.getHeight(), b.getWidth(), b.getHeight() ) ;
			}
		}
		
		buses.add( b );
	}
	
	public void drawCar( Graphics2D g2 ) {
		g2.setColor( laneColor );
		g2.fill( rectangle );
		
		for ( int i=0; i<sides.size(); i++ ) {
			g2.setColor( sideColor );
			g2.draw( sides.get(i) );
		}
		
		for ( int i= cars.size()-1; i >= 0; i-- ) {
			CarGui c = cars.get(i);
			if ( !redLight ) {
				c.move( xVelocity, yVelocity );
			}
			
			double x = c.getX();
			double y = c.getY();

			//Remove the vehicle from the list if it is at the end of the lane
			if ( isHorizontal ) {
				//End of lane is xOrigin + width - vehicle width
				double endOfLane = xOrigin + width - c.getWidth();
				if ( xVelocity > 0 && x >= endOfLane ) {
					cars.remove(i);					
				} else if ( x <= xOrigin ) {
					cars.remove(i);
				}
			} else {
				//End of lane is xOrigin + height - vehicle height
				double endOfLane = yOrigin + height - c.getHeight();
				if ( yVelocity > 0 && y >= endOfLane ) {
					cars.remove(i);					
				} else if ( y <= yOrigin ) {
					cars.remove(i);
				}
			}
		}
		
		for ( int i=0; i<cars.size(); i++ ) {
			CarGui c = cars.get(i);
			g2.setColor( c.getColor() );
			g2.fill( c );
		}
	}
	
	public void drawBus( Graphics2D g2 ) {
		g2.setColor( laneColor );
		g2.fill( rectangle );
		
		for ( int i=0; i<sides.size(); i++ ) {
			g2.setColor( sideColor );
			g2.draw( sides.get(i) );
		}
		
		for ( int i= buses.size()-1; i >= 0; i-- ) {
			BusGui b = buses.get(i);
			if ( !redLight ) {
				b.move( xVelocity, yVelocity );
			}
			
			double x = b.getX();
			double y = b.getY();

			//Remove the vehicle from the list if it is at the end of the lane
			if ( isHorizontal ) {
				//End of lane is xOrigin + width - vehicle width
				double endOfLane = xOrigin + width - b.getWidth();
				if ( xVelocity > 0 && x >= endOfLane ) {
					buses.remove(i);					
				} else if ( x <= xOrigin ) {
					buses.remove(i);
				}
			} else {
				//End of lane is xOrigin + height - vehicle height
				double endOfLane = yOrigin + height - b.getHeight();
				if ( yVelocity > 0 && y >= endOfLane ) {
					buses.remove(i);					
				} else if ( y <= yOrigin ) {
					buses.remove(i);
				}
			}
		}
		
		for ( int i=0; i<buses.size(); i++ ) {
			BusGui b = buses.get(i);
			g2.setColor( b.getColor() );
			g2.fill( b );
		}
	}
	
	public void redLight() {
		redLight = true;
	}
	
	public void greenLight() {
		redLight = false;
	}
}
