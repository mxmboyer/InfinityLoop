package components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * 
 * Type of the piece enum
 * 
 */
public enum PieceType {
	// Each Type has a number of connectors and a specific value
	// PIECETYPE (number of connectors, int value),
	VOID (0, 0),
	ONECONN (1, 1),
	BAR (2, 2),
	TTYPE (3, 3),
	FOURCONN (4, 4),
	LTYPE (2, 5);

	private final int connectors;
	private final int intValue;
	
	/**
	 * Constructor of the enumeration PieceType
	 * @param connectors : int for the number of connectors of this type of piece
	 * @param intValue : int representing the intValue of the type of the piece
	 */
	PieceType(int connectors, int intValue) {
		this.connectors = connectors;
		this.intValue = intValue;
	}
	
	/**
	 *  Return the given orientation
	 * @param orientation : an Orientation
	 * @return an Orientation same as the parameter
	 */
	Orientation getOrientation(Orientation orientation) {
		return orientation;
	}
	
	/**
	 * getter for the intValue representing the type of the piece
	 * @return
	 */
	public int getIntValue() {
		return this.intValue;
	}
	
	/**
	 * Given the actual PieceType, tells all the possible Orientation for this piece
	 * @return a list of possible orientations
	 */
	public ArrayList<Orientation> getListOfPossibleOri() {
		ArrayList<Orientation> oriList = new ArrayList<Orientation>();
		switch(this.intValue) {
			case 0:
				oriList.add(Orientation.NORTH);
				break;
			case 1:
				oriList.add(Orientation.NORTH);
				oriList.add(Orientation.EAST);
				oriList.add(Orientation.SOUTH);
				oriList.add(Orientation.WEST);
				break;
			case 2:
				oriList.add(Orientation.NORTH);
				oriList.add(Orientation.EAST);
				break;
			case 3:
				oriList.add(Orientation.NORTH);
				oriList.add(Orientation.EAST);
				oriList.add(Orientation.SOUTH);
				oriList.add(Orientation.WEST);
				break;
			case 4:
				oriList.add(Orientation.NORTH);
				break;
			case 5:
				oriList.add(Orientation.NORTH);
				oriList.add(Orientation.EAST);
				oriList.add(Orientation.SOUTH);
				oriList.add(Orientation.WEST);
		}
		return oriList;
	}
	
	/**
	 * For a piece and its type and its orientation, gives the orientations of its connectors
	 * @param orientation : orientation of the piece
	 * @return a list of orientation
	 */
	public LinkedList<Orientation> setConnectorsList(Orientation orientation) {
		LinkedList<Orientation> connectorsList = new LinkedList<Orientation>();
		switch(this.intValue) {
			case 1:
				connectorsList.add(orientation);
				break;
			case 2:
				connectorsList.add(orientation);
				connectorsList.add(orientation.getOpposedOrientation());
				break;
			case 3:
				Orientation opposed = orientation.getOpposedOrientation();
				for(Orientation o : Orientation.values()) {
					if(o != opposed) {
						connectorsList.add(o);
					}
				}
				break;
			case 4:
				connectorsList.add(Orientation.NORTH);
				connectorsList.add(Orientation.EAST);
				connectorsList.add(Orientation.SOUTH);
				connectorsList.add(Orientation.WEST);
				break;
			case 5:
				connectorsList.add(orientation);
				connectorsList.add(orientation.turn90());
		}
		return connectorsList;
	}
	
	/**
	 * Gives the PieceType corresponding to the number given in parameter
	 * @param typeValue : an int between 0 and 5
	 * @return a PieceType
	 */
	public static PieceType getTypefromValue(int typeValue) {
		for(PieceType pt : PieceType.values()) {
			if(typeValue == pt.intValue) {
				return pt;
			}
		}
		return PieceType.VOID;
	}
	
	/**
	 * getter for the number of the connectors of this type of piece
	 * @return : an int between 0 and 4
	 */
	public int getNbConnectors() {
		return this.connectors;
	}
}
