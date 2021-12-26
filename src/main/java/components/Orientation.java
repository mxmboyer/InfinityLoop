package components;

import java.util.HashMap;

/**
 * 
 * Orientation of the piece enum
 * 
 */
public enum Orientation {
	/* Implement all the possible orientations and 
	 *  required methods to rotate
	 */
	NORTH (0),
	EAST (1),
	SOUTH (2),
	WEST (3);
	
	private final int compassDirection;
	
	Orientation(int compassDirection) {
		this.compassDirection = compassDirection;
	}
	
	static Orientation getOrifromValue(int orientationValue) {
		for(Orientation o : Orientation.values()) {
			if(o.compassDirection == orientationValue) {
				return o;
			}
		}
		return Orientation.NORTH; //à modifier : trouver + propre
	}
	
	Orientation turn90() {
		switch(this.compassDirection) {
			case 0:
				return Orientation.EAST;
			case 1:
				return Orientation.SOUTH;
			case 2:
				return Orientation.WEST;
			default :
				return Orientation.NORTH;
		}
		//peut mieux faire avec un truc comme : return new Orientation((this.compassDirection + 1) % 3);
		//mais ce return en '//' ne fonctionne pas
	}
}
