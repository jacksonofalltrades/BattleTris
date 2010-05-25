package battletris;

import java.util.ArrayList;

public class OpponentRenderThread extends Thread 
{
	protected static final int QUEUE_MAX_LEN = 1024;
	protected OpponentBoardImpl m_oppBoardImpl;
	protected ArrayList m_renderQueue;
	protected boolean m_isRunning;
	
	public OpponentRenderThread(OpponentBoardImpl board)
	{
		m_oppBoardImpl = board;
		m_renderQueue = new ArrayList(QUEUE_MAX_LEN);
		for(int i = 0; i < QUEUE_MAX_LEN; i++)
		{
			m_renderQueue.add(i, null);
		}
		m_isRunning = true;
	}
	
	public void addData(int seqIndex, int[] x, int[] y, int[] id)
	{
		BoardImageData l_data = new BoardImageData(seqIndex, x.length, x, y, id);
		m_renderQueue.set(seqIndex, l_data);
	}
	
	public void shutdown()
	{
		m_isRunning = false;
	}
	
	public void run()
	{
		// Loop through contents of ArrayList and render in order up until first null
		// element, then pause and restart from beginning
		while(m_isRunning)
		{
			boolean l_inRun = false;
			for(int i = 0; i < QUEUE_MAX_LEN; i++)
			{
				if (i < m_renderQueue.size()) {
					BoardImageData l_data = (BoardImageData)m_renderQueue.get(i);
				
					boolean l_hasData = (null != l_data);
					boolean l_startRun = (!l_inRun && l_hasData);
					boolean l_contRun = (l_inRun && l_hasData);
					boolean l_endRun = (l_inRun && !l_hasData);
				
					if (l_startRun)
					{
						l_inRun = true;
						//System.out.println("OpponentRenderThread.run: drawing seq index="+i);
						m_oppBoardImpl.drawBoardImageData(l_data.getX(), l_data.getY(), l_data.getId());
						m_renderQueue.set(i, null);
					}
					else if (l_contRun)
					{
						//System.out.println("OpponentRenderThread.run: drawing seq index="+i);
						m_oppBoardImpl.drawBoardImageData(l_data.getX(), l_data.getY(), l_data.getId());
						m_renderQueue.set(i, null);
					}
					else if (l_endRun)
					{
						l_inRun = false;
						break;
					}
				}
				
				Thread.yield();
			}
		}
	}
}
