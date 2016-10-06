/**
 * A heuristic interface utilised in A*searches
 * @author Joel Smith
 *
 */
public interface Heuristic {
	/**
	 * Returns the int value of the heuristic (Hcost)
	 * @return Hcost
	 */
	public int getHeuristic();
}