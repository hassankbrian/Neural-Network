/*
 * Useful little grid object. Its size is customizable so that's pretty sweet.
 * 
 * I would use this again if I were to need a drawing board for swing, since you
 * you can basically emulate paint or any image editor from this. Here we use it
 * to draw the symbols we want to represent.
 *  
 *  
 *  
 * @author Brian Hassan 
 * @version 1.0 
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class Grid extends JPanel{

	private Tile[][] tiles;
	private int rows;
	private int cols;

	public Grid(int size, int rows, int cols) {
		super(new GridLayout(rows, cols),true);
				
		setPreferredSize(new Dimension(size,size));
		
		this.rows = rows;
		this.cols = cols;
		tiles = new Tile[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tiles[i][j] = new Tile();
				add(tiles[i][j]);
			}
		}
	}
	
	public Grid(Grid grid) {
		super(new GridLayout(grid.rows, grid.cols),true);
		
		setPreferredSize(grid.getSize());
		
		this.rows = grid.rows;
		this.cols = grid.cols;
		tiles = new Tile[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				tiles[i][j] = new Tile(); 
				if (grid.tiles[i][j].pressed) {
					tiles[i][j].toggleButton();
				}
				add(tiles[i][j]);
			}
		}
	}
	
	public void clear() {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (tiles[i][j].pressed) {
					tiles[i][j].toggleButton();
				}
			}
		}
	}
	
	public void update(Grid grid) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (tiles[i][j].pressed != grid.tiles[i][j].pressed) {
					tiles[i][j].toggleButton();
				}
			}
		}	
	}
	
	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
	public boolean isPressedAt(int row, int col) {
		return tiles[row][col].pressed;
	}
	
	private boolean clicking;
	private boolean overwriting;

	private class Tile extends JPanel implements MouseListener{

		private boolean pressed;
		
		private Color pressedColor = Color.GREEN;
		private Color releasedColor = Color.BLACK;
		private Color borderColor = Color.WHITE;
		private int borderThickness = 1;
		
		public Tile() {
			setBackground(pressed ? pressedColor:releasedColor);
			setBorder(new LineBorder(borderColor,borderThickness));
			addMouseListener(this);
		}
		
		public void toggleButton() {
			pressed = !pressed;
			setBackground(pressed ? pressedColor:releasedColor);
		}

		public void mouseClicked(MouseEvent e) {}

		public void mouseEntered(MouseEvent e) {
			if (clicking) {
				if (overwriting) {
					if (pressed) {
						toggleButton();
					}
				} else {
					if (!pressed) {
						toggleButton();
					}
				}
			}
		}

		public void mouseExited(MouseEvent e) {}

		public void mousePressed(MouseEvent e) {
			clicking = true;
			overwriting = pressed ? true:false;
			toggleButton();
		}

		public void mouseReleased(MouseEvent e) {
			clicking = false;
		}
		
	}
}
