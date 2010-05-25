package battletris.display;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import battletris.BattleTrisApp;

public class AppDisplay extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PLAYER_DISPLAY = "P";
	public static final String ABOUT_DISPLAY = "A";
	public static final String BAZAAR_DISPLAY = "B";
	public static final String SERVER_PLAYER_DISPLAY = "SP";

	protected static final String TITLE_IMAGE_PATH = "/images/title.jpg";

	public static final String ABOUT_TITLE = "About BattleTris";
	public static final String ABOUT_MESSAGE = 
			"BattleTris was originally created as a software engineering course\n" +
			"project at Brown University by Bryan Cantrill, Mike Shapiro, and\n" +
			"Charles Hoecker. The original BattleTris, and all references herein\n" +
			"is copyrighted by Brown University and the aforementioned creators.";
	
	public static final String WEB_ADDRESS_MESSAGE_PREFIX = "Your IP Address is ";
	public static final String WEB_ADDRESS_TITLE = "IP Address";
	
	protected AppFrame m_app;

	protected JLabel m_titleLabel;
	/*
	protected JButton m_aboutButton;
	protected JButton m_webAddressButton;
	protected JButton m_quitButton;
	*/
	protected JPanel m_infoPanel;
	
	protected JPanel m_mainDisplay;

	public AppDisplay(AppFrame app, BoardDisplay boardDisplay, InventoryDisplay invDisplay, PlayerDisplay playerDisplay, BoardDisplay oppBoardDisplay, CommunicationDisplay commDisplay)
	{
		m_app = app;
		m_infoPanel = new JPanel(new GridLayout(1,1));
		m_infoPanel.setBackground(Color.BLACK);
		m_infoPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

		ImageIcon l_imgIcon = new ImageIcon(BattleTrisApp.getRootPath()+TITLE_IMAGE_PATH);
		m_titleLabel = new JLabel(l_imgIcon, SwingConstants.LEFT);
		m_infoPanel.add(m_titleLabel);
		
		/*
		JPanel l_bottomPanel = new JPanel(new GridLayout(2,1));
		l_bottomPanel.setBackground(Color.BLACK);
		
		m_infoPanel.add(l_bottomPanel);

		JPanel l_upperInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		l_upperInfoPanel.setBackground(Color.BLACK);

		l_bottomPanel.add(l_upperInfoPanel);

		JLabel l_commandLabel = new JLabel("c - set up controls   p - pause game   s - start game", SwingConstants.CENTER);
		l_commandLabel.setForeground(Color.YELLOW);

		l_upperInfoPanel.add(l_commandLabel);
		*/
		
		//JPanel l_lowerInfoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		//l_lowerInfoPanel.setBackground(Color.BLACK);
		
		//l_bottomPanel.add(l_lowerInfoPanel);

		m_mainDisplay = new JPanel(new CardLayout());
		m_mainDisplay.add(playerDisplay, PLAYER_DISPLAY);
		
		JPanel l_aboutPanel = new JPanel(new BorderLayout(5, 5));
		l_aboutPanel.setBackground(Color.BLACK);
		l_aboutPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		
		JLabel l_aboutTitle = new JLabel(ABOUT_TITLE, SwingConstants.CENTER);
		l_aboutTitle.setForeground(Color.WHITE);
		l_aboutTitle.setBackground(Color.BLACK);
		
		JTextArea l_aboutText = new JTextArea(10, 10);
		l_aboutText.setBackground(Color.BLACK);
		l_aboutText.setForeground(Color.WHITE);
		l_aboutText.setEditable(false);
		l_aboutText.setMargin(new Insets(10,10,10,10));
		l_aboutText.append(ABOUT_MESSAGE);
		
		JButton l_killButton = new JButton("Close");
		l_killButton.setForeground(Color.WHITE);
		l_killButton.setBackground(Color.BLACK);
		l_killButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		l_killButton.addActionListener(this);
		
		JPanel[] l_empties = new JPanel[3];
		for(int i = 0; i < l_empties.length; i++) {
			l_empties[i] = new JPanel();
			l_empties[i].setBackground(Color.BLACK);
		}
		
		JPanel l_killButtonPane = new JPanel(new BorderLayout(5, 5));
		l_killButtonPane.setBackground(Color.BLACK);
		l_killButtonPane.add(l_killButton, BorderLayout.CENTER);
		l_killButtonPane.add(l_empties[0], BorderLayout.SOUTH);
		l_killButtonPane.add(l_empties[1], BorderLayout.WEST);
		l_killButtonPane.add(l_empties[2], BorderLayout.EAST);
						
		l_aboutPanel.add(l_aboutTitle, BorderLayout.NORTH);
		l_aboutPanel.add(l_aboutText, BorderLayout.CENTER);
		l_aboutPanel.add(l_killButtonPane, BorderLayout.SOUTH);
		
		m_mainDisplay.add(l_aboutPanel, ABOUT_DISPLAY);

		JPanel l_playerPanel = new JPanel(new GridLayout(2, 1, 0, 20));
		l_playerPanel.setBackground(Color.BLACK);

		l_playerPanel.add(m_mainDisplay);
		l_playerPanel.add(invDisplay);

		setBackground(Color.BLACK);
		setLayout(new BorderLayout(5, 5));
		add(m_infoPanel, BorderLayout.NORTH);
		add(boardDisplay, BorderLayout.WEST);
		add(l_playerPanel, BorderLayout.CENTER);
		add(oppBoardDisplay, BorderLayout.EAST);
				
		add(commDisplay, BorderLayout.SOUTH);		
	}
	
	public void addPanel(String key, JPanel panel)
	{
		m_mainDisplay.add(panel, key);
	}
	
	public void showPanel(String key)
	{
		CardLayout l_cl = (CardLayout)m_mainDisplay.getLayout();
		l_cl.show(m_mainDisplay, key);
	}
	
	public void addKeyListener(KeyListener kl)
	{
		super.addKeyListener(kl);

		m_titleLabel.addKeyListener(kl);
		m_infoPanel.addKeyListener(kl);
	}

	public void actionPerformed(ActionEvent event)
	{
		showPanel(PLAYER_DISPLAY);
	}
}
