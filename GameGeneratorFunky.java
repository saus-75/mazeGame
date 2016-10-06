import java.awt.Point;
import java.util.Random;


/**
 * Created by julianblacket on 12/05/2016.
 */

/**
 * The 'Funky' {@code GameGenerator} strategy. Generates mazes whose path follow interesting shapes.
 * Uses a set of {@code FunctionR3} functions to weight regions of the maze, then carves the
 * maze with no chance for loops.
 */
public class GameGeneratorFunky implements GameGenerator {
    private int size = 25;
    private int timerLength = 60;
    private int timeIncrement = 2;
    private int numCoins = 20;

    private Random random;

    private MazeCarver carver;
    private PiecewiseFunctionR3 function;
    private MazeWeighterCartesian weighter;
    private CoinPlacer coinPlacer;

    /**
     * Initialises the {@code GameGeneratorFunky} and sets all the necessary fields to default values.
     */
    public GameGeneratorFunky() {
        this.random = new Random(System.currentTimeMillis());
        setDefaultCarver();
        setDefaultFunction();
        setDefaultWeighter();
        setDefaultLoopCreator();
        setDefaultTimerLength();
    }

    private void setDefaultTimerLength() {
        this.timerLength = 60;
    }

    private void setDefaultCarver() {
        carver = new MazeCarverNoLoops(MazeCarver.STACK_NORMAL, MazeCarver.NO_BACKTRACK, MazeCarver.NO_MAX_RUN);
    }

    private FunctionR3 getRandomAvailableFunction() {
        FunctionR3 ret;
        int i = random.nextInt(5);
        switch (i) {
            case (0): ret = new FunctionR3Diagonal(size); break;
            case (1): ret = new FunctionR3Paraboloid(size, 1); break;
            case (2): ret = new FunctionR3ParaboloidFlipped(size, 1); break;
            case (3): ret = new FunctionR3SquarePeaks(size, 1, 1); break;
            default: ret = new FunctionR3Polynomial(size); break;
        }
        return ret;
    }

    private void setDefaultFunction() {
        PiecewiseFunctionR3 f = new PiecewiseFunctionR3(size);

        int i = random.nextInt(15);
        switch (i) {
            // ORTHOGONAL
            case(0): f.coverRegion(getRandomAvailableFunction()); break;
            case(1): f.divideHorizontal(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(2): f.divideHorizontal(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideVertical(PiecewiseFunctionR3.UPPER, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(3): f.divideHorizontal(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideVertical(PiecewiseFunctionR3.LOWER, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(4): f.divideVertical(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(5): f.divideVertical(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideHorizontal(PiecewiseFunctionR3.LEFT, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(6): f.divideVertical(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideHorizontal(PiecewiseFunctionR3.RIGHT, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(7): f.divideHorizontal(PiecewiseFunctionR3.LEFT, getRandomAvailableFunction(), getRandomAvailableFunction());
                f.divideHorizontal(PiecewiseFunctionR3.RIGHT, getRandomAvailableFunction(), getRandomAvailableFunction());break;
            // TRIANGULAR
            case(8): f.divideDiagonalRight(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(9): f.divideDiagonalRight(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideDiagonalRight(PiecewiseFunctionR3.UPPER, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(10): f.divideDiagonalRight(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideDiagonalRight(PiecewiseFunctionR3.LOWER, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(11): f.divideDiagonalLeft(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(12): f.divideDiagonalLeft(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideDiagonalLeft(PiecewiseFunctionR3.LOWER, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(13): f.divideDiagonalLeft(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideDiagonalLeft(PiecewiseFunctionR3.UPPER, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
            case(14): f.divideDiagonalLeft(PiecewiseFunctionR3.ALL, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideDiagonalLeft(PiecewiseFunctionR3.UPPER, getRandomAvailableFunction(), getRandomAvailableFunction());
                        f.divideDiagonalLeft(PiecewiseFunctionR3.LOWER, getRandomAvailableFunction(), getRandomAvailableFunction()); break;
        }
        function = f;
    }

    private int randomInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }



    private void setDefaultWeighter() {
        weighter = new MazeWeighterCartesian(function);
    }

    private void setDefaultLoopCreator() {
        coinPlacer = new CoinPlacer(numCoins);
    }


    public void generateMaze(Maze maze) {
        maze.resizeMaze(size, size);
        weighter.weightMaze(maze);
        carver.generateMaze(maze);
        MazeSolverBFS solver = new MazeSolverBFS();
        Node max = solver.getFurthestFromStart(maze);
        max.changeState(Node.end);
        maze.setEnd(new Point(max.getJ(), max.getI()));
        coinPlacer.placeCoins(maze);
    }

    @Override
    public FunctionR3 getFunction() {
        return function;
    }

    @Override
    public MazeCarver getCarver() {
        return carver;
    }

    @Override
    public MazeWeighter getWeighter() {
        return weighter;
    }

    @Override
    public int getTimerLength() {
        return timerLength;
    }

    @Override
    public CoinPlacer getCoinPlacer() {
        return coinPlacer;
    }

    public String difficultyToString() {
        return "hard";
    }

    public int getSize() {
        return size;
    }

    public int getNumCoins() {
        return this.numCoins;
    }

    public int getStartTime () {
        return timerLength;
    }

    public int getTimeIncrement () {
        return timeIncrement;
    }

}
