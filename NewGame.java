import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.awt.Point;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class NewGame extends JPanel implements ActionListener {
	public static int size = 20;
	private int numCoins = 10;
	private int START_TIME = 100;
	private int timeIncrement = 0;
	private char mode;

	public static final int cellSize = 20;
	public static final int startBound = 0;
	public static final int width = 600;
	public static final int height = 600;
	private static final int DELAY = 1000;
	
	private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
	private static final char MOVE_UP = 'u';
	private static final char MOVE_DOWN = 'd';
	private static final char MOVE_LEFT = 'l';
	private static final char MOVE_RIGHT = 'r';
	private static final char GHOST = ' ';
	
	private Navigator navi;
	private NewApp theApp;
	
	private BufferedImage playerImg;
	private BufferedImage playerBack;
	private BufferedImage playerLeft;
	private BufferedImage playerRight;
	private BufferedImage ghostImg;
	private BufferedImage pathImg;
	private BufferedImage wallImg;
	private BufferedImage wallDirt;
	private BufferedImage coinImg;
	private BufferedImage AIImg;
    private BufferedImage AIBack;
    private BufferedImage AILeft;
    private BufferedImage AIRight;
	private BufferedImage start;
	private BufferedImage end;
	private BufferedImage hintHelper;

	private Maze maze;
	public Timer timer;
	public Timer AItimer;
	public Timer hintTimer;
	private Player player1;
	private List<EnemyAI> AIs;
	private int timeLeft;
	private int timeElapsed;
	private boolean started;
	private boolean gameOver;
	private boolean noMessage;
	private boolean finished;
	private Hint hint;
	
	private int previousi;
	private int previousj;
	private List<Integer> previousiAI;
	private List<Integer> previousjAI;
	private int AImoveCount;
	
	private JButton restart = new JButton("Restart");
    private JButton newMaze = new JButton("New Maze");
    private JButton save = new JButton("Quit");
    private JButton menu = new JButton("Menu");
    private JButton hintB = new JButton("Hint");
    
    private JLabel congratulations = new JLabel();
    private JLabel countdown = new JLabel();
	
    /**
     * Creates a new game, which is a panel that holds the maze and allows input
     * @param navi a navigator which allows for switching between JPanels
     * @param father the app which called this JPanel, which allows for communication between panels
     */
	public NewGame (Navigator navi, NewApp father) {
		this.navi = navi;
		this.theApp = father;
		
		try {
			playerImg = ImageIO.read(new File(theApp.getLoc()+"Linkfront.png"));
		} catch (IOException e) {
		}
		try {
			playerBack = ImageIO.read(new File(theApp.getLoc()+"Linkback.png"));
		} catch (IOException e) {
		}
		try {
			playerLeft = ImageIO.read(new File(theApp.getLoc()+"Linkleft.png"));
		} catch (IOException e) {
		}
		try {
			playerRight = ImageIO.read(new File(theApp.getLoc()+"Linkright.png"));
		} catch (IOException e) {
		}
		try {
			coinImg = ImageIO.read(new File(theApp.getLoc()+"Coin.png"));
		} catch (IOException e) {
		}
		try {
			pathImg = ImageIO.read(new File(theApp.getLoc()+"dirtPath.png"));
		} catch (IOException e) {
		}
		try {
			wallImg = ImageIO.read(new File(theApp.getLoc()+"Wall.png"));
		} catch (IOException e) {
		}
		try {
			wallDirt = ImageIO.read(new File(theApp.getLoc()+"WallDirt.png"));
		} catch (IOException e) {
		}
		try {
			ghostImg = ImageIO.read(new File(theApp.getLoc()+"ghost.png"));
		} catch (IOException e) {
		}
		try {
            AIImg = ImageIO.read(new File(theApp.getLoc()+"enemyFront.png"));
        } catch (IOException e) {
        }
        try {
            AIBack = ImageIO.read(new File(theApp.getLoc()+"enemyBack.png"));
        } catch (IOException e) {
        }
        try {
            AILeft = ImageIO.read(new File(theApp.getLoc()+"enemyLeft.png"));
        } catch (IOException e) {
        }
        try {
            AIRight = ImageIO.read(new File(theApp.getLoc()+"enemyRight.png"));
        } catch (IOException e) {
        }
    	try {
    		start = ImageIO.read(new File(theApp.getLoc()+"upstairs.png"));
    	} catch (IOException e) {
    	}
    	try {
    		end = ImageIO.read(new File(theApp.getLoc()+"downstairs.png"));
    	} catch (IOException e) {
    	}
    	try {
    		hintHelper = ImageIO.read(new File(theApp.getLoc()+"navi.png"));
    	} catch (IOException e) {
    	}
    
		
		getInputMap(IFW).put(KeyStroke.getKeyStroke("UP"), MOVE_UP);
		getInputMap(IFW).put(KeyStroke.getKeyStroke("DOWN"), MOVE_DOWN);
		getInputMap(IFW).put(KeyStroke.getKeyStroke("LEFT"), MOVE_LEFT);
		getInputMap(IFW).put(KeyStroke.getKeyStroke("RIGHT"), MOVE_RIGHT);
		getInputMap(IFW).put(KeyStroke.getKeyStroke("SPACE"), GHOST);
		InputMap im = (InputMap)UIManager.get("Button.focusInputMap");
		im.put(KeyStroke.getKeyStroke("pressed SPACE"), "none");
		im.put(KeyStroke.getKeyStroke("released SPACE"), "none");
		
		getActionMap().put(MOVE_UP, new MoveAction('u'));
		getActionMap().put(MOVE_DOWN, new MoveAction('d'));
		getActionMap().put(MOVE_LEFT, new MoveAction('l'));
		getActionMap().put(MOVE_RIGHT, new MoveAction('r'));
		getActionMap().put(GHOST, new MoveAction(' '));
		
        setLayout(null);
        
        repaint();
        
        restart.setVisible(true);
        newMaze.setVisible(true);
        save.setVisible(true);
        menu.setVisible(true);
        hintB.setVisible(true);
        countdown.setVisible(true);
        congratulations.setVisible(false);
        
        add(restart); restart.addActionListener(this);
        add(newMaze); newMaze.addActionListener(this);
        add(save); save.addActionListener(this);
        add(menu); menu.addActionListener(this);
        add(hintB); hintB.addActionListener(this);
        add(countdown);
        add(congratulations);
        
        countdown.setFont(new Font("Arial", Font.BOLD, 35));
        countdown.setBorder(new LineBorder(Color.BLACK, 1));
        countdown.setForeground(Color.RED);
        
        congratulations.setOpaque(true);
        countdown.setOpaque(true);
        congratulations.setVerticalTextPosition(JLabel.CENTER);
        congratulations.setHorizontalTextPosition(JLabel.CENTER);
        countdown.setVerticalTextPosition(JLabel.CENTER);
        countdown.setHorizontalTextPosition(JLabel.CENTER);
        
    	setFocusable(true);
    	setBackground(Color.DARK_GRAY);    	
    }
	
	/**
	 * Class that deals with input from player
	 */
	private class MoveAction extends AbstractAction {

		char direction;
		
		/**
		 * Sets up map from input to actions
		 * @param input the binding which will link to an action
		 */
		public MoveAction(char input) {
			direction = input;
		}
		
		/**
		 * Switches between events to determine the appropriate action
		 * @param e the triggering event
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			switch(direction) {
			case 'u': if(!finished) {player1.moveUp(maze);} break;
			case 'd': if(!finished) {player1.moveDown(maze);} break;
			case 'l': if(!finished) {player1.moveLeft(maze);} break;
			case 'r': if(!finished) {player1.moveRight(maze);} break;
			case ' ': if(!finished) {player1.enterGhostMode();} break;
			}
			if (!gameOver) {
				repaint();
			}
		}
		
	}
	
	/**
	 * Sets up a new game in the specified input mode
	 * @param inputMode the mode of game to create
	 */
	public void newGame (char inputMode) {
		mode = inputMode;
    	gameOver = false;
    	finished = false;
    	generateGame();
    	timeLeft = START_TIME;
    	timeElapsed = 0;
    	countdown.setText("<html><center>"+Integer.toString(timeLeft)+"</center></html>");
    	timer = new Timer(DELAY, this);
    	AItimer = new Timer(DELAY/6, this);
    	hintTimer = new Timer(DELAY/2 + 1, this);
    	player1 = new Player();
    	AIs = new LinkedList<EnemyAI>();
    	previousiAI = new LinkedList<Integer>();
    	previousjAI = new LinkedList<Integer>();
    	hint = new Hint();    	
    	noMessage = true;
    	started = false;
    	congratulations.setVisible(false);
		restart.setBounds(size*cellSize + 25, 80, 100, 50);
        newMaze.setBounds(size*cellSize + 25, 140, 100, 50);
        save.setBounds(size*cellSize + 25, 200, 100, 50);
        menu.setBounds(size*cellSize + 25, 260, 100, 50);
        hintB.setBounds(size*cellSize + 25, 320, 100, 50);
        countdown.setBounds(size*cellSize + 25, 20, 100, 50);
    	repaint();
	}
	
	/**
	 * Generates the maze
	 */
	private void generateGame () {
		maze = new Maze();
		GameGenerator gameGenerator = new GameGeneratorMedium();
		if (mode == NewApp.easy) {
			gameGenerator = new GameGeneratorEasy();
		} else if (mode == NewApp.hard) {
			gameGenerator = new GameGeneratorHard();
		} else if (mode == NewApp.funky){
			gameGenerator = new GameGeneratorFunky();
			theApp.initialiseTheFunk();
		}
		
		if(mode != NewApp.funky && theApp.isFunkBGMplaying()) theApp.playNormalBGM();
		size = gameGenerator.getSize();
		numCoins = gameGenerator.getNumCoins();
		START_TIME = gameGenerator.getStartTime();
		timeIncrement = gameGenerator.getTimeIncrement();
		gameGenerator.generateMaze(maze);
    }
	
	/**
	 * Paints the current state of the game
	 * @param g the Graphics component from repaint()
	 */
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		for (EnemyAI e : AIs) {
		   if (previousi == previousiAI.get(e.getID()) && previousj == previousjAI.get(e.getID()) && !player1.getGhost() && e.isSpawned()) {
		      gameOver = true;
		      theApp.playGameOverSound();
		   }
		}
		if (!gameOver) {
		
			if (!started) {
				started = true;
				timer.start();
				AItimer.start();
				player1.clearCoins();
			}
        	for (int row = 0; row < size; row++) {
        		for (int col = 0; col < size; col++) {
        			switch (maze.get(row, col).getState()) {
        			case Node.wall: g.drawImage(wallImg, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
        							break;
        			case Node.oldCoin:
        			case Node.path: 
        							if ((row-1 >= 0 && row-1 <= size-1) && (maze.get(row-1, col).getState() == Node.wall)){
        								g.drawImage(wallDirt, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
        							} else {
        								g.drawImage(pathImg, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
        							}
        							break;
        			case Node.end: g.drawImage(end, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
        							break;
        			case Node.start: g.drawImage(start, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
    								break;
        			case Node.coin: // edge case of coin spawning on start 
        							if(maze.getStart().getX() == row && maze.getStart().getY() == col){
        								g.drawImage(start, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
        								maze.get(row, col).changeState(Node.start);
        							} else {
										if ((row-1 >= 0 && row-1 <= size-1) && (maze.get(row-1, col).getState() == Node.wall)){
											g.drawImage(wallDirt, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
										} else {
											g.drawImage(pathImg, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
										}
        							}
        							g.drawImage(coinImg, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
        							break;
        			case Node.hint: 
        							if ((row-1 >= 0 && row-1 <= size-1) && (maze.get(row-1, col).getState() == Node.wall)){
										g.drawImage(wallDirt, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
									} else {
										g.drawImage(pathImg, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
									}	
        							int timing = hint.getHintTiles().indexOf(maze.get(row, col));
        							if(timing == hint.getHintCycle()){
        								g.drawImage(hintHelper, startBound+cellSize*col, startBound+cellSize*row, cellSize, cellSize, null);
        							}
        			}
        		}
        	}
	    	if (player1.getGhost()) {
	     	   	g.drawImage(ghostImg, player1.getX(), player1.getY(), cellSize, cellSize, null);
	    	} else {
	    	   switch(player1.getDirection()) {
	    	      case Player.north: g.drawImage(playerBack, player1.getX(), player1.getY(), cellSize, cellSize, null); break;
	    	      case Player.south: g.drawImage(playerImg, player1.getX(), player1.getY(), cellSize, cellSize, null); break;
	    	      case Player.east: g.drawImage(playerRight, player1.getX(), player1.getY(), cellSize, cellSize, null); break;
	    	      case Player.west: g.drawImage(playerLeft, player1.getX(), player1.getY(), cellSize, cellSize, null); break;
	    	   }
	    	}
	    	
	    	if (maze.get(player1.getRow(), player1.getCol()).getState() == Node.coin) {
	    	    theApp.playCoinSound();
	    	    timeLeft += timeIncrement;
	    		player1.collectCoin();
	    		maze.get(player1.getRow(), player1.getCol()).changeState(Node.oldCoin);
	    	}
	    	
	       	// Spawn AIs
		    	
	       	if(canSpawnAI()){

	       		int ID = 0;
	       		if(AIs.size() != 0){
	       			ID = AIs.size();
	       		}
	       		EnemyAI AI = new EnemyAI(ID, new Point(startBound, startBound));
	       		AI.spawnAI();
	       		AIs.add(AI);
	       		AImoveCount = 0;
	       		previousiAI.add(AI.getRow());
	       		previousjAI.add(AI.getCol());

	       	}
		    	
		    // AI
	    	for(EnemyAI e : AIs){
		       	if(e.isSpawned()){
		       		switch (e.getDirection()) {
		       			case EnemyAI.north: g.drawImage(AIBack, e.getX(), e.getY(), cellSize, cellSize, null); break;
		       			case EnemyAI.south: g.drawImage(AIImg, e.getX(), e.getY(), cellSize, cellSize, null); break;
		       			case EnemyAI.west: g.drawImage(AILeft, e.getX(), e.getY(), cellSize, cellSize, null); break;
		       			case EnemyAI.east: g.drawImage(AIRight, e.getX(), e.getY(), cellSize, cellSize, null); break;
		       		}
		       	} 
		       	
		       	previousiAI.set(e.getID(),  e.getRow());
		       	previousjAI.set(e.getID(),  e.getCol());
	       	}
		    	
	    	countdown.setText("<html><center>"+Integer.toString(timeLeft)+"</center></html>");
			
	    	previousi = player1.getRow();
	    	previousj = player1.getCol();
		    	
	 	   	if (player1.getCol() == maze.getEnd().x && player1.getRow() == maze.getEnd().y) {
	 	   		if (noMessage) {
		 	   		if (player1.getCoinCount() == numCoins) {
		 	   			timer.stop();
		 	   			finished = true;
			 	   		congratulations.setText("<html><center>CONGRATULATIONS PRESS RESTART OR NEW GAME TO PLAY AGAIN</center></html>");
			 	   		congratulations.setBounds(0, 0, size*cellSize, size*cellSize);
			 	   		congratulations.setFont(new Font("Arial", Font.BOLD, 35));
			 	   		congratulations.setBorder(new LineBorder(Color.BLACK, 1));
			 	   		congratulations.setBackground(Color.green);
			 	   		congratulations.setVisible(true);
		 	   		} else {
		 	   			congratulations.setText("<html><center>You must collect all coins to finish</center></html>");
		 	   			congratulations.setBounds(size*cellSize + 25, 400, 100, 100);
			 	   		congratulations.setFont(new Font("Arial", Font.PLAIN, 12));
			 	   		congratulations.setBorder(new LineBorder(Color.BLACK, 1));
		 	   			congratulations.setBackground(Color.red);
		 	   			congratulations.setVisible(true);
		 	   		}
	 	   		} else {
	 	   			congratulations.setVisible(false);
	 	   		}
	 	   	} else {
	 	   		congratulations.setVisible(false);
	 	   		noMessage = true;
	 	   	}			
		} else {
			paintGameOver(g);
		}
    }
	
	/**
	 * Paints the gameOver screen
	 * @param g the Graphics component given from paintComponent(g)
	 */
	private void paintGameOver (Graphics g) {
    	timer.stop();
    	countdown.setText("<html><center>"+Integer.toString(timeLeft)+"</center></html>");
    	super.paintComponent(g);
    	congratulations.setText("<html><center>GAME OVER<br />CLICK RESTART TO TRY AGAIN!!!</center></html>");
   		congratulations.setBounds(0, 0, size*cellSize, size*cellSize);
   		congratulations.setFont(new Font("Arial", Font.BOLD, 35));
   		congratulations.setBorder(new LineBorder(Color.BLACK, 1));
   		congratulations.setBackground(Color.red);
   		congratulations.setVisible(true);
    }

	/**
	 * Deals with input from buttons and timers
	 * @param e the source of the triggered Action
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == restart) {
			restart();
			repaint();
		} else if (source == newMaze) {
			newGame(mode);
		} else if (source == save) {
			System.exit(0);
		} else if (source == menu) {
			navi.changeMode(NewApp.menu);
		} else if (source == hintB){
			showHint();
		} else if (source == timer) {
			timeElapsed++;
			if (player1.getGhost()) {
				timeLeft -= 2;
			} else {
				timeLeft--;
			}
			if (timeLeft <= 0) {
				timeLeft = 0;
				gameOver = true;
			}
			repaint();
		} else if(source == AItimer){
			AImoveCount++;
			for(EnemyAI x : AIs){
				if (x.isSpawned() && gameOver == false) {
					// Attempt a move
					if(AImoveCount % x.getSpeed() == 0){
						x.attemptMove(maze.getNodes(), previousi, previousj, player1);
						repaint();
					}
					
					// Adjust Speed (if required)
						if (timeLeft == 10 && AImoveCount == 4){
							if(x.getSpeed() == EnemyAI.INITIAL_SPEED) x.setSpeed(EnemyAI.MED_SPEED);
							else if(x.getSpeed() == EnemyAI.MED_SPEED) x.setSpeed(EnemyAI.TOP_SPEED);
						
						} else if (player1.getCoinCount() == numCoins && !x.isCoinFlag()){
							x.setCoinFlag(true);
							if(x.getSpeed() == EnemyAI.INITIAL_SPEED) x.setSpeed(EnemyAI.MED_SPEED);
							else if(x.getSpeed() == EnemyAI.MED_SPEED) x.setSpeed(EnemyAI.TOP_SPEED);
						}
					} 
				}
			} else if (source == hintTimer){
				hint.setHintCycle(hint.getHintCycle()+1);
				repaint();
				if(hint.getHintCycle() > 2){
					hintTimer.stop();
					hint.revertStates();	
				}
			}
			
			if(AImoveCount == 6) AImoveCount = 1;
		}

	/**
	 * Restarts the game
	 */
	public void restart () {
		congratulations.setVisible(false);
		finished = false;
		gameOver = false;
		started = false;
		timeElapsed = 0;
		timeLeft = START_TIME + 1;
		player1.clearCoins();
		player1.setX(startBound);
		player1.setY(startBound);
		for(EnemyAI e : AIs){
			e.despawnAI();
		}
		AIs.clear();
		clearCoins();
	}
	
	/**
	 * Returns collected coins to an uncollected state
	 */
	private void clearCoins () {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (maze.get(i, j).getState() == Node.oldCoin) {
					maze.get(i, j).changeState(Node.coin);
				}
			}
		}
	}
	
	/**
	 * Allows customized number of AIs to be implemented based off need
	 * @return whether an AI should be spawned
	 */
	private boolean canSpawnAI(){
		// AI1 condition
		if (timeElapsed > 5 && AIs.size() == 0) return true;
		else if (timeElapsed > 15 && AIs.size() == 1) return true;
		else if (timeElapsed > 25 && AIs.size() == 2) return true;
		else if (timeElapsed > 35 && AIs.size() == 3 && mode != NewApp.easy) return true;
		else if (timeElapsed > 45 && AIs.size() == 4 && mode == NewApp.hard) return true;
		
		return false;
		
	}
	
	/**
	 * Displays a brief hint to give directions to the nearest coin
	 */
	private void showHint(){
		MazeSolverBFS solver = new MazeSolverBFS();
		List<Node> closestCoin = solver.getPathToNodeType(maze, new Point(player1.getCol(), player1.getRow()), Node.coin);
		List<Node> hintTiles = new LinkedList<Node>();
		for(int x = closestCoin.size()-1; x > closestCoin.size() - 4 || x == 0; x--){
			int i = closestCoin.get(x).getI();
			int j = closestCoin.get(x).getJ();
			if(maze.get(i, j).getState() == Node.coin) break;
			maze.get(i, j).changeState(Node.hint);
			hintTiles.add(maze.get(i, j));
		}
		hintTimer.start();
		hint.setHintCycle(0);
		hint.setHintTiles(hintTiles);
		hint.setRequested(true);
	}
}
