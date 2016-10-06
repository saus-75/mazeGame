import java.util.LinkedList;
import java.util.List;

public class Hint {
	
	public static final int north = 0;
	public static final int south = 1;
	public static final int east = 2;
	public static final int west = 3;
	
	private List<Node> hintTiles;
	private List<Integer> directions;
	private int initiated;
	private boolean requested;
	private int hintCycle;
	
	/**
	 * Creates a new hint 
	 */
	public Hint(){
		requested = false;
		directions = new LinkedList<Integer>();
		hintCycle = 0;
	}
	
	/**
	 * gets the hint tiles from a list of nodes
	 * @return list of hint tiles nodes
	 */
	public List<Node> getHintTiles() {
		return hintTiles;
	}
	
	/**
	 * sets the hint tiles in a list of nodes
	 * @param Sets in a list of nodes
	 */
	public void setHintTiles(List<Node> hintTiles) {
		this.hintTiles = hintTiles;
	}
	
	/**
	 * return a value to check for initiation
	 * @return a value for the hint
	 */
	public int getInitiated() {
		return initiated;
	}
	
	/**
	 * initiates a hint cycle
	 * @param initiated
	 */
	public void setInitiated(int initiated) {
		this.initiated = initiated;
	}
	
	/**
	 * Checks if hint is requested
	 */
	public boolean isRequested() {
		return requested;
	}
	
	/**
	 * Sets the request for hint
	 * @param requested
	 */
	public void setRequested(boolean requested) {
		this.requested = requested;
	}
	
	/**
	 * Gets the list of Direction nodes
	 * @return
	 */
	public List<Integer> getDirections() {
		return directions;
	}
	
	/**
	 * Gets the list of Direction nodes
	 * @param directions
	 */
	public void setDirections(List<Integer> directions) {
		this.directions = directions;
	}
	
	/**
	 * Reverts back to its original state
	 */
	public void revertStates() {
		for(Node n : hintTiles){
			n.changeState(Node.path);
		}
	}
	
	/**
	 * Gets the hint cycle
	 * @return the hint cycle
	 */
	public int getHintCycle() {
		return hintCycle;
	}
	
	/**
	 * Sets the hint cycle
	 * @param hintCycle
	 */
	public void setHintCycle(int hintCycle) {
		this.hintCycle = hintCycle;
	}	
	
	
}
