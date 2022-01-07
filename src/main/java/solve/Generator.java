package solve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public static void generateGrillFilled() {
		int i, j, rand;
		for(i = 0; i < Generator.filledGrid.getHeight(); i++) {
			for(j = 0; j < Generator.filledGrid.getWidth(); j++) {
				Piece p = new Piece(i,j);
				List<Integer> possiblePiece = new ArrayList<Integer>(){{add(0);add(1);add(5);}};
				List<Orientation> contrainteConnPos = new ArrayList<Orientation>();//mettre un conn pour chacune de ces orientations
				List<Integer> contrainteConnNeg = new ArrayList<Integer>(); //ne pas mettre de conn pour cette orientation
				//rajouter dans les contraintes neg le cas d'un voisin null
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
					possiblePiece.add(2);
					possiblePiece.add(3);
					possiblePiece.add(4);
				}
				
				
				Piece leftNeighbor = Generator.filledGrid.leftNeighbor(p);
				Piece topNeighbor = Generator.filledGrid.topNeighbor(p);
				// si la piece a droite a un connecteur a gauche (donc doit se connecter a nous)
				// alors on doit forcement se connecter a droite et on rajoute cette orientation dans les contraintes
				// nb :on avance de gauche � droite et de haut en bas, pas encore de voisins de droite et de bas
				if(leftNeighbor!=null && leftNeighbor.hasRightConnector()) {
					contrainteConnPos.add(Orientation.getOrifromValue(3));
					possiblePiece.remove(0);
				}
				else {
					contrainteConnNeg.add(3);
				}
				if(topNeighbor!=null && topNeighbor.hasBottomConnector()) {
					contrainteConnPos.add(Orientation.getOrifromValue(0));
					possiblePiece.remove(0);
				}
				else {
					contrainteConnNeg.add(0);
				}
				
				if(contrainteConnPos.size()!=0) {
					for(int t : possiblePiece) {
						if(contrainteConnPos.size()>PieceType.values()[t].getNbConnectors()) {
							possiblePiece.remove(t);
						}
					}
				}
				
				//on choisit le type de piece parmis ceux possible
				if(possiblePiece.size()==0) System.out.println("error : no possible piece"); // DEBUG
				rand = (int)(Math.random() * possiblePiece.size());
				p.setType(PieceType.values()[possiblePiece.get(rand)]);
				
				boolean check;
				//on liste les orientations ou il n'y a pas de prbl avec les contraintes negatives
				for(Orientation ori : p.getPossibleOrientations()){ 
					LinkedList<Orientation> connecteurs = p.getType().setConnectorsList(ori);
					check = true;
					for(Orientation orientation: connecteurs) {
						if(contrainteConnNeg.contains(orientation.getCompassDirection())) {
							check = false;
						}
					}
					if(check) possibleOri.add(ori);
				}
				
				
				if(contrainteConnPos.size()!=0) {
					for(Orientation ori : possibleOri) {
						LinkedList<Orientation> connecteurs = p.getType().setConnectorsList(ori);
						if(!connecteurs.containsAll(contrainteConnPos)) {
							possibleOri.remove(ori);
						}
					}
				}
				
				if(possibleOri.size() == 0) System.out.println("error : no possible ori"); //DEBUG
				rand = (int)(Math.random() * possibleOri.size());
				p.setOrientation(possibleOri.get(rand));
				
				Generator.filledGrid.setPiece(j, i, p);
			}
		}
	}
	
	public static void generateLevel(String fileName) throws IOException { 
		
		Generator.generateGrillFilled();
			
		if(Generator.filledGrid.getNbcc()!=-1) {
			//on compte le nombre de nbcc et si ça correspond pas au nbr demandé
			//alors on regénère une grille jusqu'à ce que ce soit bon 
			
			//plan b : rajouter ou retirer qq connecteurs pour avoir un bon nbcc
			// coder une fonction qui compte le nombre de nbcc !
			
		}
		
		try {
			FileWriter fw = new FileWriter(new File(fileName));
            fw.write(Generator.filledGrid.getWidth()+ "\n" + Generator.filledGrid.getHeight());
            
            int i, j, rand;
			
			//la grille est sous sa forme résolue, il faut donc changer les orientations au hasard 
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
