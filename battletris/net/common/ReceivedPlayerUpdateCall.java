package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;

public class ReceivedPlayerUpdateCall extends MethodCallImpl implements BattleTrisServerRefMethodCall
{
	private static final long serialVersionUID = 1L;

	protected int m_lines;
	protected int m_points;
	protected int m_funds;
	protected PlayerInfo m_opponent;
	
	public ReceivedPlayerUpdateCall(PlayerInfo pi, int lines, int points, int funds, PlayerInfo opponent)
	{
		super(pi);
		
		m_lines = lines;
		m_points = points;
		m_funds = funds;
		m_opponent = opponent;
	}
		
	public void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try
		{
			btServerRef.receivedPlayerUpdate(m_pi.getUsername(), m_lines, m_points, m_funds, m_opponent);
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}
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