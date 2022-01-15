package components;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Handling of pieces with general functions
 */
public class Piece {
	private int posX;// j
	private int posY;// i
	private PieceType type;
	private Orientation orientation;
	private LinkedList<Orientation> connectors;
	private ArrayList<Orientation> possibleOrientations;

	private boolean isFixed;

	/**
	 * Basic constructor of the class Piece with the 2 coordinates of the piece
	 * @param posY : int representing in which line is the piece
	 * @param posX : int representing in which column is the piece
	 */
	public Piece(int posY, int posX) {
		this.posX = posX;
		this.posY = posY;
		this.type = PieceType.VOID;
		this.orientation = type.getOrientation(Orientation.NORTH);
		this.connectors = type.setConnectorsList(Orientation.NORTH);
		this.isFixed = false; // Is there any orientation for the piece
		this.possibleOrientations = type.getListOfPossibleOri();
	}
	
	/**
	 * A more complete constructor for the class Piece, with the type of the piece and its orientation in addition
	 * @param posY : int representing in which line is the piece
	 * @param posX : int representing in which column is the piece
	 * @param type : the type of the piece
	 * @param orientation : the orientation of the piece
	 */
	public Piece(int posY, int posX, PieceType type, Orientation orientation) {
		this.posX = posX;
		this.posY = posY;
		this.type = type;
		this.orientation = type.getOrientation(orientation);
		this.connectors = type.setConnectorsList(orientation);
		this.isFixed = false;
		this.possibleOrientations = type.getListOfPossibleOri();
	}

	/**
	 * Another version of the complete constructor for the class Piece with the type and the orientation of the piece
	 * given as integers and not PieceType and Orientation
	 * @param posY : int representing in which line is the piece
	 * @param posX : int representing in which column is the piece
	 * @param typeValue : the type of the piece as an integer between 0 and 5
	 * @param orientationValue : the orientation of the piece as an integer between 0 and 3
	 */
	public Piece(int posY, int posX, int typeValue, int orientationValue) {
		this.posX = posX;
		this.posY = posY;
		this.type = PieceType.getTypefromValue(typeValue);
		this.orientation = type.getOrientation(Orientation.getOrifromValue(orientationValue));
		this.connectors = type.setConnectorsList(Orientation.getOrifromValue(orientationValue));
		this.isFixed = false;
		this.possibleOrientations = type.getListOfPossibleOri();
	}
	
	/**
	 * Setter for the possible orientations of the piece
	 * @param possibleOrientations : a list containing the possible orientations of the piece
	 */
	public void setPossibleOrientations(ArrayList<Orientation> possibleOrientations) {
		this.possibleOrientations = possibleOrientations;
	}

	/**
	 * Getter for the possible orientations of the piece
	 * @return a list containing Orientation
	 */
	public ArrayList<Orientation> getPossibleOrientations() {
		return this.possibleOrientations;
	}

	
	public LinkedList<Orientation> getInvPossibleOrientation() {
		LinkedList<Orientation> invPossibleOrientations = new LinkedList<Orientation>();
		for (Orientation ori : this.getPossibleOrientations()) {
			invPossibleOrientations.addFirst(ori);
		}
		return invPossibleOrientations;
	}

	public void deleteFromPossibleOrientation(Orientation ori) {
		if (this.possibleOrientations.contains(ori)) {
			this.possibleOrientations.remove(ori);
		}
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}

	public boolean isFixed() {
		return isFixed;
	}

	public int getPosX() { // get j
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() { // get i
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public PieceType getType() {
		return type;
	}

	public void setType(PieceType type) {
		this.type = type;
		this.possibleOrientations = type.getListOfPossibleOri();
	}

	public void setOrientation(int orientationValue) {
		this.orientation = type.getOrientation(Orientation.getOrifromValue(orientationValue));
		this.connectors = type.setConnectorsList(this.orientation);
	}
	
	public void setOrientation(Orientation orientation) {
		this.orientation = type.getOrientation(orientation);
		this.connectors = type.setConnectorsList(orientation);
	}

	public Orientation getOrientation() {
		return orientation;
	}

	public LinkedList<Orientation> getConnectors() {
		return connectors;
	}

	public boolean hasTopConnector() {
		for (Orientation ori : this.getConnectors()) {
			if (ori == Orientation.NORTH) {
				return true;
			}
		}
		return false;
	}

	public boolean hasRightConnector() {
		for (Orientation ori : this.getConnectors()) {
			if (ori == Orientation.EAST) {
				return true;
			}
		}
		return false;
	}

	public boolean hasBottomConnector() {
		for (Orientation ori : this.getConnectors()) {
			if (ori == Orientation.SOUTH) {
				return true;
			}
		}
		return false;
	}

	public boolean hasLeftConnector() {
		for (Orientation ori : this.getConnectors()) {
			if (ori == Orientation.WEST) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Turn the piece 90 degrees on the right and redefine the connectors's position
	 */
	public void turn() {
		this.orientation = type.getOrientation(orientation.turn90());
		this.connectors = type.setConnectorsList(orientation);
	}
	
	/**
	 * Tourne une piece en prenant une autre de ses possibles orientations
	 */
	public void turnFromPossibleOrientation() {
		ArrayList<Orientation> ori = getPossibleOrientations();
		if(ori.size()!=1) {
			this.orientation = type.getOrientation(orientation.turn90());
			while(!ori.contains(this.orientation)){
				this.orientation = type.getOrientation(orientation.turn90());
			}
			this.connectors = type.setConnectorsList(orientation);
		}
		else {
			this.orientation = ori.get(0);
			this.connectors = type.setConnectorsList(orientation);
		}		
	}

	@Override
	public String toString() {
		String s = "[" + this.getPosY() + ", " + this.getPosX() + "] " + this.getType() + " ";
		for (Orientation ori : this.getConnectors()) {
			s += " " + ori.toString();
		}
		s += " Orientation / " + this.getOrientation();
		return s;
	}
}
