package connect4;

/**
 * Class to keep track of move value (in game tree) and actual move (column number)
 * Inspired by KalahMove.java
 * 
 * @author Etai Klein & Katie Lachance
 * 
 */
public class Connect4Move
{
  public int value;       // Game value of this move
  public int move;        // Number of column to drop a checker into
  
  public Connect4Move(int value, int move) {
    this.value = value;
    this.move = move;
  }
} 