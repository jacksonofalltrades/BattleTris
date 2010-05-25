package battletris;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import battletris.piece.Piece;

import battletris.weapon.type.PieceGenerationWeaponType;
import battletris.weapon.type.PieceMovementWeaponType;
import battletris.weapon.type.SpecialPieceWeaponType;

public class PieceGenerator
{
	protected ArrayList m_pieceClassList;

	protected Random m_random;
	
	protected ArrayList m_generationWeaponList;
	protected ArrayList m_specialWeaponList;
	protected ArrayList m_pieceMovementWeaponList;
	
	protected ArrayList getFilteredPieceClassList(Class excludeWeaponType)
	{
		return getFilteredPieceClassList(m_pieceClassList, excludeWeaponType);
	}

	protected ArrayList getFilteredPieceClassList(ArrayList origList, Class excludeWeaponType)
	{
		if (null == excludeWeaponType)
		{
			return origList;
		}
		else
		{
			ArrayList l_filteredList = new ArrayList();
			for(int i = 0; i < origList.size(); i++)
			{
				Class l_pc = (Class)origList.get(i);
				if (false == l_pc.equals(excludeWeaponType))
				{
					l_filteredList.add(l_pc);
				}
			}

			return l_filteredList;
		}
	}

	protected Class getNextPieceClass(ArrayList pieceClassList)
	{
		int l_choice = m_random.nextInt(pieceClassList.size());
		Class l_pieceClass = (Class)pieceClassList.get(l_choice);
		
		return l_pieceClass;
	}

	protected Piece generateNewPiece(Board b, Class pieceClass)
	{
		Object[] l_params = new Object[1];
		l_params[0] = b;

		Class[] l_declaredParams = new Class[1];
		l_declaredParams[0] = Board.class;
		Constructor l_cons = null;
		try
		{
			l_cons = pieceClass.getConstructor(l_declaredParams);
		}
		catch(NoSuchMethodException p_nsme)
		{
			return null;
		}
		catch(SecurityException p_se)
		{
			return null;
		}

		Piece l_piece = null;
		try
		{
			l_piece = (Piece)l_cons.newInstance(l_params);
		}
		catch(InstantiationException p_ie)
		{
			return null;
		}
		catch(IllegalAccessException iace)
		{
			return null;
		}
		catch(IllegalArgumentException iare)
		{
			return null;
		}
		catch(InvocationTargetException ite)
		{
			return null;
		}

		return l_piece;
	}

	public PieceGenerator()
	{
		m_pieceClassList = new ArrayList();
		m_pieceClassList.add(battletris.piece.LPiece.class);
		m_pieceClassList.add(battletris.piece.IPiece.class);
		m_pieceClassList.add(battletris.piece.JPiece.class);
		m_pieceClassList.add(battletris.piece.SquarePiece.class);
		m_pieceClassList.add(battletris.piece.SPiece.class);
		m_pieceClassList.add(battletris.piece.TPiece.class);
		m_pieceClassList.add(battletris.piece.ZPiece.class);
		m_pieceClassList.add(battletris.piece.MoneyPiece.class);
		m_pieceClassList.add(battletris.piece.MoneyPiece.class);
		m_pieceClassList.add(battletris.piece.MoneyPiece.class);
		m_pieceClassList.add(battletris.piece.MoneyPiece.class);

		m_random = new Random();
		
		m_specialWeaponList = new ArrayList();
		m_generationWeaponList = new ArrayList();
		m_pieceMovementWeaponList = new ArrayList();
	}

	public void apply(PieceGenerationWeaponType pgwt)
	{
		m_generationWeaponList.add(pgwt);
	}

   	public void apply(SpecialPieceWeaponType spwt)
	{
   		m_specialWeaponList.add(spwt);
	}
   	
   	public void apply(PieceMovementWeaponType pmwt)
   	{
   		m_pieceMovementWeaponList.add(pmwt);
   	}

	public Piece getNextPiece(Board b)
	{
		// Initial attempt
		ArrayList l_filteredPieceClassList = getFilteredPieceClassList(null);
		Class l_pieceClass = getNextPieceClass(l_filteredPieceClassList);

		Piece l_piece = null;

		// Apply weapons first if not expired
		PieceGenerationWeaponType l_pieceGenWeapon;
		
		ArrayList l_tmpGenWeaponList = new ArrayList(m_generationWeaponList);
		for(int i = 0; i < l_tmpGenWeaponList.size(); i++)
		{
			l_pieceGenWeapon = (PieceGenerationWeaponType)l_tmpGenWeaponList.get(i);
		
			if (l_pieceGenWeapon.isActive())
			{
				if (l_pieceGenWeapon.regenerate(l_pieceClass))
				{
					l_filteredPieceClassList = getFilteredPieceClassList(l_filteredPieceClassList, l_pieceClass);
					l_pieceClass = getNextPieceClass(l_filteredPieceClassList);
					l_piece = generateNewPiece(b, l_pieceClass);
				}
			}
			else
			{
				m_generationWeaponList.remove(l_pieceGenWeapon);
			}
		}

		if (null == l_piece)
		{
			l_piece = generateNewPiece(b, l_pieceClass);
		}
		
		ArrayList l_tmpSpecialWeaponList = new ArrayList(m_specialWeaponList);
		SpecialPieceWeaponType l_specialPieceWeapon;
		for(int i = 0; i < l_tmpSpecialWeaponList.size(); i++)
		{
			l_specialPieceWeapon = (SpecialPieceWeaponType)l_tmpSpecialWeaponList.get(i);

			if (l_specialPieceWeapon.isActive())
			{
				l_piece = l_specialPieceWeapon.modifyPiece(b, l_piece);
			}
			else
			{
				m_specialWeaponList.remove(l_specialPieceWeapon);
			}
		}
		
		ArrayList l_tmpPieceMovementWeaponList = new ArrayList(m_pieceMovementWeaponList);
		PieceMovementWeaponType l_pieceMovementWeapon;
		for(int i = 0; i < l_tmpPieceMovementWeaponList.size(); i++)
		{
			l_pieceMovementWeapon = (PieceMovementWeaponType)l_tmpPieceMovementWeaponList.get(i);
			
			if (l_pieceMovementWeapon.isActive())
			{
				l_pieceMovementWeapon.modifyMovement(l_piece);
			}
			else
			{
				m_pieceMovementWeaponList.remove(l_pieceMovementWeapon);
			}
		}

		return l_piece;
	}
}
