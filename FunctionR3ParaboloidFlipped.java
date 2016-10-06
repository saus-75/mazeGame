/**
 * Created by julianblacket on 14/05/2016.
 */
public class FunctionR3ParaboloidFlipped extends FunctionR3Paraboloid {
	
    /**
     * Constructs a flipped paraboloid function and have it carve out onto the maze
     * @param range of the x and y domain range at 0
     * @param numRoots the number flipped paraboloid
     */
    public FunctionR3ParaboloidFlipped(double range, double numRoots) {
        super(range, numRoots);
    }

    /**
     * Returns the value of the Flipped Paraboloid function
     * @param x position
     * @param y position
     */
    public double getZ(double x, double y) {
        return -super.getZ(x, y);
    }
}