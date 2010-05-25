package battletris.weapon;

import battletris.PieceGenerator;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.PieceGenerationWeaponType;

import battletris.piece.IPiece;

public class SoLong extends Weapon implements PieceGenerationWeaponType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SoLong(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
	}

	public Class getWeaponType()
	{
		return PieceGenerationWeaponType.class;
	}

	public void visit(WeaponTarget wt)
	{
		PieceGenerator l_pg = (PieceGenerator)wt.getTarget();

		l_pg.apply(this);
	}

	public boolean regenerate(Class pieceClass)
	{
		if (IPiece.class.equals(pieceClass))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public Object clone()
	{
		SoLong l_other = new SoLong(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
