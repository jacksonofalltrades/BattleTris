package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;

public class SendGameEndedCall extends MethodCallImpl implements BattleTrisServerRefMethodCall
{
	private static final long serialVersionUID = 1L;
	
	public SendGameEndedCall(PlayerInfo pi)
	{
		super(pi);
	}
		
	public void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try
		{
			btServerRef.sendGameEnded(m_pi.getUsername());
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}
	}
}