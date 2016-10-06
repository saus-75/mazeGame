/**
 * Common state class used in A*searches
 * @author Joel Smith
 *
 */
public class State implements Comparable<State>{
	private int x;
	private int y;
	private State parent;
	private int gcost;
	private Heuristic hcost;
	
	/**
	 * Constructs a state
	 * @param xCoor X coordinate of state
	 * @param yCoor Y coordinate of state
	 * @param g Cost to get that state from original state
	 * @param p The player state (Where the player is)
	 */
	public State(int xCoor, int yCoor, int g, State p){
		x = xCoor;
		y = yCoor;
		gcost = g;
		hcost = null;
		parent = p;
	}

	/**
	 * Gets the X value of the currState
	 * @return X coordinate
	 */
	public int getX() {
		return x;
	}


	/**
	 * Gets the Y value of the currState
	 * @return Y coordinate
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the Gcost of the currState
	 * @return Gcost
	 */
	public int getGcost() {
		return gcost;
	}
	
	/**
	 * Gets the Hcost of the currState
	 * @return Hcost
	 */
	public int getHcost() {
		return hcost.getHeuristic();
	}
	
	/**
	 * Sets the Hcost of the currState
	 * @param h Heuristic of the currState
	 */
	public void setHcost(Heuristic h) {
		hcost = h;
	}
	
	/**
	 * Gets the Fcost of the state
	 * @return Fcost
	 */
	public int getFcost() {
		return (gcost + getHcost());
	}
	
	/**
	 * Gets the parent state of the currState
	 * @return Parent state
	 */
	public State getParent() {
		return parent;
	}

	/**
	 * Used for priority queues.
	 */

	@Override
	public int compareTo(State o) {
		return this.getFcost() - o.getFcost();
	}

}