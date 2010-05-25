package battletris;

import java.rmi.RemoteException;

import battletris.weapon.Weapon;

public interface BazaarBuyer
{
	public int getMoney() throws RemoteException;

	public boolean purchaseWeapon(Weapon weapon);

	public InventoryView getInventory();
}
