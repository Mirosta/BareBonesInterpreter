package BareBones;
import java.util.HashMap;

public class BareBonesDecr extends BareBonesStatement 
{
	String variableName;
	
	public BareBonesDecr(String[] Lines, String[] CurrentLineParts, LineReference currentLine, HashMap<String, Integer> Variables) throws BareBonesSyntaxException, BareBonesCompilerException
	{
		super(Lines, currentLine, Variables);
		
		if(!CurrentLineParts[0].equalsIgnoreCase("decr")) throw new BareBonesCompilerException("The compiler isn't working, shouldn't be creating a decr statement");
		if(CurrentLineParts.length != 2) throw new BareBonesSyntaxException("Expecting one argument for decr, a variable name");
		
		variableName = CurrentLineParts[1];
	}
	
	@Override
	public void executeStatment(LineReference currentLineNumber) throws BareBonesRuntimeException 
	{
		Integer val = getVariable(variableName);
		if(val - 1 < 0) throw new BareBonesRuntimeException("Cannot have a non positive integer value in a variable");
		setVariable(variableName, getVariable(variableName) - 1);
	}

}
