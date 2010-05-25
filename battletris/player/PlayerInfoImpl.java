package battletris.player;

public class PlayerInfoImpl implements PlayerInfo
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int m_score;
	public int m_money;
	public int m_lines;
	String m_displayName;
	String m_username;
	String m_uri;

	public PlayerInfoImpl()
	{
		
	}
	
	public PlayerInfoImpl(String username, String displayName)
	{
		m_username = username;
		m_displayName = displayName;
	}

	public int getScore()
	{
		return m_score;
	}

	public int getMoney()
	{
		return m_money;
	}

	public int getLines()
	{
		return m_lines;
	}

	public String getDisplayName()
	{
		return m_displayName;
	}

	public String getUri()
	{
		return m_uri;
	}

	public String getUsername()
	{
		return m_username;
	}

	public String toString()
	{
		StringBuffer l_sb = new StringBuffer();
		l_sb.append("PlayerInfo {");
		l_sb.append("\n\tusername=");
		l_sb.append(m_username);
		l_sb.append("\n\tdisplayName=");
		l_sb.append(m_displayName);
		l_sb.append("\n\turi=");
		l_sb.append(m_uri);
		l_sb.append("\n\tLines=");
		l_sb.append(m_lines);
		l_sb.append("\n\tPoints=");
		l_sb.append(m_score);
		l_sb.append("\n\tFunds=");
		l_sb.append(m_money);		
		l_sb.append("}");

		return l_sb.toString();
	}

	public boolean equals(Object other)
	{
		if (other instanceof PlayerInfo)
		{
			PlayerInfo l_pi = (PlayerInfo)other;
			if (this.m_username.equals(l_pi.getUsername()))
			{
				return true;
			}
		}
		else if (other instanceof PlayerImpl)
		{
			PlayerImpl l_p = (PlayerImpl)other;
			if (this.m_username.equals(l_p.getUsername()))
			{
				return true;
			}
		}

		return false;
	}
}
