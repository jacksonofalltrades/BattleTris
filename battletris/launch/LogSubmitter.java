package battletris.launch;

public class LogSubmitter 
{
	protected static final String LOG_HTML_PATH = "/log/logsubmit.html";
	
	protected String m_username;
	protected String m_sessionId;
	
	protected boolean generateHtml()
	{
		return false;
	}
	
	protected void launchSubmitPage()
	{
		
	}
	
	public LogSubmitter(String username, String sessionId)
	{
		m_username = username;
		m_sessionId = sessionId;
	}
	
	public void submit()
	{
		
	}
}
