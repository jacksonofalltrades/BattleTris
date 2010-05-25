package battletris.net;

import battletris.player.PlayerInfo;

class PlayerInfoImpl implements PlayerInfo 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected String m_username;
	protected String m_displayName;
	protected String m_uri;
	
	PlayerInfoImpl(String username, String displayName, String uri)
	{
		m_username = username;
		m_displayName = displayName;
		m_uri = uri;
	}

	public int getScore() 
	{
		return 0;
	}

	public int getMoney() 
	{
		return 0;
	}

	public int getLines() 
	{
		return 0;
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

}
