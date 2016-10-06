
import java.awt.Point;
import java.util.*;

/**
 * Created by julianblacket on 19/05/2016.
 */
public class MazeCarverPaths implements MazeCarver {
    protected int stackType;
    protected boolean backtrack;
    protected double maxRun;
    protected static int lookAhead = 2;
    private int seed;

    private List<NodeRep> sCandidates;
    private List<NodeRep> eCandidates;
    private HashSet<NodeRep> sSeen;
    private HashSet<NodeRep> eSeen;
    private List<NodeRep> solutionPoints;
    private Maze maze;

    private int numSolutions;
    private int maxSolutions;

    private Point start;
    private Point end;

    public MazeCarverPaths(int stackType, boolean backtrack, double maxRun) {
        this.sCandidates = new ArrayList<>();
        this.eCandidates = new ArrayList<>();
        this.solutionPoints = new ArrayList<>();
        this.sSeen = new HashSet<>();
        this.eSeen = new HashSet<>();
        this.stackType = stackType;
        this.backtrack = backtrack;
        this.maxRun = maxRun;
        this.numSolutions = 0;
        this.maxSolutions = 0;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }


    @Override
    public void generateMaze(Maze maze) {
        this.maze = maze;
        int sJ = fixX(start.x); int sI = fixY(start.y);
        int eJ = fixX(end.x); int eI = fixY(end.y);
        NodeRep sCurr = new NodeRep(sI, sJ);
        NodeRep eCurr = new NodeRep(eI, eJ);
        sCandidates.add(sCurr);
        eCandidates.add(eCurr);

        List<Integer> neighbours;
        int move; Point next;
        int sCurrentRun = 0; int eCurrentRun = 0;
        boolean startEmpty = false; boolean endEmpty = false;
        Random r = new Random(this.seed);

        maze.setMazeToWalls();

        while (!eCandidates.isEmpty() || !sCandidates.isEmpty()) {
            if (sCandidates.isEmpty()) {
                startEmpty =  true;
            }
            if (eCandidates.isEmpty()) {
                endEmpty = true;
            }
            boolean validMove = false;
            if (!startEmpty) {
                sSeen.add(new NodeRep(sI, sJ));
                neighbours = getNeighbours(eSeen, sI, sJ);
                if (neighbours.size() > 0 && sCurrentRun < maxRun) {
                    move = getMinNeighbour(neighbours, sI, sJ);
                    next = progressPath(eSeen, move, sI, sJ);
                    if (next != null) {
                        sI = next.y; sJ = next.x;
                        this.sCandidates.add(0, new NodeRep(sI, sJ));
                        validMove = true;
                        sCurrentRun++;
                    }
                }

                if (!validMove) {
                    int nextIndex = 0;
                    if (stackType == STACK_RANDOM) {
                        nextIndex = r.nextInt(sCandidates.size());
                    }
                    sCurr = sCandidates.remove(nextIndex);
                    sCurrentRun = 0;
                    sI = sCurr.geti();
                    sJ = sCurr.getj();
                }
            }
            validMove = false;
            if (!endEmpty) {
                eSeen.add(new NodeRep(eI, eJ));
                neighbours = getNeighbours(sSeen, eI, eJ);
                if (neighbours.size() > 0 && eCurrentRun < maxRun) {
                    move = getMinNeighbour(neighbours, eI, eJ);
                    next = progressPath(sSeen, move, eI, eJ);
                    if (next != null) {
                        eI = next.y; eJ = next.x;
                        this.eCandidates.add(0, new NodeRep(eI, eJ));
                        validMove = true;
                        eCurrentRun++;
                    }
                }

                if (!validMove) {
                    int nextIndex = 0;
                    if (stackType == STACK_RANDOM) {
                        nextIndex = r.nextInt(eCandidates.size());
                    }
                    eCurr = eCandidates.remove(nextIndex);
                    eCurrentRun = 0;
                    eI = eCurr.geti();
                    eJ = eCurr.getj();
                }
            }
        }
        maze.get(maze.getEnd().y, maze.getEnd().x).changeState(Node.path);
        maze.get(maze.getStart().y, maze.getStart().x).changeState(Node.path);
        maze.get(maze.getEnd().y, fixX(maze.getEnd().x)).changeState(Node.path);
        maze.get(fixY(maze.getStart().y), maze.getStart().x).changeState(Node.path);

        maze.get(maze.getStart().y, maze.getStart().x).changeState(Node.start);
        maze.get(maze.getEnd().y, maze.getEnd().x).changeState(Node.end);
    }

