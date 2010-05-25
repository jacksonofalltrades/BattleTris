package battletris.weapon.target;

import battletris.player.Player;

class PlayerWeaponTarget implements WeaponTarget
{
	protected Player m_target;

	public PlayerWeaponTarget(Player p)
	{
		m_target = p;
	}

	public Object getTarget()
	{
		return m_target;
	}
}
