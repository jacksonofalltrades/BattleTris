package battletris;

import java.util.Arrays;

public class BoardImageData implements java.io.Serializable
{
	private static final long serialVersionUID = 1L;
	
	public static final int EMPTY = -7;
	
	protected int seq;
	
	protected int size;
	
	protected int[] x;
	protected int[] y;
	protected int[] id;
		
	public BoardImageData(int seq, int size, int[] x, int[] y, int[] id)
	{
		this.seq = seq;
		this.size = size;
		
		this.x = new int[size];
		this.y = new int[size];
		this.id = new int[size];
		
		Arrays.fill(this.x, EMPTY);
		Arrays.fill(this.y, EMPTY);
		Arrays.fill(this.id, EMPTY);		
		
		setData(x, y, id);
	}
	
	public int getSequenceIndex()
	{
		return this.seq;
	}
	
	private void setData(int[] x, int[] y, int[] id)
	{		
		// Get min size of all values
		int n = Math.min(Math.min(Math.min(x.length, y.length), id.length), this.size);
		
		for(int i = 0; i < n; i++) {
			// Only fill up to first empty
			if (EMPTY == x[i] || EMPTY == y[i] || EMPTY == id[i]) {
				break;
			}
			
			this.x[i] = x[i];
			this.y[i] = y[i];
			this.id[i] = id[i];
		}
	}
	
	public int[] getX()
	{
		return this.x;
	}
	
	public int[] getY()
	{
		return this.y;
	}
	
	public int[] getId()
	{
		return this.id;
	}
	
	public void render(OpponentBoardImpl board)
	{
		board.drawBoardImageData(this.seq, x, y, id);
	}
}