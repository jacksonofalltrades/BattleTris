package battletris.piece;

public class PieceAdapter implements Piece
{
	protected PieceImpl m_realPiece;
	
	public PieceAdapter(PieceImpl p)
	{
		m_realPiece = p;
		
		// TODO: Fill this in to mutate the real piece
		
		//m_realPiece.makeBlockAt();
	}

	public void addPieceMovementModifier(PieceMovementModifier pmm)
	{
		m_realPiece.addPieceMovementModifier(pmm);
	}

	public void run()
	{
		m_realPiece.run();
	}
	
	public void setPause(boolean pause)
	{
		m_realPiece.setPause(pause);
	}

	public void togglePause()
	{
		m_realPiece.togglePause();
	}

	public void stop()
	{
		m_realPiece.stop();
	}

	public void create()
	{
		m_realPiece.create();
	}

	public void delete()
	{
		m_realPiece.delete();
	}

	public void moveLeft()
	{
		m_realPiece.moveLeft();
	}

	public void moveRight()
	{
		m_realPiece.moveRight();
	}
	
	public void drop()
	{
		m_realPiece.drop();
	}

	public void drop(long dropDelay)
	{
		m_realPiece.drop(dropDelay);
	}

	public void rotateCW()
	{
		m_realPiece.rotateCW();
	}

	public void rotateCCW()
	{
		m_realPiece.rotateCCW();
	}
	
	public void setAllowSlide(boolean allow)
	{
		m_realPiece.setAllowSlide(allow);
	}
}
