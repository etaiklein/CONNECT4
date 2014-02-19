package connect4;

import java.util.ArrayList;

/**
 * Implements a computer player that chooses moves using
 * game tree search with alpha-beta pruning
 * Inspired by ComputerKalahPlayerAB.java
 * 
 * @author Etai Klein & Katie Lachance
 * Problem Set #6
 */
public class ComputerConnect4Player extends Player {
	// Private instance variables
	private int depth;			// Horizon (look-ahead for computer; how many levels to search down)
	
	// Private weights given to each in-a-row
	// Note that there is an exponentially higher weight given to longer "in-a-rows"
	private static final int THREE_VAL = 64;
	private static final int TWO_VAL = 16;
	private static final int ONE_VAL = 4;
	private static final int ZERO_VAL = 1;
	// Split multiplier weight moves that will directly result in a longer "in-a-rows"
	private static final int SPLIT_MULTIPLIER = 2;
	// End multiplier highly weights any moves that will end the game
	private static final int END_MULTIPLIER = 100;
	
	/**
	 * Constructor to set name and depth
	 * @param String name
	 * @param int horizon
	 */
	public ComputerConnect4Player(String name, int horizon) {
		super(name);
		depth = horizon;
	}

	/**
	 * Overload the constructor for when not initializing the game
	 * @param name
	 */
	public ComputerConnect4Player(String name) {
		super(name);
	}

	/**
	 * Returns and reports the computer's move using alpha-beta game tree pruning
	 * @return int (column of computer's move choice)
	 */
	public int getMove(Connect4State state, Connect4View view) {
		// Find the best move
		int move = this.pickMove(state, depth, -Integer.MAX_VALUE, Integer.MAX_VALUE).move;
		// Report the move chosen
		view.reportMove(move, state.getPlayerToMove().getName());
		// Return move
		return move;
	}
	
	/**
	 * Static evaluation function to determine the "score" of the board for the current
	 * player. In Connect4, "score" is determined by the weighted sum of how many 3 in 
	 * a rows, 2 in a rows, 1 in a rows, and 0 in a rows, multiplying the score if that
	 * space is directly available (empty, but full directly below it)
	 * @param Connect4State state
	 * @return int (score of board for player)
	 */
	public static int staticEval (Connect4State state) {
		// Initialize score to 0
		int score = 0;
		// Get the board array representation of the board
		int[] board = ((Connect4Game) state).getBoardArray();
		// Get the current player number to know whose checkers are whose
		int playerNum = state.getPlayerNum();
		// Calculate 4 in a row combinations
		NInARow fourInARow= new NInARow(4, board);
		// Collect all possible 4 in a rows
		ArrayList<int[]> possibleInARows = fourInARow.getTotalInARows();
		// Iterate over each possible 4 in a rows
		for (int[] row : possibleInARows) {
			// Evaluate the score for 3 in a rows, 2 in a rows, 1 in a rows, 0 in a rows
			// 3 in a rows
			// 3 in a rows are especially threatening, so the program takes extra care to 
			// account for all possible situations in which one move, including those with gaps,
			// will win the game, essentially extending the horizon in threatening cases 
			// Note that in all cases, if the skip space is open to drop a checker directly into, 
			// the score has a multiplier, to weight the move even more
			if (board[row[0]] == 0 && board[row[1]] == playerNum && 
					board[row[2]] == playerNum && board[row[3]] == playerNum) {
				score += THREE_VAL;
				if (fourInARow.possibleChecker(state, board[row[0]])) {
					score += THREE_VAL * SPLIT_MULTIPLIER;
				}
			}
			if (board[row[0]] == playerNum && board[row[1]] == 0 && 
					board[row[2]] == playerNum && board[row[3]] == playerNum) {
				score += THREE_VAL;
				if (fourInARow.possibleChecker(state, board[row[1]])) {
					score += THREE_VAL * SPLIT_MULTIPLIER;
				}
			}
			if (board[row[0]] == playerNum && board[row[1]] == playerNum && 
					board[row[2]] == 0 && board[row[3]] == playerNum) {
				score += THREE_VAL;
				if (fourInARow.possibleChecker(state, board[row[2]])) {
					score += THREE_VAL * SPLIT_MULTIPLIER;
				}
			}
			if (board[row[0]] == playerNum && board[row[1]] == playerNum && 
					board[row[2]] == playerNum && board[row[3]] == 0) {
				score += THREE_VAL;
				if (fourInARow.possibleChecker(state, board[row[3]])) {
					score += THREE_VAL * SPLIT_MULTIPLIER;
				}
			}
			
			// Two in a rows
			// Note that this does not account for gaps between 2 in a rows (like 3 in
			// a rows), but two in a rows are generally less threatening
			if (board[row[0]] == playerNum && board[row[1]] == playerNum && 
					board[row[2]] == 0 && board[row[3]] == 0) {
				score += TWO_VAL;
				if (fourInARow.possibleChecker(state, board[row[2]]) &&
						fourInARow.possibleChecker(state, board[row[3]])) {
					score += TWO_VAL * SPLIT_MULTIPLIER;
				}
			}
			if (board[row[0]] == 0 && board[row[1]] == playerNum && 
					board[row[2]] == playerNum && board[row[3]] == 0)  {
				score += TWO_VAL;
				if (fourInARow.possibleChecker(state, board[row[0]]) &&
						fourInARow.possibleChecker(state, board[row[3]])) {
					score += TWO_VAL * SPLIT_MULTIPLIER;
				}
			}
			if (board[row[0]] == 0 && board[row[1]] == 0 && 
					board[row[2]] == playerNum && board[row[3]] == playerNum) {
				score += TWO_VAL;
				if (fourInARow.possibleChecker(state, board[row[0]]) &&
						fourInARow.possibleChecker(state, board[row[1]])) {
					score += TWO_VAL * SPLIT_MULTIPLIER;
				}
			}
			
			// One in a rows
			if (board[row[0]] == playerNum && board[row[1]] == 0 && 
					board[row[2]] == 0 && board[row[3]] == 0) {
				score += ONE_VAL;
				if (fourInARow.possibleChecker(state, board[row[1]]) &&
						fourInARow.possibleChecker(state, board[row[2]]) &&
						fourInARow.possibleChecker(state, board[row[3]])) {
					score += ONE_VAL * SPLIT_MULTIPLIER;
				}
			}
			if (board[row[0]] == 0 && board[row[1]] == playerNum && 
					board[row[2]] == 0 && board[row[3]] == 0) {
				score += ONE_VAL;
				if (fourInARow.possibleChecker(state, board[row[0]]) &&
						fourInARow.possibleChecker(state, board[row[2]]) &&
						fourInARow.possibleChecker(state, board[row[3]])) {
					score += ONE_VAL * SPLIT_MULTIPLIER;
				}
			}
			if (board[row[0]] == 0 && board[row[1]] == 0 && 
					board[row[2]] == playerNum && board[row[3]] == 0) { 
				score += ONE_VAL;
			if (fourInARow.possibleChecker(state, board[row[0]]) &&
					fourInARow.possibleChecker(state, board[row[1]]) &&
					fourInARow.possibleChecker(state, board[row[3]])) {
				score += ONE_VAL * SPLIT_MULTIPLIER;
				}
			}
		if (board[row[0]] == 0 && board[row[1]] == 0 && 
				board[row[2]] == 0 && board[row[3]] == playerNum) {
				score += ONE_VAL;
				if (fourInARow.possibleChecker(state, board[row[0]]) &&
						fourInARow.possibleChecker(state, board[row[1]]) &&
						fourInARow.possibleChecker(state, board[row[2]])) {
					score += ONE_VAL * SPLIT_MULTIPLIER;
				}
			}
		
			// Zero in a rows
			// This case is important at the beginning of the game when there are not 
			// too many checkers placed, so we really just have to evaluate the "potential"
			// of each spot (middle is better)
			if (board[row[0]] == 0 && board[row[1]] == 0 && 
					board[row[2]] == 0 && board[row[3]] == 0) {
				score += ZERO_VAL;
			}
		}
		// For testing purposes, print the score
		System.out.println("Score: " + score);
		// Return the score
		return score;
	}

