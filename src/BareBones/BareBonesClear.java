package BareBones;
import java.util.HashMap;


public class BareBonesClear extends BareBonesStatement 
{
	String variableName;
	
	public BareBonesClear(String[] Lines, String[] CurrentLineParts, LineReference currentLine, HashMap<String, Integer> Variables) throws BareBonesSyntaxException, BareBonesCompilerException
	{
		super(Lines, currentLine, Variables);
		
		if(!CurrentLineParts[0].equalsIgnoreCase("clear")) throw new BareBonesCompilerException("The compiler isn't working, shouldn't be creating a clear statement");
		if(CurrentLineParts.length != 2) throw new BareBonesSyntaxException("Expecting one argument for clear, a variable name");
		
		variableName = CurrentLineParts[1];
	}
	
	@Override
	public void executeStatment(LineReference currentLineNumber) 
	{
		setVariable(variableName, 0);
	}

}
