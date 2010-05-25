package battletris;

public interface InventoryView
{
	public String getNameAt(int slot);

	public int getQuantityAt(int slot);

	public int size();
}