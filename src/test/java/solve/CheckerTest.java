package solve;

import static org.junit.Assert.*;

import org.junit.Test;

public class CheckerTest {

	@Test
	public void testIsSolution() {
		Generator gen = new Generator(10, 10, -1);
		gen.generateGrillFilled();
		Checker checker = new Checker(gen.getFilledGrid());
		assertTrue(checker.isSolution());
	}

}
