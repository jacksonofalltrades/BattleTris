package battletris;

public class BlockSetInfo {
	protected int seqIndex;
	protected int size;
	
	private static int globalSeqIndex;
	private static final int GLOBAL_SEQ_START = 12;
	private static final int GLOBAL_SEQ_MAX = 1023;
	
	static {
		globalSeqIndex = GLOBAL_SEQ_START;
	}
	
	private static synchronized int getNextSeqIndex()
	{
		if (GLOBAL_SEQ_MAX == globalSeqIndex) {
			globalSeqIndex = GLOBAL_SEQ_START;
		}
		return ++globalSeqIndex;
	}
	
	public BlockSetInfo(int size)
	{
		this.seqIndex = getNextSeqIndex();
		this.size = size;
	}
	
	public int getSeqIndex()
	{
		return this.seqIndex;
	}
	
	public int getSize()
	{
		return this.size;
	}
}
