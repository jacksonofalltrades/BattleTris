package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.BoardImageData;
import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;

public class SendBoardUpdateCall extends MethodCallImpl implements BattleTrisServerRefMethodCall
{
	private static final long serialVersionUID = 1L;
	
	protected BoardImageData m_data;
	
	public SendBoardUpdateCall(PlayerInfo pi, BoardImageData data)
	{
		super(pi);

		m_data = data;
	}
		
	public void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try
		{
			btServerRef.sendBoardUpdate(m_pi.getUsername(), m_data);
		}
		catch(NetworkException ne)
		{
			ne.printStackTrace(System.err);
		}
	}
}