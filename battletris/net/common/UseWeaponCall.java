package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.net.BattleTrisServer;
import battletris.player.PlayerInfo;
import battletris.weapon.Weapon;

public class UseWeaponCall extends MethodCallImpl implements BattleTrisServerRefMethodCall
{
	private static final long serialVersionUID = 1L;
	
	protected Weapon m_weapon;
	
	public UseWeaponCall(PlayerInfo pi, Weapon weapon)
	{
		super(pi);

		m_weapon = weapon;
	}
		
	public void invoke(BattleTrisServer btServerRef, ReturnValueHandler handler) 
	{
		try
		{
			btServerRef.useWeapon(m_pi.getUsername(), m_weapon);			
		}
		catch(NetworkException ne)
		{
			ne.getCause().printStackTrace(System.err);
		}
	}
}