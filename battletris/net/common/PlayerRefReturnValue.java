package battletris.net.common;

import java.util.ArrayList;
import java.util.HashSet;

import starwarp.net.DataPacket;
import starwarp.net.PacketIdGenerator;

import battletris.player.PlayerInfo;

public class PlayerRefReturnValue implements DataPacket
{
	private static final long serialVersionUID = 1L;
	protected String m_id;
	protected int m_origIdLength;
	protected String m_senderId;
	
	protected Object m_value;
	
	public PlayerRefReturnValue(MethodCallImpl origCall, Object value)
	{
		m_senderId = origCall.getSenderId();
		m_origIdLength = origCall.getPacketId().length();
		m_id = PacketIdGenerator.returnId(origCall.getPacketId());
		m_value = value;
	}
	
	public int getCallerIdLength()
	{
		return m_origIdLength;
	}
	
	public int type()
	{
		return DataPacket.TYPE_CUST;
	}
	
	public String getPacketId()
	{
		return m_id;
	}
	
	public String getSenderId()
	{
		return m_senderId;
	}
	
	public ArrayList getArrayList()
	{
		return (ArrayList)m_value;
	}
	
	public HashSet getHashSet()
	{
		return (HashSet)m_value;
	}
	
	public String getString()
	{
		return (String)m_value;
	}
	
	public Integer getInteger()
	{
		return (Integer)m_value;
	}
	
	public Boolean getBoolean()
	{
		return (Boolean)m_value;
	}
	
	public PlayerInfo getPlayerInfo()
	{
		return (PlayerInfo)m_value;
	}
	
	public String toString()
	{
		StringBuffer l_sb = new StringBuffer();
		l_sb.append("PlayerRefReturnValue {");
		l_sb.append("\n\tid=");
		l_sb.append(m_id);
		l_sb.append("\n\tsenderId=");
		l_sb.append(m_senderId);
		l_sb.append("\n\tvalue=");
		l_sb.append(m_value);
		return l_sb.toString();
	}
}
