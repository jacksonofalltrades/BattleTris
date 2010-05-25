package battletris;

import java.util.ArrayList;
import java.util.HashMap;

public class Inventory implements InventoryView
{
	protected static final int DEFAULT_NUM_SLOTS = 10;

	protected HashMap m_nameSlotMap;
	protected ArrayList[] m_inventorySlots;

 	protected boolean isSlotFilled(int slot)
 	{

		if (slot >= 0 && slot < m_inventorySlots.length)
		{
			if (null != m_inventorySlots[slot])
			{
				if (m_inventorySlots[slot].size() > 0)
				{
					return true;
				}
			}
		}
		return false;
	}

	public Inventory()
	{
		this(DEFAULT_NUM_SLOTS);
	}

	public Inventory(int slots)
	{
		m_inventorySlots = new ArrayList[slots];
		m_nameSlotMap = new HashMap();
	}

	public boolean addItem(InventoryItem item)
	{
		String l_name = item.getName();
		// Check to see if this name is somewhere yet
		Integer l_slotIndex = (Integer)m_nameSlotMap.get(l_name);
		if (null == l_slotIndex)
		{
			boolean l_foundEmpty = false;

			// Find first empty slot
			for(int i = 0; i < m_inventorySlots.length; i++)
			{
				if (null == m_inventorySlots[i])
				{
					m_inventorySlots[i] = new ArrayList();
					m_inventorySlots[i].add(item);
					m_nameSlotMap.put(l_name, new Integer(i));
					l_foundEmpty = true;
					break;
				}
			}

			if (!l_foundEmpty)
			{
				return false;
			}
		}
		else
		{
			ArrayList l_slot = m_inventorySlots[l_slotIndex.intValue()];
			l_slot.add(item);
		}

		return true;
	}

	public InventoryItem getAndRemoveItemAt(int slot)
	{
		if (isSlotFilled(slot))
		{
			InventoryItem l_item = (InventoryItem)m_inventorySlots[slot].remove(0);
			if (m_inventorySlots[slot].size() <= 0)
			{
				m_inventorySlots[slot] = null;
				
				m_nameSlotMap.remove(l_item.getName());
			}

			return l_item;
		}

		return null;
	}

	public String getNameAt(int slot)
	{
		if (isSlotFilled(slot))
		{
			InventoryItem l_item = (InventoryItem)m_inventorySlots[slot].get(0);
			return l_item.getName();
		}

		return null;
	}

	public int getQuantityAt(int slot)
	{
		if (isSlotFilled(slot))
		{
			return m_inventorySlots[slot].size();
		}

		return 0;
	}

	public int size()
	{
		return m_inventorySlots.length;
	}

	public void removeAll()
	{
		m_nameSlotMap.clear();
		for(int i = 0; i < m_inventorySlots.length; i++)
		{
			if (null != m_inventorySlots[i])
			{
				m_inventorySlots[i].clear();
				m_inventorySlots[i] = null;
			}
		}
	}
}
