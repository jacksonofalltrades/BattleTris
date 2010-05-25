package battletris.display;

import java.awt.*;

import javax.swing.*;

import battletris.BazaarInfo;
import battletris.player.PlayerListener;
import battletris.player.PlayerInfo;

public class PlayerDisplay extends JPanel implements PlayerListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String PLAYER_SCORE_LABEL = "  Your Score:";
	protected static final String OPPONENT_SCORE_LABEL = "  Opponent's Score:";
	protected static final String PLAYER_LINES_LABEL = "  Your Lines:";
	protected static final String OPPONENT_LINES_LABEL = "  Opponent's Lines:";
	protected static final String PLAYER_FUNDS_LABEL = "  Your Funds:";
	protected static final String NEXT_BAZAAR_PREFIX_LABEL = "  Next Bazaar in";
	protected static final String NEXT_BAZAAR_SUFFIX_LABEL = " lines";

	protected BazaarInfo m_bazaarInfo;

	protected JPanel m_scorePanel;
	protected JLabel m_playerScoreLabel;
	protected JLabel m_playerScore;
	protected JLabel m_opponentScoreLabel;
	protected JLabel m_opponentScore;

	protected JPanel m_linesPanel;
	protected JLabel m_playerLinesLabel;
	protected JLabel m_playerLines;
	protected JLabel m_opponentLinesLabel;
	protected JLabel m_opponentLines;

	protected JPanel m_fundsPanel;
	protected JLabel m_playerFundsLabel;
	protected JLabel m_playerFunds;
	protected JLabel m_nextBazaarLabel;
	protected JLabel m_nextBazaar;
	
	protected PlayerInfo m_playerInfo;

	public PlayerDisplay(BazaarInfo bi, PlayerInfo playerInfo)
	{
		super(new GridLayout(3, 1, 0, 0));

		m_bazaarInfo = bi;

		setBackground(Color.BLACK);

		Color l_purple = new Color(0.5f, 0.0f, 0.5f);

		m_scorePanel = new JPanel(new GridLayout(2, 2, 20, 0));
		m_scorePanel.setBackground(l_purple);
		m_scorePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

		m_playerScoreLabel = new JLabel(PLAYER_SCORE_LABEL);
		m_playerScoreLabel.setForeground(Color.WHITE);
		m_scorePanel.add(m_playerScoreLabel);

		m_playerScore = new JLabel("0");
		m_playerScore.setForeground(Color.WHITE);
		m_scorePanel.add(m_playerScore);

		m_opponentScoreLabel = new JLabel(OPPONENT_SCORE_LABEL);
		m_opponentScoreLabel.setForeground(Color.WHITE);
		m_scorePanel.add(m_opponentScoreLabel);

		m_opponentScore = new JLabel("0");
		m_opponentScore.setForeground(Color.WHITE);
		m_scorePanel.add(m_opponentScore);

		m_linesPanel = new JPanel(new GridLayout(2, 2, 20, 0));
		m_linesPanel.setBackground(l_purple);
		m_linesPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

		m_playerLinesLabel = new JLabel(PLAYER_LINES_LABEL);
		m_playerLinesLabel.setForeground(Color.WHITE);
		m_linesPanel.add(m_playerLinesLabel);

		m_playerLines = new JLabel("0");
		m_playerLines.setForeground(Color.WHITE);
		m_linesPanel.add(m_playerLines);

		m_opponentLinesLabel = new JLabel(OPPONENT_LINES_LABEL);
		m_opponentLinesLabel.setForeground(Color.WHITE);
		m_linesPanel.add(m_opponentLinesLabel);

		m_opponentLines = new JLabel("0");
		m_opponentLines.setForeground(Color.WHITE);
		m_linesPanel.add(m_opponentLines);

		m_fundsPanel = new JPanel(new GridLayout(2, 2, 20, 0));
		m_fundsPanel.setBackground(l_purple);
		m_fundsPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

		m_playerFundsLabel = new JLabel(PLAYER_FUNDS_LABEL);
		m_playerFundsLabel.setForeground(Color.WHITE);
		m_fundsPanel.add(m_playerFundsLabel);

		m_playerFunds = new JLabel("0");
		m_playerFunds.setForeground(Color.WHITE);
		m_fundsPanel.add(m_playerFunds);

		m_nextBazaarLabel = new JLabel(NEXT_BAZAAR_PREFIX_LABEL);
		m_nextBazaarLabel.setForeground(Color.WHITE);
		m_fundsPanel.add(m_nextBazaarLabel);

		m_nextBazaar = new JLabel(String.valueOf(m_bazaarInfo.getNumLinesToNextBazaar())+NEXT_BAZAAR_SUFFIX_LABEL);
		m_nextBazaar.setForeground(Color.WHITE);
		m_fundsPanel.add(m_nextBazaar);

		add(m_scorePanel);
		add(m_linesPanel);
		add(m_fundsPanel);
		
		m_playerInfo = playerInfo;
	}

	public void playerUpdate(int lines, int points, int funds, PlayerInfo playerInfo)
	{
		boolean opponent = !(playerInfo.getUsername().equals(m_playerInfo.getUsername()));
		
		m_nextBazaar.setText(String.valueOf(m_bazaarInfo.getNumLinesToNextBazaar())+NEXT_BAZAAR_SUFFIX_LABEL);

		if (opponent)
		{
			m_opponentScore.setText(String.valueOf(playerInfo.getScore()));
			m_opponentLines.setText(String.valueOf(playerInfo.getLines()));
		}
		else
		{
			m_playerScore.setText(String.valueOf(playerInfo.getScore()));
			m_playerLines.setText(String.valueOf(playerInfo.getLines()));
			m_playerFunds.setText(String.valueOf(playerInfo.getMoney()));
		}
	}
}
