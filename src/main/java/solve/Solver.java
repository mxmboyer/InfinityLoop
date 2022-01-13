package solve;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import components.Orientation;
import components.Pair;
import components.Piece;
import components.PieceType;
import gui.Grid;

public class Solver {
	
	private final Grid inputGrid;
	private final String fileNameOutput;
	
	public Solver(Grid inputGrid, String fileNameOutput) {
		this.inputGrid = new Grid(inputGrid.getWidth(), inputGrid.getHeight());
		Generator.copyGrid(inputGrid, this.inputGrid, 0, 0);
		this.fileNameOutput = fileNameOutput;
	}
	
	public Solver(String fileNameInput, String fileNameOutput) {
		this.inputGrid = new Grid(0,0);
		this.inputGrid.generateGridFromFile(fileNameInput);
		this.fileNameOutput = fileNameOutput;
	}
	
	public boolean isValidOrientationSolve(int line, int column, LinkedList<Orientation> connecteurs) {
		Piece p = this.inputGrid.getPiece(line, column);
		
		Piece tn = this.inputGrid.topNeighbor(p);
		Piece ln = this.inputGrid.leftNeighbor(p);
		Piece rn = this.inputGrid.rightNeighbor(p);
		Piece bn = this.inputGrid.bottomNeighbor(p);
		
		if (p.getType() != PieceType.VOID && p.getType() != PieceType.FOURCONN) {
			if (line == 0) {
				if (column == 0) {
					if (connecteurs.contains(Orientation.WEST)) {
						return false;
					}
				} 
				else if (column == this.inputGrid.getWidth() - 1) {
					if (connecteurs.contains(Orientation.EAST)) {
						return false;
					}
				}
				if (connecteurs.contains(Orientation.NORTH)) {
					return false;
				}
				if (!connecteurs.contains(Orientation.WEST) && ln != null && ln.hasRightConnector()) {
					return false;
				}
				if (connecteurs.contains(Orientation.WEST) && ln != null && !ln.hasRightConnector()) {
					return false;
				}

			} 
			else if (line > 0 && line < this.inputGrid.getHeight() - 1) {
				if (column == 0) {
					if (connecteurs.contains(Orientation.WEST)) {
						return false;
					}

				} 
				else if (column == this.inputGrid.getWidth() - 1) {
					if (connecteurs.contains(Orientation.EAST)) {
						return false;
					}
				}

				if (!connecteurs.contains(Orientation.WEST) && ln != null && ln.hasRightConnector()) {
					return false;
				}
				if (connecteurs.contains(Orientation.WEST) && ln != null && !ln.hasRightConnector()) {
					return false;
				}
				if (!connecteurs.contains(Orientation.NORTH) && tn != null && tn.hasBottomConnector()) {
					return false;
				}
				if (connecteurs.contains(Orientation.NORTH) && tn != null && !tn.hasBottomConnector()) {
					return false;
				}

			} 
			else if (line == this.inputGrid.getHeight() - 1) {
				if (column == 0) {
					if (connecteurs.contains(Orientation.WEST)) {
						return false;
					}
				} 
				else if (column == this.inputGrid.getWidth() - 1) {
					if (connecteurs.contains(Orientation.EAST)) {
						return false;
					}
				}
				if (connecteurs.contains(Orientation.SOUTH)) {
					return false;
				}
				if (!connecteurs.contains(Orientation.WEST) && ln != null && ln.hasRightConnector()) {
					return false;
				}
				if (connecteurs.contains(Orientation.WEST) && ln != null && !ln.hasRightConnector()) {
					return false;
				}

			}
			if (connecteurs.contains(Orientation.WEST) && ln == null) {
				return false;
			}
			if (connecteurs.contains(Orientation.NORTH) && tn == null) {
				return false;
			}
			if (connecteurs.contains(Orientation.SOUTH) && bn == null) {
				return false;
			}
			if (connecteurs.contains(Orientation.EAST) && rn == null) {
				return false;
			}
		}
		return true;
	}
	
	public void eliminatePossibleOrientation(int line, int column) {
		Piece p = this.inputGrid.getPiece(line, column);
		LinkedList<Orientation> connecteurs;
		LinkedList<Orientation> toSupp = new LinkedList<Orientation>();
		
		for(Orientation ori : p.getPossibleOrientations()) {
			connecteurs = p.getType().setConnectorsList(ori);
			if(!this.isValidOrientationSolve(line, column, connecteurs)) {
				toSupp.add(ori);
			}
		}
		
		for(Orientation ori : toSupp) {
			p.deleteFromPossibleOrientation(ori);
		}
	}

	public boolean solveGrid(int i, int j) { //à l'appel de cette fonction ds le main i et j = 0
		int h, w, x, y, departX;;
		Piece p, p2;
		for(h=i ; h<this.inputGrid.getHeight(); h++) {
			for(w=j ; w<this.inputGrid.getWidth(); w++) {
				System.out.println(h + "," + w + ": ");
				p = this.inputGrid.getPiece(h, w);
				System.out.println(p.getType().toString());
				if(!p.isFixed()) {
					
					this.eliminatePossibleOrientation(h, w);
					System.out.println(p.getPossibleOrientations());
					
					if(p.getPossibleOrientations().size()==1) { 
						p.turnFromPossibleOrientation();
						p.setFixed(true);
						System.out.println("piece ok");
					}
					
					else if(p.getPossibleOrientations().size()==0) {
						p.setPossibleOrientations(p.getType().getListOfPossibleOri());
						p.setFixed(false);
						Piece pieceRetour = null;
						departX = w-1;
						for(y=h; y>=0; y--) {
							for(x=departX; x>=0; x--) {
								p2 = this.inputGrid.getPiece(y, x);
								System.out.println(y + " " + x);
								System.out.println(p2.isFixed());
								if(!p2.isFixed()) {
									pieceRetour = this.inputGrid.getPiece(y, x);
									break;
								}
								p2.setPossibleOrientations(p2.getType().getListOfPossibleOri());
								p2.setFixed(false);
							}
							if(x!=-1) break;
							departX = this.inputGrid.getWidth() - 1;
						}
						if(pieceRetour==null) {
							//grille non solvable
							System.out.println("SOLVED : false");
							return false;
						}
						else {
							pieceRetour.deleteFromPossibleOrientation(pieceRetour.getOrientation());
							pieceRetour.turnFromPossibleOrientation();
							return solveGrid(pieceRetour.getPosY(), pieceRetour.getPosX());
						}
					}
					
					else {
						p.turnFromPossibleOrientation();
					}
				}
				System.out.println("orientation : " + p.getOrientation());
			}
			j=0;
		}
		Checker check = new Checker(this.inputGrid);
		if(!check.isSolution()) {
			System.out.println("SOLVED : false error");
			return false;
		}
		System.out.println("SOLVED : true");
		this.inputGrid.GenerateFileFromGrid(this.fileNameOutput);
		return true;
	}
	
	public static void main(String[] args) {
		Solver s = new Solver("test_solver_input.txt", "test_solver.txt");
		s.solveGrid(0, 0);
	}

}
