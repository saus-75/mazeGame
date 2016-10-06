import java.io.Serializable;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by julianblacket on 15/05/2016.
 */
public class PiecewiseFunctionR3 extends FunctionR3 {
    public static final int ALL = 0;
    public static final int LOWER = 1;
    public static final int UPPER = 2;
    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    private NavigableMap<Integer, FunctionRep> functions;
    private int top; // key of the top function
    public PiecewiseFunctionR3(double range) {
        super(range);
        this.functions = new TreeMap<>();
        //System.out.println(xBound() + " " +  yBound());
    }

    private int xBound() {
        return (int) (range)/2;
    }

    private int yBound() {
        return (int) (range)/2;
    }

    /**
     * Adds a function to the piecewise function, defined in the given range (inclusive of boundary).
     * Conflicts created by overlapping regions is handled by the layer argument. Higher layers take precedence.
     * Does not delete 'covered' layers.
     * @param function The sub {@code FunctionR3} to be added to the piecewise function
     * @param layer The priority of the function. Used to handle conflicts in domain.
     * @param x The bottom left coordinate of the domain of the sub function.
     * @param y The bottom right coordinate of the domain of the sub function.
     * @param xh The x coord of the top right of the domain of the sub function.
     * @param yh The y coord of the top right of the domain of the sub function.
     */
    public void addFunction(FunctionR3 function, int layer, double x, double y, double xh, double yh) {
        functions.put(layer, new FunctionRep(function, x, y, xh, yh));
        if (layer > top) {
            top = layer;
        }
    }

    /**
     * Adds a {@code FunctionR3} defined in a triangular region in the given layer. Replaces the function at the given layer.
     * @param function The {@code FunctionR3} which is defined over the given region.
     * @param layer The layer to store the function.
     * @param x The x coordinate of the first point of the triangular region.
     * @param y The y coordinate of the first point of the triangular region.
     * @param xm The x coordinate of the second point of the triangular region.
     * @param ym The y coordinate of the second point of the triangular region.
     * @param xh The x coordinate of the third point of the triangular region.
     * @param yh The y coordinate of the third point of the triangular region.
     */
    public void addTriFunction(FunctionR3 function, int layer, double x, double y, double xm, double ym, double xh, double yh) {
        functions.put(layer, new TriangularFunctionRep(function, x, y, xm, ym, xh, yh));
        if (layer > top) {
            top = layer;
        }
    }

    /**
     * Pushes a sub {@code FunctionR3} to the top layer in the given range (inclusive of boundary).
     * @param function The {@code FunctionR3} to add to the top layer
     * @param x The lower left x coordinate of the domain of the sub function.
     * @param y The lower right y coordinate of the domain of the sub function.
     * @param xh The upper right x coordinate of the domain of the sub function.
     * @param yh The upper right y coordinate of the domain of the sub function.
     */
    public void pushOrthogonalFunction(FunctionR3 function, double x, double y, double xh, double yh) {

        addFunction(function, top++, x, y, xh, yh);
    }

    /**
     * Puts a {@code FunctionR3} defined in a triangular region on the top 'layer'.
     * @param f The {@code FunctionR3} which is defined over the given region.
     * @param xl The x coordinate of the first point of the triangular region.
     * @param yl The y coordinate of the first point of the triangular region.
     * @param xm The x coordinate of the second point of the triangular region.
     * @param ym The y coordinate of the second point of the triangular region.
     * @param xh The x coordinate of the third point of the triangular region.
     * @param yh The y coordinate of the third point of the triangular region.
     */
    public void pushTriFunction(FunctionR3 f, double xl, double yl, double xm, double ym, double xh, double yh) {
        addTriFunction(f, top++, xl, yl, xm, ym, xh, yh);
    }

    public void clearFunction() {
        this.functions.clear();
        top = 0;
    }

