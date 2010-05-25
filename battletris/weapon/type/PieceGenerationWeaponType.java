package battletris.weapon.type;

//import battletris.weapon.target.WeaponTarget;

public interface PieceGenerationWeaponType extends WeaponType
{
	public boolean regenerate(Class pieceClass);
}
