package components;

import static org.junit.Assert.*;

import org.junit.Test;

public class PieceTest {

	@Test
	public void turnFromPossibleOrientationTest() {
		Piece p = new Piece(3, 4, PieceType.VOID, Orientation.NORTH);
		p.turnFromPossibleOrientation();
		assertEquals(p.getOrientation(), Orientation.NORTH);
	}	
}