	/**
	 * Uses game tree search with alpha-beta pruning to pick computer's move
	 * low and high define the current range for the best move.
	 * The current player has another move choice which will get him at least low,
	 * and his opponent has another choice that will hold his losses to high.
	 * 
	 * @param state current state of the game
	 * @param depth number of moves to look ahead in game tree search
	 * @param low a value that the player can achieve by some other move
	 * @param high a value that the opponent can force by a different line of play
	 * @return the move chosen
	 */
	private Connect4Move pickMove (Connect4State state, int depth, int low, int high) {
		// Variables to keep track of current and best move so far
		Connect4Move bestMove;         // Hold best move found and its value
		Connect4Move currentMove;         // Hold the move found and its value
		
		// For testing purposes, print out high and low
		System.out.println("High: " + high + "; Low: " + low);
		
		// Gives all information about game state
		int[] board = ((Connect4Game) state).getBoardArray();
		// So the current player knows which chips are his/hers
		int playerToMove = state.getPlayerNum();

		// A dummy move that will be replaced when a real move is evaluated, 
		// so the column number is irrelevant.
		bestMove = new Connect4Move(Integer.MIN_VALUE, 0);

		// Run through possible moves 
		for (int col = 0; bestMove.value < high && col < Connect4State.COLS; col++) {
			// If the move is legal
			if (state.isValidMove(col)) {
				// Make a scratch copy of state
				Connect4Game copy = new Connect4Game(playerToMove, state.getPlayers(), board);   
				// Make the move
				copy.makeMove(col);             

				// Find the value of this board by evaluating if game over or looking ahead if not
				if (copy.gameIsOver())
					// Evaluate the true score, and multiply it by a huge constant so that the 
					// program will choose a sure win over a potentially larger speculative win 
					// and a possible loss over a sure loss.  
					currentMove = new Connect4Move(END_MULTIPLIER * staticEval(state), col);
				// If the player changed but there depth is non-zero
				else if (depth > 0) {
					// Get the next move recursively
					currentMove = pickMove(copy, depth - 1, -high, -low);
					// Set that moves value to its negative
					currentMove.value = -currentMove.value;   // Good for opponent is bad for me 
					// Store the move made
					currentMove.move = col;                   
				}
				// Otherwise, the depth is exhausted, so estimate who is winning 
				// with the static evaluation method
				else {
					currentMove = new Connect4Move(staticEval(copy), col);
				}
				// If a new best move was found
				if (currentMove.value > bestMove.value)  {  
					// Update the best move
					bestMove = currentMove;
					// Update the low value
					low = Math.max(low, bestMove.value);
				}
			}
		}
		// Return the best move
		return bestMove;
	}
} // This brace } ends the ComputerConnect4Player Class
