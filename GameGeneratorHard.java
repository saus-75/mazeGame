import java.awt.Point;
import java.util.Random;


/**
 * Created by julianblacket on 12/05/2016.
 */

/**
 * The Hard {@code GameGenerator} strategy. Generates mazes whose solution is complex and obscured.
 * Uses a set of {@code FunctionR3} functions to weight regions of the maze, then carves the
 * maze with no chance for loops.
 */
public class GameGeneratorHard implements GameGenerator {
    private int size = 31;
    private int timerLength = 60;
    private int timeIncrement = 10;
    private int numCoins = 20;


    private MazeCarver carver;
    private FunctionR3 function;
    private MazeWeighter weighter;
    private CoinPlacer coinPlacer;


    /**
     * Initialises the {@code GameGeneratorHard} and sets all the necessary fields to default values.
     */
    public GameGeneratorHard() {
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
        carver = new MazeCarverNoLoops(MazeCarver.STACK_RANDOM, MazeCarver.BACKTRACK, 5);
    }

    private void setDefaultFunction() {
        Random r = new Random(System.currentTimeMillis());
        int f = r.nextInt(100);
        if (f < 33) {
            f0();
        } else if (f > 33 && f >= 33) {
            f1();
        } else if (f >= 66) {
            f2();
        }
    }

    private void f0() {
        PiecewiseFunctionR3 f = new PiecewiseFunctionR3(size);
        double range = size;
        double numPeaks = 20;
        double height;
        double roots = 1/(numPeaks/range);
        double i, j;
        Random r = new Random();
        System.out.println(roots);
        int layer = 0;
        for (i=(range-1)/2; i>= -(range-1)/2; i-= roots) {
            for (j= -(range-1)/2; j<= (range-1)/2; j+= roots) {
                height = r.nextInt();
                f.addFunction(new FunctionR3SquarePeaks(range, roots, height), layer++, j, i-roots, j+roots, i );
            }
        }
        function = f;
    }

    private void f1() {
        PiecewiseFunctionR3 f = new PiecewiseFunctionR3(size);
        double range = size;
        double numPeaks = 15;
        double height;
        double roots = 1/(numPeaks/range);
        double i, j;
        Random r = new Random();
        System.out.println(roots);
        int layer = 0;
        for (i=(range-1)/2; i>= -(range-1)/2; i-= roots) {
            for (j= -(range-1)/2; j<= (range-1)/2; j+= roots) {
                height = r.nextInt(100);
                f.addFunction(new FunctionR3Paraboloid(range, height), layer++, j, i-roots, j+roots, i );
            }
        }
        function = f;
    }

    private void f2() {
        PiecewiseFunctionR3 f = new PiecewiseFunctionR3(size);
        double range = size;
        double numPeaks = 3;
        double height;
        double roots = 1/(numPeaks/range);
        double i, j;
        Random r = new Random();
        System.out.println(roots);
        int layer = 0;
        FunctionR3Polynomial subf = null;
        for (i=(range-1)/2; i>= -(range-1)/2; i-= roots) {
            for (j= -(range-1)/2; j<= (range-1)/2; j+= roots) {
                subf = new FunctionR3Polynomial(((int)range));
                subf.setToRandom();
                f.addFunction(subf, layer++, j, i-roots, j+roots, i );


            }
        }
        function = f;
    }

    private void setDefaultWeighter() {
        weighter = new MazeWeighterCartesian(function);
    }

    private void setDefaultLoopCreator() {
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
