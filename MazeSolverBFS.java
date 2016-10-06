import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by julianblacket on 21/05/2016.
 */

/**
 * A maze solver which uses the Breadth First Search algorithm to find paths on the maze.
 */
public class MazeSolverBFS implements MazeSolver {
    private int max;
    private Node maxNode;
    /**
     * Initialises the maze solver.
     */
    public MazeSolverBFS(){

    }

    /**
     * Uses the BFS algorithm to find the {@code Node} with the furthest distance from the start point of the given maze.
     * @param maze The maze on which the path will be found
     * @return The {@code Node} whose shortest path to the start point is equal to or greater than any other point on the maze.
     */
    public Node getFurthestFromStart(Maze maze) {
        getPathToNodeType(maze, maze.getStart(), 'z');
        return maxNode;
    }

    /**
     * Returns the shortest path from the given point to a {@code Node} of the given type. See {@code MazeSolver} interface
     * for details.
     * @param maze The maze on which the path will be searched.
     * @param start The start point of the path search.
     * @param type The type of node to search for
     * @return
     */
    @Override
    public List<Node> getPathToNodeType(Maze maze, Point start, char type) {
        Queue<Node> toDo = new LinkedList<Node>();
        Set<Node> seen = new HashSet<>();
        List<Node> neighbours = null;
        Map<Node, Node> prevs = new HashMap<>();
        Map<Node, Integer> dist = new HashMap<>();
        List<Node> path = new LinkedList<>();
        Node curr = maze.get(start.y, start.x);
        toDo.add(curr);
        dist.put(curr, 0);

        while (!toDo.isEmpty()) {
            curr = toDo.remove();
            if (curr.getState() == type) {
                break;
            }
            seen.add(curr);
            neighbours = maze.getNeighbouringPaths(curr, 1);
            for (Node n : neighbours) {
                if (dist.containsKey(n)) {
                    if (dist.get(n) > dist.get(curr) + 1) {
                        dist.put(n, dist.get(curr) + 1);
                    }
                }
                if (seen.contains(n)) {
                    continue;
                }
                dist.put(n, dist.get(curr) + 1);
                toDo.add(n);
                prevs.put(n, curr);
            }
        }
        for (Node n : seen) {
            if (dist.get(n) > max) {
                max = dist.get(n);
                maxNode = n;
            }
        }
        Node s = maze.get(start.y, start.x);
        while (!curr.equals(s)) {
            path.add(curr);
            curr = prevs.get(curr);
        }
        return path;
    }


}
