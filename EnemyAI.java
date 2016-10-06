import java.awt.Point;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
/**
 * This is the AI class, the main obstacle to the player completing their primary objective. It utilises an A*search to 
 * locate the optimal path to take to find and destroy the player.
 * @author Joel Smith
 *
 */
public class EnemyAI {
	
	public static final int north = 0;
	public static final int south = 1;
	public static final int east = 2;
	public static final int west = 3;
	public static final int GAME_OVER = -1;
	public static final int NULL_EXCEPTION = -10; // If the player for some reason cannot be located in time (less lag, less accuracy, more efficient)
	
	public static final int INITIAL_SPEED = 6; // Note: speed correlates to how much timer iterations pass btn each AI move
	public static final int MED_SPEED = 3;
	public static final int TOP_SPEED = 2;
	
	private int x;
	private int y;
	private int direction;
	private boolean spawn;
	private boolean vulnerable;
	private int ID;
	private int speed;
	private boolean coinFlag;
	
	/**
	 * Instantiates an enemyAI
	 * @param ID The unique ID of an AI
	 * @param spawnPoint The point on the map the AI spawns to
	 * @param spawn Whether the AI is spawned or not
	 * @param speed The speed at which the AI is travelling (measured in how often the AI moves tiles)
	 * @param coinFlag The flag used to determine if the AI has had its speed increased by player reaching max coins
	 * 
	 */
	public EnemyAI (int id, Point spawnPoint) {
		x = spawnPoint.x;
		y = spawnPoint.y;

		spawn = false;
		vulnerable = false;
		direction = south;
		speed = INITIAL_SPEED;
		ID = id;
		coinFlag = false;
	}
	
	/**
	 * Obtains the AI's speed
	 * @return Speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Sets the AI's speed
	 * @param speed The new speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * Checks if the coinFlag has been set
	 * @return Boolean result
	 */
	public boolean isCoinFlag() {
		return coinFlag;
	}

	/**
	 * Sets the coinFlag
	 * @param coinFlag Flag that is toggled when the AI has already increased speed for player reaching max coins
	 */
	public void setCoinFlag(boolean coinFlag) {
		this.coinFlag = coinFlag;
	}

	/**
	 * Returns the ID of the AI
	 * @return Unique AI ID
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Sets the ID of the AI
	 * @param iD Unique ID assigned to AIs
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * Gets X coordinate of AI
	 * @return X coordinate based off pixels
	 */
	public int getX () {
		return x;
	}
	/**
	 * Sets X coordinate of AI
	 * @param newX New X coordinate based off pixels
	 */
	public void setX (int newX) {
		x = newX;
	}
	
	/**
	 * Gets Y coordinate of AI
	 * @return Y coordinate based off pixels
	 */
	public int getY () {
		return y;
	}

	/**
	 * Sets the Y coordinate of AI
	 * @param newY New Y coordinate based off pixels
	 */
	public void setY (int newY) {
		y = newY;
	}
	
	/**
	 * Gets the direction of the AI
	 * @return Direction AI is heading
	 */
	public int getDirection (){
		return direction;
	}
	
	/**
	 * Sets the direction of the AI
	 * @param d Direction
	 */
	public void setDirection (int d){
		direction = d;
	}
	
	/*
	public boolean getVulnerable () {
		return vulnerable;
	}
	
	public void setVulnerable (boolean v) {
		vulnerable = v;
	}
	*/
	/**
	 * Returns a boolean to see if AI is spawned
	 * @return Boolean
	 */
	public boolean isSpawned () {
		return spawn;
	}
	
	/**
	 * Sets the spawn of an AI to true, making AI visible.
	 */
	public void spawnAI () {
		spawn = true;
	}
		
	/**
	 * Gets the row tile value the AI is on
	 * @return Row tile value
	 */
	public int getRow () {
		return (getY()-NewGame.startBound)/NewGame.cellSize;
	}
	
	/**
	 * Gets the column tile value the AI is on
	 * @return Column tile value
	 */
	public int getCol () {
		return (getX()-NewGame.startBound)/NewGame.cellSize;
	}
	
	/**
	 * Moves the AI left if possible
	 * @param nodes All nodes in the maze
	 */
	public void moveLeft(List<List<Node>> nodes) {
		if (getCol() > 0 && nodes.get(getRow()).get(getCol()-1).getState() != Node.wall) {
			x -= NewGame.cellSize;
		}
	}
	
