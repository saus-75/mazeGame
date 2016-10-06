import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class SettingsScreen extends JPanel implements ActionListener{
	private Navigator navi;
	private NewApp theApp;
	private BufferedImage background;

	private JButton back = new JButton("Back");
	private JToggleButton music = new JToggleButton("");
	
	/**
	 * Creates a new setting screen using JPanel, it also sets the buttons, and background image.
	 * @param navi is the navigator, in charge of switching between the JPanels
	 * @param father is app that calls, so it can call the public method of that app for communicating between the app
	 */
	public SettingsScreen (Navigator navi, NewApp father) {
		this.navi = navi;
		this.theApp = father;
		try {
    		background = ImageIO.read(new File(theApp.getLoc()+"background.png"));
    	} catch (IOException e) {
    	}		
        setLayout(null);
        Font buttons = new Font("Impact", Font.BOLD, 20);
        back.setFont(buttons);
        music.setFont(buttons);
        
        back.setBounds(0, 610, 80, 50); back.setVisible(true);
		music.setBounds(320, 180, 160, 50); music.setVisible(true);
		repaint();
        add(back); back.addActionListener(this);
        add(music); music.addActionListener(this);
        music.setText("Music : ON ");
	}

	/**
	 * Paints the screen with a background Image
	 * @param g the Graphics object supplied by repaint
	 */
	@Override
	public void paintComponent (Graphics g) {
		g.clearRect(0, 0, 800, 800);
		g.drawImage(background, 0, 0, 800, 660, null);
	}
	
	/**
	 * Executes command when a specific button is pressed
	 * @param e the Event supplied by ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == back) {
			navi.changeMode(NewApp.menu);
		} else if (source == music) {
			if(music.isSelected()) {
				music.setText("Music : OFF");
				theApp.setMute();
			} else {
				music.setText("Music : ON ");
				theApp.unMute();
			}
		}
	}
}
