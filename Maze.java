
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by julianblacket on 17/05/2016.
 */

/**
 * The {@code Maze} representation used to store {@code Node}s in a grid structure.
 * The Maze's width, height, start, and end {@code Point}s are configurable.
 * Provides methods to get useful information about the {@code Maze} state.
 */
public class Maze {
    public List<List<Node>> nodes;
    private int width;
    private int height;
    private Point start;
    private Point end;
    public Maze() {
        this.width = 0;
        this.height = 0;
        start = new Point(0,0);
        end = new Point(width-1, height-1);
        initialiseNodes();
    }

    private void initialiseNodes() {
        this.nodes = new ArrayList<>(height);
        for (int i = 0; i < height; i++) {
            nodes.add(i, new ArrayList<Node>(width));
            for (int j = 0; j < width; j++) {
                Node n = new Node(i ,j);
                nodes.get(i).add(j, n);
            }
        }
    }

    /**
     * Resets the maze to the given width and height values. The state of all contained {@codes Node}s is reset.
     * @param width The width of the reset maze.
     * @param height The height of the reset maze.
     */
    public void resizeMaze(int width, int height){
        this.width = width;
        this.height = height;
        this.initialiseNodes();
    }

    /**
     * Gets the {@code Node} located at the given indexes
     * @param i The 'row' of the node as an index within a 2d array.
     * @param j The 'column' of the node as an index within a 2d array.
     * @return The {@code Node} located at the given indexes
     */
    public Node get(int i, int j) {
        return this.nodes.get(i).get(j);
    }

    /**
     * Gets the {@code Point} of the node, where the x corresponds to the column (j)
     * and y cooresponds to the row (i)
     * @param node
     * @return The {@code Node} located at the given indexes
     */
    public Point getNodePosition(Node node) {
        int i =0;
        for (List<Node> list : this.nodes) {
            if (list.contains(node)) {
                return new Point(list.indexOf(node), i);
            }
            i++;
        }
        return null;
    }

    /**
     * Given a {@code Node} in the maze and the distance from that node that defines a 'neighbouring' node,
     * returns a {@code List} of neighbouring {@code Nodes} with the 'path' state.
     * @param node The node whose neighbours will be found.
     * @param lookAhead The distance from the given node that defines a neighbour.
     * @return A {@code List} of nodes which are 'paths' and are +- the lookahead from the given node.
     */
    public List<Node> getNeighbouringPaths(Node node, int lookAhead) {
        List<Node> neighbours = new ArrayList<>();
        Point point = getNodePosition(node);
        int i, j;
        for (i = point.y-lookAhead; i <= point.y+lookAhead; i+=lookAhead) {
            for (j = point.x-lookAhead; j <= point.x +lookAhead; j+=lookAhead) {
                if ((i == point.y-lookAhead || i == point.y+lookAhead) && (j == point.x-lookAhead || j == point.x+lookAhead)) {
                    continue;
                }
                if (i == point.y && j == point.x) {
                    continue;
                }
                if (i > height - 1 || i < 0 || j < 0 || j > width-1) {
                    continue;
                }
                if (get(i, j).getState() == Node.wall) {
                    continue;
                }
                neighbours.add(get(i, j));
            }
        }
        return neighbours;
    }

    /**
     * Gets the width of the maze.
     * @return The {@code Integer} width of the maze.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Gets the height of the maze.
     * @return The {@code Integer} height of the maze.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Sets the start {@code Point} of the maze
     * @param start A {@code Point} where the x coordinate is the
     *            column (j) and y coordinate is the row (i)
     */
    public void setStart(Point start) {
        this.start = start;
    }

    /**
     * Gets the start {@code Point }of the maze.
     * @return The start {@code Point } of the maze, where the x coordinate is the row (j)
     *          and the y coordinate is the column (i).
     */
    public Point getStart() {
        return start;
    }

    /**
     * Sets the end {@code Point} of the maze
     * @param end A {@code Point} where the x coordinate is the
     *            column (j) and y coordinate is the row (i)
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * Gets the end {@code Point} of the maze.
     * @return The end {@code Point } of the maze, where the x coordinate is the row (j)
     *          and the y coordinate is the column (i).
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Sets every {@code Node} in the maze to a {@Code Node.Wall} state
     */
    public void setMazeToWalls() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                nodes.get(i).get(j).changeState(Node.wall);
            }
        }
    }
    /**
     * Sets every {@code Node} in the maze to a {@Code Node.Path} state
     */
    public void setMazeToPaths() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                nodes.get(i).get(j).changeState(Node.path);
            }
        }
    }

    /**
     * Gets the entire list of nodes
     * @return A 2 dimensional {@code List} representation of the maze's {@code Node}s.
     */
	public List<List<Node>> getNodes() {
		return nodes;
	}

    
}
