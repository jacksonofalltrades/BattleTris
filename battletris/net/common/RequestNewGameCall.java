package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;

public class RequestNewGameCall extends MethodCallImpl implements BattleTrisServerRefMethodCall
{
	private static final long serialVersionUID = 1L;
	
	protected String m_opponentUsername;
	
	public RequestNewGameCall(PlayerInfo pi, String opponentUsername)
	{
		super(pi);
		
		m_opponentUsername = opponentUsername;
	}
		
	public void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try
		{
			btServerRef.requestNewGame(m_pi.getUsername(), m_opponentUsername);
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}
	}
}