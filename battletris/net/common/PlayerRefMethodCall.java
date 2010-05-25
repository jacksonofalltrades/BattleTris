package battletris.net.common;

import starwarp.net.DataPacket;
import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;

/**
 * Represents a method call on a PlayerRef
 * @author dej
 *
 */
public interface PlayerRefMethodCall extends MethodCall, DataPacket
{
	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	
	void invoke(ConnectedPlayerProxy p, ReturnValueHandler handler)
	throws NetworkException;
}
