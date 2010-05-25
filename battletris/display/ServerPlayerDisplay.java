package battletris.display;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.*;

import battletris.AppControllable;
import battletris.player.PlayerInfo;
import battletris.net.ServerPlayerInfo;

public class ServerPlayerDisplay extends JPanel implements ActionListener, Runnable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final int REFRESH_RATE = 500;
	protected static final String WINDOW_TITLE = "BattleTris Players Online";
	protected static final String SELECTION_TEXT = "Choose A Player To Challenge";
	protected static final String CHALLENGE = "Challenge!";
	protected static final String PLAYING_PREFIX = "Playing with ";

	protected static final String USERNAME_HEADER = "Username";
	protected static final String DISPLAY_NAME_HEADER = "Handle";
	protected static final String CHALLENGE_HEADER = "Status";

	protected static final String CLOSE_TEXT = "Close";

	protected boolean m_isRunning;
	
	protected AppControllable m_ac;
	protected PlayerInfo m_player;

	protected HashSet m_serverPlayerSet;
	protected HashMap m_buttonPlayerInfoMap;
	protected HashMap m_usernameButtonMap;

	protected Font m_textFont;

	protected JPanel m_titlePanel;
	protected JLabel m_serverTitle;
	protected JPanel m_userTable;
	protected JPanel m_dialogFooter;
	protected JButton m_closeButton;

	protected ChallengeRequestDisplay m_challengeRequestDisplay;

	protected JDialog m_requestCancelButton;

	protected Thread m_refreshThread;

	protected Thread m_challengeRequestThread;

	protected boolean m_isShowing;

	protected void refreshPlayerInfo()
	{
		m_serverPlayerSet = m_ac.getPlayerList();
		
		ArrayList l_playerList = new ArrayList(m_serverPlayerSet);
		Collections.sort(l_playerList);

		m_userTable.removeAll();
		m_buttonPlayerInfoMap.clear();
		
		GridBagLayout l_gridBag = new GridBagLayout();
		GridBagConstraints l_gbc = new GridBagConstraints();

		m_userTable.setLayout(l_gridBag);

		//Color l_darkBlue = new Color(0.0f, 0.0f, 0.3f);

		JLabel l_usernameHeader = new JLabel(USERNAME_HEADER);
		l_usernameHeader.setForeground(Color.WHITE);
		//l_usernameHeader.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		//l_usernameHeader.setFont(m_textFont);

		JLabel l_displayNameHeader = new JLabel(DISPLAY_NAME_HEADER);
		l_displayNameHeader.setForeground(Color.WHITE);
		//l_displayNameHeader.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		//l_displayNameHeader.setFont(m_textFont);

		JLabel l_challengeHeader = new JLabel(CHALLENGE_HEADER);
		l_challengeHeader.setForeground(Color.WHITE);
		//l_challengeHeader.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		//l_challengeHeader.setFont(m_textFont);

		l_gbc.fill = GridBagConstraints.HORIZONTAL;
		l_gbc.anchor = GridBagConstraints.NORTHWEST;
		l_gbc.ipadx = 5;
		l_gbc.ipady = 5;

		l_gbc.anchor = GridBagConstraints.NORTHWEST;
		l_gbc.gridx = 0;
		l_gbc.gridy = 0;
		l_gbc.ipadx = 5;
		l_gbc.ipady = 5;
		l_gridBag.setConstraints(l_usernameHeader, l_gbc);
		m_userTable.add(l_usernameHeader);

		l_gbc.anchor = GridBagConstraints.NORTHWEST;
		l_gbc.gridx = 1;
		l_gbc.gridy = 0;
		l_gridBag.setConstraints(l_displayNameHeader, l_gbc);
		m_userTable.add(l_displayNameHeader);

		l_gbc.anchor = GridBagConstraints.NORTHWEST;
		l_gbc.gridx = 2;
		l_gbc.gridy = 0;
		l_gbc.ipadx = 10;
		l_gbc.ipady = 5;
		l_gridBag.setConstraints(l_challengeHeader, l_gbc);
		m_userTable.add(l_challengeHeader);

		int i = 0;
		Iterator l_iter = l_playerList.iterator();
		while(l_iter.hasNext())
		{
			ServerPlayerInfo l_spi = (ServerPlayerInfo)l_iter.next();

			if (!l_spi.getUsername().equals(m_player.getUsername()))
			{
				JLabel l_username = new JLabel(l_spi.getUsername());
				//l_username.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
				l_username.setForeground(Color.RED);
				//l_username.setFont(m_textFont);

				JLabel l_displayName = new JLabel(l_spi.getDisplayName());
				//l_displayName.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
				l_displayName.setForeground(Color.RED);
				//l_displayName.setFont(m_textFont);

				l_gbc.anchor = GridBagConstraints.NORTHWEST;
				l_gbc.gridx = 0;
				l_gbc.gridy = (i+1);
				l_gbc.ipadx = 5;
				l_gbc.ipady = 5;
				l_gridBag.setConstraints(l_username, l_gbc);
				m_userTable.add(l_username);

				l_gbc.anchor = GridBagConstraints.NORTHWEST;
				l_gbc.gridx = 1;
				l_gbc.gridy = (i+1);
				l_gbc.ipadx = 5;
				l_gbc.ipady = 5;
				l_gridBag.setConstraints(l_displayName, l_gbc);
				m_userTable.add(l_displayName);

				if (l_spi.isAvailable())
				{
					JButton l_btn = (JButton)m_usernameButtonMap.get(l_spi.getUsername());
					if (null == l_btn)
					{
						l_btn = new JButton(CHALLENGE);
						l_btn.setForeground(Color.RED);
						//l_btn.setBackground(l_darkBlue);
						l_btn.addActionListener(this);
						//l_btn.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
						//l_btn.setFont(m_textFont);

						m_usernameButtonMap.put(l_spi.getUsername(), l_btn);
					}

					l_gbc.anchor = GridBagConstraints.NORTHWEST;
					l_gbc.gridx = 2;
					l_gbc.gridy = (i+1);
					l_gbc.ipadx = 10;
					l_gridBag.setConstraints(l_btn, l_gbc);
					m_userTable.add(l_btn);
					m_buttonPlayerInfoMap.put(l_btn, l_spi);
				}
				else
				{
					JLabel l_challenge = new JLabel(PLAYING_PREFIX+l_spi.getOpponentDisplayName());
					//l_challenge.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
					l_challenge.setForeground(Color.RED);
					//l_challenge.setFont(m_textFont);

					l_gbc.anchor = GridBagConstraints.NORTHWEST;
					l_gbc.gridx = 2;
					l_gbc.gridy = (i+1);
					l_gbc.ipadx = 10;
					l_gridBag.setConstraints(l_challenge, l_gbc);
					m_userTable.add(l_challenge);
				}
			}
			i++;
		}
		
		this.revalidate();
		this.repaint();
	}

	public void
	showServerPlayerInfo()
	{
		m_isShowing = true;
		//this.show();
		//this.setVisible(true);
		JPanel l_parent = (JPanel)this.getParent();
		CardLayout l_cl = (CardLayout)l_parent.getLayout();
		l_cl.show(l_parent, AppDisplay.SERVER_PLAYER_DISPLAY);
	}

	public void
	hideServerPlayerInfo()
	{
		m_isShowing = false;
		//this.hide();
		//this.setVisible(false);
		JPanel l_parent = (JPanel)this.getParent();
		CardLayout l_cl = (CardLayout)l_parent.getLayout();
		l_cl.show(l_parent, AppDisplay.PLAYER_DISPLAY);
	}

	public void run()
	{
		// TODO: Replace this with a variable so we can shutdown
		while(m_isRunning)
		{
			this.refreshPlayerInfo();

			try
			{
				Thread.sleep(REFRESH_RATE);
			}
			catch(InterruptedException ie)
			{
			}

			while(!m_isShowing)
			{
				try
				{
					Thread.sleep(REFRESH_RATE);
				}
				catch(InterruptedException ie)
				{
				}
			}
		}
	}

	public void shutdown()
	{
		m_isRunning = false;
	}
	
	public ServerPlayerDisplay(AppFrame app, AppControllable ac, PlayerInfo player)
	{
		super(new BorderLayout(0, 0));
		
		this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		
		m_isRunning = true;

		m_ac = ac;
		m_player = player;

		m_buttonPlayerInfoMap = new HashMap();
		m_usernameButtonMap = new HashMap();

		Color l_darkBlue = new Color(0.0f, 0.0f, 0.3f);
		m_textFont = new Font("SansSerif", Font.PLAIN, 10);

		m_titlePanel = new JPanel();
		m_titlePanel.setBackground(l_darkBlue);

		m_serverTitle = new JLabel(SELECTION_TEXT, SwingConstants.CENTER);
		m_serverTitle.setForeground(Color.WHITE);
		//m_serverTitle.setFont(m_textFont);

		m_titlePanel.add(m_serverTitle);

		m_userTable = new JPanel();
		m_userTable.setBackground(l_darkBlue);
		m_userTable.setAlignmentX(SwingConstants.TOP);
		m_userTable.setAlignmentY(SwingConstants.WEST);

		//refreshPlayerInfo(serverPlayerSet);

		m_dialogFooter = new JPanel(new FlowLayout(FlowLayout.LEFT));
		m_dialogFooter.setBackground(l_darkBlue);
		//m_dialogFooter.setBorder(BorderFactory.createLineBorder(Color.RED, 1));

		m_closeButton = new JButton(CLOSE_TEXT);
		m_closeButton.setForeground(Color.RED);
		m_closeButton.setBackground(l_darkBlue);
		m_closeButton.addActionListener(this);
		m_closeButton.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		//m_closeButton.setFont(m_textFont);

		m_dialogFooter.add(m_closeButton);
		
		add(m_titlePanel, BorderLayout.NORTH);
		add(m_userTable, BorderLayout.CENTER);
		add(m_dialogFooter, BorderLayout.SOUTH);

		m_challengeRequestDisplay = new ChallengeRequestDisplay(app, this, m_ac);

		m_isShowing = false;

		m_refreshThread = new Thread(this);
		m_refreshThread.start();
	}

	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		if (src == m_closeButton)
		{
			this.hideServerPlayerInfo();
		}
		else if (src == m_requestCancelButton)
		{
			// This should cause the UI thread that is waiting on a response to become free again
			m_ac.cancelNewGameRequest();
		}
		else if (src instanceof JButton)
		{
			ServerPlayerInfo l_spi = (ServerPlayerInfo)m_buttonPlayerInfoMap.get((JButton)src);

			if (null != l_spi)
			{
				JButton l_btn = (JButton)src;
				l_btn.setEnabled(false);

				m_challengeRequestThread = new Thread(m_challengeRequestDisplay);

				m_challengeRequestDisplay.setOpponentUsername(l_spi.getUsername());
				m_challengeRequestDisplay.setButton(l_btn);

				m_challengeRequestThread.start();
				//m_challengeRequestDisplay.show();
				m_challengeRequestDisplay.setVisible(true);
			}
		}
	}
}
