package battletris.player;

import battletris.AppControllable;
import battletris.PieceControllable;

public class PlayerFactory
{
	public static Player makePlayer(AppControllable ac, PieceControllable pc, String serverHost, String username, String displayName)
	{
		PlayerImpl l_player = null;
		l_player = new PlayerImpl(ac, pc, serverHost, username, displayName);
		
		return l_player;

		//return new PlayerAdapter(l_player);
	}
}
