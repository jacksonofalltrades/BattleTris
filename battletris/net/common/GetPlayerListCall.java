package battletris.net.common;

import java.util.HashSet;

import starwarp.net.NetworkException;

import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;

public class GetPlayerListCall extends MethodCallImpl implements BattleTrisServerRefMethodCall
{
	private static final long serialVersionUID = 1L;

	public GetPlayerListCall(PlayerInfo pi)
	{
		super(pi);
	}
	
	public 
	void 
	invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try
		{
			HashSet l_plist = btServerRef.getPlayerList();
			
			PlayerRefReturnValue l_retVal = new PlayerRefReturnValue(this, l_plist);
			handler.send(l_retVal);
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}
	}
}