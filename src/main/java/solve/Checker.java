package solve;

import components.Piece;
import gui.Grid;

public class Checker {
	private final Grid inputGrid;
	
	/**
	 * Create a checker with a grid to check
	 * @param inputGrid : Grid
	 */
	public Checker(Grid inputGrid) {
		this.inputGrid = new Grid(inputGrid.getWidth(), inputGrid.getHeight());
		Generator.copyGrid(inputGrid, this.inputGrid, 0, 0);
	}
	
	/**
	 * Create a checker with a file name of a grid to generate and check
	 * @param fileName
	 */
	public Checker(String fileName) {
		this.inputGrid = new Grid(0, 0);
		this.inputGrid.generateGridFromFile(fileName);
	}
	
	/**
	 * Permet de verifier si une grille est solvee
	 * @return true si solvee, false sinon
	 */
	public boolean isSolution() {
		Piece[][] pieces = this.inputGrid.getAllPieces();
		boolean check = true;
		for(int h = 0 ; h < this.inputGrid.getHeight() && check; h++) {
			for(int w = 0 ; w < this.inputGrid.getWidth() && check ; w++) {
				if(!this.inputGrid.isValidOrientation(h,w)) {
					check = false;
					System.out.println("SOLVED : false");
				}
			}
		}
		if(check) {
			System.out.println("SOLVED : true");
		}
		return check;
	}
}
