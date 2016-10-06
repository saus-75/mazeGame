public class Node {
	
	public static final char wall = 'w';
	public static final char path = 'p';
	public static final char end = 'e';
	public static final char start = 's';
	public static final char coin = 'c';
	public static final char hint = 'h';
	public static final char oldCoin = 'o';
	
	private char state;
	private double north;
	private double east;
	private double south;
	private double west;
	private int i;
	private int j;
	
	/**
	 * Creates a new Node with position i,j
	 * @param i the row position
	 * @param j the col position
	 */
	public Node (int i, int j) {
		this.north = 0;
		this.south = 0;
		this.east = 0;
		this.west = 0;
		this.i = i;
		this.j = j;
	}
	
	/**
	 * Changes this nodes state
	 * @param newState the state to change to
	 */
	public void changeState (char newState) {
		this.state = newState;
	}

	/**
	 * Gets this nodes state
	 * @return the state of this node
	 */
	public char getState () {
		return this.state;
	}

	/**
	 * Sets the weight of the link to the north of this node
	 * @param weight the weight of the link to the north of this node
	 */
	public void setNorth(double weight) {
		this.north = weight;
	}
	
	/**
	 * Gets the weight of the link to the north of this node
	 * @return weight the weight of the link to the north of this node
	 */
	public double getNorth () {
		return this.north;
	}

	/**
	 * Sets the weight of the link to the south of this node
	 * @param weight the weight of the link to the south of this node
	 */
	public void setSouth(double weight) {
		this.south = weight;
	}
	
	/**
	 * Gets the weight of the link to the south of this node
	 * @return weight the weight of the link to the south of this node
	 */
	public double getSouth () {
		return this.south;
	}

	/**
	 * Sets the weight of the link to the east of this node
	 * @param weight the weight of the link to the east of this node
	 */
	public void setEast(double weight) {
		this.east = weight;
	}
	
	/**
	 * Gets the weight of the link to the east of this node
	 * @return weight the weight of the link to the east of this node
	 */
	public double getEast () {
		return this.east;
	}

	/**
	 * Sets the weight of the link to the west of this node
	 * @param weight the weight of the link to the west of this node
	 */
	public void setWest(double weight) {
		this.west = weight;
	}
	
	/**
	 * Gets the weight of the link to the west of this node
	 * @return weight the weight of the link to the west of this node
	 */
	public double getWest () {
		return this.west;
	}

	/**
	 * Gets the row of this node
	 * @return the row
	 */
	public int getI() {
		return this.i;
	}

	/**
	 * Gets the column of this node
	 * @return the column
	 */
	public int getJ() {
		return this.j;
	}

	/**
	 * Checks if two nodes are equal
	 * @param another node
	 * @return whether they are equal
	 */
	public boolean equals(Object other) {
		if (other instanceof Node) {
			Node that = (Node) other;
			return (i == that.getI() && j == that.getJ());
		}
		return false;
	}
}
