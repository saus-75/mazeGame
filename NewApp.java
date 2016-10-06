import java.applet.AudioClip;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JApplet;

public class NewApp extends JApplet{

	public static final char menu = 'm';
	public static final char easy = 'e';
	public static final char medium = 'd';
	public static final char funky = 'f';
	public static final char hard = 'h';
	public static final char help = '?';
	public static final char settings = 's';
	
	private char mode;
	
	private String location;
	
	private boolean muted;
	private boolean funkDropped;
	private NewMenu Menu;
	private NewGame Game;
	private HelpScreen Help;
	private SettingsScreen Settings;
	
	private AudioClip BGM;
	private AudioClip CoinSound;
	private AudioClip gameOverSound;
	private AudioClip FunkBGM;
	
	/**
	 * Creates a New Applet and sets the first panel to the main menu
	 */
	public NewApp () {
		mode = menu;
		setFocusable(true);
	}
	
	/**
	 * Initiate the new applet and calls all the music and background pictures needed
	 * Sets up all the panels and cases to switch from panel to panel
	 */
	@Override
	public void init () {
		try {
			URL url = new URL(getCodeBase()+"BGMv1.wav");
			BGM = getAudioClip(url);
			BGM.loop();
			muted = false;
		} catch (MalformedURLException e1) {
		}
		try {
			URL url = new URL(getCodeBase()+"Coin.wav");
			CoinSound = getAudioClip(url);
		} catch (MalformedURLException e1) {
		}
		try {
			URL url = new URL(getCodeBase()+"gameOverSound.wav");
			gameOverSound = getAudioClip(url);
		} catch (MalformedURLException e1) {
		}
		try {
			URL url = new URL(getCodeBase()+"funkBGM.wav");
			FunkBGM = getAudioClip(url);

		} catch (MalformedURLException e1) {
			System.out.println("File not found");
		}
		setLoc(getCodeBase().toString());
		final CardLayout cardLayout = new CardLayout();
        setLayout(cardLayout);
        cardLayout.preferredLayoutSize(this);
        
        final NewApp na = new NewApp();
        
        Navigator navi = new Navigator() {
        	@Override
        	public void changeMode (char newMode) {
        		na.setMode(newMode);
        		na.repaint();
        		switch (newMode) {
        		case menu: Game.setVisible(false);
	        		Help.setVisible(false);
	    			Settings.setVisible(false);
        			cardLayout.show(getContentPane(), "Menu"); break;
        		case easy: Menu.setVisible(false);
        			Game.newGame(NewApp.easy);
					cardLayout.show(getContentPane(), "Game"); break;
        		case medium: Menu.setVisible(false);
        			Game.newGame(NewApp.medium);
					cardLayout.show(getContentPane(), "Game"); break;
        		case hard: Menu.setVisible(false);
        			Game.newGame(NewApp.hard);
					cardLayout.show(getContentPane(), "Game"); break;
        		case funky: Menu.setVisible(false);
    				Game.newGame(NewApp.funky);
    				cardLayout.show(getContentPane(), "Game"); break;
        		case help: Menu.setVisible(false);
        			cardLayout.show(getContentPane(), "Help"); break;
        		case settings: Menu.setVisible(false);
    				cardLayout.show(getContentPane(), "Settings"); break;
        		}
        		na.repaint();
        	}
        };
        
        Menu = new NewMenu(navi, this);
        Menu.setBackground(new Color(21,43,85));
        Menu.setOpaque(true);
        Game = new NewGame(navi, this);
        Game.setFocusable(true);
        Game.setBackground(new Color(21,43,85));
        Game.setOpaque(true);
        Help = new HelpScreen(navi, this);
        Help.setBackground(new Color(21,43,85));
        Help.setOpaque(true);
        Settings = new SettingsScreen(navi, this);
        Settings.setBackground(new Color(21,43,85));
        Settings.setOpaque(true);

        add(Menu, "Menu");
        add(Game, "Game");
        add(Help, "Help");
        add(Settings, "Settings");
        
        cardLayout.show(getContentPane(), "Menu");
        setSize(800,660);
        funkDropped = false;
	}
	
	/**
	 * Sets the appropriate location for retrieving the Files
	 * @param location of the folder
	 */
	private void setLoc (String location) {
		this.location = location.split(":", 2)[1];
	}
	
	/**
	 * Gets the appropriate location for retrieving the Files
	 * @param location of the folder
	 */
	public String getLoc () {
		return location;
	}
	
	/**
	 * Paints the corresponding cases for the panels
	 * @param g the Graphics component from paint();
	 */
	@Override
	public void paint (Graphics g) {
		switch (mode) {
		case menu: Menu.paint(g); break;
		case easy: Game.paint(g); break;
		case medium: Game.paint(g); break;
		case hard: Game.paint(g); break;
		case help: Help.paint(g); break;
		case settings: Settings.paint(g); break;
		}
	}
	
	/**
	 * Gets which panel it is at currently in the applet
	 * @return char which is the mode
	 */
	public char getMode () {
		return mode;
	}
	
	/**
	 * Sets which panel it is at currently in the applet
	 * @return char which is the mode
	 */
	public void setMode (char newMode) {
		mode = newMode;
	}
	
	/**
	 * plays the coin sound, if mute is not selected
	 */
	public void playCoinSound () {
		if(!muted) CoinSound.play();
	}
	
	/**
	 * plays the game over sound, if mute is not selected
	 */
	public void playGameOverSound () {
		if(!muted) gameOverSound.play();
	}
	
	/**
	 * Sets sound off
	 */
	public void setMute() {
		muted = true;
		BGM.stop();
		FunkBGM.stop();
	}
	
	/**
	 * Sets sound on
	 */
	public void unMute() {
		muted = false;
		if (mode == funky) FunkBGM.loop();
		else BGM.loop();
	}
	
	/**
	 * Plays the funky tune for funky mode
	 */
	public void initialiseTheFunk() {
		BGM.stop();
		if(!muted) FunkBGM.loop();	
		funkDropped = true;
	}
	
	/**
	 * Plays the normal BGM tune for normal mode
	 */
	public void playNormalBGM() {
		FunkBGM.stop();
		if(!muted) BGM.loop();	
		funkDropped = false;
	}
	
	/**
	 * Checks if the funky tune is playing 
	 * @return boolean, true if funky tune is playing, false if not
	 */
	public boolean isFunkBGMplaying() {
		return funkDropped;
	}
}
