package battletris.weapon.target;

import battletris.CurrentPieceContainer;
import battletris.ModifiableBoard;
import battletris.PieceGenerator;
import battletris.Bazaar;
import battletris.player.Player;

public class WeaponTargetFactory
{
	public static WeaponTarget makePieceWeaponTarget(CurrentPieceContainer cpc)
	{
		return new PieceWeaponTarget(cpc);
	}

	public static WeaponTarget makeGeneratorWeaponTarget(PieceGenerator pg)
	{
		return new GeneratorWeaponTarget(pg);
	}

	public static WeaponTarget makeBoardWeaponTarget(ModifiableBoard b)
	{
		return new BoardWeaponTarget(b);
	}

	public static WeaponTarget makeBazaarWeaponTarget(Bazaar b)
	{
		return new BazaarWeaponTarget(b);
	}

	public static WeaponTarget makePlayerWeaponTarget(Player p)
	{
		return new PlayerWeaponTarget(p);
	}
}
