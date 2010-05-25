package battletris.display;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.ArrayList;
import java.util.HashMap;

import java.rmi.RemoteException;

import battletris.Bazaar;
import battletris.BazaarBuyer;
import battletris.InventoryListener;
import battletris.weapon.Weapon;

public class BazaarDisplay extends JPanel implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final String TITLE_IMAGE_PATH = "/images/bazaartitle.jpg";
	protected static final String WEAPON_NAME_PATH = "/images/weaponname.jpg";
	protected static final String WEAPON_DESC_PATH = "/images/weapondesc.jpg";
	protected static final String WEAPON_DURATION_PATH = "/images/weaponduration.jpg";
	protected static final String WEAPON_COST_PATH = "/images/weaponcost.jpg";
	protected static final String CASH_PATH = "/images/bazaarcash.jpg";

	protected static final String EXIT_LABEL = "Done Shopping";
	protected static final String WINDOW_TITLE = "BattleTris Weapons Bazaar";

	protected static final String INSUFFICIENT_FUNDS_MESSAGE = "<html>You cannot<br>afford that</html>";
	protected static final String NO_WEAPON_SPACE_MESSAGE = "<html>You have no room<br>for new weapons</html>";
	
	protected static final String BUYER_CASH_LABEL_TEXT = "Cash: ";

	protected ArrayList m_inventoryListenerList;

	protected Bazaar m_bazaarRef;
	protected BazaarBuyer m_buyerRef;

	protected JPanel m_titlePanel;
	protected JLabel m_bazaarTitle;
	protected JPanel m_weaponTable;
	protected JPanel m_bazaarFooter;
	protected JLabel m_buyerCashLabel;
	protected JLabel m_buyerCash;
	protected JLabel m_bazaarMessages;
	protected JButton m_exitButton;
	
	protected JLabel[] m_weaponCostArr;

	protected JButton[] m_weaponButtonArr;
	protected HashMap m_buttonWeaponMap;
	
	protected Object m_bazaarLock;
	
	protected boolean m_isOpen;

	protected void notifyInventoryListeners()
	{
		if (null != m_buyerRef)
		{
			for(int i = 0; i < m_inventoryListenerList.size(); i++)
			{
				InventoryListener l_il = (InventoryListener)m_inventoryListenerList.get(i);
				l_il.inventoryUpdate(m_buyerRef.getInventory());
			}
		}
	}

	public BazaarDisplay(Bazaar bazaar)
	{
		super(new BorderLayout(0,0));
		//super(app.getVisualFrame(), WINDOW_TITLE, true);

		this.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		
		m_inventoryListenerList = new ArrayList();

		m_bazaarRef = bazaar;
		
		m_bazaarLock = new Object();

		ArrayList l_weaponList = m_bazaarRef.getWeaponList();

		m_buttonWeaponMap = new HashMap();

		Color l_darkBlue = new Color(0.0f, 0.0f, 0.3f);
		m_titlePanel = new JPanel();
		m_titlePanel.setBackground(l_darkBlue);

		//ImageIcon l_titleImage = new ImageIcon(BattleTrisApp.getRootPath()+TITLE_IMAGE_PATH);
		String l_titleImage = "Ye Olde Weapons Bazaar";
		m_bazaarTitle = new JLabel(l_titleImage, SwingConstants.CENTER);
		m_bazaarTitle.setForeground(Color.WHITE);

		m_titlePanel.add(m_bazaarTitle);
		
		//this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		//addWindowListener(this);

		GridBagLayout l_gridBag = new GridBagLayout();
		
		GridBagConstraints l_gbc = new GridBagConstraints();
		m_weaponTable = new JPanel(l_gridBag);//l_weaponList.size()+1, 4));		
		m_weaponTable.setBackground(l_darkBlue);

		// Render header labels
		//ImageIcon l_wname = new ImageIcon(BattleTrisApp.getRootPath()+WEAPON_NAME_PATH);
		//ImageIcon l_wdesc = new ImageIcon(BattleTrisApp.getRootPath()+WEAPON_DESC_PATH);
		//ImageIcon l_wdur = new ImageIcon(BattleTrisApp.getRootPath()+WEAPON_DURATION_PATH);
		//ImageIcon l_wcost = new ImageIcon(BattleTrisApp.getRootPath()+WEAPON_COST_PATH);
		String l_wname = "Name";
		String l_wdesc = "Desc.";
		String l_wdur = "Dur.";
		String l_wcost = "Cost";

		JLabel l_nameHeader = new JLabel(l_wname, SwingConstants.CENTER);
		//l_nameHeader.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		l_nameHeader.setForeground(Color.WHITE);

		JLabel l_descHeader = new JLabel(l_wdesc, SwingConstants.CENTER);
		//l_descHeader.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		l_descHeader.setForeground(Color.WHITE);

		JLabel l_durHeader = new JLabel(l_wdur, SwingConstants.CENTER);
		//l_durHeader.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		l_durHeader.setForeground(Color.WHITE);

		JLabel l_costHeader = new JLabel(l_wcost, SwingConstants.CENTER);
		//l_costHeader.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		l_costHeader.setForeground(Color.WHITE);

		//l_gbc.anchor = GridBagConstraints.NORTHWEST;
		l_gbc.fill = GridBagConstraints.NONE;
		
		l_gbc.gridx = 0;
		l_gbc.gridy = 0;
		l_gbc.anchor = GridBagConstraints.WEST;
		l_gridBag.setConstraints(l_nameHeader, l_gbc);		
		m_weaponTable.add(l_nameHeader);
		
		l_gbc.gridx = 1;
		l_gbc.gridy = 0;
		l_gbc.ipadx = 5;
		l_gridBag.setConstraints(l_descHeader, l_gbc);
		m_weaponTable.add(l_descHeader);

		l_gbc.gridx = 2;
		l_gbc.gridy = 0;
		l_gbc.ipadx = 10;
		l_gridBag.setConstraints(l_costHeader, l_gbc);
		m_weaponTable.add(l_costHeader);
		
		l_gbc.gridx = 3;
		l_gbc.gridy = 0;
		l_gbc.ipadx = 10;
		l_gridBag.setConstraints(l_durHeader, l_gbc);
		m_weaponTable.add(l_durHeader);
		
		m_weaponCostArr = new JLabel[l_weaponList.size()];
		m_weaponButtonArr = new JButton[l_weaponList.size()];

		for(int i = 0; i < l_weaponList.size(); i++)
		{
			Weapon l_weapon = (Weapon)l_weaponList.get(i);

			m_weaponButtonArr[i] = new JButton(l_weapon.getName());
			
			m_weaponButtonArr[i].setForeground(Color.RED);
			//m_weaponButtonArr[i].setBackground(l_darkBlue);
			m_weaponButtonArr[i].addActionListener(this);
			//m_weaponButtonArr[i].setBorder(BorderFactory.createLineBorder(Color.RED, 1));

			JLabel l_desc = new JLabel("<html>&nbsp;"+l_weapon.getDesc()+"</html>", SwingConstants.LEFT);
			//l_desc.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			l_desc.setForeground(Color.RED);

			JLabel l_dur = new JLabel(String.valueOf(l_weapon.getDuration()), SwingConstants.CENTER);
			//l_dur.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			l_dur.setForeground(Color.RED);

			m_weaponCostArr[i] = new JLabel(String.valueOf(l_weapon.getCost()), SwingConstants.CENTER);
			//m_weaponCostArr[i].setBorder(BorderFactory.createLineBorder(Color.RED, 1));
			m_weaponCostArr[i].setForeground(Color.RED);

			l_gbc.gridx = 0;
			l_gbc.gridy = (i+1);
			l_gbc.ipady = 10;
			l_gbc.fill = GridBagConstraints.HORIZONTAL;
			l_gridBag.setConstraints(m_weaponButtonArr[i], l_gbc);
			m_weaponTable.add(m_weaponButtonArr[i]);
			
			l_gbc.gridx = 1;
			l_gbc.gridy = (i+1);
			l_gbc.ipadx = 5;
			l_gbc.ipady = 10;
			l_gbc.fill = GridBagConstraints.NONE;
			l_gridBag.setConstraints(l_desc, l_gbc);
			m_weaponTable.add(l_desc);

			l_gbc.gridx = 2;
			l_gbc.gridy = (i+1);
			l_gbc.ipadx = 10;
			l_gbc.ipady = 10;
			l_gbc.fill = GridBagConstraints.NONE;
			l_gridBag.setConstraints(m_weaponCostArr[i], l_gbc);			
			m_weaponTable.add(m_weaponCostArr[i]);
			
			l_gbc.gridx = 3;
			l_gbc.gridy = (i+1);
			l_gbc.ipadx = 10;
			l_gbc.ipady = 10;
			l_gbc.fill = GridBagConstraints.NONE;
			l_gridBag.setConstraints(l_dur, l_gbc);
			m_weaponTable.add(l_dur);
			
			m_buttonWeaponMap.put(m_weaponButtonArr[i], l_weapon);
		}

		JScrollPane l_weaponScroller = new JScrollPane(m_weaponTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		//l_weaponScroller.setPreferredSize(new Dimension(680, 300));
		l_weaponScroller.setPreferredSize(new Dimension(150, 150));

		m_bazaarFooter = new JPanel(new GridLayout(1, 3, 10, 10));
		m_bazaarFooter.setBackground(l_darkBlue);
		m_bazaarFooter.setBorder(BorderFactory.createLineBorder(Color.RED, 1));

		String l_cashIcon = BUYER_CASH_LABEL_TEXT;
		//ImageIcon l_cashIcon = new ImageIcon(BattleTrisApp.getRootPath()+CASH_PATH);
		m_buyerCashLabel = new JLabel(l_cashIcon, SwingConstants.LEFT);
		m_buyerCashLabel.setForeground(Color.WHITE);

		m_bazaarFooter.add(m_buyerCashLabel);

		/*
		m_buyerCash = new JLabel("");
		m_buyerCash.setForeground(Color.RED);
		m_bazaarFooter.add(m_buyerCash);
		*/

		// Message
		m_bazaarMessages = new JLabel("");
		m_bazaarMessages.setForeground(Color.RED);

		m_bazaarFooter.add(m_bazaarMessages);

		// Empty space
		//m_bazaarFooter.add(new JLabel(""));

		m_exitButton = new JButton(EXIT_LABEL);
		m_exitButton.setForeground(Color.RED);
		m_exitButton.addActionListener(this);

		m_bazaarFooter.add(m_exitButton);

		add(m_titlePanel, BorderLayout.NORTH);
		add(l_weaponScroller, BorderLayout.CENTER);
		add(m_bazaarFooter, BorderLayout.SOUTH);

		//pack();

	}
	
	public boolean isOpen()
	{
		return m_isOpen;
	}

	public void addInventoryListener(InventoryListener invListener)
	{
		m_inventoryListenerList.add(invListener);
	}

	public void showBazaar(BazaarBuyer buyer)
	{
		m_isOpen = true;
		m_buyerRef = buyer;
		
		// Clear messages
		this.m_bazaarMessages.setText("");
		
		// Update weapon costs
		ArrayList l_weapons = m_bazaarRef.getWeaponList();
		int l_len = l_weapons.size();
		if (l_len > m_weaponCostArr.length)
		{
			l_len = m_weaponCostArr.length;
		}
		
		for(int i = 0; i < l_len; i++)
		{
			Weapon l_w = (Weapon)l_weapons.get(i);
			m_weaponCostArr[i].setText(String.valueOf(l_w.getCost()));
			
			this.m_buttonWeaponMap.put(this.m_weaponButtonArr[i], l_w);
		}

		try
		{
			m_buyerCashLabel.setText(BUYER_CASH_LABEL_TEXT+String.valueOf(m_buyerRef.getMoney()));
		}
		catch(RemoteException re)
		{
			System.err.println(re);
		}

		//this.show();
		//this.setVisible(true);
		//m_buyerRef = null;

		JPanel l_parent = (JPanel)this.getParent();
		CardLayout l_cl = (CardLayout)l_parent.getLayout();
		l_cl.show(l_parent, AppDisplay.BAZAAR_DISPLAY);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object src = e.getSource();

		if (src == m_exitButton)
		{
			//this.hide();
			//this.setVisible(false);
			JPanel l_parent = (JPanel)this.getParent();
			CardLayout l_cl = (CardLayout)l_parent.getLayout();
						
			l_cl.show(l_parent, AppDisplay.PLAYER_DISPLAY);
			m_isOpen = false;
			m_buyerRef = null;
		}
		else if (src instanceof JButton)
		{
			Weapon l_weapon = (Weapon)m_buttonWeaponMap.get((JButton)src);

			if (null != l_weapon)
			{				
				if (null != m_buyerRef)
				{					
					int l_money = 0;
					try
					{
						l_money = m_buyerRef.getMoney();
					}
					catch(RemoteException re)
					{
						System.err.println(re);
					}

					if (l_money >= l_weapon.getCost())
					{
						if (m_buyerRef.purchaseWeapon(l_weapon))
						{
							int l_cashLeft = 0;
							try
							{
								l_cashLeft = m_buyerRef.getMoney();
							}
							catch(RemoteException re)
							{
								System.err.println(re);
							}
							m_buyerCashLabel.setText(BUYER_CASH_LABEL_TEXT+String.valueOf(l_cashLeft));
							// Reset
							m_bazaarMessages.setText("");

							this.notifyInventoryListeners();
						}
						else
						{
							m_bazaarMessages.setText(NO_WEAPON_SPACE_MESSAGE);
						}
					}
					else
					{
						// TODO: Show message "not enough money"
						m_bazaarMessages.setText(INSUFFICIENT_FUNDS_MESSAGE);
					}
				}
			}
		}
	}
}
