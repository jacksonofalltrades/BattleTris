package battletris.net.common;

import starwarp.net.DataPacket;
import starwarp.net.PacketIdGenerator;
import battletris.player.PlayerInfo;

public class MethodCallImpl implements MethodCall, DataPacket
{
	private static final long serialVersionUID = 1L;
	
	protected String m_id;
	
	protected PlayerInfo m_pi;
	
	public MethodCallImpl(PlayerInfo pi)
	{
		m_pi = pi;

		m_id = PacketIdGenerator.newId(TYPE_CUST, m_pi.getUsername());	
	}
	
	public int type()
	{
		return TYPE_CUST;
	}
	
	public String getPacketId()
	{
		return m_id;
	}
	
	public String getSenderId()
	{
		return m_pi.getUsername();
	}
	
	public 
	PlayerInfo
	getPlayerInfo()
	{
		return m_pi;
	}
	
	public String toString()
	{
		StringBuffer l_sb = new StringBuffer();
		l_sb.append("PacketType=");
		l_sb.append(this.getClass().getName());
		l_sb.append("\nPlayerInfo=");
		l_sb.append(m_pi.toString());
		
		return l_sb.toString();
	}
}
