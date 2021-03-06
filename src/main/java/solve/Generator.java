package solve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import components.Orientation;
import components.Piece;
import components.PieceType;
import gui.Grid;

/**
 * Generate a solution, number of connexe composant is not finished
 *
 */
public class Generator {
	private static Grid filledGrid;
	
	/**
	 * Constructor of the class Generator, initiate the Grid associate to the class
	 * @param w : width of the Grid to generate
	 * @param h : height of the Grid to generate
	 * @param c : nbcc of the Grid to generate
	 */
	public Generator(int w, int h, int c) {
		Generator.filledGrid = new Grid(w, h, c);
	}
	
	/**
	 * Getter for the Grid attribute filledGrid
	 * @return the filled grid of this Generator
	 */
	public Grid getFilledGrid() {
		return this.filledGrid;
	}
	
	/**_
	 * @param output
	 *            file name
	 * @throws IOException
	 *             - if an I/O error occurs.
	 * @return a File that contains a grid filled with pieces (a level)
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void generateLevel(String fileName, Grid inputGrid) {
		inputGrid.generateFileFromGrid(fileName);
	}
	
	/**
	 * function to generate a grid which can be solved
	 */
	public static void generateGrillFilled() {
		for(int h=0 ; h<filledGrid.getHeight() ; h++) {
			for(int w=0 ; w<filledGrid.getWidth() ; w++) {
				Piece p = new Piece(h, w);
				filledGrid.setPiece(h, w, p);
				Piece leftNeighbor = filledGrid.leftNeighbor(p);
				Piece topNeighbor = filledGrid.topNeighbor(p);
				int top, left;
				if(leftNeighbor != null) {
					if(leftNeighbor.hasRightConnector()) {
						left = 1;
					}
					else {
						left = 0;
					}
				}
				else {
					left = 0;
				}
				if(topNeighbor != null) {
					if(topNeighbor.hasBottomConnector()) {
						top = 1;
					}
					else {
						top = 0;
					}
				}
				else {
					top = 0;
				}
				ArrayList<PieceType> possiblePieceType = possiblePieceType(p, top, left);
				choosePieceTypeAndOrientation(p, possiblePieceType);
				//System.out.println(p.toString());
			}
		}
	}
	
	/**
	 * this function returns possible PieceType for a piece
	 * this function may return impossible type of pieces like bar and ltype when the piece can have 2 connectors
	 * but we do not know where they are : sometimes they must be opposite (bar) or near (L)
	 * @param p : a piece
	 * @param top : 0 if no connector at top, 1 if connector at top
	 * @param left : 0 if no connector at left, 1 if connector at left
	 * @return a list of possible PieceType
	 */
	public static ArrayList<PieceType> possiblePieceType(Piece p, int top, int left) {
		ArrayList<PieceType> possiblePieceType = new ArrayList<PieceType>();
		if(p.getPosY() == filledGrid.getHeight() - 1 && p.getPosX() == filledGrid.getWidth() - 1) { // coin bas droit
			for(PieceType pt : PieceType.values()) { // choix bonne piece
				if(pt.getNbConnectors() == top + left) {
					possiblePieceType.add(pt);
				}
			}
		}
		else if(p.getPosY() == filledGrid.getHeight() - 1 || p.getPosX() == filledGrid.getWidth() - 1) { // derniere ligne/colonne
			for(PieceType pt : PieceType.values()) { // choix bonnes pieces possibles
				if(pt.getNbConnectors() >= top + left && pt.getNbConnectors() <= 1 + top + left) {
					possiblePieceType.add(pt);
				}
			}
		}
		else { // autres pieces sans contrainte bas et droite
			for(PieceType pt : PieceType.values()) { // choix bonnes pieces possibles
				if(pt.getNbConnectors() >= top + left && pt.getNbConnectors() <= 2 + top + left) {
					possiblePieceType.add(pt);
				}
			}
		}
		return possiblePieceType;
	}
	
