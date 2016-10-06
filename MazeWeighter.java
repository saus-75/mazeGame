import java.io.Serializable;

/**
 * Created by julianblacket on 11/05/2016.
 */

/**
 * The interface for strategies which weight the nodes of the maze.
 */
public interface MazeWeighter extends Serializable {

    /**
     * Weights the nodes of the given maze.
     * @param maze The {@code} whose nodes will be weighted.
     */
    public void weightMaze(Maze maze);

}
