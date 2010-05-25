package battletris.display;

import java.awt.*;

import javax.swing.*;

import battletris.Board;

public class BoardDisplay extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected JLabel m_playerHandle;

	public BoardDisplay(boolean opponent, Board board)
	{
		super(new BorderLayout(0, 0));
		//super(new FlowLayout(FlowLayout.CENTER, 0, 0));
		
		m_playerHandle = new JLabel("");
		m_playerHandle.setBackground(Color.BLACK);
		m_playerHandle.setHorizontalAlignment(SwingConstants.CENTER);
		this.setPlayerHandle(opponent, " ");
		
		add(m_playerHandle, BorderLayout.NORTH);
		add(board.getComponent(), BorderLayout.CENTER);

		add(board.getComponent());
		setBackground(Color.DARK_GRAY);
		setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
	}
	
	public void setPlayerHandle(boolean isOpponent, String handle)
	{
		if (isOpponent)
		{
			m_playerHandle.setForeground(Color.RED);
			m_playerHandle.setBorder(BorderFactory.createLineBorder(Color.RED, 3));		
		}
		else {
			m_playerHandle.setForeground(Color.GREEN);
			m_playerHandle.setBorder(BorderFactory.createLineBorder(Color.GREEN, 3));
		}
		m_playerHandle.setText(handle);
	}
}
