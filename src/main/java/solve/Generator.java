package solve;

import java.util.ArrayList;
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

	/**_
	 * @param output
	 *            file name
	 * @throws IOException
	 *             - if an I/O error occurs.
	 * @return a File that contains a grid filled with pieces (a level)
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	
	public Generator(int w, int h, int c) {
		Generator.filledGrid = new Grid(w, h, c);
	}
	
	public static void generateLevel(String fileName, Grid inputGrid) {
		
	}
	
	public static void generateLevel(String fileName) { //attention pour l'instant on ne prend pas en compte c
		int i, j, rand;
		for(i = 0; i < Generator.filledGrid.getHeight(); i++) {
			for(j = 0; j < Generator.filledGrid.getWidth(); j++) {
				Piece p = new Piece(i,j);
				if(Generator.filledGrid.isCorner(j,i)) { // cas d'un coin
					List<Integer> possiblePieceCorner = new ArrayList<Integer>(){{add(0);add(1);add(5);}};
					List<Integer> contrainteConnNeg = new ArrayList<Integer>(); //ne pas mettre de conn pour cette orientation
					List<Integer> contrainteConnPos = new ArrayList<Integer>();//mettre un conn pour chacune de ces orientations
					List<Orientation> possibleOri = new ArrayList<Orientation>();
					//on liste les orientations sur lesquelles ne pas mettre de connecteur :
					if(j==0) {
						contrainteConnNeg.add(3);
						if(i==0) contrainteConnNeg.add(0);
						else contrainteConnNeg.add(2);
					}
					else {
						contrainteConnNeg.add(1);
						if(i==0) contrainteConnNeg.add(0);
						else contrainteConnNeg.add(2);
					}
					ArrayList<Piece> voisins = Generator.filledGrid.listOfNeighbours(p);
					if(voisins.size()!=0) { //si notre piece a des voisins 
						Piece rightNeighbor = Generator.filledGrid.rightNeighbor(p);
						Piece leftNeighbor = Generator.filledGrid.leftNeighbor(p);
						Piece topNeighbor = Generator.filledGrid.topNeighbor(p);
						Piece bottomNeighbor = Generator.filledGrid.bottomNeighbor(p);
						// si la piece a droite a un connecteur a gauche (donc doit se connecter a nous)
						// alors on doit forcement se connecter a droite et on rajoute cette orientation dans les contraintes
						// nb : à vérifier mais vu que l'on avance de gauche à droite et de haut en bas, cela ne sert surement à rien 
						// de verifier les voisins de droite et du bas car il n'y en aura pas encore
						if(rightNeighbor!=null && rightNeighbor.hasLeftConnector()) contrainteConnPos.add(1);
						if(leftNeighbor!=null && leftNeighbor.hasRightConnector()) contrainteConnPos.add(3);
						if(topNeighbor!=null && topNeighbor.hasBottomConnector()) contrainteConnPos.add(0);
						if(bottomNeighbor!=null && bottomNeighbor.hasTopConnector()) contrainteConnPos.add(2);
						if(contrainteConnPos.size()==2) p.setType(PieceType.values()[5]);
						else { //on choisit le type de piece parmis ceux possible
							possiblePieceCorner.remove(0);
							rand = (int)(Math.random() * 2);
							p.setType(PieceType.values()[possiblePieceCorner.get(rand)]);
						}
					}
					else {
						rand = (int)(Math.random() * 3);
						p.setType(PieceType.values()[possiblePieceCorner.get(rand)]);
					}
					//on choisit l'orientation de notre piece en fonction de là où on ne doit pas mettre de connecteurs
					for(Orientation ori : p.getPossibleOrientations()){ 
						LinkedList<Orientation> connecteurs = p.getType().setConnectorsList(ori);
						for(Orientation orientation: connecteurs) {
							if(!contrainteConnNeg.contains(orientation)) {
								possibleOri.add(orientation);
							}
						}
					}
					//on prend une orientation parmis celles possible
					rand = (int)(Math.random() * possibleOri.size());
					Orientation orientation = possibleOri.get(rand);
					// si on a des voisins, on verifie que l'orientation choisie permet de faire de se connecter 
					// a tous les voisins sont c'est necessaire :
					if(contrainteConnPos.size()!=0) {
						boolean valide = false;
						LinkedList<Orientation> connecteurs = p.getType().setConnectorsList(orientation);
						if(connecteurs.containsAll(contrainteConnPos)) valide = true;
						while(valide==false) {
							possibleOri.remove(orientation);
							rand = (int)(Math.random() * possibleOri.size());
							orientation = possibleOri.get(rand);
						}
					}
					p.setOrientation(orientation);
				}
				else if(Generator.filledGrid.isBorderLine(j,i)) {
					List<Integer> possiblePieceBorder = new ArrayList<Integer>(){{add(0);add(1);add(2);add(3);add(5);}};
				}
				else if(Generator.filledGrid.isBorderColumn(j,i)) {
					List<Integer> possiblePieceBorder = new ArrayList<Integer>(){{add(0);add(1);add(2);add(3);add(5);}};
				}
				else {
					List<Integer> possiblePiece = new ArrayList<Integer>(){{add(0);add(1);add(2);add(3);add(4);add(5);}};
				}
				Generator.filledGrid.setPiece(j, i, p);
			}
		}
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
