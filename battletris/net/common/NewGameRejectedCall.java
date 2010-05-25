package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

public class NewGameRejectedCall extends VoidPlayerRefMethodCall 
{
	private static final long serialVersionUID = 1L;
	
	public NewGameRejectedCall(PlayerInfo pi)
	{
		super(pi);
	}
	
	public 
	void 
	invoke(ConnectedPlayerProxy p) throws NetworkException 
	{
		p.newGameRejected();
	}
}
