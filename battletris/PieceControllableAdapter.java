package battletris;

import battletris.piece.Piece;

public class PieceControllableAdapter implements PieceControllable
{
	protected Piece m_pieceRef;

	public PieceControllableAdapter()
	{
	}

	public void updatePiece(Piece piece)
	{
		m_pieceRef = piece;
	}

	public void moveLeft()
	{
		if (null != m_pieceRef)
		{
			m_pieceRef.moveLeft();
		}
	}

	public void moveRight()
	{
		if (null != m_pieceRef)
		{
			m_pieceRef.moveRight();
		}
	}

	public void drop()
	{
		if (null != m_pieceRef)
		{
			m_pieceRef.drop();
		}
	}

	public void rotateCCW()
	{
		if (null != m_pieceRef)
		{
			m_pieceRef.rotateCCW();
		}
	}

	public void rotateCW()
	{
		if (null != m_pieceRef)
		{
			m_pieceRef.rotateCW();
		}
	}
}
