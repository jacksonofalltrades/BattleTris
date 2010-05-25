package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.BoardImageData;
import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

public class ReceivedBoardUpdateCall extends VoidPlayerRefMethodCall
{
	protected BoardImageData m_data;
	
	public ReceivedBoardUpdateCall(PlayerInfo pi, BoardImageData data)
	{
		super(pi);
		m_data = data;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void invoke(ConnectedPlayerProxy p)
	throws NetworkException
	{
		p.receivedBoardUpdate(m_data);
	}
}
