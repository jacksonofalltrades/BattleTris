package battletris.display;

import java.awt.Frame;
import java.awt.event.ActionListener;

public interface AppFrame extends ActionListener
{
	public Frame getVisualFrame();
}