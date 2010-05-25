package battletris.weapon.target;

import battletris.PieceGenerator;

class GeneratorWeaponTarget implements WeaponTarget
{
	protected PieceGenerator m_target;

	public GeneratorWeaponTarget(PieceGenerator p)
	{
		m_target = p;
	}

	public Object getTarget()
	{
		return m_target;
	}
}
