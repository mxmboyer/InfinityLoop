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
	
	Orientation getOrientation(Orientation orientation) {
		// TODO
	}
	
	ArrayList<Orientation> getListOfPossibleOri() {
		//TODO
	}
	
	LinkedList<Orientation> setConnectorsList(Orientation orientation) {
		//TODO
	}
	
	static PieceType getTypefromValue(int typeValue) {
		for(PieceType pt : PieceType.values()) {
			if(typeValue == pt.intValue) {
				return pt;
			}
		}
		return PieceType.VOID; //à modifier : trouver + propre
	}
}
