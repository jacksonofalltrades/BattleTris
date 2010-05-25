package battletris.launch;

import java.awt.*;
import java.awt.event.*;

import java.io.IOException;

import javax.swing.*;

import battletris.BattleTrisApp;
import battletris.BattleTrisExitCodes;

public class BattleTrisLauncher extends JFrame implements ActionListener, ItemListener, LauncherRef, WindowListener
{
	private static final long serialVersionUID = 1L;
	protected static final String USERNAME_LABEL = "Username";
	protected static final String HANDLE_LABEL = "Handle";
	protected static final String SERVER_LABEL = "Server";
	protected static final String IS_SERVER_LABEL = "Is this computer the server?";
	protected static final String LAUNCH_LABEL = "Play BattleTris!!!";
	protected static final String ERROR_TITLE = "Field Error";
	protected static final String ERROR_MESSAGE_SUFFIX = " is a required field.";
	protected static final String SAVE_ERROR_TITLE = "Save Error";
	protected static final String SAVE_ERROR = "There was an error saving your configs!";
	protected static final String EXIT_LABEL = "Exit";
	protected static final String NO_SAVE_WARN = "Exiting now will discard your changes. Are you sure?";
	protected static final String NO_SAVE_TITLE = "Warning";
	protected static final String NO_SERVER = "Could not find server!";
	protected static final String CONNECT_ERROR_TITLE = "Error Connecting";
	protected static final String USER_EXISTS = "User already exists!";
	protected static final String SERVER_FULL = "The server is full!";

	protected static final int PROC_WAIT_TIME = 2000;

	protected String m_rootPath;

	protected LaunchConfig m_launchConfig;

	protected LauncherCleanupThread m_launcherCleanupThread;

	protected Process m_serverProc;
	protected Process m_clientProc;

	protected JLabel m_usernameLabel;
	protected JTextField m_usernameField;
	
	protected JLabel m_handleLabel;
	protected JTextField m_handleField;

	protected JLabel m_serverLabel;
	protected JTextField m_serverField;

	protected JCheckBox m_isServerCheckbox;
	protected boolean m_isServer;

	protected JButton m_launchButton;
	protected JButton m_exitButton;

	protected void enableFields(boolean enable)
	{
		m_usernameField.setEditable(enable);
		m_usernameField.setEnabled(enable);

		m_handleField.setEditable(enable);
		m_handleField.setEnabled(enable);

		m_serverField.setEditable(enable);
		m_serverField.setEnabled(enable);

		m_isServerCheckbox.setEnabled(enable);
	}

	protected void updateFields()
	{
		m_launchConfig.setUsername(m_usernameField.getText());
		m_launchConfig.setHandle(m_handleField.getText());
		m_launchConfig.setServer(m_serverField.getText());
		m_launchConfig.setIsServer(m_isServer);
	}

	protected boolean fieldsChanged()
	{
		boolean l_changed = false;

		// Compare fields to what is in launch config
		if (false == m_usernameField.getText().equals(m_launchConfig.getUsername()))
		{
			l_changed = true;
		}

		if (false == m_handleField.getText().equals(m_launchConfig.getHandle()))
		{
			l_changed = true;
		}

		if (false == m_serverField.getText().equals(m_launchConfig.getServer()))
		{
			l_changed= true;
		}

		if (m_isServer != m_launchConfig.getIsServer())
		{
			l_changed = true;
		}

		return l_changed;
	}

