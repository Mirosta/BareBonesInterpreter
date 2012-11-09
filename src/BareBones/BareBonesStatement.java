package BareBones;
import java.util.HashMap;


public abstract class BareBonesStatement 
{
	HashMap<String, Integer> variables;
	
	public BareBonesStatement(String[] lines, LineReference currentLine, HashMap<String, Integer> Variables)
	{
		variables = Variables;
	}
	
	public abstract void executeStatment(LineReference currentLineNumber) throws BareBonesRuntimeException, InterruptedException;
	
	protected void setVariable(String name, Integer value)
	{
		synchronized(variables)
		{
			variables.put(name, value);
		}
	}
	
	protected Integer getVariable(String name)
	{
		synchronized(variables)
		{
			if(!variables.containsKey(name)) setVariable(name, 0);
		
			return variables.get(name);
		}
	}

}