	/**
	 * for a piece p of the grid, choose randomly its type and orientation
	 * @param p : a Piece for which we are going to test its type and orientation
	 * @param possiblePieceType : a List of possible types for this piece p
	 */
	public static void choosePieceTypeAndOrientation(Piece p, ArrayList<PieceType> possiblePieceType) {
		if(filledGrid.isCorner(p.getPosY(), p.getPosX()) && possiblePieceType.contains(PieceType.BAR)) {
			possiblePieceType.remove(PieceType.BAR); // si coin on retire BAR car impossible
		}

		ArrayList<Integer> rand = new ArrayList<Integer>();
		for(int i=0 ; i<possiblePieceType.size() ; i++) {
			rand.add(i);
		}
		Collections.shuffle(rand);
		for(int r : rand) { // on va tester aleatoirement les types de pieces
			p.setType(possiblePieceType.get(r));
			//System.out.println(p.getType());
			//System.out.println(p.getType().getIntValue());
			//System.out.println(p.getPossibleOrientations());
			ArrayList<Orientation> possibleOrientation = p.getPossibleOrientations();
			Collections.shuffle(possibleOrientation);
			for(Orientation ori : possibleOrientation) {
				p.setOrientation(ori);
				//System.out.println(p.getConnectors());
				if(isValidOrientationForGridCreation(p.getPosY(), p.getPosX())) {
					break; // si orientation ok on quitte boucle for, sinon on passera a la piece d apres
				}
			}
			if(isValidOrientationForGridCreation(p.getPosY(), p.getPosX())) {
				break; // si orientation ok on quitte boucle for, sinon on passera a la piece d apres
			}
		}
	}
	
	/**
	 * With the 2 params we get the piece and check if it is connected to its neighbors when necessary
	 * @param line : int for number of the line 
	 * @param column : int for the number of the column
	 * @return true if orientation ok, false if not ok
	 */
	public static boolean isValidOrientationForGridCreation(int line, int column) {
		Piece p = filledGrid.getPiece(line, column);
		
		Piece tn = filledGrid.topNeighbor(p);
		Piece ln = filledGrid.leftNeighbor(p);
		Piece rn = filledGrid.rightNeighbor(p);
		Piece bn = filledGrid.bottomNeighbor(p);

		if (p.getType() != PieceType.VOID) {
			if (line == 0) {
				if (column == 0) {
					if (p.hasLeftConnector()) {
						return false;
					}
				} else if (column == filledGrid.getWidth() - 1) {
					if (p.hasRightConnector()) {
						return false;
					}
				}
				if (p.hasTopConnector()) {
					return false;
				}
				if (!p.hasLeftConnector() && ln != null && ln.hasRightConnector()) {
					return false;
				}
				if (p.hasLeftConnector() && ln != null && !ln.hasRightConnector()) {
					return false;
				}

			} else if (line > 0 && line < filledGrid.getHeight() - 1) {
				if (column == 0) {
					if (p.hasLeftConnector()) {
						return false;
					}

				} else if (column == filledGrid.getWidth() - 1) {
					if (p.hasRightConnector()) {
						return false;
					}
				}

				if (!p.hasLeftConnector() && ln != null && ln.hasRightConnector()) {
					return false;
				}
				if (p.hasLeftConnector() && ln != null && !ln.hasRightConnector()) {
					return false;
				}
				if (!p.hasTopConnector() && tn != null && tn.hasBottomConnector()) {
					return false;
				}
				if (p.hasTopConnector() && tn != null && !tn.hasBottomConnector()) {
					return false;
				}

			} else if (line == filledGrid.getHeight() - 1) {
				if (column == 0) {
					if (p.hasLeftConnector()) {
						return false;
					}
				} else if (column == filledGrid.getWidth() - 1) {
					if (p.hasRightConnector()) {
						return false;
					}
				}
				if (p.hasBottomConnector()) {
					return false;
				}
				if (!p.hasLeftConnector() && ln != null && ln.hasRightConnector()) {
					return false;
				}
				if (p.hasLeftConnector() && ln != null && !ln.hasRightConnector()) {
					return false;
				}
				if (!p.hasTopConnector() && tn != null && tn.hasBottomConnector()) {
					return false;
				}
				if (p.hasTopConnector() && tn != null && !tn.hasBottomConnector()) {
					return false;
				}

			}
			if (p.hasLeftConnector() && ln == null) {
				return false;
			}
			if (p.hasTopConnector() && tn == null) {
				return false;
			}
		}

		return true;
	}
	
