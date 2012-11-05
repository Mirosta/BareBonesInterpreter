import java.util.HashMap;

public class BareBonesIncr extends BareBonesStatement 
{
	String variableName;
	
	public BareBonesIncr(String[] Lines, String[] CurrentLineParts, Integer currentLine, HashMap<String, Integer> Variables) throws BareBonesSyntaxException, BareBonesCompilerException
	{
		super(Lines, currentLine, Variables);
		
		if(!CurrentLineParts[0].equalsIgnoreCase("incr")) throw new BareBonesCompilerException("The compiler isn't working, shouldn't be creating a incr statement");
		if(CurrentLineParts.length != 2) throw new BareBonesSyntaxException("Expecting one argument for incr, a variable name");
		
		variableName = CurrentLineParts[1];
	}
	
	@Override
	public void executeStatment() 
	{
		setVariable(variableName, getVariable(variableName) + 1);
	}

}
