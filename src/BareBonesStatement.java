import java.util.HashMap;


public abstract class BareBonesStatement 
{
	HashMap<String, Integer> variables;
	
	public BareBonesStatement(String[] lines, Integer currentLine, HashMap<String, Integer> Variables)
	{
		variables = Variables;
	}
	
	public abstract void executeStatment() throws BareBonesRuntimeException;
	
	protected void setVariable(String name, Integer value)
	{
		variables.put(name, value);
	}
	
	protected Integer getVariable(String name)
	{
		if(!variables.containsKey(name)) setVariable(name, 0);
		
		return variables.get(name);
	}

}