	/**
	 * Moves the AI right if possible
	 * @param nodes All nodes in the maze
	 */
	public void moveRight(List<List<Node>> nodes) {
		if (getCol() < NewGame.size - 1 && nodes.get(getRow()).get(getCol()+1).getState() != Node.wall) {
			x += NewGame.cellSize;
		}
	}
	
	/**
	 * Moves the AI up if possible
	 * @param nodes All nodes in the maze
	 */
	public void moveUp(List<List<Node>> nodes) {
		if (getRow() > 0 && nodes.get(getRow()-1).get(getCol()).getState() != Node.wall) {
			y -= NewGame.cellSize;
		}
	}
	
	/**
	 * Moves the AI down if possible
	 * @param nodes All nodes in the maze
	 */
	public void moveDown(List<List<Node>>nodes) {
		if (getRow() < NewGame.size - 1 && nodes.get(getRow()+1).get(getCol()).getState() != Node.wall) {
			y += NewGame.cellSize;
		}
	}
	
 /*
	public void triggerVulnerable () {
		vulnerable = true;
	}
	*/
	
	/**
	 * Despawns an AI from the game.
	 */
	public void despawnAI() {
		spawn = false;		
	}
	
	/**
	 * This function is how the AI knows which tile to make a move to. It finds the optimal path to the player
	 * and returns the direction it should move in to get closer to the player.
	 * @precondition Assumes player is spawned
	 * @precondition Assumes AI and Player are on path tiles
	 * @precondition Assumes AI is spawned
	 * @precondition Deals with an instance of where the player was (does not handle a player moving halfway through a search)
	 * @postcondition Will always deliver a direction for the AI to move in
	 * @postcondition The AI will never be stationary
	 * @param nodes All the nodes in the maze
	 * @param prevI The X coor of where the player was last situated
	 * @param prevJ The Y coor of where the player was last situated
	 * @param p Player
	 * @return A move integer (with pre-assigned values) which will inform the AI which direction to head in.
	 */
	public int shortestPathToPlayer (List<List<Node>> nodes, int prevI, int prevJ, Player p) {
		int move = 0;
		Queue<State> toVisit = new PriorityQueue<State>();
		State currState = new State(getRow(), getCol(), 0, null);
		State AI = currState;
		List<Node> queued = new LinkedList<Node>();
		//System.out.println("Player is at : "+prevI +", "+prevJ);
		//System.out.println("I'm currently at : "+this.getRow()+", "+this.getCol());
		//If AI is on top of player and not in ghost mode, return gameOver
		if(AI.getX() == (prevI) && AI.getY() == prevJ && !p.getGhost()) return GAME_OVER;
		
		while(!(currState.getX() == prevI && currState.getY() == prevJ) && currState != null) {
			int i = currState.getX();
			int j = currState.getY();

			if (i + 1 >= 0 && i + 1 <= nodes.size()-1) {
                if (nodes.get(i+1).get(j).getState() == Node.path && !queued.contains(nodes.get(i+1).get(j))) {
                	int gCost = currState.getGcost() + (int) nodes.get(i+1).get(j).getSouth();
                	State newState = new State(i+1, j, gCost, currState);
                	newState.setHcost(new Heuristic0(newState, prevI, prevJ));
                	toVisit.add(newState);
                	queued.add(nodes.get(i+1).get(j));
                }
            }
            if (i - 1 >= 0 && i - 1 <= nodes.size()-1) {
                if (nodes.get(i-1).get(j).getState() == Node.path && !queued.contains(nodes.get(i-1).get(j))) {
                	int gCost = currState.getGcost() + (int) nodes.get(i-1).get(j).getNorth();
                	State newState = new State(i-1, j, gCost, currState);
                	newState.setHcost(new Heuristic0(newState, prevI, prevJ));
                	toVisit.add(newState);
                   	queued.add(nodes.get(i-1).get(j));
                }
            }
            if (j + 1 >= 0 && j + 1 <= nodes.size()-1) {
                if (nodes.get(i).get(j+1).getState() == Node.path && !queued.contains(nodes.get(i).get(j+1))) {
                	int gCost = currState.getGcost() + (int) nodes.get(i).get(j+1).getEast();
                	State newState = new State(i, j+1, gCost, currState);
                	newState.setHcost(new Heuristic0(newState, prevI, prevJ));
                	toVisit.add(newState);
                   	queued.add(nodes.get(i).get(j+1));
                }
            }
            if (j - 1 >= 0 && j - 1 <= nodes.size()-1) {
                if (nodes.get(i).get(j-1).getState() == Node.path && !queued.contains(nodes.get(i).get(j-1))) {
                	int gCost = currState.getGcost() + (int) nodes.get(i).get(j-1).getWest();
                	State newState = new State(i, j-1 , gCost, currState);
                	newState.setHcost(new Heuristic0(newState, prevI, prevJ));
                	toVisit.add(newState);
                   	queued.add(nodes.get(i).get(j-1));
                }
            }
		    currState = toVisit.poll();	
		    if(currState == null) {
		    	return NULL_EXCEPTION;
		    }		
		}
		
		while(!currState.getParent().equals(AI)) {
			currState = currState.getParent();
		}
		
		int deltaX = AI.getX() - currState.getX();
		int deltaY = AI.getY() - currState.getY();
		
		if (deltaX > 0) move = north;
		else if (deltaX < 0) move = south;	
		else if (deltaY < 0) move = east;
		else if (deltaY > 0) move = west;
		return move;
	}
    /**
     * Makes the actual move by choosing the correct direction to head in to adjust coordinates
     * @param move The direction the AI is travelling in
     * @param nodes All the nodes in the maze
     */
	public void makeMove(int move, List<List<Node>> nodes) {
		if(move == north) moveUp(nodes);
		else if(move == south) moveDown(nodes);
		else if(move == east) moveRight(nodes);
		else if(move == west) moveLeft(nodes);
		else if(move == NULL_EXCEPTION) movementWhenGhost(nodes);
	}
	
