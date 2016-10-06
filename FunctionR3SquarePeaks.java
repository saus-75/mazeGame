/**
 * Created by julianblacket on 14/05/2016.
 */
public class FunctionR3SquarePeaks extends FunctionR3 {
    private double numPeaks;
    private double height;
    
    /**
     * Constructs square peak functions and have it carve out onto the maze
     * @param range of the x and y domain range at 0
     * @param numPeaks the number of peaks
     * @param height the height of the peaks
     */
    public FunctionR3SquarePeaks(double range, double numPeaks, double height) {
        super(range);
        this.numPeaks = numPeaks;
        this.height = height;
    }
    
    /**
     * Returns the Square Peaks function
     * @param x position
     * @param y position
     */
    @Override
    public double getZ(double x, double y) {
        return height*Math.sin((numPeaks*Math.PI/range)*x)*Math.cos((numPeaks*Math.PI/range)*y);
    }
}