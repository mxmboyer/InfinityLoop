package solve;

import java.io.File;
import java.io.FileWriter;
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
		//parametres du prof -> pas compris inputGrid
	}
	
	public static void generateLevel(String fileName) { 
		//attention pour l'instant on ne prend pas en compte c
		//et rentrer tout ça dans un fichier
		int i, j, rand;
		for(i = 0; i < Generator.filledGrid.getHeight(); i++) {
			for(j = 0; j < Generator.filledGrid.getWidth(); j++) {
				Piece p = new Piece(i,j);
				List<Integer> possiblePiece = new ArrayList<Integer>(){{add(0);add(1);add(5);}};
				List<Integer> contrainteConnPos = new ArrayList<Integer>();//mettre un conn pour chacune de ces orientations
				List<Integer> contrainteConnNeg = new ArrayList<Integer>(); //ne pas mettre de conn pour cette orientation
				List<Orientation> possibleOri = new ArrayList<Orientation>();
				
				if(Generator.filledGrid.isCorner(j,i)) {
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
				}
				
				else if(Generator.filledGrid.isBorderLine(j,i) || Generator.filledGrid.isBorderColumn(j,i)) {
					possiblePiece.add(2);
					possiblePiece.add(3);
					if(i==0) contrainteConnNeg.add(0);
					else if(i!=0) contrainteConnNeg.add(2);
					else if(j==0) contrainteConnNeg.add(3);
					else contrainteConnNeg.add(1);
				}
				
				else {
					possiblePiece.add(4);
				}
				
				ArrayList<Piece> voisins = Generator.filledGrid.listOfNeighbours(p);
				if(voisins.size()!=0) { //si notre piece a des voisins 
					Piece leftNeighbor = Generator.filledGrid.leftNeighbor(p);
					Piece topNeighbor = Generator.filledGrid.topNeighbor(p);
					// si la piece a droite a un connecteur a gauche (donc doit se connecter a nous)
					// alors on doit forcement se connecter a droite et on rajoute cette orientation dans les contraintes
					// nb :on avance de gauche à droite et de haut en bas, pas encore de voisins de droite et de bas
					if(leftNeighbor!=null && leftNeighbor.hasRightConnector()) contrainteConnPos.add(3);
					if(topNeighbor!=null && topNeighbor.hasBottomConnector()) contrainteConnPos.add(0);
					else possiblePiece.remove(0);
				}
				
				//on choisit le type de piece parmis ceux possible
				rand = (int)(Math.random() * possiblePiece.size());
				int type = possiblePiece.get(rand);
				if(contrainteConnPos.size()!=0) {
					while(contrainteConnPos.size()!=PieceType.values()[type].getNbConnectors()) {
						possiblePiece.remove(type);
						rand = (int)(Math.random() * possiblePiece.size());
						type = possiblePiece.get(rand);
					}
				}
				p.setType(PieceType.values()[type]);
				
				if(Generator.filledGrid.isCorner(j,i) || Generator.filledGrid.isBorderLine(j,i) || Generator.filledGrid.isBorderColumn(j,i)) {
					//on liste les orientations ou il n'y a pas de prbl avec les bordures
					for(Orientation ori : p.getPossibleOrientations()){ 
						LinkedList<Orientation> connecteurs = p.getType().setConnectorsList(ori);
						for(Orientation orientation: connecteurs) {
							if(!contrainteConnNeg.contains(orientation)) {
								possibleOri.add(ori);
							}
						}
					}
				}
				else {
					for(Orientation ori : p.getPossibleOrientations()){ 
						possibleOri.add(ori);
					}
				}
				
				//on prend une orientation parmis celles possible
				rand = (int)(Math.random() * possibleOri.size());
				Orientation orientation = possibleOri.get(rand);
				// si on a des voisins, on verifie que l'orientation choisie permet de faire de se connecter 
				// a tous les voisins sont c'est necessaire :
				if(contrainteConnPos.size()!=0) {
					LinkedList<Orientation> connecteurs = p.getType().setConnectorsList(orientation);
					while(!connecteurs.containsAll(contrainteConnPos)) {
						possibleOri.remove(orientation);
						rand = (int)(Math.random() * possibleOri.size());
						orientation = possibleOri.get(rand);
					}
				}
				p.setOrientation(orientation);
				
				Generator.filledGrid.setPiece(j, i, p);
				
				try (FileWriter fw = new FileWriter(new File(fileName))) {
		 
		            fw.write("humpty dumpty");
		        } 
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
