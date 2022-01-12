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
		this.inputGrid = new Grid(0,0);
		this.inputGrid.generateGridFromFile(fileNameInput);
		for(int h=0 ; h<this.inputGrid.getHeight(); h++) {
			for(int w=0 ; w<this.inputGrid.getWidth(); w++) {
				this.piecesNonFixees.add(this.inputGrid.getPiece(h, w));
			}
		}
		this.fileNameOutput = fileNameOutput;
	}
	
	public void eliminatePossibleOrientation(int line, int column) {
		Piece p = this.inputGrid.getPiece(line, column);
		
		Piece tn = this.inputGrid.topNeighbor(p);
		Piece ln = this.inputGrid.leftNeighbor(p);
		
		LinkedList<Orientation> connecteurs;
		LinkedList<Orientation> toSupp = new LinkedList<Orientation>();
		
		if (p.getType() != PieceType.VOID && p.getType() != PieceType.FOURCONN) {
			for(Orientation ori : p.getPossibleOrientations()) {
				connecteurs = p.getType().setConnectorsList(ori);
				if (line == 0) {
					if (column == 0) {
						if (connecteurs.contains(Orientation.WEST)) {
							toSupp.add(ori);
						}
					} 
					else if (column == this.inputGrid.getWidth() - 1) {
						if (connecteurs.contains(Orientation.EAST)) {
							toSupp.add(ori);
						}
					}
					if (connecteurs.contains(Orientation.NORTH)) {
						toSupp.add(ori);
					}
					if (!connecteurs.contains(Orientation.WEST) && ln != null && ln.hasRightConnector()) {
						toSupp.add(ori);
					}
					if (connecteurs.contains(Orientation.WEST) && ln != null && !ln.hasRightConnector()) {
						toSupp.add(ori);
					}

				} 
				else if (line > 0 && line < this.inputGrid.getHeight() - 1) {
					if (column == 0) {
						if (connecteurs.contains(Orientation.WEST)) {
							toSupp.add(ori);
						}

					} 
					else if (column == this.inputGrid.getWidth() - 1) {
						if (connecteurs.contains(Orientation.EAST)) {
							toSupp.add(ori);
						}
					}

					if (!connecteurs.contains(Orientation.WEST) && ln != null && ln.hasRightConnector()) {
						toSupp.add(ori);
					}
					if (connecteurs.contains(Orientation.WEST) && ln != null && !ln.hasRightConnector()) {
						toSupp.add(ori);
					}
					if (!connecteurs.contains(Orientation.NORTH) && tn != null && tn.hasBottomConnector()) {
						toSupp.add(ori);
					}
					if (connecteurs.contains(Orientation.NORTH) && tn != null && !tn.hasBottomConnector()) {
						toSupp.add(ori);
					}

				} else if (line == this.inputGrid.getHeight() - 1) {
					if (column == 0) {
						if (connecteurs.contains(Orientation.WEST)) {
							toSupp.add(ori);
						}
					} else if (column == this.inputGrid.getWidth() - 1) {
						if (connecteurs.contains(Orientation.EAST)) {
							toSupp.add(ori);
						}
					}
					if (connecteurs.contains(Orientation.SOUTH)) {
						toSupp.add(ori);
					}
					if (!connecteurs.contains(Orientation.WEST) && ln != null && ln.hasRightConnector()) {
						toSupp.add(ori);
					}
					if (connecteurs.contains(Orientation.WEST) && ln != null && !ln.hasRightConnector()) {
						toSupp.add(ori);
					}

				}
				if (connecteurs.contains(Orientation.WEST) && ln == null) {
					toSupp.add(ori);
				}
				if (connecteurs.contains(Orientation.NORTH) && tn == null) {
					toSupp.add(ori);
				}
			}
			for(Orientation ori : toSupp) {
				p.deleteFromPossibleOrientation(ori);
			}
		}
	}

	public boolean solveGrid(int i, int j) { //à l'appel de cette fonction ds le main i et j = 0
		// prbl : si la grille est pas solvable -> à gérer
		int h, w, x, y, departY, departX;;
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
						this.piecesNonFixees.remove(p);
					}
					
					if(p.getPossibleOrientations().size()==0) {
						Piece pieceRetour = this.piecesNonFixees.get(0);
						System.out.println(h + " " + w);
						departX = w-1;
						for(y=h; y>=0; y--) {
							for(x=departX; x>=0 && y>=0; x--) {
								System.out.println(this.inputGrid.getPiece(y, x).isFixed());
								if(!this.inputGrid.getPiece(y, x).isFixed()) {
									pieceRetour = this.inputGrid.getPiece(y, x);
									break;
								}
							}
							if(!this.inputGrid.getPiece(y, x).isFixed()) {
								break;
							}
							departX = 0;
							
						}
						System.out.println(pieceRetour);
						pieceRetour.deleteFromPossibleOrientation(pieceRetour.getOrientation());
						pieceRetour.turnFromPossibleOrientation();
						departY = pieceRetour.getPosY();
						departX = pieceRetour.getPosX()+1;
						for(y=departY; y<=p.getPosY(); y++) {
							for(x=departX; y<=p.getPosY() && x<=p.getPosX(); x++) {
								p2 = this.inputGrid.getPiece(y, x);
								p2.setPossibleOrientations(p2.getType().getListOfPossibleOri());
								p2.setFixed(false);
							}
							departX=0;
						}
						solveGrid(pieceRetour.getPosY(), pieceRetour.getPosX());
						return false;
					}
					
					else {
						p.turnFromPossibleOrientation();
						System.out.println("orientation selectionee : " + p.getOrientation());
					}
				}
				System.out.println("orientation finale : " + p.getOrientation());
			}
			j=0;
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
