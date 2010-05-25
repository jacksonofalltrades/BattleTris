package battletris.weapon;

import java.lang.reflect.Constructor;
import java.util.HashMap;

import battletris.Board;
import battletris.PieceGenerator;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.SpecialPieceWeaponType;

import battletris.piece.IPiece;
import battletris.piece.JPiece;
import battletris.piece.LPiece;
import battletris.piece.Piece;
import battletris.piece.SPiece;
import battletris.piece.SquarePiece;
import battletris.piece.TPiece;
import battletris.piece.WeirdIPiece;
import battletris.piece.WeirdJPiece;
import battletris.piece.WeirdLPiece;
import battletris.piece.WeirdSPiece;
import battletris.piece.WeirdSquarePiece;
import battletris.piece.WeirdTPiece;
import battletris.piece.WeirdZPiece;
import battletris.piece.ZPiece;

public class TheFearedWeird extends Weapon implements SpecialPieceWeaponType
{
	protected static final HashMap WEIRD_PIECE_MAP;
	
	static
	{
		WEIRD_PIECE_MAP = new HashMap();
		WEIRD_PIECE_MAP.put(IPiece.class, WeirdIPiece.class);
		WEIRD_PIECE_MAP.put(JPiece.class, WeirdJPiece.class);
		WEIRD_PIECE_MAP.put(LPiece.class, WeirdLPiece.class);
		WEIRD_PIECE_MAP.put(SPiece.class, WeirdSPiece.class);
		WEIRD_PIECE_MAP.put(SquarePiece.class, WeirdSquarePiece.class);
		WEIRD_PIECE_MAP.put(TPiece.class, WeirdTPiece.class);
		WEIRD_PIECE_MAP.put(ZPiece.class, WeirdZPiece.class);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * Turns normal pieces into bizarro versions:
	 * SQ - .  .
     *       ..
     *        
     *  I - .. ..
     *  
     *  J - .. .
     *        .
     *        
     *  S -  . .
     *       ..
     *       
     *        .
     *  T - . .
     *       .
     *       
     *  Z - . .
     *       ..
     *
     *      . .
     *  L - . .
	 */
	public TheFearedWeird(String name, String desc, Integer duration, Integer cost)
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
		Class l_newClass = (Class)WEIRD_PIECE_MAP.get(p.getClass());
		if (null == l_newClass)
		{
			return p;
		}
		else
		{
			// Construct appropriate piece type
			Class[] l_paramTypeArr = new Class[1];
			l_paramTypeArr[0] = Board.class;
			
			Object[] l_paramArr = new Object[1];
			l_paramArr[0] = b;
			
			Constructor l_cons = null;
			Piece l_newPiece = null;
			try
			{
				l_cons = l_newClass.getConstructor(l_paramTypeArr);
				l_newPiece = (Piece)l_cons.newInstance(l_paramArr);
				
				return l_newPiece;
			}
			catch(Exception e)
			{
				e.printStackTrace(System.err);
				return p;
			}
		}
	}

	public Object clone()
	{
		TheFearedWeird l_other = new TheFearedWeird(this.m_name, this.m_description, this.m_duration, this.m_cost);

		return l_other;
	}
}
