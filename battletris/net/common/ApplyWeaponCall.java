package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;
import battletris.weapon.Weapon;

public class ApplyWeaponCall extends VoidPlayerRefMethodCall
{
	protected Weapon m_weapon;
	
	public ApplyWeaponCall(PlayerInfo pi, Weapon w)
	{
		super(pi);
		m_weapon = w;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void invoke(ConnectedPlayerProxy p)
	throws NetworkException
	{
		p.applyWeapon(m_weapon);
	}
}
