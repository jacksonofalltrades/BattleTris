package battletris.weapon.target;

import battletris.Bazaar;

class BazaarWeaponTarget implements WeaponTarget
{
	protected Bazaar m_target;

	public BazaarWeaponTarget(Bazaar b)
	{
		m_target = b;
	}

	public Object getTarget()
	{
		return m_target;
	}
}
