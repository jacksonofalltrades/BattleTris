package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

public class GameEndedCall extends VoidPlayerRefMethodCall 
{
	private static final long serialVersionUID = 1L;
	
	public GameEndedCall(PlayerInfo pi)
	{
		super(pi);
	}

	public 
	void 
	invoke(ConnectedPlayerProxy p) 
	throws NetworkException 
	{
		p.gameEnded();
	}
}
