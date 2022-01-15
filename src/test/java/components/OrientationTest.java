package components;

import static org.junit.Assert.*;

import org.junit.Test;

public class OrientationTest {

	@Test
	public void getOpposedPieceCoordinatesTest() {
		Piece p = new Piece(3, 4);
		assertEquals(Orientation.getOpposedPieceCoordinates(p)[0], 2);
		assertEquals(Orientation.getOpposedPieceCoordinates(p)[1], 4);
	}

}
