package battletris.net.common;

import starwarp.net.NetworkException;
import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;

public class RequestReturnFromBazaarCall extends MethodCallImpl implements
		BattleTrisServerRefMethodCall 
{
	private static final long serialVersionUID = 1L;
	protected boolean m_firstRequest;
	
	public RequestReturnFromBazaarCall(PlayerInfo pi, boolean firstRequest)
	{
		super(pi);
		m_firstRequest = firstRequest;
	}

	public void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try {
			boolean l_stat = btServerRef.requestReturnFromBazaar(m_pi.getUsername(), m_firstRequest);
			
			PlayerRefReturnValue l_retValCall = new PlayerRefReturnValue(this, new Boolean(l_stat));
			handler.send(l_retValCall);
		}
		catch(NetworkException ne)
		{
			ne.printStackTrace(System.err);
		}
	}

}
