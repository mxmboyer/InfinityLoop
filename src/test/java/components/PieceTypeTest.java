package components;

import static org.junit.Assert.*;

import org.junit.Test;

public class PieceTypeTest {

	@Test
	public void getOrientationTest() {
		assertEquals(PieceType.LTYPE.getOrientation(Orientation.WEST), Orientation.WEST);
	}
	
	@Test
	public void getIntValueTest() {
		assertEquals(PieceType.LTYPE.getIntValue(), 5);
	}
	
	@Test
	public void getListOfPossibleOriTest() {
		assertEquals(PieceType.LTYPE.getListOfPossibleOri().get(0), Orientation.NORTH);
		assertEquals(PieceType.LTYPE.getListOfPossibleOri().get(1), Orientation.EAST);
		assertEquals(PieceType.LTYPE.getListOfPossibleOri().get(2), Orientation.SOUTH);
		assertEquals(PieceType.LTYPE.getListOfPossibleOri().get(3), Orientation.WEST);
	}
	
	@Test
	public void setConnectorsListTest() {
		Piece p = new Piece(3, 4, PieceType.ONECONN, Orientation.WEST);
		assertEquals(p.getType().setConnectorsList(Orientation.WEST).get(0), Orientation.WEST);
	}
	
	@Test
	public void getNbConnectorsTest() {
		assertEquals(PieceType.LTYPE.getNbConnectors(), 2);
	}
	
	@Test
	public void getTypefromValueTest() {
		assertEquals(PieceType.getTypefromValue(0), PieceType.VOID);
	}
}
