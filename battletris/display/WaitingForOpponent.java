package battletris.display;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.*;

public class WaitingForOpponent extends JDialog implements WindowListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String WINDOW_TITLE = "";
	protected static final String MESSAGE_PREFIX = "Waiting for ";

	protected String m_opponentUsername;
	protected JLabel m_messageLabel;

	public WaitingForOpponent(AppFrame parent, String opponentUsername)
	{
		super(parent.getVisualFrame(), WINDOW_TITLE, false);

		m_messageLabel = new JLabel(MESSAGE_PREFIX+opponentUsername);
		
		//this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(m_messageLabel, BorderLayout.CENTER);

		pack();
	}

	public void windowActivated(WindowEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	public void windowClosed(WindowEvent e) 
	{
		// Intentionally do nothing
	}

	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
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
