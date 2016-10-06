/**
 * Created by julianblacket on 17/05/2016.
 */
public class FunctionR3Diagonal extends FunctionR3 {

    /**
     * Constructs a diagonal canyon function and have it carve out onto the maze
     * @param range of the x and y domain range at 0
     */
    public FunctionR3Diagonal(double range) {
        super(range);
    }
    
    /**
     * Returns the value of a diagonal canyon function
     * @param x position
     * @param y position
     */
    @Override
    public double getZ(double x, double y) {
        return -sinC((cube(x/range) + cube(y/range)));
    }
}