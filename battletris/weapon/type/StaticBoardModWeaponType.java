package battletris.weapon.type;

import battletris.ModifiableBoard;

public interface StaticBoardModWeaponType extends WeaponType
{
	public void modifyBoard(ModifiableBoard board);
}
