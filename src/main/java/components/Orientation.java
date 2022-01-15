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
	
	/**
	 * Constructor of the enum, taking one argument as input
	 * @param compassDirection : int representing the orientation
	 */
	Orientation(int compassDirection) {
		this.compassDirection = compassDirection;
	}
	
	/**
	 * Getter for compassDirection
	 * @return an int representing the orientation with a compass
	 */
	
	public int getCompassDirection() {
		return this.compassDirection;
	}
	
	/**
	 * For a given number, returns the Orientation associated to this number
	 * @param orientationValue : an int between 0 and 3 representing an orientation
	 * @return an Orientation
	 */
	//junit
	public static Orientation getOrifromValue(int orientationValue) {
		for(Orientation o : Orientation.values()) {
			if(o.compassDirection == orientationValue) {
				return o;
			}
		}
		return Orientation.NORTH; //� modifier : trouver + propre
	}
	
	/**
	 * 
	 * @return an Orientation turned of 90� to the right (clockwise)
	 */
	public Orientation turn90() {
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
	}

	
	public static int[] getOpposedPieceCoordinates(Piece p) {
		int [] oppPiece = new int[2];
		switch(p.getOrientation().getCompassDirection()) {
			case 0:
				oppPiece[0]=p.getPosY()-1;
				oppPiece[1]=p.getPosX();
				break;
			case 1:
				oppPiece[0]=p.getPosY();
				oppPiece[1]=p.getPosX()+1;
				break;
			case 2:
				oppPiece[0]=p.getPosY()+1;
				oppPiece[1]=p.getPosX();
				break;
			case 3:
				oppPiece[0]=p.getPosY();
				oppPiece[1]=p.getPosX()-1;
				break;
		}
		return oppPiece;
	}
	
	/**
	 * 
	 * @return an Orientation which is the opposed as the actual one
	 */
	
	public Orientation getOpposedOrientation() {
		switch(this.compassDirection) {
			case 0:
				return Orientation.SOUTH;
			case 1:
				return Orientation.WEST;
			case 2:
				return Orientation.NORTH;
			case 3:
				return Orientation.EAST;
			default:
				return Orientation.NORTH;
		}
	}
}
