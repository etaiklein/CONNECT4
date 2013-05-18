package connect4;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A text implementation of the "view" part of a model-view-controller for Connect4
 * 
 * 
 * @author Etai Klein & Katie Lachance
 *
 */
public class Connect4ViewText implements Connect4View {
	// Takes input from user
	private Scanner input;
	
	/**
	 * Constructs text view
	 */
	public Connect4ViewText () {
		// Set up input to take input from console
		input = new Scanner (System.in);
	}
	
	 /**  
   * Displays the current board
   * @param state current state of the game
   */
	public void display(Connect4State state) {
		// Print the character array representation of the game board
		char [][] board = state.getBoard();
		// Iterate through the rows backwards (0 is at bottom) and columns forwards (0 is left)
		for (int r = Connect4State.ROWS-1 ; r >= 0 ; r--) {
			for (int c = 0 ; c < Connect4State.COLS ; c++) {
				// Print the character and a space between each checker in each row
				System.out.print (board[r][c] + "    ");
			}
			// Enter between each row
			System.out.println();
		}
		// Print out column numbers to make play easier
		for (int i = 0; i < Connect4Game.COLS; i++) {
			System.out.print (i + "    ");
		}
		// Enter a row
		System.out.println();
	}

	/**
	 * Asks the user for a move
	 * The move will be in the range 0 to Connect4State.COLS-1.
	 * @param state current state of the game
	 * @return the number of the move that player chose
	 */
	public int getUserMove(Connect4State state) {
		// Keeps track of column current player wants to drop a checker in
		int col;
		// Format
		System.out.println();
		// Get the column to drop the checker in
		col = this.getIntAnswer("Column to drop in (indexed from 0), " 
				+ state.getPlayerToMove().getName() + "?");
		// If this is not a valid column to drop a checker in
		while (!state.isValidMove(col)) {
			// Say so and ask again
			System.out.println("Illegal move. Try again.");
			col = this.getIntAnswer("Column to drop in?");
		}
		// Return the column number (indexed from 0) as the move
		return col;
	}

	/**
	 * Reports the move that a player has made.
	 * The move should be in the range 0 to Connect4State.COLS-1.
	 * @param chosenMove the move to be reported
	 * @param name the player's name
	 */
	public void reportMove(int chosenMove, String name) {
		System.out.println("\n" + name + " places a checker in column " + chosenMove + ". \n");
		
	}

	/**
	 * Ask the user the question and return the answer as an int
	 * @param question the question to ask
	 * @return The depth the player chose
	 */
	public int getIntAnswer(String question) {
		// Initialize variables to hold move and if the input is valid
		int answer = 0;
		boolean valid = false;
		// Ask for a move
		System.out.print(question + " ");
		// While the answer is not valid
		while (!valid) {
			try {
				// Try to get the answer
				answer = input.nextInt();
				// If no error is thrown, than this was a valid entry
				valid = true;
			}
			// Catch an input mismatch exception
			catch (InputMismatchException ex) {
				// Report to user and try again
				this.reportToUser("That was not a valid column. \n");
				valid = false;
				input.nextLine();
				System.out.print(question + " ");
			}
		}
		input.nextLine();
		// Return the answer
		return answer;
	}

	/**
   * Convey a message to user
   * @param message the message to be reported
   */
	public void reportToUser(String message) {
		System.out.print(message);
	}

	/**
   * Ask the question and return the answer
   * @param question the question to ask
   * @return the answer to the question
   */
	public String getAnswer(String question) {
		System.out.print(question);
		return input.nextLine();
	}
} // This brace } ends the Connect4ViewText Class