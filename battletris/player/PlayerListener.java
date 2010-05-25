package battletris.player;

import starwarp.net.NetworkException;

public interface PlayerListener
{
	public void playerUpdate(int lines, int points, int funds, PlayerInfo playerInfo) throws NetworkException;
}