    @Override
    public double getZ(double x, double y) {
        FunctionRep f;
        for (Integer i : functions.descendingKeySet()) {
            f = functions.get((i));
            if (f.isInRange(x, y)) {
                return f.getFunction().getZ(x, y);
            }
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Covers entire region with given {@code FunctionR3}. Deletes all existing {@code FunctionR3}'s in the process.
     * @param f The {@code FunctionR3} to cover the region.
     */
    public void coverRegion(FunctionR3 f) {
        clearFunction();
        pushOrthogonalFunction(f, -xBound(), -yBound(), xBound(), yBound());
    }

    /**
     * Divides the region into two vertical parts and places {@code f} on the left half and g on the right half.
     * REGION == ALL: Entire domain is covered with first function on left half and second on right half.
     * REGION == LOWER: Lower half of domain is covered with first function on lower left and second on lower right.
     * REGION == HIGHER: Upper half of domain is covered with first function on upper left and second on upper right.
     * @param REGION REGION type to be covered to be covered (as defined in static variables).
     * @param f {@code FunctionR3} for left half of vertical division
     * @param g {@code FunctionR3} for right half of vertical division
     */
    public void divideVertical(int REGION, FunctionR3 f, FunctionR3 g) {
        if (REGION == ALL) {
            pushOrthogonalFunction(f, -xBound(), -yBound(), 0, yBound());
            pushOrthogonalFunction(g, 0, -yBound(), xBound(), yBound());
        } else if (REGION == LOWER) {
            pushOrthogonalFunction(f, -xBound(), -yBound(), 0, 0);
            pushOrthogonalFunction(g, 0, -yBound(), xBound(), 0);
        } else { // REGION == UPPER
            pushOrthogonalFunction(f, -xBound(), 0, 0, yBound());
            pushOrthogonalFunction(g, 0, 0, xBound(), yBound());
        }
    }
    /**
     * Divides the region into two horizonal parts and places {@code f} on the lower half and g on the upper half.
     * REGION == ALL: Entire domain is covered with first function on left half and second on right half.
     * REGION == RIGHT: Right half of domain is covered with first function on bottom right and second on upper right.
     * REGION == LEFT: Left half of domain is covered with first function on bottom left and second on upper left.
     * @param REGION Region to be covered (as defined in static variables).
     * @param f {@code FunctionR3} for bottom half of horizontal division
     * @param g {@code FunctionR3} for upper half of horizontal division
     */
    public void divideHorizontal(int REGION, FunctionR3 f, FunctionR3 g) {
        if (REGION == ALL) {
            pushOrthogonalFunction(f, -xBound(), -yBound(), xBound(), 0);
            pushOrthogonalFunction(g, -xBound(), 0, xBound(), yBound());
        } else if (REGION == RIGHT) {
            pushOrthogonalFunction(f, 0, -yBound(), xBound(), 0);
            pushOrthogonalFunction(g, 0, 0, xBound(), yBound());
        } else { // REGION == LEFT
            pushOrthogonalFunction(f, -xBound(), -yBound(), 0, 0);
            pushOrthogonalFunction(g, -xBound(), 0, 0, yBound());
        }
    }

    /**
     * Creates a division from the bottom left to the top right of the region. Places {@code f} in the 'lower' region and {@code g} in the 'upper' region.
     * @param REGION The type of region to create as defined in the fields.
     * @param f The {@code FunctionR3} to place in the lower region.
     * @param g The {@code FunctionR3} to place in the upper region.
     */
    public void divideDiagonalRight(int REGION, FunctionR3 f, FunctionR3 g) {
        if (REGION == ALL) {
            clearFunction();
            pushTriFunction(f, -xBound(), -yBound(), xBound(), -yBound(), xBound(), yBound());
            pushTriFunction(f, -xBound(), -yBound(), -xBound(), yBound(), xBound(), yBound());
        } else if (REGION == LOWER) {
            pushTriFunction(f, -xBound(), -yBound(), 0D, 0D, xBound(), -yBound());
            pushTriFunction(f, xBound(), -yBound(), 0D, 0D, xBound(), yBound());
        } else if (REGION == UPPER) {
            pushTriFunction(f, -xBound(), -yBound(), 0D, 0D, -xBound(), yBound());
            pushTriFunction(f, -xBound(), yBound(), 0D, 0D, xBound(), yBound());
        }
    }

    /**
     * Creates a division from the bottom right to the top left of the region. Places {@code f} in the 'lower' region and {@code g} in the 'upper' region.
     * @param REGION The type of region to create as defined in the fields.
     * @param f The {@code FunctionR3} to place in the lower region.
     * @param g The {@code FunctionR3} to place in the upper region.
     */
    public void divideDiagonalLeft(int REGION, FunctionR3 f, FunctionR3 g) {
        if (REGION == ALL) {
            clearFunction();
            pushTriFunction(f, xBound(), -yBound(), -xBound(), -yBound(), -xBound(), yBound());
            pushTriFunction(f, xBound(), -yBound(), xBound(), yBound(), -xBound(), yBound());
        } else if (REGION == LOWER) {
            pushTriFunction(f, -xBound(), -yBound(), 0D, 0D, xBound(), -yBound());
            pushTriFunction(f, -xBound(), -yBound(), 0D, 0D, -xBound(), yBound());
        } else if (REGION == UPPER) {
            pushTriFunction(f, -xBound(), yBound(), 0D, 0D, xBound(), yBound());
            pushTriFunction(f, xBound(), -yBound(), 0D, 0D, xBound(), yBound());
        }
    }

    /**
     * A wrapper for a {@code FunctionR3} that is defined over a rectangular region.
     */
    private class FunctionRep implements Serializable {
        protected double xl;
        protected double yl;
        protected double xh;
        protected double yh;
        protected FunctionR3 function;

        /**
         * Initialises a Function Rep
         * @param function The {@code FunctionR3} that is defined in this region.
         * @param x The x coord of the bottom left corner of the domain
         * @param y The y coord bottom left corner of the domain
         * @param xh The x coord of the top right of the domain
         * @param yh The y coord of the top right of the domain
         */
        public FunctionRep(FunctionR3 function, double x, double y, double xh, double yh) {
            this.function = function;
            this.xl = x;
            this.yl = y;
            this.xh = xh;
            this.yh = yh;
        }

        /**
         * Tests if the given coordinates lie in the {@code FunctionRep}'s domain.
         * @param x The x coordinate on the x, y plane
         * @param y The y coordinate on the x, y plane
         * @return The result of the test
         */
        public boolean isInRange(double x, double y) {
            return (x >= this.xl && x <= this.xh &&
                    y >= this.yl && y <= this.yh);
        }

        /**
         * Returns the {@code FunctionR3} stored in the function rep.
         * @return
         */
        public FunctionR3 getFunction() {
            return this.function;
        }
    }

    /**
     * A wrapper for a {@code FunctionR3} that is defined over a triangular region.
     */
    private class TriangularFunctionRep extends FunctionRep implements Serializable {
        private double xm;
        private double ym;

        /**
         * Initialises the function representation defined over a triangular region.
         * @param f The {@code FunctionR3} which is defined over the given region.
         * @param xl The x coordinate of the first point of the triangular region.
         * @param yl The y coordinate of the first point of the triangular region.
         * @param xm The x coordinate of the second point of the triangular region.
         * @param ym The y coordinate of the second point of the triangular region.
         * @param xh The x coordinate of the third point of the triangular region.
         * @param yh The y coordinate of the third point of the triangular region.
         */
        public TriangularFunctionRep(FunctionR3 f, double xl, double yl, double xm, double ym, double xh, double yh) {
            super(f, xl, yl, xh, yh);
            this.xm = xm;
            this.ym = ym;
        }

        /**
         * Tests if the given x, y coordinats lie in this {@code FunctionR3}'s domain.
         * @param x The x coordinate on the x, y plane
         * @param y The y coordinate on the x, y plane
         * @return The boolean result of the test.
         */
        public boolean isInRange(double x, double y) {
            //System.out.println("X:Y " + x + ":" + y);
           // System.out.println("Outer: " + xl + ":" + yl + " " + xm + ":" + ym + " " + xh + ":" + yh + " ");
            double A = area();
            double s = Math.abs(1/(2*A)*(yl*xh - xl*yh + (yh - yl)*x + (xl - xh)*y));
            double t = Math.abs(1/(2*A)*(xl*ym - yl*xm + (yl - ym)*x + (xm - xl)*y));
            //System.out.println((s > 0 && t > 0 && (1-s-t) > 0));
            return (s > 0 && t > 0 && (1-s-t) > 0);
        }

        /**
         * Gets the area of triangular domain.
         * @return The value of the domain.
         */
        private double area() {
            double a = Math.sqrt(Math.pow(xm-xl, 2) + Math.pow(ym-yl, 2));
            double b = Math.sqrt(Math.pow(xm-xh, 2) + Math.pow(ym-yh, 2));
            double c = Math.sqrt(Math.pow(xl-xh, 2) + Math.pow(yl-yh, 2));
            double p = (a + b + c)/2;
            return Math.sqrt(p*(p-a)*(p-b)*(p-c));
        }
        /**
         * Returns the {@code FunctionR3} used in this region.
         */
        public FunctionR3 getFunction() {
            return this.function;
        }
    }
}
