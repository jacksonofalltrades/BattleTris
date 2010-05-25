package battletris.weapon;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Random;

import battletris.Block;
import battletris.ModifiableBoard;
import battletris.weapon.target.WeaponTarget;
import battletris.weapon.type.StaticBoardModWeaponType;

abstract public class RandomBlockWeapon extends Weapon 
implements StaticBoardModWeaponType 
{
	protected Dimension getRandomBlockDimension(ModifiableBoard board, boolean empty)
	{
		return getRandomBlockDimension(board, empty, board.getMinX(), board.getMinY(), board.getMaxX(), board.getMaxY());
	}

	protected Dimension getRandomBlockDimension(ModifiableBoard board, boolean empty, int minX, int minY, int maxX, int maxY)
	{
		// Modify the board and deactivate myself
		Block[][] l_blocks = board.getBlocks();
		
		ArrayList l_toModify = new ArrayList();
		
		// Find non-empty blocks and randomly pick one
		for(int x = maxX; x >= minX; x--)
		{
			for(int y = maxY; y >= minY; y--)
			{
				if (empty && (null == l_blocks[x][y]))
				{
					l_toModify.add(new Dimension(x,y));
				}
				else if (!empty && (null != l_blocks[x][y]))
				{
					l_toModify.add(new Dimension(x,y));
				}
			}
		}
		
		// Pick a random #
		Random l_rand = new Random();
		
		int l_size = l_toModify.size();
		if (l_size < 0) {
			l_size = 0;
		}
		
		if (l_size < 1) {
			return null;
		}
		
		int l_toModifyIndex = l_rand.nextInt(l_size);

		Dimension l_blockToModify = null;
		if (l_toModifyIndex >= 0)
		{
			l_blockToModify = (Dimension)l_toModify.get(l_toModifyIndex);
		}
		
		return l_blockToModify;
	}
	
	public RandomBlockWeapon(String name, String desc, Integer duration,
			Integer cost) {
		super(name, desc, duration, cost);
	}

	public Class getWeaponType() 
	{
		return StaticBoardModWeaponType.class;	
	}

	abstract public void visit(WeaponTarget wt);
	
	final public void modifyBoard(ModifiableBoard board)
	{
		// Do nothing
	}
}
