import java.util.Random;

/**
 * Created by julianblacket on 24/05/2016.
 */
public class FunctionR3Polynomial extends FunctionR3 {

    private double height;
    private double xDegree;
    private double yDegree;
    private double xCoefficient;
    private double yCoefficient;

    /**
     * Constructs a randomised Polynomial and have it carve out onto the maze
     * @param range of the x and y domain range at 0
     */
    public FunctionR3Polynomial(int range) {
        super(range);
        setToRandom();
    }
    
    /**
     * Returns the value of the Polynomial function
     * @param x position
     * @param y position
     */
    @Override
    public double getZ(double x, double y) {
        double xR = x/range; double yR = y/range;
        return height*(Math.pow(xR, xDegree) - xCoefficient*xR)*(Math.pow(yR, yDegree) - yCoefficient*yR);
    }
    
    /**
     * Sets the height
     * @param h
     */
    public void setHeight(double h) {
        this.height = h;
    }
    
    /**
     * Sets the exponent of the x variable
     * @param x
     */
    public void setxDegree(double x) {
        this.xDegree = x;
    }
    
    /**
     * Sets the exponent of the y variable
     * @param y
     */
    public void setyDegree(double y) {
        this.yDegree = y;
    }
    
    /**
     * Sets the x co-efficient
     * @param x
     */
    public void setxCoefficient(double x) {
        this.xCoefficient = x;
    }
    
    /**
     * Sets the y co-efficient
     * @param y
     */
    public void setyCoefficient(double y) {
        this.yCoefficient = y;
    }
    
    /**
     * Setting the function to be randomised
     */
    public void setToRandom() {
        Random r = new Random(System.currentTimeMillis());
        int height = r.nextInt();
        int xC = r.nextInt();
        int yC = r.nextInt();
        int xD = r.nextInt();
        int yD = r.nextInt();
        setHeight(height);
        setxCoefficient(xC);
        setyCoefficient(yC);
        setxDegree(xD);
        setyDegree(yD);
    }
}