	/**
	 * When the player is in ghost mode, the AI must pretend to not know where the player is. Therefore,
	 * a new movement strategy is employed, where the AI continues to move in the same direction, make a turn or,
	 * as a last resort, turn around and head back the way it came. This provides the most logical solution for a blind AI. 
	 * @param nodes All the nodes in the maze
	 */
	public void movementWhenGhost(List<List<Node>> nodes) {
		if(canMove(nodes)) makeMove(direction, nodes);
	    else if (!canMove(nodes)){
			int i = getRow();
			int j = getCol();
			
			if (i + 1 >= 0 && i + 1 <= nodes.size()-1 && nodes.get(i+1).get(j).getState() == Node.path && direction != north) {
				direction = south;
            } else  if (i - 1 >= 0 && i - 1 <= nodes.size()-1 && nodes.get(i-1).get(j).getState() == Node.path && direction != south) {
            	direction = north;
            } else if (j + 1 >= 0 && j + 1 <= nodes.size()-1 && nodes.get(i).get(j+1).getState() == Node.path && direction != west) {
            	direction = east;
            } else  if (j - 1 >= 0 && j - 1 <= nodes.size()-1 && nodes.get(i).get(j-1).getState() == Node.path && direction != east) {
               direction = west;
            } else {
            	reverseDirection();
            }
			movementWhenGhost(nodes);
		}
	}
	
	/**
	 * Tests if the AI can move in current direction it is facing.
	 * @param nodes All the nodes in the maze
	 * @return boolean
	 */
	public boolean canMove(List<List<Node>> nodes){
		if(direction == west && getCol() > 0 && nodes.get(getRow()).get(getCol()-1).getState() != Node.wall) {
			return true;
		} else if (direction == east && getCol() < NewGame.size - 1 && nodes.get(getRow()).get(getCol()+1).getState() != Node.wall) {
			return true;
		}else if (direction == north && getRow() > 0 && nodes.get(getRow()-1).get(getCol()).getState() != Node.wall) {
			return true;
		} else if (direction == south && getRow() < NewGame.size - 1 && nodes.get(getRow()+1).get(getCol()).getState() != Node.wall) {
			return true;
		}
	
		return false;
	}

	/**
	 * This method determines what strategy the AI should move in, depending on state of player
	 * @precondition AI and player must be spawned and in valid positions in the maze
	 * @param nodes All the nodes in the maze
	 * @param previousi The previous x position of the player
	 * @param previousj The previous y position of the player
	 * @param player1 The player
	 */
	public void attemptMove(List<List<Node>> nodes, int previousi, int previousj, Player player1) {
		if(player1.getGhost()){
			movementWhenGhost(nodes);
		} else {
			int move = shortestPathToPlayer(nodes, previousi, previousj, player1);
			makeMove(move, nodes);
			if(move == NULL_EXCEPTION) return;
			setDirection(move);
		}	
	}
	
	/**
	 * Reverses the direction of the AI (i.e. U-turn)
	 */
	public void reverseDirection(){
		if (direction == north) direction = south;
		else if (direction == south) direction = north;
		else if (direction == east) direction = west;
		else if (direction == west) direction = east;
	}
}
