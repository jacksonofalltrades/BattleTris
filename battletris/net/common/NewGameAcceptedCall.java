package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

public class NewGameAcceptedCall extends VoidPlayerRefMethodCall 
{
	private static final long serialVersionUID = 1L;

	protected PlayerInfo m_opponent;
	
	public NewGameAcceptedCall(PlayerInfo pi, PlayerInfo opponent)
	{
		super(pi);
		m_opponent = opponent;
	}
	
	public PlayerInfo getOpponent()
	{
		return m_opponent;
	}

	public 
	void 
	invoke(ConnectedPlayerProxy p) 
	throws NetworkException 
	{
		p.newGameAccepted(m_opponent);
	}
}
