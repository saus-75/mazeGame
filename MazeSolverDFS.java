import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by julianblacket on 13/05/2016.
 */
public class MazeSolverDFS implements MazeSolver {
    private int maxDist;
    private Node maxNode;

    public MazeSolverDFS() {
        maxDist = -1;
        maxNode = null;
    }


    @Override
    public List<Node> getPathToNodeType(Maze maze, Point start, char type) {
        Node startNode = maze.get((int)start.y, (int) start.x);
        if (startNode == null) {
            System.out.println(start.x + " " + start.y + " invalid");
        }
        int i = start.y;
        int j = start.x;
        List<Node> path = new LinkedList<>();


        boolean foundPathFromNode = false;

        foundPathFromNode = recursiveDFS(maze, path, startNode, i, j, Node.end, 0);

        if (foundPathFromNode) {
            return path;
        } else {
            return null;
        }
    }

    public Node getFurthestFromStart(Maze maze) {
        getPathToNodeType(maze, maze.getStart(), 'z');
        return maxNode;
    }


    private boolean recursiveDFS(Maze maze, List<Node> path, Node current, int i, int j, char type, int dist) {
        if (maze.get(i, j).getState() == Node.wall) {
            //System.out.println("Found a wall");
            return false;
        }
        path.add(current);
        if (maze.get(i, j).getState() == type) {
            return true;
        }
        if (dist > maxDist) {
            maxDist = dist;
            maxNode = current;
        }
        boolean foundPath;
        for (int s = i - 1; s <= i + 1; s++) {
                if (s < 0 || s>= maze.getHeight()) {
                    continue;
                }
            for (int t = j - 1; t <= j + 1; t++) {
                if ((s == i-1 || s== i+1) && (t == j-1 || t == j+1)) {
                    continue;
                }
                if (t < 0 || t >= maze.getWidth()) {
                    continue;
                }
                if (path.contains(maze.get(s, t))) {
                    continue;
                }
                foundPath = recursiveDFS(maze, path, maze.get(s, t), s, t, type, dist+1);
                if (foundPath) {
                    return true;
                }
            }
        }

        path.remove(current);
        return false;
    }
}
