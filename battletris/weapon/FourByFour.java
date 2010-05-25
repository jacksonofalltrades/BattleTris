package battletris.weapon;

import battletris.Board;
import battletris.PieceGenerator;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.SpecialPieceWeaponType;

import battletris.piece.Piece;
import battletris.piece.SquarePiece;

public class FourByFour extends Weapon implements SpecialPieceWeaponType
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FourByFour(String name, String desc, Integer duration, Integer cost)
	{
		super(name, desc, duration, cost);
	}

	public Class getWeaponType()
	{
		return SpecialPieceWeaponType.class;
	}

	public void visit(WeaponTarget wt)
	{
		PieceGenerator l_pg = (PieceGenerator)wt.getTarget();

		l_pg.apply(this);
	}

	public Piece modifyPiece(Board b, Piece p)
	{
		// Convert 2x2 SquarePiece into 4x4
		if (SquarePiece.class.equals(p.getClass()))
		{
			return new battletris.piece.FourByFourPiece(b);
		}
		else
		{
			return p;
		}
	}

	public Object clone()
	{
		FourByFour l_other = new FourByFour(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
