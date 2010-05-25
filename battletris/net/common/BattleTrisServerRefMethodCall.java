package battletris.net.common;

import battletris.net.BattleTrisServer;

public interface BattleTrisServerRefMethodCall extends MethodCall
{
	/**
	 * 
	 */
	static final long serialVersionUID = 1L;
	
	void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler);
}
