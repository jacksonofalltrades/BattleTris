package battletris.display;

import java.awt.*;

import javax.swing.*;

import battletris.InventoryListener;
import battletris.InventoryView;

public class InventoryDisplay extends JPanel implements InventoryListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final String EMPTY_ITEM_SLOT = "< Empty >";

	protected JLabel[] m_inventorySlots;

	public InventoryDisplay(InventoryView inv)
	{
		super(new GridLayout(inv.size(), 1, 0, 0));

		setBackground(Color.RED);
		setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));

		m_inventorySlots = new JLabel[inv.size()];

		for(int i = 0; i < m_inventorySlots.length; i++)
		{
			String l_prefix = "  "+String.valueOf(i)+". ";
			m_inventorySlots[i] = new JLabel(l_prefix+EMPTY_ITEM_SLOT);
			m_inventorySlots[i].setForeground(Color.WHITE);
			add(m_inventorySlots[i]);
		}
	}

	public void inventoryUpdate(InventoryView invView)
	{
		for(int i = 0; i < m_inventorySlots.length; i++)
		{
			String l_name = invView.getNameAt(i);
			int l_qty = invView.getQuantityAt(i);

			String l_prefix = "  "+String.valueOf(i)+". ";

			if (l_qty <= 0)
			{
				m_inventorySlots[i].setText(l_prefix+EMPTY_ITEM_SLOT);
			}
			else
			{
				String l_newLabel = l_name+(l_qty>1?" ("+String.valueOf(l_qty)+")":"");

				m_inventorySlots[i].setText(l_prefix+l_newLabel);
			}
		}
	}
}
