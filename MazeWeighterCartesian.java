
/**
 * Created by julianblacket on 13/05/2016.
 */

/**
 * Weights the nodes of the maze with the given {@code FunctionR3}, where the origin of the x,y plane is centered
 * in the middle of the maze.
 */
public class MazeWeighterCartesian implements MazeWeighter {

    private FunctionR3 function;
    private int domainX;
    private int domainY;
    private int seed;

    /**
     * Converts an index to an x coordinate in the x, y plane, where the index of 0 is the lowest
     * point of the x axis.
     * @param i The index in an array
     * @return The mapping to the x axis.
     */
    public int iToX(int i) {
        return -(((domainX-1)/2)-i);
    }

    /**
     * Converts an index to an y coordinate in the x, y plane, where the index of 0 is the highest
     * point of the y axis.
     * @param i The index in an array
     * @return The mapping to the x axis.
     */
    public int iToY(int i) {
        return (((domainY-1)/2)-i);
    }

    /**
     * Initialises the {@code MazeWeighterCartesian} with the given {@code FucntionR3}
     * @param function
     */
    public MazeWeighterCartesian(FunctionR3 function) {
        this.function = function;
    }

    /**
     * Weights the {@code Node}s of the {@code Maze} with the set {@code FuntionR3}
     * @param maze The {@code} whose nodes will be weighted.
     */
    public void weightMaze(Maze maze) {
        domainX = maze.getWidth();
        domainY = maze.getHeight();
        double north;
        double south;
        double east;
        double west;
        for (int i = 0; i < maze.getHeight(); i++) {
            for (int j = 0; j < maze.getWidth(); j++) {
                if (i == 0) {
                    north = 0;
                } else {
                    north = maze.get(i, j).getNorth() + function.getZ(iToX(j), iToY(i-1));
                }
                maze.get(i, j).setNorth(north);
                if (i == maze.getHeight()-1) {
                    south = 0;
                } else {
                    south = maze.get(i, j).getSouth() + function.getZ(iToX(j), iToY(i+1));
                }
                maze.get(i, j).setSouth(south);
                if (j == 0) {
                    west = 0;
                } else {
                    west = maze.get(i, j).getWest() + function.getZ(iToX(j-1), iToY(i));
                }
                maze.get(i, j).setWest(west);
                if (j == maze.getWidth() - 1) {
                    east = 0;
                } else {
                    east = maze.get(i, j).getEast() + function.getZ(iToX(j+1), iToY(i));
                }
                maze.get(i, j).setEast(east);
            }
        }
    }
}