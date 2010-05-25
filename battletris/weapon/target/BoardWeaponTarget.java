package battletris.weapon.target;

import battletris.ModifiableBoard;

class BoardWeaponTarget implements WeaponTarget
{
	protected ModifiableBoard m_target;

	public BoardWeaponTarget(ModifiableBoard b)
	{
		m_target = b;
	}

	public Object getTarget()
	{
		return m_target;
	}
}
