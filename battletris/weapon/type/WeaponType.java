package battletris.weapon.type;

import battletris.weapon.target.WeaponTarget;

public interface WeaponType
{
	public void visit(WeaponTarget wt);

	public boolean isActive();
}
