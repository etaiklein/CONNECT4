package connect4;

/**
 * Holds information about and interacts with a human player
 * Inspired by HumanKalahPlayer.java
 * 
 * @author Etai Klein & Katie Lachance
 * 
 */
public class HumanConnect4Player extends Player {

	/**
	 * Constructs a human player
	 * @param name player's name
	 */
	public HumanConnect4Player(String name) {
		super(name);
	}

	/**
	 * Gets and returns the player's choice of move
	 * @param state current game state
	 * @param view the object that displays the game
	 * @return move chosen by the player, in the range
	 *   0 to Connect4State-1
	 */
	public int getMove(Connect4State state, Connect4View view) {
		return view.getUserMove(state);
	}
} // This brace } ends the HumanConnect4Player Class
