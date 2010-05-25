package battletris.net;

import battletris.player.PlayerInfo;

public class ServerPlayerInfo implements java.io.Serializable, Comparable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String m_playerUsername;
	protected String m_playerDisplayName;
	protected String m_playerUri;
	protected String m_opponentUsername;
	protected String m_opponentDisplayName;
	protected boolean m_isRequestor;
	protected boolean m_available;
	protected PlayerInfo m_playerInfo;

	public String toString()
	{
		StringBuffer l_sb = new StringBuffer();
		l_sb.append("ServerPlayerInfo {");
		l_sb.append("\n\tplayerUsername=");
		l_sb.append(m_playerUsername);
		l_sb.append("\n\tplayerDisplayName=");
		l_sb.append(m_playerDisplayName);
		l_sb.append("\n\tplayerUri=");
		l_sb.append(m_playerUri);
		l_sb.append("\n\topponentUsername=");
		l_sb.append(m_opponentUsername);
		l_sb.append("\n\topponentDisplayName=");
		l_sb.append(m_opponentDisplayName);
		l_sb.append("\n\tisRequestor=");
		if (m_isRequestor)
		{
			l_sb.append("true");
		}
		else
		{
			l_sb.append("false");
		}
		l_sb.append("\n\tisAvailable=");
		if (m_available)
		{
			l_sb.append("true");
		}
		else
		{
			l_sb.append("false");
		}
		l_sb.append("");
		l_sb.append("}\n");
		return l_sb.toString();
	}

	public ServerPlayerInfo(String playerUsername, String playerDisplayName, String playerUri)
	{
		m_playerUsername = playerUsername;
		m_playerDisplayName = playerDisplayName;
		m_playerUri = playerUri;
		m_isRequestor = false;
		m_available = true;

		m_playerInfo = new PlayerInfoImpl(m_playerUsername, m_playerDisplayName, m_playerUri);
	}
	
	public int compareTo(Object o)
	{
		ServerPlayerInfo spi = (ServerPlayerInfo)o;
		return this.getUsername().compareTo(spi.getUsername());
	}

	public void setOpponent(ServerPlayerInfo opponent)
	{
		if (null != opponent)
		{
			m_opponentUsername = opponent.getUsername();
			m_opponentDisplayName = opponent.getDisplayName();
		}
		else
		{
			m_opponentUsername = null;
			m_opponentDisplayName = null;
		}
	}

	public void setAvailable(boolean available)
	{
		m_available = available;
	}

	public void setRequestor(boolean requestor)
	{
		m_isRequestor = requestor;
	}

	public String getUsername()
	{
		return m_playerUsername;
	}

	public String getDisplayName()
	{
		return m_playerDisplayName;
	}

	public String getPlayerUri()
	{
		return m_playerUri;
	}

	public String getOpponentUsername()
	{
		return m_opponentUsername;
	}

	public String getOpponentDisplayName()
	{
		return m_opponentDisplayName;
	}

	public boolean isAvailable()
	{
		return m_available;
	}

	public boolean isRequestor()
	{
		return m_isRequestor;
	}
	
	public PlayerInfo getPlayerInfo()
	{
		return m_playerInfo;
	}
}
