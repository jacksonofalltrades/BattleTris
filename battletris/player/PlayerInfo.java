package battletris.player;
public interface PlayerInfo extends java.io.Serializable
{	public static final String KEY_DISPLAY_NAME = "dn";	
	public int getScore();
	public int getMoney();
	public int getLines();
	public String getDisplayName();
	public String getUri();
	public String getUsername();
	public String toString();
	public boolean equals(Object other);
}
