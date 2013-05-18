package connect4;

import java.util.ArrayList;

/**
 * Class to find all "in a rows" given a Connect4Board
 * @author Etai Klein & Katie Lachance
 * 
 */
public class NInARow {
	// Instance variables
	// ArrayList to hold all possible n in a rows given a board
	private ArrayList<int[]> totalInARows = new ArrayList<int[]>();
	// Array of individual n in a rows
	private int[] inARow;
	
	/**
	 * Constructs totalInARows
	 * @param int n (number in a row looking for)
	 * @param int [] board (state of the game)
	 */
	public NInARow (int n, int[] board) {
		// Positive diagonal
		for (int r = 0; r <= Connect4Game.ROWS - n; r++) {
			for (int c = 0; c <= Connect4Game.COLS - n; c++) {
				inARow = new int[n];
				for (int i = 0; i < n; i++) {
					inARow[i] = ((r+i) * Connect4Game.COLS) + (c+i);
				}
				totalInARows.add(inARow);
			}
		}
		// Negative diagonals
		for (int r = Connect4Game.ROWS - n + 1; r <= Connect4Game.ROWS - 1; r++) {
			for (int c = 0; c <= Connect4Game.COLS - n; c++) {
				inARow = new int[n];
				for (int i = 0; i < n; i++) {
					inARow[i] = ((r-i) * Connect4Game.COLS) + (c+i);
				}
				totalInARows.add(inARow);
			}
		}
		// Horizontal
		for (int c = 0; c <= Connect4Game.COLS - n; c++) {
			for (int r = 0; r < Connect4Game.ROWS; r++) {
				inARow = new int[n];
				for (int i = 0; i < n; i++) {
					inARow[i] = (r * Connect4Game.COLS) + c + i;
				}
				totalInARows.add(inARow);
			}
		}
		// Vertical
		for (int c = 0; c < Connect4Game.COLS; c++) {
			for (int r = 0; r <= Connect4Game.ROWS - n; r++) {
				inARow = new int[n];
				for (int i = 0; i < n; i++) {
					inARow[i] = ((r+i) * Connect4Game.COLS) + c; 
				}
				totalInARows.add(inARow);
			}
		}
	}
	
	/**
	 * Getter method for totalInARows
	 * @return ArrayList<int[]> totalInARows
	 */
	public ArrayList<int[]> getTotalInARows() {
		return totalInARows;
	}
	
	/**
	 * toString method for testing purposes
	 */
	public String toString() {
		String retString = "";
		for (int[] combo : totalInARows) {
			for (int i : combo) {
				retString += i + " ";
			}
			retString += "\n";
		}
		return retString;
	}
	
	/**
	 * Tests if, given an index and a game state, if a checker can be 
	 * directly placed in that index
	 * @param Connect4State state
	 * @param int index
	 * @return boolean (true if can directly place checker in index)
	 */
	public boolean possibleChecker(Connect4State state, int index) {
		// If index is not in the first row
		// Note that this assumes a crazy index will not be passed
		if (index >= Connect4Game.COLS) {
			// Return if the spot directly below the index is full and the given 
			// spot is empty
			return ((((Connect4Game) state).getBoardArray()[index-Connect4Game.COLS]) > 0) &&
					((((Connect4Game) state).getBoardArray()[index]) == 0);
		}
		// Otherwise, return if the spot is empty (in the first row)
		return (((Connect4Game) state).getBoardArray()[index]) == 0;
	}
} // This brace } ends the NInARow Class
