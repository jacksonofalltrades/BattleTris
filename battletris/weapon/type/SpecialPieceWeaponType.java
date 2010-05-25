package battletris.weapon.type;

import battletris.Board;
//import battletris.weapon.target.WeaponTarget;

import battletris.piece.Piece;

public interface SpecialPieceWeaponType extends WeaponType
{
	public Piece modifyPiece(Board b, Piece p);
}
