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
	
	@Test
	public void getCompassDirectionTest() {
		assertEquals(Orientation.WEST.getCompassDirection(), 3);
	}
	
	@Test
	public void getOrifromValueTest() {
		assertEquals(Orientation.getOrifromValue(3), Orientation.WEST);
	}
	
	@Test
	public void turn90Test() {
		assertEquals(Orientation.NORTH.turn90(), Orientation.EAST);
	}
	
	@Test
	public void getOpposedOrientationTest() {
		assertEquals(Orientation.NORTH.getOpposedOrientation(), Orientation.SOUTH);
	}
	
}
