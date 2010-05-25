package battletris.launch;

public interface LauncherRef
{
	public void notifyFailure(int errorCode);
	
	public String getRootPath();
}
