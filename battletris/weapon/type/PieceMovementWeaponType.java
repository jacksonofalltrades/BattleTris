package battletris.weapon.type;

import battletris.piece.Piece;

public interface PieceMovementWeaponType extends WeaponType
{
	public void modifyMovement(Piece p);
}
