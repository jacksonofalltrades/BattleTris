package battletris.piece;

public interface Piece extends Runnable
{	
	public void addPieceMovementModifier(PieceMovementModifier pmm);

	public void run();
	
	public void setPause(boolean pause);

	public void togglePause();

	public void stop();

	public void create();

	public void delete();

	public void moveLeft();

	public void moveRight();
	
	public void drop();

	public void drop(long dropDelay);

	public void rotateCW();

	public void rotateCCW();
	
	public void setAllowSlide(boolean allow);
}
