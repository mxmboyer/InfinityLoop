package components;

import static org.junit.Assert.*;

import org.junit.Test;

public class PieceTypeTest {

	@Test
	public void getIntValueTest() {
		assertEquals(PieceType.LTYPE.getIntValue(), 5);
	}

}
