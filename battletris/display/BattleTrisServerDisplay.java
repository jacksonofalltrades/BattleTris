package battletris.display;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class BattleTrisServerDisplay extends JFrame implements ActionListener, WindowListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String SERVER_TITLE = "BattleTris Server";
	protected static final String EXIT_LABEL = "Exit";

	protected JButton m_exitButton;
	
	protected void exit()
	{
		System.exit(0);
	}

	public BattleTrisServerDisplay()
	{
		super(SERVER_TITLE);

		try {
			//UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch(Exception xcp) {
		}

		m_exitButton = new JButton(EXIT_LABEL);
		m_exitButton.addActionListener(this);
		m_exitButton.setPreferredSize(new Dimension(200, 50));
		
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		getContentPane().add(m_exitButton);

		pack();

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		if (src == m_exitButton)
		{
			exit();
		}
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
