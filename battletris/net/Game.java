package battletris.net;

import battletris.player.ConnectedPlayerProxy;

public class Game
{
	protected ConnectedPlayerProxy m_requestor;
	protected ConnectedPlayerProxy m_opponent;
	protected boolean m_requestorWaiting;
	protected boolean m_opponentWaiting;

	public Game(ConnectedPlayerProxy requestor, ConnectedPlayerProxy opponent)
	{
		m_requestor = requestor;
		m_opponent = opponent;
		m_requestorWaiting = false;
		m_opponentWaiting = false;
	}

	public ConnectedPlayerProxy getRequestor()
	{
		return m_requestor;
	}

	public ConnectedPlayerProxy getOpponent()
	{
		return m_opponent;
	}

	public void setWaiting(ConnectedPlayerProxy player)
	{
		if (player == m_requestor)
		{
			m_requestorWaiting = true;
		}
		else if (player == m_opponent)
		{
			m_opponentWaiting = true;
		}
	}

	public boolean isBazaarDone()
	{
		if (m_requestorWaiting && m_opponentWaiting)
		{
			return true;
		}
		else if (!m_requestorWaiting && !m_opponentWaiting)
		{
			return true;
		}

		return false;
	}

	public void resetBazaar()
	{
		m_requestorWaiting = false;
		m_opponentWaiting = false;
	}
}
