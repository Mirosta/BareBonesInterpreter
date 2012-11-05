package BareBones;

public class BareBonesRuntimeException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int lineNumber;
	
	public BareBonesRuntimeException(String errorMessage)
	{
		super(errorMessage);
	}
	
	public void setLineNumber(int value)
	{
		lineNumber = value;
	}
	
	public void setLineNumber(LineReference value)
	{
		lineNumber = value.getLineNumber();
	}
	
	public int getLineNumber()
	{
		return lineNumber;
	}
}
