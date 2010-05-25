package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

abstract public class VoidPlayerRefMethodCall extends MethodCallImpl implements PlayerRefMethodCall
{
	public VoidPlayerRefMethodCall(PlayerInfo pi)
	{
		super(pi);
	}
	
	public void invoke(ConnectedPlayerProxy p, ReturnValueHandler handler)
	{
		try
		{
			invoke(p);			
		}
		catch(NetworkException re)
		{
			re.getCause().printStackTrace(System.err);
		}
	}
	
	abstract public void invoke(ConnectedPlayerProxy p) throws NetworkException;
}
