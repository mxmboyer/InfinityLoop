package solve;

import static org.junit.Assert.*;

import org.junit.Test;

import components.Orientation;
import components.Piece;
import components.PieceType;
import gui.Grid;

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
	
	@Test
	public void eliminatePossibleOrientationTest() {
		Grid grid = new Grid(2,1);
		Piece p = new Piece(0, 0, PieceType.ONECONN, Orientation.EAST);
		Piece p2 = new Piece(0, 1, PieceType.ONECONN, Orientation.EAST);
		grid.setPiece(0, 0, p);
		grid.setPiece(0, 1, p2);
		Solver solver = new Solver(grid, "testSolverGrillSolvable.txt");
		solver.eliminatePossibleOrientation(0, 0);
		assertEquals(grid.getPiece(0,0).getPossibleOrientations().get(0), Orientation.EAST);
	}
}
