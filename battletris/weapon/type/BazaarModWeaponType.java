package battletris.weapon.type;

import battletris.weapon.Weapon;

public interface BazaarModWeaponType extends WeaponType
{
	public Weapon modifyWeapon(Weapon w);
}
