/**
 * Created by julianblacket on 14/05/2016.
 */
public class FunctionR3Paraboloid extends FunctionR3 {
    private double scaler;
    private double range;
    
    /**
     * Constructs a diagonal canyon function and have it carve out onto the maze
     * @param range of the x and y domain range at 0
     * @param scaler will scale the function based on the given size
     */
    public FunctionR3Paraboloid(double range, double scaler) {
        super(range);
        this.scaler = scaler;
    }

    /**
     * Returns the value of the Paraboloid function
     * @param x position
     * @param y position
     */
    public double getZ(double x, double y) {
        return super.sqr(x)/scaler + super.sqr(y)/scaler;
    }
}