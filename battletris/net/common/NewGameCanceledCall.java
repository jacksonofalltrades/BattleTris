package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

public class NewGameCanceledCall extends VoidPlayerRefMethodCall 
{
	private static final long serialVersionUID = 1L;
	
	public NewGameCanceledCall(PlayerInfo pi)
	{
		super(pi);
	}
	
	public 
	void 
	invoke(ConnectedPlayerProxy p) throws NetworkException 
	{
		p.newGameCanceled();
	}

}
