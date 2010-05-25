package battletris;

import java.util.Arrays;

public class BoardImageDataStore 
{
	protected static final int DEFAULT_CAPACITY = 1024;

	protected int capacity;
	protected int[] x;
	protected int[] y;
	protected int[] id;
	
	protected int curr;
	
	public BoardImageDataStore()
	{
		this(DEFAULT_CAPACITY);
	}
	public BoardImageDataStore(int capacity)
	{
		this.capacity = capacity;
		
		x = new int[capacity];
		y = new int[capacity];
		id = new int[capacity];

		Arrays.fill(this.x, BoardImageData.EMPTY);
		Arrays.fill(this.y, BoardImageData.EMPTY);
		Arrays.fill(this.id, BoardImageData.EMPTY);
		
		curr = -1;
	}
	
	public synchronized void addImage(int x, int y, int id)
	{
		curr++;
		this.x[curr] = x;
		this.y[curr] = y;
		this.id[curr] = id;
	}
	
	public boolean isFull()
	{
		return ((this.capacity-1) == this.curr);
	}
	
	public synchronized BoardImageData flush(int seqIndex)
	{
		BoardImageData l_bid = new BoardImageData(seqIndex, curr+1, this.x, this.y, this.id);
		
		curr = -1;
		Arrays.fill(this.x, BoardImageData.EMPTY);
		Arrays.fill(this.y, BoardImageData.EMPTY);
		Arrays.fill(this.id, BoardImageData.EMPTY);
		
		return l_bid;
	}
}
