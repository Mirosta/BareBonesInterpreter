package BareBones;

public class LineReference 
{
	private int lineNumber;
	
	public LineReference(int initialLineNumber)
	{
		lineNumber = initialLineNumber;
	}
	
	public int getLineNumber()
	{
		return lineNumber;
	}
	
	public void setLineNumber(int value)
	{
		lineNumber = value;
	}
	
	public void increment()
	{
		lineNumber ++;
	}
}
