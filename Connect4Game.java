package connect4;

import java.util.ArrayList;

/**
 * Represents the state of the Connect4 game.  It is the model in the
 * model-view-controller pattern.
 * 
 * 
 * @author Etai Klein & Katie Lachance
 * 
 */
public class Connect4Game implements Connect4State {
	// Instance variables
	private int [] board;						// Holds the state of the game
	private int playerToMoveNum;    // 0 or 1 for first and second player
	private Player [] players;      // Array of the two players
	private Connect4View view;      // Holds the view, so can update display as state changes (if desired).

	/**
	 * Create the game state initially
	 * @param int playerNum
	 * @param Player [] thePlayers
	 * @param Connect4View aView
	 */
	public Connect4Game (int playerNum, Player [] thePlayers, Connect4View aView) {
		// Initialize the initial board to all empty
		// Note that the array will be as long as every position in the board
		// and be automatically filled with zeros
		int [] initBoard = new int[ROWS * COLS];
		this.initialize(playerNum, thePlayers, initBoard);
		this.view = aView;
	}

	/**
	 * Overload the constructor to update the game state
	 * @param int playerNum (the payer whose move it is)
	 * @param Player [] thePlayers (the players of the game)
	 * @param int [] initBoard (board to copy into the game state)
	 */
	public Connect4Game (int playerNum, Player [] thePlayers, int [] initBoard) {
		this.initialize(playerNum, thePlayers, initBoard);
	}

	private void initialize(int playerNum, Player [] thePlayers, int [] initBoard) {
		// Create the game state board to be updated
		this.board = new int [ROWS * COLS];
		// Copy the game state into the board
		for (int i = 0; i < board.length; i++) {
			board[i] = initBoard[i];           
		}
		// Update current players and player
		playerToMoveNum = playerNum;
		players = thePlayers;
	}

	/**
	 * Gets a 2-D array representing the board.
	 * The first subscript is the row number and the second the column number.
	 * The bottom of the board is row 0 and the top is row ROWS-1.
	 * The left side of the board is column 0 and the right side is column COLS-1.
	 * @return the board
	 */
	public char[][] getBoard() {
		// Create board to be returned
		char[][] boardMatrix = new char[ROWS][COLS];
		// Loop through each row, and then through each column (recall format of multidimensional arrays) of the game state
		// For each entry site, put an empty character, a player1 character, or a player2 character
		for (int r = 0 ; r < ROWS ; r++) {
			for (int c = 0 ; c < COLS ; c++) {
				// If the entry in the board is 0, there is no checker here and should be given the empty representation in the board
				if (this.board[r*COLS+c] == 0) {boardMatrix[r][c] = EMPTY;}
				// If the entry in the board is 1, there is player1's checker here and should be given the checker0 representation
				// in the board
				else if (this.board[r*COLS+c] == 1) {boardMatrix[r][c] = CHECKER0;}
				// Otherwise, the entry in the board is 2 and player2's checker is here and should be given the checker1 representation
				// in the board
				else {boardMatrix[r][c] = CHECKER1;}
			}
		}
		// Return the board
		return boardMatrix;
	}
	
	/**
	 * Getter method to access private instance varibale board (1D array)
	 * @return int[] board
	 */
	public int[] getBoardArray() {
		return this.board;
	}

	/**
	 * Gets an array holding 2 Player objects
	 * @return the players
	 */
	public Player[] getPlayers() {
		return this.players;
	}

	/**
	 * Gets the number of the player whose move it is
	 * @return the number of the player whose move it is
	 */
	public int getPlayerNum() {
		return this.playerToMoveNum;
	}

	/**
	 * Gets the Player whose turn it is to move
	 * @return the Player whose turn it is to move
	 */
	public Player getPlayerToMove() {
		return this.players[this.playerToMoveNum];
	}

	/**
	 * Is this move valid?
	 * @param col column where we want to move
	 * @return true if the move is valid
	 */
	public boolean isValidMove(int col) {
		// If the column is not in the board, it is not a valid move
		if (col < 0 || col >= COLS) {
			// Return false
			return false;
			}
		// Iterate through each row of the board
		for (int r = 0; r < ROWS ; r++) {
			// If there is an empty (0) entry in the column of interest, then it 
			// is possible to make a valid move in this column
			if (this.board[r*COLS + col] == 0) {
				// Return true
				return true;
			}
		}
		// If there were no empty entries in this column
		// Return false
		return false;
	}

	/**
	 * Make a move, dropping a checker in the given column
	 * @param col the column to get the new checker
	 */
	public void makeMove(int col) {
		// Iterate through the rows
		// Note that by starting at row 0, the checker will be placed in the 
		// lowest possible slot in the given column
		for (int r = 0 ; r < ROWS ; r++) {
			// If the slot is not already occupied (has a value equal to 0)
			if (this.board[r*COLS + col] == 0) {
				// Place the checker (according to who is placing it) here and stop the search
				// Note that 1 is added to the player number so that player1 places 1 and player2 places 2
				this.board[r*COLS + col] = this.playerToMoveNum + 1;
				// Switch players
				this.playerToMoveNum = (this.playerToMoveNum + 1) % 2;
				return;
			}
		}
	}

	/**
	 * Is the board full?
	 * @return true if the board is full
	 */
	public boolean isFull() {
		// Iterate through each column. If there is no valid move in any column, 
		// then the board is full (return true). Otherwise, the board is not full
		// (return false)
		for (int c = 0 ; c < COLS ; c ++) {
			if (this.isValidMove(c)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Decides if the game is over
	 * @return boolean (true if the game is over)
	 */
	public boolean gameIsOver() {
		// The game is over only when the board is full or when one player has gotten a 4 checkers in a row
		// Check if the board is full
		if (this.isFull()) {
			// If the board is full, the game is over
			// Return true
			return true;
		}
		// Otherwise, check if there is a 4 in a row
		else {
			// Get all 4 in a row combinations
			NInARow fourInARow= new NInARow(4, this.board);
			ArrayList<int[]> possibleInARows = fourInARow.getTotalInARows();
			// Iterate over each possibility
			for (int[] row : possibleInARows) {
				// If either player has a 4 in a row
				if (this.board[row[0]] == 1 && this.board[row[1]] == 1 && 
						this.board[row[2]] == 1 && this.board[row[3]] == 1 ||
						this.board[row[0]] == 2 && this.board[row[1]] == 2 && 
						this.board[row[2]] == 2 && this.board[row[3]] == 2) {
					// The game is over
					// Return true
					return true;
				}
			}
		}
		// Otherwise, the game is not over
		// Return false
		return false;	
	}
}