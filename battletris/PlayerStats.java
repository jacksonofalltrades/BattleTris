package battletris;

import java.util.ArrayList;

import battletris.net.Game;

public interface PlayerStats
{
	public void save(Game game);

	public ArrayList toArrayList();
}
