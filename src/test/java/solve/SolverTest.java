package solve;

import static org.junit.Assert.*;

import org.junit.Test;

public class SolverTest {

	@Test
	public void SolveGrillNonSolvableTest() {
		Solver solver = new Solver("test_solver_input_non_solvable.txt", "testSolverGrillSolvable.txt");
		assertFalse(solver.solveGrid(0, 0));
	}
	
	@Test
	public void SolveGrilSolvableTest() {
		Solver solver = new Solver("test_solver_input_solvable.txt", "testSolverGrillSolvable.txt");
		assertTrue(solver.solveGrid(0, 0));
	}

}
