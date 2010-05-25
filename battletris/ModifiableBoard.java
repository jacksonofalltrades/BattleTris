package battletris;

import java.awt.Color;

import battletris.weapon.type.StaticBoardModWeaponType;

public interface ModifiableBoard 
{
	public Block[][] getBlocks();
	
	public int getMinX();
	public int getMaxX();
	
	public int getMinY();
	public int getMaxY();
	
	public void drawBlock(int t, int x, int y, Color c);
	
	public void eraseBlock(int x, int y);
		
	public void setScale(int x, int y);
	
	public boolean moveSquares(Block[] blocks, int inX, int inY, long sleepTime);
	
	public void addWeapon(StaticBoardModWeaponType wt);
	
	public void removeWeapon(StaticBoardModWeaponType wt);
}
