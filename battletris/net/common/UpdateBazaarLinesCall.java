package battletris.net.common;

import starwarp.net.NetworkException;

import battletris.player.ConnectedPlayerProxy;
import battletris.player.PlayerInfo;

public class UpdateBazaarLinesCall extends VoidPlayerRefMethodCall 
{
	private static final long serialVersionUID = 1L;

	protected int m_lines;


	public UpdateBazaarLinesCall(PlayerInfo pi, int lines)
	{
		super(pi);
		m_lines = lines;
	}
	
	public
	int
	getLines()
	{
		return m_lines;
	}

	public 
	void 
	invoke(ConnectedPlayerProxy p) throws NetworkException 
	{
		p.updateBazaarLines(m_lines);
	}
}
