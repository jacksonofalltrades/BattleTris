package battletris.weapon;

import battletris.piece.Piece;
import battletris.piece.PieceMovementModifier;

public class SpinModifier extends Thread implements PieceMovementModifier 
{
	protected static final long SPIN_DELAY = 1000;
	
	protected boolean m_done;
	protected Piece m_piece;

	public SpinModifier()
	{
		m_done = false;
		m_piece = null;
	}

	public void init(Piece p)
	{
		m_piece = p;
		
		this.start();
	}
	
	public void finish()
	{
		m_done = true;
		this.interrupt();
	}
	
	public void run()
	{
		while(!m_done)
		{
			try
			{
				Thread.sleep(SPIN_DELAY);
			}
			catch(InterruptedException ie)
			{
			}
		
			m_piece.rotateCW();
		}
	}
}
