package battletris.weapon;

import battletris.PieceGenerator;
import battletris.piece.Piece;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.PieceMovementWeaponType;

public class SpeedyGonzales extends Weapon implements PieceMovementWeaponType
{
	protected static final long SPEEDY_DELAY = 100;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpeedyGonzales(String name, String desc, Integer duration, Integer cost)
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
		p.drop(SPEEDY_DELAY);
	}

	public Object clone()
	{
		SpeedyGonzales l_other = new SpeedyGonzales(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