    private Point progressPath(Set<NodeRep> oppSeen, int move, int i, int j) {
        boolean path = false;
        if (move == south) {
            path = carveMaze(oppSeen, i, j, lookAhead, 0, lookAhead-1, 0);
            if (path) {
                i += lookAhead;
            }
        } else if (move == north) {
            path = carveMaze(oppSeen, i, j, -lookAhead, 0, -lookAhead+1, 0);
            if (path) {
                i -= lookAhead;
            }

        } else if (move == east) {
            path = carveMaze(oppSeen, i, j, 0, lookAhead, 0, lookAhead-1);
            if (path) {
                j += lookAhead;
            }
        } else if (move == west) {
            path = carveMaze(oppSeen, i, j, 0, -lookAhead, 0, -lookAhead+1);
            if (path) {
                j -= lookAhead;
            }
        }
        if (!path) {
            return null;
        }
        return new Point(j, i);
    }

    private boolean carveMaze(Set<NodeRep> oppSeen, int i, int j,
                           int iMove, int jMove, int iMoveAlt, int jMoveAlt) {
        if (oppSeen.contains(new NodeRep(i + iMove, j + jMove)) ||
                oppSeen.contains(new NodeRep(i + iMoveAlt, j + jMoveAlt)) ) {
            if (numSolutions < maxSolutions) {
                maze.get(i + iMoveAlt, j + jMoveAlt).changeState(Node.path);
                maze.get(i + iMoveAlt, j + jMoveAlt).changeState(Node.start);
                solutionPoints.add(new NodeRep(i + iMoveAlt, j + jMoveAlt));
                numSolutions++;
            }
            return false;
        } else {
            maze.get(i + iMove, j + jMove).changeState(Node.path);
            maze.get(i + iMoveAlt, j+ jMoveAlt).changeState(Node.path);
            return true;
        }
    }

    private int getMinNeighbour(List<Integer> neighbours, int i, int j) {
        Random r = new Random();
        int move = r.nextInt(neighbours.size());
        double min = Double.POSITIVE_INFINITY;
        for (int c : neighbours) {
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
        return move;
    }

    private List<Integer> getNeighbours(HashSet<NodeRep> oppSeen, int i, int j) {
        List<Integer> neighbours = new LinkedList<>();
        if (i + lookAhead >= 0 && i + lookAhead < maze.getHeight()) {
            if (maze.get(i+lookAhead, j).getState() == Node.wall ||
                    oppSeen.contains(new NodeRep(i+lookAhead, j))) {
                neighbours.add(south);
            }
        }
        if (i-lookAhead >= 0 && i - lookAhead < maze.getHeight()) {
            if (maze.get(i-lookAhead, j).getState() == Node.wall ||
                    oppSeen.contains(new NodeRep(i-lookAhead, j))) {
                neighbours.add(north);
            }
        }
        if (j+lookAhead >= 0 && j+lookAhead < maze.getWidth()) {
            if (maze.get(i, j+lookAhead).getState() == Node.wall ||
                    oppSeen.contains(new NodeRep(i, j+lookAhead))) {
                neighbours.add(east);
            }
        }
        if (j-lookAhead >= 0 && j-lookAhead < maze.getWidth()) {
            if (maze.get(i, j-lookAhead).getState() == Node.wall ||
                    oppSeen.contains(new NodeRep(i, j-lookAhead))) {
                neighbours.add(west);
            }
        }
        return neighbours;
    }

    private int fixX(Integer coord) {
        if (coord == 0) {
            coord += 1;
        } else if (coord == maze.getWidth()-1) {
            coord -= 1;
        }
        return coord;

    }

    private int fixY(Integer coord) {
        if (coord == 0) {
            coord += 1;
        } else if (coord == maze.getHeight()-1) {
            coord -= 1;
        }
        return coord;
    }

    public void setMaxSolutions(int num) {
        this.maxSolutions = num;
    }
    public void setSeed(int seed) {
        this.seed = seed;
    }
}