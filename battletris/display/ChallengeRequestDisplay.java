package battletris.display;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import battletris.AppControllable;
import battletris.BattleTrisApp;

public class ChallengeRequestDisplay extends JDialog implements ActionListener, Runnable, WindowListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String CANCEL_TEXT = "Cancel";
	protected static final String WAITING_MESSAGE_PREFIX = "Waiting for ";
	protected static final String WINDOW_TITLE = "";
	protected static final String ACCEPTED_MESSAGE = "Challenge Accepted!";
	protected static final String REJECTED_MESSAGE = "Challenge Rejected!";
	protected static final String TIMEOUT_MESSAGE = "Challenge timed out!";

	protected JFrame m_parentFrame;
	protected ServerPlayerDisplay m_parent;
	protected AppControllable m_ac;

	protected String m_opponentUsername;
	protected JButton m_buttonRef;

	protected JLabel m_messageLabel;
	protected JButton m_cancelButton;

	public ChallengeRequestDisplay(AppFrame app, ServerPlayerDisplay parent, AppControllable ac)
	{
		super(app.getVisualFrame(), WINDOW_TITLE, true);
		m_parent = parent;
		m_ac = ac;

		m_messageLabel = new JLabel("");

		m_cancelButton = new JButton(CANCEL_TEXT);
		m_cancelButton.addActionListener(this);
		
		//this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(m_messageLabel, BorderLayout.CENTER);
		getContentPane().add(m_cancelButton, BorderLayout.SOUTH);

		pack();
	}

	public void setOpponentUsername(String username)
	{
		m_opponentUsername = username;
		m_messageLabel.setText(WAITING_MESSAGE_PREFIX+username);
		pack();
	}

	public void setButton(JButton button)
	{
		m_buttonRef = button;
	}

	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();
		if (src == m_cancelButton)
		{
			class CancelThread extends Thread
			{
				protected AppControllable m_ac;

				public CancelThread(AppControllable ac)
				{
					m_ac = ac;
				}

				public void run()
				{
					m_ac.cancelNewGameRequest();
				}
			}

			CancelThread l_ct = new CancelThread(m_ac);
			l_ct.start();
		}
	}

	public void run()
	{
		int l_result = m_ac.requestNewGame(m_opponentUsername);

		m_cancelButton.setEnabled(false);
		if (AppControllable.REQUEST_ACCEPTED == l_result)
		{
			System.out.println("Request Accepted");

			m_messageLabel.setText(ACCEPTED_MESSAGE);
			pack();

			/*
			try
			{
				Thread.sleep(BattleTrisApp.REQUEST_PAUSE_TIME);
			}
			catch(InterruptedException ie)
			{
			}
			*/

			//this.hide();
			this.setVisible(false);
			m_parent.hideServerPlayerInfo();
		}
		else if (AppControllable.REQUEST_REJECTED == l_result)
		{
			System.out.println("Request Rejected");

			m_messageLabel.setText(REJECTED_MESSAGE);
			pack();

			try
			{
				Thread.sleep(BattleTrisApp.REQUEST_PAUSE_TIME);
			}
			catch(InterruptedException ie)
			{
			}
			//this.hide();
			this.setVisible(false);
		}
		else if (AppControllable.REQUEST_TIMEOUT == l_result)
		{
			System.out.println("Request Timeout");

			// TODO: Set text accordingly, pause briefly and then hide the
			// challenge request dialog
			m_messageLabel.setText(TIMEOUT_MESSAGE);
			pack();
			
			try
			{
				Thread.sleep(BattleTrisApp.REQUEST_PAUSE_TIME);
			}
			catch(InterruptedException ie)
			{
			}
			//this.hide();
			this.setVisible(false);
		}
		else if (AppControllable.REQUEST_CANCELED == l_result)
		{
			System.out.println("Request Canceled");

			//this.hide();
			this.setVisible(false);
		}

		m_cancelButton.setEnabled(true);
		m_buttonRef.setEnabled(true);
	}

	public void windowActivated(WindowEvent e) {
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
