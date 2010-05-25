package battletris.net.common;

import java.util.HashMap;

import starwarp.net.ClosedForSendingException;
import starwarp.net.PacketSender;

public class ReturnValueHandlerImpl implements ReturnValueHandler 
{
	protected static final long RETVAL_WAIT = 500;
	
	protected PacketSender m_sender;
	
	protected HashMap m_retValMap;
	
	public ReturnValueHandlerImpl(PacketSender packetSender)
	{
		m_retValMap = new HashMap();
		m_sender = packetSender;
	}
	
	public void addReturnValue(PlayerRefReturnValue retVal)
	{
		String id = retVal.getPacketId();
		int callerIdLen = retVal.getCallerIdLength();		
		String origPacketId = id.substring(0, callerIdLen);
		
		m_retValMap.put(origPacketId, retVal);
	}

	public void send(PlayerRefReturnValue retVal) 
	throws ClosedForSendingException
	{
		m_sender.send(retVal);
	}

	public PlayerRefReturnValue getReturnValue(String origPacketId) 
	{
		PlayerRefReturnValue l_retVal = null;
		while(null == l_retVal)
		{
			if (m_retValMap.containsKey(origPacketId))
			{
				l_retVal = (PlayerRefReturnValue)m_retValMap.remove(origPacketId);
			}	
			try {
					Thread.sleep(RETVAL_WAIT);
			}
			catch(InterruptedException ie)
			{
			}
			
			//System.err.println("ReturnValueHandlerImpl::getReturnValue: still waiting to return value origPacketId="+origPacketId);
		}
		
		return l_retVal;
	}

}
