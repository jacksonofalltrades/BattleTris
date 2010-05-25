package battletris.display;

import java.awt.*;

import javax.swing.*;

public class CommunicationDisplay extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	protected JTextArea m_textArea;
	
	protected JTextArea m_ipMessages;
	
	protected String m_clientIPMessage;
	protected String m_serverIPMessage;

	public CommunicationDisplay()
	{
		super();

		setBackground(Color.BLACK);
		setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		
		m_textArea = new JTextArea(3, 50);
		m_textArea.setForeground(Color.YELLOW);
		m_textArea.setBackground(Color.BLACK);
		m_textArea.setEditable(false);
		m_textArea.setMargin(new Insets(3, 3, 3, 3));
		
		JScrollPane l_textScroller = new JScrollPane(m_textArea);
		l_textScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		m_ipMessages = new JTextArea(2, 20);
		m_ipMessages.setForeground(Color.YELLOW);
		m_ipMessages.setBackground(Color.BLACK);
		m_ipMessages.setEditable(false);
		m_ipMessages.setMargin(new Insets(3, 3, 3, 3));		
		m_clientIPMessage = "\n";
		m_serverIPMessage = "\n";
		
		GridBagLayout l_gbl = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		
		this.setLayout(l_gbl);

		c.gridx = 0;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 2;
		c.weightx = 2.0;
		c.weighty = 2.0;
		c.fill = GridBagConstraints.BOTH;
		l_gbl.setConstraints(l_textScroller, c);
		this.add(l_textScroller);

		c.gridx = 2;
		c.gridy = 0;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		l_gbl.setConstraints(m_ipMessages, c);
		this.add(m_ipMessages);
	}
	
	public void addMessage(String msg)
	{		
		m_textArea.append(msg+"\n");
	}
	
	public void setClientIPMessage(String addr)
	{
		if (null != addr) {
			m_clientIPMessage = "Client IP Address: ["+addr+"]\n";
		}
		else {
			m_clientIPMessage = "\n";
		}
		
		m_ipMessages.setText(null);
		m_ipMessages.append(m_clientIPMessage);
		m_ipMessages.append(m_serverIPMessage);
	}
	
	public void setServerIPMessage(String addr)
	{
		if (null != addr) {
			m_serverIPMessage = "Server IP Address: ["+addr+"]\n";
		}
		else {
			m_serverIPMessage = "\n";			
		}
		
		m_ipMessages.setText(null);
		m_ipMessages.append(m_clientIPMessage);
		m_ipMessages.append(m_serverIPMessage);
	}
}