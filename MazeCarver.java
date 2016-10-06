
import java.awt.Point;
import java.io.Serializable;

/**
 * Created by julianblacket on 20/05/2016.
 */

/**
 * The interface for classes that create the paths and walls in a maze. Implementations can use the stack, backtrack and max run
 * variables to allow users to change the generation algorithm. Any random choices in the algorithm can be seeded with a value.
 * The start, and end points are configurable.
 */
public interface MazeCarver extends Serializable {

    public static final int north = 0;
    public static final int south = 1;
    public static final int east = 2;
    public static final int west = 3;

    // Determines how the next node is selected when
    // the max run is reached or no valid neighbours exist
    public static final int STACK_NORMAL = 0;
    public static final int STACK_RANDOM = 1;
    // DETERMINES WHETHER VISITED NODES CAN BE REVISITED
    public static final boolean BACKTRACK = true;
    public static final boolean NO_BACKTRACK = false;

    public static final double NO_MAX_RUN = Double.POSITIVE_INFINITY;
    public static final double DIAGONAL_MAX_RUN = Math.sqrt(NewGame.size * NewGame.size);

    public void generateMaze(Maze maze);

    public void setStart(Point start);

    public void setEnd(Point end);

    public void setSeed(int seed);
}
