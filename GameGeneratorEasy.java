import java.awt.Point;
import java.util.Random;

/**
 * Created by julianblacket on 12/05/2016.
 */

/**
 * The easy {@code GameGenerator} strategy. Generates mazes that are easy to solve. Uses a {@code FunctionR3}
 * instance to weight the maze, then carves the maze with the possibility for loops.
 */
public class GameGeneratorEasy implements GameGenerator {
    private int size = 21;
    private int timerLength = 50;
    private int timeIncrement = 10;
    private int numCoins = 10;


    private MazeCarver carver;
    private FunctionR3 function;
    private MazeWeighter weighter;
    private CoinPlacer coinPlacer;

    /**
     * Initialises a {@code GameGeneratorEasy} and initialises the classes needed to use the
     * {@code generateMaze} method.
     */
    public GameGeneratorEasy() {
        setDefaultCarver();
        setDefaultFunction();
        setDefaultWeighter();
        setDefaultCoinPlacer();
    }

    public void setDefaultCarver() {

        carver = new MazeCarverBasic(MazeCarver.STACK_NORMAL, MazeCarver.BACKTRACK, 3);

    }

    private FunctionR3 getRandomAvailableFunction() {
        FunctionR3 ret;
        Random r = new Random();
        int i = r.nextInt(5);
        switch (i) {
            case (0): ret = new FunctionR3Diagonal(size); break;
            case (1): ret = new FunctionR3Paraboloid(size, r.nextInt(2)); break;
            case (2): ret = new FunctionR3ParaboloidFlipped(size, r.nextDouble()); break;
            case (3): ret = new FunctionR3SquarePeaks(size, r.nextInt(2), r.nextInt(3)); break;
            default: ret = new FunctionR3Polynomial(size); break;
        }
        System.out.println(ret.getClass().getName());
        return ret;
    }

    public void setDefaultFunction() {
        f0();
    }

    private void f0() {
        PiecewiseFunctionR3 f = new PiecewiseFunctionR3(size);
        double range = size;
        double numPeaks = 4;
        double roots = 1/(numPeaks/range);
        double i, j;
        Random r = new Random();
        System.out.println(roots);
        int layer = 0;
        for (i=(range-1)/2; i>= -(range-1)/2; i-= roots) {
            for (j= -(range-1)/2; j<= (range-1)/2; j+= roots) {
                f.addFunction(getRandomAvailableFunction(), layer++, j, i-roots, j+roots, i );
            }
        }
        function = f;
    }

    public void setDefaultWeighter() {
        weighter = new MazeWeighterCartesian(function);
    }

    public void setDefaultCoinPlacer() {
        coinPlacer = new CoinPlacer(numCoins);
    }

    public void generateMaze(Maze maze) {
        maze.setStart(new Point(0,0));
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
        return "easy";
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
