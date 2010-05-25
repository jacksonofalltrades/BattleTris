package battletris;

public class DefaultPerformanceCalculator implements PerformanceCalculator
{
	protected static final int ONE_LINE = 10;

	protected static final int LINE_MULTIPLIER = 2;

	protected static final int FUNDS_MULTIPLIER = 3;

	public int getPoints(int lines, int totalValue)
	{
		if (lines > 0)
		{
			return ONE_LINE*(int)Math.pow(LINE_MULTIPLIER, lines-1);
		}
		else
		{
			return 0;
		}
	}

	public int getFunds(int lines, int totalValue)
	{
		return totalValue*FUNDS_MULTIPLIER*lines;
	}
}