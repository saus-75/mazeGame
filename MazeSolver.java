
import java.awt.Point;
import java.util.List;

/**
 * Created by julianblacket on 13/05/2016.
 */

/**
 * The interface for a maze solver strategy.
 */
public interface MazeSolver {
    /**
     * Returns a path on the given maze between the given {@code Point} and a node of the given type. The {@code Point}s x coordinate is the row (j)
     * and y coordinate is the column (i). Not guaranteed to be the shortest path. If no path can be found returns an empty list.
     * @precondition The given point is a valid path point on the maze.
     * @param maze The maze on which the path will be searched.
     * @param start The start point of the path search.
     * @param type The type of node to search for
     * @return A {@code List} of nodes from the {@code Node} at the given {@code Point} to a node of {@code char} type.
     */
    public List<Node> getPathToNodeType(Maze maze, Point start, char type);
}
