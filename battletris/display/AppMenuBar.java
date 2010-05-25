package battletris.display;

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import battletris.AppControllable;

public class AppMenuBar extends MenuBar implements ItemListener
{
	private static final long serialVersionUID = 1L;
	
	public static final String CMD_CONNECT = "@";
	public static final String CMD_CHALLENGE = "!";
	public static final String CMD_QUIT = "Q";
	public static final String CMD_CUST = "C";
	public static final String CMD_PAUSE = "P";
	public static final String CMD_SHOW_NEXT = "N";
	public static final String CMD_VIEW_HALL = "V";
	public static final String CMD_ABOUT = "?";
	public static final String CMD_QUICKREF = "QR";
	public static final String CMD_SHOW_MYIP = "I";
	public static final String CMD_SHOW_SERVERIP = "S";
	public static final String CMD_HELP = "H";
	
	protected AppFrame m_app;
	protected AppControllable m_ac;
	
	protected MenuItem m_connect;
	protected MenuItem m_challenge;
	protected MenuItem m_quit;
	
	protected MenuItem m_custControls;
	protected CheckboxMenuItem m_enablePause;
	protected CheckboxMenuItem m_showNextPiece;
	protected MenuItem m_viewHallOfFame;
	
	protected MenuItem m_about;
	protected MenuItem m_quickRef;
	protected CheckboxMenuItem m_showMyIP;
	protected CheckboxMenuItem m_showServerIP;
	protected MenuItem m_help;
		
	public AppMenuBar(AppFrame app, AppControllable ac)
	{
		m_app = app;
		m_ac = ac;
		
		Menu l_playMenu = new Menu("Play");
		m_connect = new MenuItem("Connect...", new MenuShortcut(KeyEvent.VK_AT));
		m_connect.addActionListener(m_app);
		m_connect.setActionCommand(CMD_CONNECT);
		
		l_playMenu.add(m_connect);
		
		m_challenge = new MenuItem("Challenge...", new MenuShortcut(KeyEvent.VK_EXCLAMATION_MARK));
		m_challenge.addActionListener(m_app);
		m_challenge.setActionCommand(CMD_CHALLENGE);
		m_challenge.setEnabled(false);
		l_playMenu.add(m_challenge);
		
		m_quit = new MenuItem("Quit");
		m_quit.addActionListener(m_app);		
		m_quit.setActionCommand(CMD_QUIT);
		l_playMenu.add(m_quit);
				
		Menu l_optionsMenu = new Menu("Options");
		
		m_custControls = new MenuItem("Customize Controls...", new MenuShortcut(KeyEvent.VK_C));
		m_custControls.addActionListener(m_app);
		m_custControls.setActionCommand(CMD_CUST);
		m_custControls.setEnabled(false);
		l_optionsMenu.add(m_custControls);
		
		m_enablePause = new CheckboxMenuItem("Enable Pause", false);
		m_enablePause.setShortcut(new MenuShortcut(KeyEvent.VK_P));
		m_enablePause.addItemListener(this);
		m_enablePause.setActionCommand(CMD_PAUSE);
		m_enablePause.setEnabled(false);

		m_showNextPiece = new CheckboxMenuItem("Show Next Piece", false);
		m_showNextPiece.setShortcut(new MenuShortcut(KeyEvent.VK_N));
		m_showNextPiece.addItemListener(this);
		m_showNextPiece.setActionCommand(CMD_SHOW_NEXT);
		m_showNextPiece.setEnabled(false);
		
		l_optionsMenu.add(m_enablePause);
		l_optionsMenu.add(m_showNextPiece);
		
		m_viewHallOfFame = new MenuItem("View Hall of Fame...", new MenuShortcut(KeyEvent.VK_V));
		m_viewHallOfFame.addActionListener(m_app);
		m_viewHallOfFame.setActionCommand(CMD_VIEW_HALL);
		m_viewHallOfFame.setEnabled(false);
		
		l_optionsMenu.add(m_viewHallOfFame);
				
		Menu l_helpMenu = new Menu("Help");
		
		m_about = new MenuItem("About...", new MenuShortcut(KeyEvent.VK_A));
		m_about.addActionListener(m_app);
		m_about.setActionCommand(CMD_ABOUT);
		l_helpMenu.add(m_about);

		m_quickRef = new MenuItem("Quick Reference...", new MenuShortcut(KeyEvent.VK_Q));
		m_quickRef.addActionListener(m_app);
		m_quickRef.setActionCommand(CMD_QUICKREF);
		m_quickRef.setEnabled(false);
		
		l_helpMenu.add(m_quickRef);
		
		m_showMyIP = new CheckboxMenuItem("Show My IP Address...", false);
		m_showMyIP.setShortcut(new MenuShortcut(KeyEvent.VK_I));
		m_showMyIP.addItemListener(this);
		m_showMyIP.setActionCommand(CMD_SHOW_MYIP);
		
		m_showServerIP = new CheckboxMenuItem("Show Server IP Address...", false);
		m_showServerIP.setShortcut(new MenuShortcut(KeyEvent.VK_S));
		m_showServerIP.addItemListener(this);
		m_showServerIP.setActionCommand(CMD_SHOW_SERVERIP);
		
		l_helpMenu.add(m_showMyIP);
		l_helpMenu.add(m_showServerIP);
		
		m_help = new MenuItem("Help...", new MenuShortcut(KeyEvent.VK_H));
		m_help.addActionListener(m_app);
		m_help.setActionCommand(CMD_HELP);
		m_help.setEnabled(false);
		
		l_helpMenu.add(m_help);
				
		add(l_playMenu);
		add(l_optionsMenu);
		add(l_helpMenu);		
	}
	
	public void setConnectEnabled(boolean ce)
	{
		m_connect.setEnabled(ce);
	}
	
	public void setChallengeEnabled(boolean ce)
	{
		m_challenge.setEnabled(ce);
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		Object src = ie.getSource();
		if (src == m_enablePause) {
			m_ac.setPauseEnabled(ie.getStateChange() == ItemEvent.SELECTED);
		}
		else if (src == m_showNextPiece) {
			m_ac.setShowNextPiece(ie.getStateChange() == ItemEvent.SELECTED);
		}
		else if (src == m_showMyIP) {
			m_ac.setShowPlayerIP(ie.getStateChange() == ItemEvent.SELECTED);
		}
		else if (src == m_showServerIP) {
			m_ac.setShowServerIP(ie.getStateChange() == ItemEvent.SELECTED);
		}
	}
}
