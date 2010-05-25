package battletris.weapon.target;

import battletris.CurrentPieceContainer;

class PieceWeaponTarget implements WeaponTarget
{
	protected CurrentPieceContainer m_target;

	public PieceWeaponTarget(CurrentPieceContainer cpc)
	{
		m_target = cpc;
	}

	public Object getTarget()
	{
		return m_target;
	}
}
