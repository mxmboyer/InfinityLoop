package components;

import java.util.ArrayList;
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
	
	PieceType(int connectors, int intValue) {
		this.connectors = connectors;
		this.intValue = intValue;
	}
	
	/**
	 *  Pas compris pourquoi on veut get l'orientation de celle pass�e en param�tre ??
	 * @param orientation
	 * @return
	 */
	//junit
	Orientation getOrientation(Orientation orientation) {
		return orientation;
	}
	
	//junit
	public int getIntValue() {
		return this.intValue;
	}
	
	/**
	 * Given the actual PieceType, tells all the possible Orientation for this piece
	 * @return a list of possible orientations
	 */
	//junit
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
	
	//junit
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
	
	//junit
	public static PieceType getTypefromValue(int typeValue) {
		for(PieceType pt : PieceType.values()) {
			if(typeValue == pt.intValue) {
				return pt;
			}
		}
		return PieceType.VOID; //� modifier : trouver + propre
	}

	//junit
	public int getNbConnectors() {
		return this.connectors;
	}
}
