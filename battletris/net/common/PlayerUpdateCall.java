package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

public class PlayerUpdateCall extends VoidPlayerRefMethodCall 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int m_lines;
	protected int m_points;
	protected int m_funds;
	protected PlayerInfo m_opponent;
	
	public PlayerUpdateCall(PlayerInfo pi, int lines, int points, int funds, PlayerInfo opponent)
	{
		super(pi);
		m_lines = lines;
		m_points = points;
		m_funds = funds;
		m_opponent = opponent;
	}

	public 
	void 
	invoke(ConnectedPlayerProxy p) 
	throws NetworkException 
	{
		p.receivedPlayerUpdate(m_lines, m_points, m_funds, m_opponent);
	}
	
	public String toString()
	{
		StringBuffer l_sb = new StringBuffer(super.toString());
		l_sb.append("\n\tLines=");
		l_sb.append(m_lines);
		l_sb.append("\n\tPoints=");
		l_sb.append(m_points);
		l_sb.append("\n\tFunds=");
		l_sb.append(m_funds);
		l_sb.append("\n\tPlayer=");
		l_sb.append(m_opponent.getUsername());
		
		return l_sb.toString();
	}

}
