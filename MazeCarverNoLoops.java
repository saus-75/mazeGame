import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by julianblacket on 10/05/2016.
 */
/**
 * A {@code MazeCarver} which implements the a modified version of the recursive backtracker algorithm.
 * Does not create loops.
 * The neighbour selection strategy is based on the weight of the neighbouring nodes.
 * The stack can be randomised, so when we DFSed as far as possible, the next node to dfs from is random.
 * The backtrack can be turned on to allow visited nodes to be added back to the candidates list.
 * The length of maximum dfs path from a node can be set with the maxRun variable.
 */
public class MazeCarverNoLoops implements MazeCarver {
    protected int stackType;
    protected boolean backtrack;
    protected static double maxRun;
    protected long seed;
    protected static final int lookAhead = 2;

    private Point start;
    private Point end;

    private List<NodeRep> candidates;
    private Map<NodeRep, boolean[]> possibleMoves;

    /**
     * Initialises the maze carver
     * @param stackType The type of stack as defined in the {@code MazeCarver} interface.
     * @param backtrack The backtrack boolean as defined in the {@code MazeCarver} interface.
     * @param maxRun The maxRun length.
     */
    public MazeCarverNoLoops(int stackType, boolean backtrack, double maxRun) {
        this.candidates = new ArrayList<>();
        this.possibleMoves = new HashMap<>();
        this.stackType = stackType;
        this.backtrack = backtrack;
        this.maxRun = maxRun;
        seed = System.currentTimeMillis();
    }

    /**
     * Generates the paths and walls in the given maze.s
     * @param maze The maze whose walls and paths will be set.
     */
    @Override
    public void generateMaze(Maze maze) {
        Random r = new Random(seed);
        maze.setMazeToWalls();
        List<List<Node>> nodes = maze.getNodes();
        NodeRep current = new NodeRep(1,1);
        candidates.add(current);
        LinkedList<Integer> validNeighbours;
        boolean isConnected;
        int i = 1; int j = 1; int move, currentRun = 0;

        while (!candidates.isEmpty()) {
            validNeighbours = new LinkedList<>();
            isConnected = false;
            if (i + lookAhead >= 0 && i + lookAhead < nodes.size()) {
                if (nodes.get(i+lookAhead).get(j).getState() == Node.wall) {
                    validNeighbours.add(south);
                    isConnected = true;
                }
            }
            if (i-lookAhead >= 0 && i - lookAhead < nodes.size()) {
                if (nodes.get(i-lookAhead).get(j).getState() == Node.wall) {
                    validNeighbours.add(north);
                    isConnected = true;
                }
            }
            if (j+lookAhead >= 0 && j+lookAhead < nodes.size()) {
                if (nodes.get(i).get(j+lookAhead).getState() == Node.wall) {
                    validNeighbours.add(east);
                    isConnected = true;
                }
            }
            if (j-lookAhead >= 0 && j-lookAhead < nodes.size()) {
                if (nodes.get(i).get(j-lookAhead).getState() == Node.wall) {
                    validNeighbours.add(west);
                    isConnected = true;
                }
            }
            if (isConnected && currentRun < maxRun) {
                //System.out.println("found neibour");
                if (backtrack) {
                    candidates.add(0, current);
                }
                move = validNeighbours.get(r.nextInt(validNeighbours.size()));
                double min = Double.POSITIVE_INFINITY;
                // get the min valid neighbour path
                for (int c : validNeighbours) {
                    if (c == north) {
                        if (nodes.get(i-1).get(j).getNorth() < min) {
                            move = north;
                            min = nodes.get(i-1).get(j).getNorth();
                        }
                    } else if (c == south) {
                        if (nodes.get(i+1).get(j).getSouth() < min) {
                            move = south;
                            min = nodes.get(i+1).get(j).getSouth();
                        }
                    } else if (c == east) {
                        if (nodes.get(i).get(j+1).getEast() < min) {
                            move = east;
                            min = nodes.get(j+1).get(j).getEast();
                        }
                    } else if (c == west) {
                        if (nodes.get(i).get(j-1).getWest() < min) {
                            move = west;
                            min = nodes.get(j-1).get(j).getWest();
                        }
                    }
                }
                if (move == south) {
                    nodes.get(i + lookAhead).get(j).changeState(Node.path);
                    nodes.get(i + lookAhead-1).get(j).changeState(Node.path);
                    i += lookAhead;
                } else if (move == north) {
                    nodes.get(i - lookAhead).get(j).changeState(Node.path);
                    nodes.get(i - lookAhead+1).get(j).changeState(Node.path);
                    i -= lookAhead;
                } else if (move == east) {
                    nodes.get(i).get(j + lookAhead).changeState(Node.path);
                    nodes.get(i).get(j + lookAhead-1).changeState(Node.path);
                    j += lookAhead;
                } else if (move == west) {
                    nodes.get(i).get(j - lookAhead).changeState(Node.path);
                    nodes.get(i).get(j - lookAhead+1).changeState(Node.path);
                    j -= lookAhead;
                }
                currentRun++;
                this.candidates.add(0, new NodeRep(i, j));
            } else {
                int nextIndex = 0;
                if (stackType == STACK_RANDOM) {
                    nextIndex = r.nextInt(candidates.size());
                }
                current = candidates.remove(nextIndex);
                currentRun = 0;
                i = current.geti();
                j = current.getj();
            }


        }
        if (r.nextInt(19) > 4) {
            nodes.get(1).get(0).changeState(Node.path);
        } else {
            nodes.get(0).get(1).changeState(Node.path);
        }
        nodes.get(0).get(0).changeState(Node.path);
        nodes.get(0).get(0).changeState(Node.start);
        maze.nodes = nodes;
    }
    /**
     * Sets the start point of the path
     * @param start
     */
    @Override
    public void setStart(Point start) {
        this.start = start;
    }

    /**
     * sets the end point of the path.
     * @param end
     */
    @Override
    public void setEnd(Point end) {
        this.start = end;
    }

    /**
     * Sets the seed of the random function.
     * @param seed
     */
    @Override
    public void setSeed(int seed) {
        this.seed = seed;
    }


}