	/**
	 * generate a solvable grid with generateGrillFiled, randomize the orientations of the pieces and write them in
	 * the file
	 * @param fileName : a name for the output file which contains the Grid
	 * @throws IOException
	 */
	public static void generateLevel(String fileName) throws IOException { 
		
		Generator.generateGrillFilled();
			
		if(Generator.filledGrid.getNbcc()!=-1) {
			//on compte le nombre de nbcc et si ca correspond pas au nbr demand???
			//alors on regenere une grille jusqu'a???ce que ce soit bon 
			
			//plan b : rajouter ou retirer qq connecteurs pour avoir un bon nbcc
			// coder une fonction qui compte le nombre de nbcc !
		}
		
		try {
			FileWriter fw = new FileWriter(new File(fileName));
            fw.write(Generator.filledGrid.getWidth()+ "\n" + Generator.filledGrid.getHeight());
            
            int i, j, rand;
			
			//la grille est sous sa forme r??solue, il faut donc changer les orientations au hasard 
			//on en profite pour recopier les pieces dans le fichier
			for(i = 0; i < Generator.filledGrid.getHeight(); i++) {
				for(j = 0; j < Generator.filledGrid.getWidth(); j++) {
					Piece p = Generator.filledGrid.getPiece(i,j);
					ArrayList<Orientation> possibleOri = p.getPossibleOrientations();
					rand = (int)(Math.random() * possibleOri.size());
					p.setOrientation(possibleOri.get(rand));
					
					fw.write(p.getType().getIntValue()+ "," + p.getOrientation().getCompassDirection() + "\n");
				}
			}
			
			fw.flush();
            fw.close();
        } 
		catch(Exception e){System.out.println(e);} 
	}
	
	
	public static int[] copyGrid(Grid filledGrid, Grid inputGrid, int i, int j) {
		Piece p;
		int hmax = inputGrid.getHeight();
		int wmax = inputGrid.getWidth();

		if (inputGrid.getHeight() != filledGrid.getHeight())
			hmax = filledGrid.getHeight() + i; // we must adjust hmax to have the height of the original grid
		if (inputGrid.getWidth() != filledGrid.getWidth())
			wmax = filledGrid.getWidth() + j;

		int tmpi = 0;// temporary variable to stock the last index
		int tmpj = 0;

		// DEBUG System.out.println("copyGrid : i =" + i + " & j = " + j);
		// DEBUG System.out.println("hmax = " + hmax + " - wmax = " + wmax);
		for (int x = i; x < hmax; x++) {
			for (int y = j; y < wmax; y++) {
				// DEBUG System.out.println("x = " + x + " - y = " + y);
				p = filledGrid.getPiece(x - i, y - j);
				// DEBUG System.out.println("x = " + x + " - y = " +
				// y);System.out.println(p);
				inputGrid.setPiece(x, y, new Piece(x, y, p.getType(), p.getOrientation()));
				// DEBUG System.out.println("x = " + x + " - y = " +
				// y);System.out.println(inputGrid.getPiece(x, y));
				tmpj = y;
			}
			tmpi = x;
		}
		//DEBUGSystem.out.println("tmpi =" + tmpi + " & tmpj = " + tmpj);
		return new int[] { tmpi, tmpj };
	}
}
