package battletris.weapon;

import battletris.PieceGenerator;
import battletris.piece.Piece;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.PieceMovementWeaponType;

public class TheMadHatter extends Weapon implements PieceMovementWeaponType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TheMadHatter(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
	}

	public Class getWeaponType()
	{
		return PieceMovementWeaponType.class;
	}

	public void visit(WeaponTarget wt)
	{
		PieceGenerator l_pg = (PieceGenerator)wt.getTarget();
		l_pg.apply(this);
	}
	
	public void modifyMovement(Piece p)
	{
		p.addPieceMovementModifier(new SpinModifier());
	}

	public Object clone()
	{
		TheMadHatter l_other = new TheMadHatter(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
