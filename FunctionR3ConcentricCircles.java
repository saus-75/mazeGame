/**
 * Created by julianblacket on 14/05/2016.
 */
public class FunctionR3ConcentricCircles extends FunctionR3 {
    private double numRoots;
    
    /**
     * Constructs a concentric circle function and have it carve out onto the maze
     * @param range of the x and y domain range at 0
     * @param numRoots the number concentrics circles
     */
    public FunctionR3ConcentricCircles(double range, double numRoots) {
        super(range);
        this.numRoots = numRoots;
    }
    
    /**
     * Returns the value of the concentric circle function
     * @param x position
     * @param y position
     */
    @Override
    public double getZ(double x, double y) {

        return sinC(Math.sin(Math.sqrt(super.sqr(x) + super.sqr(y))));
    }
}