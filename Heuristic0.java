/**
 * Main heuristic which finds the difference between the currState and the player.
 * @author Joel Smith
 *
 */
public class Heuristic0 implements Heuristic {
	private int hcost;
	
	/**
	 * Constructs a Heuristic0
	 * @param curr Current State
	 * @param i X coor of player
	 * @param j Y coor of player
	 */
	public Heuristic0 (State curr, int i, int j) {
		hcost += curr.getX() - i;
		hcost += curr.getY() - j;
	}
	
	
	@Override
	public int getHeuristic() {
		return hcost;
	}

}