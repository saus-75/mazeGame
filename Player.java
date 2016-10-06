
/**
 * The representation for the user's player avatar. User can move the {@code Player} around the maze's path using the
 * up, down, left, and right arrow keys.
 * The player has several modes: Ghost mode, where they are invisible and invincible to enemy AIs.
 * The player stores the number of coins they have collected and their position on the maze.
 */
public class Player {
	public static final int north = 0;
	public static final int south = 1;
	public static final int east = 2;
	public static final int west = 3;
	
	private int x;
	private int y;
	private boolean ghost;
	private int coinCount;
	private int direction;

	/**
	 * Initialises the player with coordinates at the {@code NewGame's} start and end points in non-ghost mode, and a coin value of 0.
	 */
	public Player () {
		x = NewGame.startBound;
		y = NewGame.startBound;
		ghost = false;
		coinCount = 0;
		direction = south;
	}

	/**
	 * Gets the players column index in the 2d maze array.
	 * @return The players column index in the 2d maze array.
     */
	public int getX () {
		return x;
	}

	/**
	 * Gets the players row index in the 2d maze array.
	 * @return The players row index in the 2d maze array.
	 */
	public int getY () {
		return y;
	}

	/**
	 * Sets the players row index in the 2d maze array.
	 */
	public void setX (int newX) {
		x = newX;
	}

	/**
	 * Sets the players column index in the 2d maze array.
	 */
	public void setY (int newY) {
		y = newY;
	}

	/**
	 * Returns whether player is in ghost mode.
	 * @return Returns true if the player is in ghost mode and false if otherwise.
     */
	public boolean getGhost () {
		return ghost;
	}

	/**
	 * Gets the number of coins that the player has collected.
	 * @return The number of coins that the player has collected.
     */
	public int getCoinCount () {
		return coinCount;
	}

	/**
	 * Sets the number of coins collected to 0.
	 */
	public void clearCoins () {
		coinCount = 0;
	}

	/**
	 * Gets the pixel offset from the top of the maze.
	 * @return The pixel offset from the top of the maze.
     */
	public int getRow () {
		return (getY()-NewGame.startBound)/NewGame.cellSize;
	}

	/**
	 * Gets the pixel offset from the left of the maze.
	 * @return The pixel offset from the left of the maze.
	 */
	public int getCol () {
		return (getX()-NewGame.startBound)/NewGame.cellSize;
	}

	/**
	 * Gets the {@code Player}'s current direction as defined in the publicly defined fields.
	 * @return The int value of the direction as defined in the publicly defined fields.
     */
	public int getDirection() {
		return direction;
	}

	/**
	 * Sets the {@code Player}'s current direction as defined in the publicly defined fields.
	 * @param direction The int value of the direction as defined in the publicly defined fields.
     */
	public void setDirection(int direction) {
		this.direction = direction;
	}

	/**
	 * Moves the player left by one node on the matrix and changes its orientation.
	 * @param maze The maze on which the move the player.
     */
	public void moveLeft(Maze maze) {
		ghost = false;
		if (getCol() > 0 && maze.get(getRow(), getCol()-1).getState() != Node.wall) {
			x -= NewGame.cellSize;
		}
		setDirection(west);
	}

	/**
	 * Moves the player right by one node on the matrix and changes its orientation.
	 * @param maze The maze on which the move the player.
	 */
	public void moveRight(Maze maze) {
		ghost = false;
		if (getCol() < NewGame.size - 1 && maze.get(getRow(), getCol()+1).getState() != Node.wall) {
			x += NewGame.cellSize;
		}
		setDirection(east);
	}

	/**
	 * Moves the player up by one node on the matrix and changes its orientation.
	 * @param maze The maze on which the move the player.
	 */
	public void moveUp(Maze maze) {
		ghost = false;
		if (getRow() > 0 && maze.get(getRow()-1, getCol()).getState() != Node.wall) {
			y -= NewGame.cellSize;
		}
		setDirection(north);
	}

	/**
	 * Moves the player down by one node on the matrix and changes its orientation.
	 * @param maze The maze on which the move the player.
	 */
	public void moveDown(Maze maze) {
		ghost = false;
		if (getRow() < NewGame.size - 1 && maze.get(getRow()+1, getCol()).getState() != Node.wall) {
			y += NewGame.cellSize;
		}
		setDirection(south);
	}

	/**
	 * Puts the player in ghost mode
	 */
	public void enterGhostMode () {
		ghost = true;
	}

	/**
	 * Adds 1 to the player's coin counter.
	 */
	public void collectCoin () {
		coinCount++;
	}
}