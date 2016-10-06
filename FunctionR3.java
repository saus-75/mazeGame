import java.io.Serializable;

/**
 * Created by julianblacket on 14/05/2016.
 */
public abstract class FunctionR3 implements Serializable {
    protected double range;
    
    /**
     * An abstract class that defines several helper functions.
     * Child classes must implement these helper function.
     * @param range of the x and y domain range at 0
     */
    public FunctionR3(double range) {
        this.range = range;
    }
    
    /**
     * Abstract class for the functions
     * @param x position
     * @param y position
     * @return Returns the function value
     */
    public abstract double getZ(double x, double y);
    
    /**
     * sin C function
     * @param x position
     * @return a sin c value at pos x
     */
    public double sinC(double x) {
        if (x == 0) {
            return 1;
        }
        return Math.sin(x)/x;
    }
    
    /**
     * square root function
     * @param x position
     * @return a square root value at pos x
     */
    protected double sqr(double x) {
        return Math.pow(x, 2);
    }

    /**
     * cubed function
     * @param x position
     * @return a cubed value at pos x
     */
    protected double cube(double x) {
        return Math.pow(x, 3);
    }
}
