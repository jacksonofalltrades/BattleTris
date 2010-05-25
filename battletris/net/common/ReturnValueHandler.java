package battletris.net.common;

import starwarp.net.ClosedForSendingException;

public interface ReturnValueHandler 
{
	public void send(PlayerRefReturnValue retVal) throws ClosedForSendingException;
	public PlayerRefReturnValue getReturnValue(String origPacketId);
}
