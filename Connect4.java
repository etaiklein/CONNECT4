package connect4;

/**
 * Creates a game of Connect 4
 * This class is the "controller" part of the model-view-controller pattern.
 * 
 * 
 * @author Etai Klein & Katie Lachance
 * 
 */
public class Connect4 {
	/**
	 * This is the main function that plays all of Connect4
	 * It creates the two players, selects the view, initializes the game state,
	 * and plays the game until the game is over
	 * @param args
	 */
	public static void main (String[] args) {
		// Choose which view to play in
		// Connect4View view = new Connect4ViewGraphical();
		Connect4View view = new Connect4ViewText();
		
		// Create players
		Player [] players = new Player[2];
		players[0] = Connect4.makePlayer(view, "first");
		players[1] = Connect4.makePlayer(view, "second");
		
		// Initialize the game state and display
		Connect4Game state = new Connect4Game(0, players, view);
		view.display(state);
		
		// While the game is happening (game is not over)
		while (!state.gameIsOver()) {
			// Get the chosen move of the player whose turn it is
    	int move = state.getPlayerToMove().getMove(state, view);
    	// Make the move
    	state.makeMove(move);
    	// Display the new game state
    	view.display(state);
    }
    // At the end of the game
		// Always going to be the loser's turn after the game ends with a winner
		if (!state.isFull())
      view.reportToUser(players[1 - state.getPlayerNum()].getName() + " wins!");
		// Otherwise the game ended because it was a draw
    else
      view.reportToUser("It is a draw");
	}
	
	/** 
   * Constructs a Connect4 player.  If the name contains "Computer" it
   * constructs a computer player; else a human player
   * @param view the view to use to communicate to the world
   * @param playerMsg the player to ask for 
   */
  public static Player makePlayer(Connect4View view, String playerMsg) {
    String playerName = view.getAnswer("Enter the name of the " + playerMsg + 
    		" player." + "\n(Include 'Computer' in the name of a computer player) ");
    // If it is a computer player, ask for horizon and create a computer player
    if(playerName.contains("Computer")) {
    	int depth = view.getIntAnswer("How far should I look ahead? ");
      return new ComputerConnect4Player(playerName, depth);
    }
    // Otherwise, create a human player
    else
      return new HumanConnect4Player(playerName);
  }	
} // This brace } ends the Connect4 Class