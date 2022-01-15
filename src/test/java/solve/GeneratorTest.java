package solve;

import static org.junit.Assert.*;

import org.junit.Test;

import components.Orientation;
import components.Piece;
import components.PieceType;
import gui.Grid;

public class GeneratorTest {

	@Test
	public void possiblePieceTypeTest() {
		Piece p = new Piece(0, 0);
		Generator generator = new Generator(2,2,0);
		assertEquals(generator.possiblePieceType(p, 0, 0).get(0), PieceType.VOID);
		assertEquals(generator.possiblePieceType(p, 0, 0).get(1), PieceType.ONECONN);
		assertEquals(generator.possiblePieceType(p, 0, 0).get(2), PieceType.BAR);		
		assertEquals(generator.possiblePieceType(p, 0, 0).get(3), PieceType.LTYPE);
	}
}
