import java.io.Serializable;

public interface GameGenerator extends Serializable {
	/**
	 * Generate the maze from the Maze class
	 * @param maze
	 */
	public void generateMaze(Maze maze);
	
	/**
	 * Get one of the function form one of the FunctionR3 class
	 * @return FunctionR3
	 */
	public FunctionR3 getFunction();
	
	/**
	 * Gets the {@code MazeCarver} instance used to carve the maze.
	 * @return The {@code MazeCarver} instance used to carve the maze.
	 */
	public MazeCarver getCarver();
	
	/**
	 * Gets the {@code MazeWeighter} strategy used to weight the maze.
	 * @return The {@code MazeWeighter} strategy used to weight the maze.
	 */
	public MazeWeighter getWeighter();
	
	/**
	 * Gets the timer length
	 * @return int which is currently how long you have in the maze
	 */
	public int getTimerLength();

	/**
	 * Gets the Start Time
	 * @return int which is the time for a specific difficulty
	 */
    public int getStartTime ();
    
    /**
     * Gets the increment amount for each coin taken
     * @return int which is the amount of time you get from getting the coin token
     */
    public int getTimeIncrement();
    
    /**
     * Gets size of the maze
     * @return The {@code int} size of the maze.
     */
	public int getSize();
	
	/**
	 * Gets the {@code CoinPlacer} instance used to place coins in the generated maze.
	 * @return The {@code CoinPlacer} instance used to place coins in the generated maze.
	 */
	public CoinPlacer getCoinPlacer();
	
	/**
	 * Returns the number of coins placed on the maze using the strategy.
	 * @return The {@code int} number of coins placed on the maze using the strategy.
	 */
	public int getNumCoins();
	
	/**
	 * Returns a description of the {@code GameGenerator}'s difficulty as a string.
	 * @return A description of the {@code GameGenerator}'s difficulty as a string.
	 */
	public String difficultyToString();
}
