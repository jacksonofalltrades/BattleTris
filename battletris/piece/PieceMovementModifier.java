package battletris.piece;

public interface PieceMovementModifier extends Runnable
{
	public void init(Piece p);
	public void finish();
}
