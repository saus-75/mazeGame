import java.awt.Point;
import java.util.Random;

/**
 * Created by julianblacket on 14/05/2016.
 */

/**
 * The medium {@code GameGenerator} strategy. Generates mazes whose solution is of moderate complexity and
 * somewhat obscured by false paths. Maze is maze easier with multiple solutions and loops. Uses a set of
 * {@code FunctionR3} functions to weight regions of the maze, then carves the maze with a chance of loops.
 */
public class GameGeneratorMedium implements GameGenerator {
    private int size = 25;
    private int timerLength = 60;
    private int timeIncrement = 10;
    private int numCoins = 10;

    private MazeCarver carver;
    private PiecewiseFunctionR3 function;
    private MazeWeighter weighter;
    private CoinPlacer coinPlacer;

    public GameGeneratorMedium() {
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
        carver = new MazeCarverBasic(MazeCarver.STACK_RANDOM, MazeCarver.BACKTRACK, 3);
    }

    private void setDefaultFunction() {
        f0();
    }

    private void f0() {
        function = new PiecewiseFunctionR3(size-1);
        double range = size-1;
        double numPeaks = 10;
        double height = 5;
        double roots = 1/(numPeaks/range);
        double i, j;
        Random r = new Random();
        int layer = 0;
        for (i=(range-1)/2; i>= -(range)/2; i-= roots) {
            for (j= -(range)/2; j<= (range-1)/2; j+= roots) {
                height = r.nextInt(100);
                function.addFunction(new FunctionR3SquarePeaks(range, numPeaks, height), layer++, j, i-roots, j+roots, i );
            }
        }
    }

    private void setDefaultWeighter() {
        weighter = new MazeWeighterCartesian(function);
    }

    private void setDefaultLoopCreator() {
        coinPlacer = new CoinPlacer(9);
    }


    public void generateMaze(Maze maze) {
        maze.setStart(new Point(0,0));
        maze.setEnd(new Point((size-1),(size-1)));
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
        return "medium";
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
