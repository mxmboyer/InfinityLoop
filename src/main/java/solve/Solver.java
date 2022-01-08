package solve;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import components.Orientation;
import components.Pair;
import components.Piece;
import components.PieceType;
import gui.Grid;

public class Solver {
	
	private final Grid inputGrid;
	List<Piece> piecesNonFixees = new ArrayList<Piece>();
	private final String fileNameOutput;
	
	public Solver(Grid inputGrid, String fileNameOutput) {
		this.inputGrid = new Grid(inputGrid.getWidth(), inputGrid.getHeight());
		Generator.copyGrid(inputGrid, this.inputGrid, 0, 0);
		for(int h=0 ; h<this.inputGrid.getHeight(); h++) {
			for(int w=0 ; w<this.inputGrid.getWidth(); w++) {
				this.piecesNonFixees.add(this.inputGrid.getPiece(h, w));
			}
		}
		this.fileNameOutput = fileNameOutput;
	}
	
	public Solver(String fileNameInput, String fileNameOutput) {
		this.inputGrid = new Grid(0, 0);
		this.inputGrid.generateGridFromFile(fileNameInput);
		for(int h=0 ; h<this.inputGrid.getHeight(); h++) {
			for(int w=0 ; w<this.inputGrid.getWidth(); w++) {
				this.piecesNonFixees.add(this.inputGrid.getPiece(h, w));
			}
		}
		this.fileNameOutput = fileNameOutput;
	}
	
	public void solveGrid(int i, int j) { //à l'appel de cette fonction ds le main i et j = 0
		// prbl : si la grille est pas solvable -> à gérer
		int h, w, k;
		Piece p;
		for(h=i ; h<this.inputGrid.getHeight(); h++) {
			for(w=j ; w<this.inputGrid.getWidth(); w++) {
				p = this.inputGrid.getPiece(h, w);
				if(!p.isFixed()) {
				
					if(p.getPossibleOrientations().size()==1) { 
						p.setFixed(true);
						this.piecesNonFixees.remove(p);
					}
					
					else {
						if(!this.inputGrid.isValidOrientation(h,w)) {
							p.turnFromPossibleOrientation();
							k = 1; // si aucune des orientations ne conviennent :
							// la solution ne fonctionne pas donc on arrête
							// si k = nbr d'orientations possibles alors on aura testé toutes les orientations
							while(!this.inputGrid.isValidOrientation(h,w) || k < p.getPossibleOrientations().size()) {
								p.turnFromPossibleOrientation();
								k++;
							}
							if(k==p.getPossibleOrientations().size()) {
								Piece pieceDebut = this.piecesNonFixees.get(0);
								pieceDebut.deleteFromPossibleOrientation(pieceDebut.getOrientation());
								pieceDebut.turnFromPossibleOrientation();
								solveGrid(pieceDebut.getPosY(), pieceDebut.getPosX()); 
							}
						}
					}
				}
			}
		}
		System.out.println("SOLVED : true");
		this.inputGrid.GenerateFileFromGrid(this.fileNameOutput);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
