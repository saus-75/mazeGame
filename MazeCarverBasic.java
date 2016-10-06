
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by julianblacket on 10/05/2016.
 */

/**
 * A {@code MazeCarver} which implements the a modified version of the recursive backtracker algorithm. Has the chance of creating
 * loops and mulitple solutions. The neighbour selection strategy is based on the weight of the neighbouring nodes.
 * The stack can be randomised, so when we DFSed as far as possible, the next node to dfs from is random.
 * The backtrack can be turned on to allow visited nodes to be added back to the candidates list.
 * The length of maximum dfs path from a node can be set with the maxRun variable.
 */
public class MazeCarverBasic implements MazeCarver {
    protected int stackType;
    protected boolean backtrack;
    protected static double maxRun;
    protected static final int lookAhead = 2;
    private long seed;
    private Point start;
    private Point end;

    private List<NodeRep> candidates;

    /**
     * Initialises the maze carver
     * @param stackType The type of stack as defined in the {@code MazeCarver} interface.
     * @param backtrack The backtrack boolean as defined in the {@code MazeCarver} interface.
     * @param maxRun The maxRun length.
     */
    public MazeCarverBasic(int stackType, boolean backtrack, double maxRun) {
        this.candidates = new ArrayList<>();
        this.stackType = stackType;
        this.backtrack = backtrack;
        this.maxRun = maxRun;
        this.seed = System.currentTimeMillis();
    }

    /**
     * Generates the paths and walls in the given maze.s
     * @param maze The maze whose walls and paths will be set.
     */
    @Override
    public void generateMaze(Maze maze) {
        Point start = maze.getStart();
        Random r = new Random(seed);
        maze.setMazeToWalls();
        if (seed != -1) {
            r = new Random(seed);
        } else {
            r = new Random();
        }
        int i = start.y+1; int j = start.x+1; int move, currentRun = 0;
        NodeRep current = new NodeRep(i, j);
        candidates.add(current);
        LinkedList<Integer> validNeighbours;
        Set<Node> seen = new HashSet<>();

        while (!candidates.isEmpty()) {
            validNeighbours = new LinkedList<>();
            if (i + lookAhead > 0 && i + lookAhead < maze.getHeight()) {
                if (maze.get(i+lookAhead, j).getState() == Node.wall) {
                    validNeighbours.add(south);
                }
            }
            if (i-lookAhead > 0 && i - lookAhead < maze.getHeight()) {
                if (maze.get(i-lookAhead, j).getState() == Node.wall) {
                    validNeighbours.add(north);
                }
            }
            if (j+lookAhead > 0 && j+lookAhead < maze.getWidth()) {
                if (maze.get(i, j+lookAhead).getState() == Node.wall) {
                    validNeighbours.add(east);
                }
            }
            if (j-lookAhead > 0 && j-lookAhead < maze.getWidth()) {
                if (maze.get(i, j-lookAhead).getState() == Node.wall) {
                    validNeighbours.add(west);
                }
            }
            if (validNeighbours.size() > 0 && currentRun < maxRun) {
                if (backtrack) {
                    candidates.add(0, current);
                }
                seen.add(maze.get(i, j));
                move = validNeighbours.get(r.nextInt(validNeighbours.size()));
                double min = Double.POSITIVE_INFINITY;
                // get the min valid neighbour path
                for (int c : validNeighbours) {
                    if (c == north) {
                        if (maze.get(i-1, j).getNorth() < min) {
                            move = north;
                            min = maze.get(i-1, j).getNorth();
                        }
                    } else if (c == south) {
                        if (maze.get(i+1, j).getSouth() < min) {
                            move = south;
                            min = maze.get(i+1, j).getSouth();
                        }
                    } else if (c == east) {
                        if (maze.get(i, j+1).getEast() < min) {
                            move = east;
                            min = maze.get(i, j+1).getEast();
                        }
                    } else if (c == west) {
                        if (maze.get(i, j-1).getWest() < min) {
                            move = west;
                            min = maze.get(i, j-1).getWest();
                        }
                    }
                }
                if (move == south) {
                    maze.get(i + lookAhead, j).changeState(Node.path);
                    maze.get(i + lookAhead-1, j).changeState(Node.path);
                    i += lookAhead;
                } else if (move == north) {
                    maze.get(i - lookAhead, j).changeState(Node.path);
                    maze.get(i - lookAhead+1, j).changeState(Node.path);
                    i -= lookAhead;
                } else if (move == east) {
                    maze.get(i, j + lookAhead).changeState(Node.path);
                    maze.get(i, j + lookAhead-1).changeState(Node.path);

                    j += lookAhead;
                } else if (move == west) {
                    maze.get(i, j - lookAhead).changeState(Node.path);
                    maze.get(i, j - lookAhead+1).changeState(Node.path);
                    j -= lookAhead;
                }
                currentRun++;
                candidates.add(0, new NodeRep(i, j));
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
            maze.get(start.y+1, start.x).changeState(Node.path);
        } else {
            maze.get(start.y, start.x+1).changeState(Node.path);
        }
        maze.get(start.y, start.x).changeState(Node.path);
        maze.get(start.y, start.x).changeState(Node.start);
    }

    /**
     * Sets the start point of the path
     * @param start
     */
    public void setStart(Point start) {
        this.start = start;
    }

    /**
     * sets the end point of the path.
     * @param end
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * Sets the seed of the random function.
     * @param seed
     */
    public void setSeed(int seed) {
        this.seed = seed;
    }


}