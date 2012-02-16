package probeermi;

public class BufferItem
{
	final int[] timeVector;
	final int destination;
	
	public BufferItem(int[] time, int dest)
	{
		timeVector = time;
		this.destination = dest;
	}

	public final int[] getTimeVector()
	{
		return timeVector;
	}
	
	public final int getTimeVector(int index)
	{
		return timeVector[index];
	}

	public final int getDestination()
	{
		return destination;
	}
	
	
}
