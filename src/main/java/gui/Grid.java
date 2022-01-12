package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import components.Orientation;
import components.Piece;
import components.PieceType;
import solve.Generator;

/**
 * Grid handler and peces'functions which depends of the grid
 * 
 *
 */
public class Grid {
	private int width; // j
	private int height; // i
	private int nbcc = -1;
	private Piece[][] pieces;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.pieces = new Piece[height][width];
	}

	// Constructor with specified number of connected component
	public Grid(int width, int height, int nbcc) {
		this.width = width;
		this.height = height;
		this.nbcc = nbcc;
		this.pieces = new Piece[height][width];
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
		this.pieces = new Piece[this.height][this.width];
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
		this.pieces = new Piece[this.height][this.width];
	}

	public Integer getNbcc() {
		return nbcc;
	}

	public void setNbcc(int nbcc) {
		this.nbcc = nbcc;
	}

	public Piece getPiece(int line, int column) {
		return this.pieces[line][column];
	}

	public void setPiece(int line, int column, Piece piece) {
		this.pieces[line][column] = piece;
	}
	
	public void setAllPieces(Piece[][] p) {
		this.pieces = p;
	}

	public Piece[][] getAllPieces() {
		return pieces;
	}

	/**
	 * Check if a case is a corner
	 * 
	 * @param line
	 * @param column
	 * @return true if the case is a corner
	 */
	public boolean isCorner(int line, int column) {
		if (line == 0) {
			if (column == 0)
				return true;
			if (column == this.getWidth() - 1)
				return true;
			return false;
		} else if (line == this.getHeight() - 1) {
			if (column == 0)
				return true;
			if (column == this.getWidth() - 1)
				return true;
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Check if a case is member of the first or the last line
	 * 
	 * @param line
	 * @param column
	 * @return true if the case is a borderLine
	 */
	public boolean isBorderLine(int line, int column) {
		if (line == 0 && column > 0 && column < this.getWidth() - 1) {
			return true;
		} else if (line == this.getHeight() - 1 && column > 0 && column < this.getWidth() - 1) {
			return true;
		}
		return false;

	}

	/**
	 * Check if a case is member of the first or the last column
	 * 
	 * @param line
	 * @param column
	 * @return true if the case is a borderColumn
	 */
	public boolean isBorderColumn(int line, int column) {
		if (column == 0 && line > 0 && line < this.getHeight() - 1) {
			return true;
		} else if (column == this.getWidth() - 1 && line > 0 && line < this.getHeight() - 1) {
			return true;
		}
		return false;

	}

	/**
	 * Check if a piece has a neighbour for its connectors for one orientation
	 * 
	 * @param p
	 *            piece
	 * @return true if there is a neighbour for all connectors
	 */
	public boolean hasNeighbour(Piece p) {
		for (Orientation ori : p.getConnectors()) {
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
			try {
				if (this.getPiece(oppPieceY, oppPieceX).getType() == PieceType.VOID) {
					return false;
				}

			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Check if a piece has a fixed neighbor for each one of its connectors
	 * 
	 * @param p
	 *            the piece
	 * @return true if there is a fixed piece for each connector
	 */
	public boolean hasFixedNeighbour(Piece p) {
		boolean bool = false;
		for (Orientation ori : p.getConnectors()) {
			bool = false;
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
			try {
				Piece neigh = this.getPiece(oppPieceY, oppPieceX);
				if (neigh.getType() == PieceType.VOID || !neigh.isFixed()) {
					return false;
				}
				if (neigh.isFixed()) {
					for (Orientation oriOppPiece : neigh.getConnectors()) {
						if (ori == oriOppPiece.getOpposedOrientation()) {
							bool = true;
						}
					}
					if (!bool) {
						return false;
					}

				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return bool;
	}

	/**
	 * Check if a piece has a at least one fixed neighbor
	 * 
	 * @param p
	 *            the piece
	 * @return true if there is a fixed piece for each connector
	 */
	public boolean hasAtLeast1FixedNeighbour(Piece p) {
		for (Orientation ori : p.getConnectors()) {
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
			try {
				Piece neigh = this.getPiece(oppPieceY, oppPieceX);
				if (neigh.isFixed()) {
					for (Orientation oriOppPiece : neigh.getConnectors()) {
						if (ori == oriOppPiece.getOpposedOrientation()) {
							return true;
						}
					}
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				return false;
			}
		}
		return false;
	}

	/**
	 * list of neighbors
	 * 
	 * @param p
	 *            the piece
	 * @return the list of neighbors
	 */
	public ArrayList<Piece> listOfNeighbours(Piece p) {
		ArrayList<Piece> lp = new ArrayList<Piece>();
		for (Orientation ori : p.getConnectors()) {
			int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
			int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j

			if (oppPieceY >= 0 && oppPieceY < this.getHeight() && oppPieceX >= 0 && oppPieceX < this.width) {
				if (this.getPiece(oppPieceY, oppPieceX).getType() != PieceType.VOID) {
					lp.add(this.getPiece(oppPieceY, oppPieceX));
				}
			}

		}
		return lp;
	}

	/**
	 * this function returns the number of neighbors
	 * 
	 * @param p
	 * @return the number of neighbors
	 */
	public int numberOfNeibours(Piece p) {
		int X = p.getPosX();
		int Y = p.getPosY();
		int count = 0;
		if (Y < this.getHeight() - 1 && getPiece(Y + 1, X).getType() != PieceType.VOID)
			count++;
		if (X < this.getWidth() - 1 && getPiece(Y, X + 1).getType() != PieceType.VOID)
			count++;
		if (Y > 0 && getPiece(Y - 1, X).getType() != PieceType.VOID)
			count++;
		if (X > 0 && getPiece(Y, X - 1).getType() != PieceType.VOID)
			count++;
		return count;
	}

	/**
	 * this function returns the number of fixed neighbors
	 * 
	 * @param p
	 * @return the number of neighbors
	 */
	public int numberOfFixedNeibours(Piece p) {
		int X = p.getPosX();
		int Y = p.getPosY();
		int count = 0;

		if (Y < this.getHeight() - 1 && getPiece(Y + 1, X).getType() != PieceType.VOID && getPiece(Y + 1, X).isFixed())
			count++;
		if (X < this.getWidth() - 1 && getPiece(Y, X + 1).getType() != PieceType.VOID && getPiece(Y, X + 1).isFixed())
			count++;
		if (Y > 0 && getPiece(Y - 1, X).getType() != PieceType.VOID && getPiece(Y - 1, X).isFixed())
			count++;
		if (X > 0 && getPiece(Y, X - 1).getType() != PieceType.VOID && getPiece(Y, X - 1).isFixed())
			count++;
		return count;
	}

	/**
	 * Check if all pieces have neighbors even if we don't know the orientation
	 * 
	 * @param p
	 * @return false if a piece has no neighbor
	 */
	public boolean allPieceHaveNeighbour() {

		for (Piece[] ligne : this.getAllPieces()) {
			for (Piece p : ligne) {

				if (p.getType() != PieceType.VOID) {
					if (p.getType().getNbConnectors() > numberOfNeibours(p)) {
						return false;
					}
				}

			}
		}
		return true;

	}

	/**
	 * Return the next piece of the current piece
	 * 
	 * @param p
	 *            the current piece
	 * @return the piece or null if p is the last piece
	 */
	public Piece getNextPiece(Piece p) {
		int i = p.getPosY();
		int j = p.getPosX();
		if (j < this.getWidth() - 1) {
			p = this.getPiece(i, j + 1);
		} else {
			if (i < this.getHeight() - 1) {
				p = this.getPiece(i + 1, 0);
			} else {
				return null;
			}

		}
		return p;
	}
	
	/**
	 * Return the next piece of the current piece right2left and bottom2top
	 * 
	 * @param p
	 *            the current piece
	 * @return the piece or null if p is the last piece
	 */
	public Piece getNextPieceInv(Piece p) {

		int i = p.getPosY();
		int j = p.getPosX();
		if (j > 0) {
			p = this.getPiece(i, j - 1);
		} else {
			if (i > 0) {
				p = this.getPiece(i - 1, this.getWidth()-1);
			} else {
				return null;
			}

		}

		return p;

	}

	/**
	 * Check if a piece is connected
	 * 
	 * @param line
	 * @param column
	 * @return true if a connector of a piece is connected
	 */
	public boolean isConnected(Piece p, Orientation ori) {
		int oppPieceY = ori.getOpposedPieceCoordinates(p)[0];// i
		int oppPieceX = ori.getOpposedPieceCoordinates(p)[1];// j
		if (p.getType() == PieceType.VOID)
			return true;
		try {
			for (Orientation oppConnector : this.getPiece(oppPieceY, oppPieceX).getConnectors()) {
				if (oppConnector == ori.getOpposedOrientation()) {
					return true;
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return false;
	}

	/**
	 * Check if a piece is totally connected
	 * 
	 * @param line
	 * @param column
	 * @return true if a piece est connectee a� chacun de ses connecteurs
	 */
	public boolean isTotallyConnected(Piece p) {
		if (p.getType() != PieceType.VOID) {
			for (Orientation connector : p.getConnectors()) {
				if (!this.isConnected(p, connector)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Check if a piece position is valid
	 * 
	 * @param line
	 * @param column
	 * @return true if a connector of a piece is connected
	 */
	public boolean isValidOrientation(int line, int column) {

		Piece tn = this.topNeighbor(this.getPiece(line, column));
		Piece ln = this.leftNeighbor(this.getPiece(line, column));
		Piece rn = this.rightNeighbor(this.getPiece(line, column));
		Piece bn = this.bottomNeighbor(this.getPiece(line, column));

		if (this.getPiece(line, column).getType() != PieceType.VOID) {
			if (line == 0) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector()) {
						//System.out.println("coin haut gauche : conn gauche");
						return false;
					}
				} 
				else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector()) {
						//System.out.println("coin haut droit : conn droit");
						return false;
					}
				}
				if (this.getPiece(line, column).hasTopConnector()) {
					//System.out.println("piece haut : conn haut");
					return false;
				}
				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector()) {
					//System.out.println("piece haut : pas conn droit & voisin droite : conn gauche");
					return false;
				}
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector()) {
					//System.out.println("piece haut : conn droit & voisin droite : pas conn gauche");
					return false;
				}
				if (!this.getPiece(line, column).hasBottomConnector() && bn != null && bn.hasTopConnector()) {
					//System.out.println("piece haut : pas conn bas & voisin bas : conn haut");
					return false;
				}
				if (this.getPiece(line, column).hasBottomConnector() && bn != null && !bn.hasTopConnector()) {
					//System.out.println("piece haut : conn bas & voisin bas : pas conn haut");
					return false;
				}

			} 
			else if (line > 0 && line < this.getHeight() - 1) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector()) {
						//System.out.println("piece gauche : conn gauche");
						return false;
					}

				} 
				else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector()) {
						//System.out.println("piece droite : conn droit");
						return false;
					}
				}
				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector()) {
					//System.out.println("piece : pas conn droit & voisin droit : conn gauche");
					return false;
				}
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector()) {
					//System.out.println("piece : conn droit & voisin droit : pas conn gauche");
					return false;
				}
				if (!this.getPiece(line, column).hasBottomConnector() && bn != null && bn.hasTopConnector()) {
					//System.out.println("piece : pas conn bas & voisin bas : conn haut");
					return false;
				}
				if (this.getPiece(line, column).hasBottomConnector() && bn != null && !bn.hasTopConnector()) {
					//System.out.println("piece : conn bas & voisin bas : pas conn haut");
					return false;
				}

			} 
			else if (line == this.getHeight() - 1) {
				if (column == 0) {
					if (this.getPiece(line, column).hasLeftConnector()) {
						//System.out.println("coin bas gauche : conn gauche");
						return false;
					}
				} 
				else if (column == this.getWidth() - 1) {
					if (this.getPiece(line, column).hasRightConnector()) {
						//System.out.println("coin bas droit : conn droit");
						return false;
					}
				}
				if (this.getPiece(line, column).hasBottomConnector()) {
					//System.out.println("piece bas : conn bas");
					return false;
				}
				if (!this.getPiece(line, column).hasRightConnector() && rn != null && rn.hasLeftConnector()) {
					//System.out.println("piece bas : pas conn droit & voisin droit : conn gauche");
					return false;
				}
				if (this.getPiece(line, column).hasRightConnector() && rn != null && !rn.hasLeftConnector()) {
					//System.out.println("piece bas : conn droit & voisin droit : pas conn gauche");
					return false;
				}

			}
			if (this.getPiece(line, column).hasLeftConnector() && ln == null) {
				//System.out.println("voisin gauche == null");
				return false;
			}
			if (this.getPiece(line, column).hasTopConnector() && tn == null) {
				//System.out.println("voisin haut == null");
				return false;
			}
			if (this.getPiece(line, column).hasRightConnector() && rn == null) {
				//System.out.println("voisin droit == null");
				return false;
			}
			if (this.getPiece(line, column).hasBottomConnector() && bn == null) {
				//System.out.println("voisin bas == null");
				return false;
			}
		}
		return true;
	}

	/**
	 * Find the left neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece leftNeighbor(Piece p) {

		if (p.getPosX() > 0) {
			if (this.getPiece(p.getPosY(), p.getPosX() - 1).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY(), p.getPosX() - 1);
			}
		}
		return null;
	}

	/**
	 * Find the top neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece topNeighbor(Piece p) {

		if (p.getPosY() > 0) {
			if (this.getPiece(p.getPosY() - 1, p.getPosX()).getType() != PieceType.VOID) {
				return this.getPiece(p.getPosY() - 1, p.getPosX());
			}
		}
		return null;
	}

	/**
	 * Find the right neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece rightNeighbor(Piece p) {

		if (p.getPosX() < this.getWidth() - 1) {
			Piece piece = this.getPiece(p.getPosY(), p.getPosX() + 1);
			if (piece != null) {
				if(piece.getType() != PieceType.VOID) {
					return this.getPiece(p.getPosY(), p.getPosX() + 1);
				}
			}
		}
		return null;
	}

	/**
	 * Find the bottom neighbor
	 * 
	 * @param p
	 * @return the neighbor or null if no neighbor
	 */
	public Piece bottomNeighbor(Piece p) {

		if (p.getPosY() < this.getHeight() - 1) {
			Piece piece = this.getPiece(p.getPosY() + 1, p.getPosX());
			if(piece != null) {
				if (piece.getType() != PieceType.VOID) {
					return this.getPiece(p.getPosY() + 1, p.getPosX());
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {

		String s = "";
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				s += DisplayUnicode.getUnicodeOfPiece(pieces[i][j].getType(), pieces[i][j].getOrientation());
			}
			s += "\n";
		}
		return s;
	}
	
	public void generateGridFromFile(String fileName) {
		FileReader fr = null;
		try {
			fr = new FileReader(fileName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		
		String width = null;
		String height = null;
		try {
			width = br.readLine();
			height = br.readLine();
			
			this.setHeight(Integer.valueOf(height));
			this.setWidth(Integer.valueOf(width));
			
			String line;
			
			for(int h = 0 ; h < this.getHeight() ; h++) {
				for(int w = 0 ; w < this.getWidth() ; w++) {
					line = br.readLine();
					Piece p = new Piece(h, w);
					p.setType(PieceType.values()[Integer.valueOf(String.valueOf(line.charAt(0)))]);
					p.setOrientation(Integer.valueOf(String.valueOf(line.charAt(2))));
					this.pieces[h][w] = p;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void GenerateFileFromGrid(String fileName) {
		try {
			FileWriter fw = new FileWriter(new File(fileName));
            fw.write(this.width + "\n" + this.height + "\n");
            
            int i, j;
			for(i = 0; i < this.height; i++) {
				for(j = 0; j < this.width; j++) {
					Piece p = this.pieces[i][j];
					fw.write(p.getType().getIntValue()+ "," + p.getOrientation().getCompassDirection() + "\n");
				}
			}
			
			fw.flush();
            fw.close();
        } 
		catch(Exception e){System.out.println(e);} 
	}
}
