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

public class NewMenu extends JPanel implements ActionListener {
	
	private BufferedImage background;
	private NewApp theApp;
	private Navigator navi;
	
	private JButton easy = new JButton("Easy"); 
	private JButton medium = new JButton("Medium"); 
	private JButton hard = new JButton("Hard");
	private JButton quit = new JButton("Quit"); 
	private JButton settings = new JButton("Settings"); 
	private JButton help = new JButton("Help");
    private JButton funky = new JButton("Funky Mode");
	
    /**
     * Creates a new menu panel using JPanel, it also sets the buttons, title, and background image.
	 * @param navi is the navigator, in charge of switching between the JPanels
	 * @param father is app that calls, so it can call the public method of that app for communicating between the app
     */
	public NewMenu (final Navigator navi, NewApp father) {
		this.navi = navi;
		this.theApp = father;
		setFocusable(true);
		try {
    		background = ImageIO.read(new File(theApp.getLoc()+"background.png"));
    	} catch (IOException e) {
    	}
        setLayout(null);
        
        Font buttons = new Font("Impact", Font.BOLD, 20);
        hard.setFont(buttons);
        medium.setFont(buttons);
        easy.setFont(buttons);
        funky.setFont(buttons);
        quit.setFont(buttons);
        settings.setFont(buttons);
        help.setFont(buttons);
        
        easy.setBounds(320, 250, 160, 50); easy.setVisible(true);
        medium.setBounds(320, 300, 160, 50); medium.setVisible(true);
        hard.setBounds(320, 350, 160, 50); hard.setVisible(true);
        funky.setBounds(320, 400, 160, 50); funky.setVisible(true);
        quit.setBounds(720, 610, 80, 50); quit.setVisible(true);
        settings.setBounds(0, 610, 80, 50); settings.setVisible(true);
        help.setBounds(320, 500, 160, 50); help.setVisible(true);
        
        JLabel GameTitle = new JLabel(" Escape The Copyright");
        GameTitle.setFont(new Font("Impact", Font.PLAIN, 50));
        GameTitle.setForeground(Color.red);
        GameTitle.setBounds(180,100,470,80);
        
        JLabel GameTitleOutline = new JLabel(" Escape The Copyright");
        GameTitleOutline.setFont(new Font("Impact", Font.PLAIN, 50));
        GameTitleOutline.setForeground(Color.black);
        GameTitleOutline.setBounds(184,104,470,80);
        
        repaint();
        
        add(GameTitle); GameTitle.setVisible(true);
        add(GameTitleOutline); GameTitle.setVisible(true);
        add(easy); easy.addActionListener(this);
        add(medium); medium.addActionListener(this);
        add(hard); hard.addActionListener(this);
        add(funky); funky.addActionListener(this);
        add(quit); quit.addActionListener(this);
        add(settings); settings.addActionListener(this);
        add(help); help.addActionListener(this);
    }
	
	/**
	 * Executes command when a specific button is pressed
	 * @param e the Event supplied by ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == easy) {
			navi.changeMode(NewApp.easy);
		} else if (source == medium) {
			navi.changeMode(NewApp.medium);
		} else if (source == hard) {
			navi.changeMode(NewApp.hard);
		} else if (source == quit) {
			System.exit(0);
		} else if (source == settings) {
			navi.changeMode(NewApp.settings);
		} else if (source == help) {
			navi.changeMode(NewApp.help);
		} else if (source == funky){
			navi.changeMode(NewApp.funky);
		}
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
}
