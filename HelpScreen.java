import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class HelpScreen extends JPanel implements ActionListener {
	private Navigator navi;
	private NewApp theApp;
	private BufferedImage background;
	
	private JButton back = new JButton("Back");
	
	/**
	 * Creates a new Help screen and position the buttons and labels in position
	 * @param navi is the navigator, in charge of switching between the JPanels
	 * @param father is app that calls, so it can call the public method of that app for communicating between the app
	 */
	public HelpScreen (Navigator navi, NewApp father) {
		this.navi = navi;
		this.theApp = father;
		try {
    		background = ImageIO.read(new File(theApp.getLoc()+"background.png"));
    	} catch (IOException e) {
    	}		
        Font buttons = new Font("Impact", Font.BOLD, 20);
        back.setFont(buttons);
        setLayout(null);
		
		back.setBounds(0, 610, 80, 50); back.setVisible(true);
		
		JLabel h = new JLabel(HelpDialog());
		h.setFont(new Font("Impact", Font.PLAIN, 16));
        h.setForeground(Color.ORANGE);
        h.setOpaque(true);
        h.setBackground(new Color(21,43,85));
        h.setBorder(new LineBorder(Color.BLACK, 2));
        h.setBounds(250,50,350,450);
        
        repaint();
        add(h); h.setVisible(true);
        add(back); back.addActionListener(this);
		
	}
	
	/**
	 * Paints the screen with a background Image
	 * @param g the Graphics object supplied by repaint
	 */
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		g.drawImage(background, 0, 0, 800, 660, null);
	}
	
    private String HelpDialog(){
    	String help =	 
    			"<html><center>You will need to traverse through the maze</center>"
    			+ "<center>and collect all the coins in order to exit.</center>"
    			+ "<center>There is a countdown timer</center>"
    			+ "<center>If the timer reached 0 and you</center>"
    			+ "<center>have not reached the exit yet it is game over</center>"
    			+ "<center>each coin you collect will increment the timer</center>"
    			+ "<center>To move:</center>"
    			+ "<center>Up button : move up</center>"
    			+ "<center>Down button : move down</center>"
    			+ "<center>Left button : move left</center>"
    			+ "<center>Right button : move right</center>"
    			+ "<center>There will be multiple enemies that will track you.</center>"
    			+ "<center>Press 'Space' once and you will go into ghost mode.</center>"
    			+ "<center>To get out of ghost mode, you just need to move.</center>"
    			+ "<center>In ghost mode the enemy will not be able to catch you.</center>"
    			+ "<center>But the countdown timer will go down faster</center></html>";
    	return help;
    }
    
	/**
	 * Executes command when a specific button is pressed
	 * @param e the Event supplied by ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		navi.changeMode(NewApp.menu);
	}
}
