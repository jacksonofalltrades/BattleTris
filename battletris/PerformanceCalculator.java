package battletris;

public interface PerformanceCalculator
{
	public int getPoints(int lines, int totalValue);

	public int getFunds(int lines, int totalValue);
}