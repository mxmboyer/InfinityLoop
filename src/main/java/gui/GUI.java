package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.Image;

import components.Piece;

/**
 * This class handles the GUI
 * 
 *
 */
public class GUI extends JPanel{

	private JFrame frame;
	private Image img;
	private int w;
	private int h;

	/**
	 * 
	 * @param inputFile
	 *            String from IO
	 * @throws IOException
	 *             if there is a problem with the gui
	 */
	public static void startGUI(String inputFile) throws NullPointerException {
		// We have to check that the grid is generated before to launch the GUI
		// construction
		Runnable task = new Runnable() {
			public void run() {

				Grid grid = new Grid(0,0);
				grid.generateGridFromFile(inputFile);
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						GUI window;
						window = new GUI(grid);
					}
				});

			}
		};
		new Thread(task).start();

	}

	/**
	 * Create the application.
	 * 
	 * @param grid : grille à afficher
	 * @throws IOException
	 */
	public GUI(Grid grid) {
		this.frame = new JFrame("Infinity Loops");
		initialize(grid);
	}
	
	@Override
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 g.drawImage(img, w, h, this);
	 }

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @param grid
	 */
	private void initialize(Grid grid) {
		
		// To implement:
		// creating frame, labels
		// Implementing method mouse clicked of interface MouseListener.
		
		Piece p = null;
		// 1 case : 70 x 70
		frame.setSize(grid.getWidth()*70,grid.getHeight()*70);
		for(int i=0 ; i<grid.getHeight() ; i++) {
			for(int j=0 ; j<grid.getWidth() ; j++) {
				p = grid.getPiece(i, j);
		        try {
					this.img = getImageIcon(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
		        this.w = j*70;
		        this.h = i*70;
				frame.add(this);
			}
		}
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Display the correct image from the piece's type and orientation
	 * 
	 * @param p
	 *            the piece
	 * @return an image icon
	 * @throws IOException 
	 */
	private Image getImageIcon(Piece p) throws IOException {
		//ImageIO.read donne une erreur de compilation : problème dans la biblioteque
		//https://bugs.openjdk.java.net/browse/JDK-6945174
		switch(p.getType().getIntValue()){
			case 1:
				switch(p.getOrientation().getCompassDirection()) {
					case 0: return ImageIO.read(new File("src/main/resources/icons/io/v1.png"));
					case 1: return ImageIO.read(new File("src/main/resources/icons/io/2.png"));
					case 2: return ImageIO.read(new File("src/main/resources/icons/io/3.png"));
					case 3: return ImageIO.read(new File("src/main/resources/icons/io/4.png"));
				}
				break;	
			case 2:
				switch(p.getOrientation().getCompassDirection()) {
				case 0: return ImageIO.read(new File("src/main/resources/icons/io/5.png"));
				case 1: return ImageIO.read(new File("src/main/resources/icons/io/6.png"));
				}
				break;	
			case 3:
				switch(p.getOrientation().getCompassDirection()) {
				case 0: return ImageIO.read(new File("src/main/resources/icons/io/7.png"));
				case 1: return ImageIO.read(new File("src/main/resources/icons/io/8.png"));
				case 2: return ImageIO.read(new File("src/main/resources/icons/io/9.png"));
				case 3: return ImageIO.read(new File("src/main/resources/icons/io/10.png"));
				}
				break;	
			case 4:
				return ImageIO.read(new File("src/main/resources/icons/io/11.png"));
			case 5:
				switch(p.getOrientation().getCompassDirection()) {
				case 0: return ImageIO.read(new File("src/main/resources/icons/io/12.png"));
				case 1: return ImageIO.read(new File("src/main/resources/icons/io/13.png"));
				case 2: return ImageIO.read(new File("src/main/resources/icons/io/14.png"));
				case 3: return ImageIO.read(new File("src/main/resources/icons/io/15.png"));
				}
				break;	
		}
		return ImageIO.read(new File("src/main/resources/icons/io/empty.png"));
	}
	
	/*public static void main(String[] args) {
		GUI.startGUI("test_solver.txt");
	}*/
}
