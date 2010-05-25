package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;

public class ConnectToServerCall extends MethodCallImpl implements BattleTrisServerRefMethodCall
{
	private static final long serialVersionUID = 1L;
	
	public ConnectToServerCall(PlayerInfo pi)
	{
		super(pi);
	}
		
	public void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try
		{
			int l_result = btServerRef.connectToServer(m_pi);
			
			PlayerRefReturnValue l_retVal = new PlayerRefReturnValue(this, new Integer(l_result));
			
			handler.send(l_retVal);
		}
		catch(NetworkException ne)
		{
			ne.printStackTrace(System.err);
		}
	}
}