	protected boolean checkFields()
	{
		// Check fields for valid values
		// If any are empty, show an error dialog
		if (m_launchConfig.getUsername().length() <= 0)
		{
			JOptionPane.showMessageDialog(this, USERNAME_LABEL+ERROR_MESSAGE_SUFFIX, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);

			return false;
		}

		if (m_launchConfig.getHandle().length() <= 0)
		{
			JOptionPane.showMessageDialog(this, HANDLE_LABEL+ERROR_MESSAGE_SUFFIX, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);

			return false;
		}

		if (m_launchConfig.getServer().length() <= 0)
		{
			JOptionPane.showMessageDialog(this, SERVER_LABEL+ERROR_MESSAGE_SUFFIX, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		return true;
	}

	protected boolean doServerTest()
	{
		//String l_server = m_launchConfig.getServer();

		// Don't know how to do this right now...

		return true;
	}

	protected boolean isWindows()
	{
		String l_osName = System.getProperty("os.name");
		if (l_osName.startsWith("Windows"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public String getRootPath()
	{
		return m_rootPath;
	}

	protected void launchServer()
	{
		String[] l_cmdArr = null;
		
		l_cmdArr = new String[3];
		l_cmdArr[0] = "java";
		l_cmdArr[1] = "-Dbt.rootpath="+m_rootPath;
		l_cmdArr[2] = "battletris.net.BattleTrisServerImpl";
		
		try
		{
			m_serverProc = Runtime.getRuntime().exec(l_cmdArr);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace(System.err);
		}

		try
		{
			Thread.sleep(PROC_WAIT_TIME);
		}
		catch(InterruptedException ie)
		{
		}
	}

	protected void launchClient()
	{
		String[] l_cmdArr = null;
		
		l_cmdArr = new String[9];
		l_cmdArr[0] = "java";
		l_cmdArr[1] = "-Dbt.istest="+System.getProperty(BattleTrisApp.TEST_MODE_KEY, BattleTrisApp.DEFAULT_TEST_MODE);
		l_cmdArr[2] = "-Dbt.rootpath="+m_rootPath;
		l_cmdArr[3] = "battletris.BattleTrisApp";
		l_cmdArr[4] = m_launchConfig.getServer();
		l_cmdArr[5] = "240";
		l_cmdArr[6] = "480";
		l_cmdArr[7] = m_launchConfig.getUsername();
		l_cmdArr[8] = m_launchConfig.getHandle();
		
		try
		{
			m_clientProc = Runtime.getRuntime().exec(l_cmdArr);
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace(System.err);
		}

		try
		{
			Thread.sleep(PROC_WAIT_TIME);
		}
		catch(InterruptedException ie)
		{
		}
	}

	public BattleTrisLauncher(String rootPath)
	{
		m_rootPath = rootPath;
		m_launchConfig = new LaunchConfig();

		// Attempt to load
		if (false == m_launchConfig.load(m_rootPath+LaunchConfig.DEFAULT_CONFIG_PATH))
		{
			// Do nothing- hopefully, we will save any configs entered
		}
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		getContentPane().setLayout(new GridLayout(5, 1));

		m_usernameLabel = new JLabel(USERNAME_LABEL+": ");
		m_usernameField = new JTextField(m_launchConfig.getUsername());
		m_usernameField.setPreferredSize(new Dimension(150, 25));

		JPanel l_panel1 = new JPanel(new FlowLayout());
		l_panel1.add(m_usernameLabel);
		l_panel1.add(m_usernameField);

		getContentPane().add(l_panel1);

		m_handleLabel = new JLabel(HANDLE_LABEL+": ");
		m_handleField = new JTextField(m_launchConfig.getHandle());
		m_handleField.setPreferredSize(new Dimension(150, 25));

		JPanel l_panel2 = new JPanel(new FlowLayout());
		l_panel2.add(m_handleLabel);
		l_panel2.add(m_handleField);

		getContentPane().add(l_panel2);
		
		m_serverLabel = new JLabel(SERVER_LABEL+": ");
		m_serverField = new JTextField(m_launchConfig.getServer());
		m_serverField.setPreferredSize(new Dimension(150, 25));

		JPanel l_panel3 = new JPanel(new FlowLayout());
		l_panel3.add(m_serverLabel);
		l_panel3.add(m_serverField);

		getContentPane().add(l_panel3);

		m_isServerCheckbox = new JCheckBox(IS_SERVER_LABEL, m_launchConfig.getIsServer());
		m_isServerCheckbox.addItemListener(this);

		m_isServer = m_launchConfig.getIsServer();

		getContentPane().add(m_isServerCheckbox);

		m_launchButton = new JButton(LAUNCH_LABEL);
		m_launchButton.addActionListener(this);

		m_exitButton = new JButton(EXIT_LABEL);
		m_exitButton.addActionListener(this);

		JPanel l_btnPanel = new JPanel(new FlowLayout());
		l_btnPanel.add(m_launchButton);
		l_btnPanel.add(m_exitButton);

		getContentPane().add(l_btnPanel);

		pack();
	}

	public void notifyFailure(int errorCode)
	{
		//this.show();
		this.setVisible(true);
		
		if (BattleTrisExitCodes.NO_SERVER == errorCode)
		{
			JOptionPane.showMessageDialog(this, NO_SERVER, CONNECT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
		else if (BattleTrisExitCodes.USER_ALREADY_EXISTS == errorCode)
		{
			JOptionPane.showMessageDialog(this, USER_EXISTS, CONNECT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
		else if (BattleTrisExitCodes.SERVER_FULL == errorCode)
		{
			JOptionPane.showMessageDialog(this, SERVER_FULL, CONNECT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
		}
		enableFields(true);
	}
	
	protected void exit()
	{
		int l_answer = JOptionPane.YES_OPTION;
		if (fieldsChanged())
		{
			l_answer = JOptionPane.showConfirmDialog(this, NO_SAVE_WARN, NO_SAVE_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		}

		if (JOptionPane.YES_OPTION == l_answer)
		{
			System.exit(0);
		}
	}

	public void actionPerformed(ActionEvent event)
	{
		Object src = event.getSource();

		if (src == m_exitButton)
		{
			exit();
		}
		else if (src == m_launchButton)
		{
			enableFields(false);

			boolean l_fieldsChanged = fieldsChanged();

			if (l_fieldsChanged)
			{
				updateFields();

				// Check fields to see if they are non-empty
				if (false == checkFields())
				{
					enableFields(true);
					return;
				}
			}

			// Do a server test
			if (false == doServerTest())
			{
				enableFields(true);
				return;
			}

			// Write new settings if they changed
			if (l_fieldsChanged)
			{
				if (false == m_launchConfig.save(m_rootPath+LaunchConfig.DEFAULT_CONFIG_PATH))
				{
					JOptionPane.showMessageDialog(this, SAVE_ERROR, SAVE_ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
				}
			}

			// Launch server
			if (m_isServer)
			{
				launchServer();
			}

			// Launch client
			launchClient();

			// Start cleanup thread
			m_launcherCleanupThread = new LauncherCleanupThread(this, m_serverProc, m_clientProc);
			m_launcherCleanupThread.start();

			// Hide window
			//this.hide();
			this.setVisible(false);
		}
	}

	public void itemStateChanged(ItemEvent e)
	{
		Object src = e.getItemSelectable();

		if (src == m_isServerCheckbox)
		{
			if (e.getStateChange() == ItemEvent.DESELECTED)
			{
				m_isServer = false;
			}
			else if (e.getStateChange() == ItemEvent.SELECTED)
			{
				m_isServer = true;
			}
		}
	}

	public static void main(String[] args)
	{
		String l_root = null;
		
		l_root = System.getProperty(battletris.BattleTrisApp.ROOT_PATH_KEY);
		if (null == l_root)
		{
			System.err.println("usage: java -Dbt.rootpath=<root> BattleTrisLauncher");
			System.exit(1);
		}
		
		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception xcp) {
		}

		BattleTrisLauncher l_launcher = new BattleTrisLauncher(l_root);

		l_launcher.setVisible(true);
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) 
	{
	}

	public void windowClosing(WindowEvent e) 
	{
		exit();
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
