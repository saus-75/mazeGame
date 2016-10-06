import java.awt.Point;
import java.io.Serializable;
import java.util.*;

/**
 * Created by julianblacket on 20/05/2016.
 */
public class CoinPlacer implements Serializable {
    private int numCoins;

    /**
     * Initialises the {@code CoinPlacer} with the desired number of coins to place on the maze.
     * @param max The number of coins to be placed on the maze by the {@code placeCoins} method.
     */
    public CoinPlacer(int max) {
        numCoins = max;
    }

    /**
     * Given a carved maze with defined start and end points, places coins along the maze's path.
     * The number of coins placed is set with {@code numCoins}. Coin placement is designed to be balanced
     * between the start and end points of the maze.
     * @param maze The {@code Maze} to whose path coins will be placed.
     */
    public void placeCoins(Maze maze) {
        Queue<Node> eToDo = new LinkedList<>();
        Queue<Node> sToDo = new LinkedList<>();
        Map<Node, Integer> eDist = new HashMap<>();
        Map<Node, Integer> sDist = new HashMap<>();
        Point end = maze.getEnd();
        Point start = maze.getStart();
        Node eCurr = maze.get(end.y, end.x);
        Node sCurr = maze.get(start.y, start.x);
        eToDo.add(eCurr);
        eDist.put(eCurr, 0);
        sToDo.add(sCurr);
        sDist.put(sCurr, 0);
        boolean eEmpty = false; boolean sEmpty = false;
        List<Node> neighbours = null;

        while (!eToDo.isEmpty() || !sToDo.isEmpty()) {
            //System.out.println("Conains");
            if (eToDo.isEmpty()) {
                eEmpty = true;
            }
            if (sToDo.isEmpty()) {
                sEmpty = true;
            }
            if (!sEmpty) {
                sCurr = sToDo.remove();
                neighbours = maze.getNeighbouringPaths(sCurr, 1);
                for (Node n : neighbours) {
                    if (sDist.keySet().contains(n)) {
                        continue;
                    }
                    if (eDist.keySet().contains(n)) {
                        continue;
                    }
                    sToDo.add(n);
                    sDist.put(n, sDist.get(sCurr) + 1);
                }
            }
            if (!eEmpty) {
                eCurr = eToDo.remove();
                neighbours = maze.getNeighbouringPaths(eCurr, 1);
                for (Node n : neighbours) {
                    if (eDist.keySet().contains(n)) {
                        continue;
                    }
                    if (sDist.keySet().contains(n)) {
                        continue;
                    }
                    eToDo.add(n);
                    eDist.put(n, eDist.get(eCurr) + 1);
                }
            }
        }
        int i = 0;
        Random r = new Random();
        List<Map.Entry<Node, Integer>> eSorted = sortPaths(eDist);
        List<Map.Entry<Node, Integer>> sSorted = sortPaths(sDist);
        List<Point> sRegions = new ArrayList<>();
        List<Point> eRegions = new ArrayList<>();
        sRegions.add(new Point(0, (sSorted.size()-1)/3));
        sRegions.add(new Point((sSorted.size()-1)/3, 2*(sSorted.size()-1)/3));
        sRegions.add(new Point(2*(sSorted.size()-1)/3, 4*(sSorted.size()-1)/5));
        sRegions.add(new Point(4*(sSorted.size()-1)/5, (sSorted.size()-1)));
        eRegions.add(new Point(0, (eSorted.size()-1)/3));
        eRegions.add(new Point((eSorted.size()-1)/3, 2*(eSorted.size()-1)/3));
        eRegions.add(new Point(2*(eSorted.size()-1)/3, 4*(eSorted.size()-1)/5));
        eRegions.add(new Point(4*(eSorted.size()-1)/5, (eSorted.size()-1)));
        eRegions.addAll(eRegions);
        sRegions.addAll(sRegions);
        List<Point> sRemaining = new ArrayList<>(sRegions);
        List<Point> eRemaining = new ArrayList<>(eRegions);
        Point curr; Node cn;
        while (i < numCoins) {
            curr = sRemaining.remove(r.nextInt(sRemaining.size()));
            cn = sSorted.get(randomInRange(curr.x, curr.y)).getKey();
            if (cn.getState() == Node.start) {
                cn = sSorted.get(randomInRange(curr.x, curr.y)).getKey();
            }
            cn.changeState(Node.coin);

            curr = eRemaining.remove(r.nextInt(eRemaining.size()));
            cn = eSorted.get(randomInRange(curr.x, curr.y)).getKey();
            if (cn.getState() == Node.start) {
                cn = eSorted.get(randomInRange(curr.x, curr.y)).getKey();
            }
            cn.changeState(Node.coin);
            if (sRemaining.size() == 0) {
                sRemaining.addAll(sRegions);
            }
            if (eRemaining.size() == 0) {
                eRemaining.addAll(eRegions);
            }
            i+= 2;
        }
    }

    private int randomInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private List<Map.Entry<Node, Integer>> sortPaths(Map map) {
        Comparator<Map.Entry<Node, Integer>> sortByValues = new Comparator<Map.Entry<Node, Integer>>() {

            @Override
            public int compare(Map.Entry<Node, Integer> a, Map.Entry<Node, Integer> b) {
                return a.getValue().compareTo(b.getValue());
            }
        };

        List<Map.Entry<Node, Integer>> paths = new ArrayList<>();

        paths.addAll(map.entrySet());

        Collections.sort(paths, sortByValues);

        return paths;
    }
}
