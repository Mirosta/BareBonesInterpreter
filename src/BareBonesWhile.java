import java.util.HashMap;


public class BareBonesWhile extends BareBonesStatement 
{

	String variableName;
	
	public BareBonesWhile(String[] Lines, String[] CurrentLineParts, Integer currentLine, HashMap<String, Integer> Variables) throws BareBonesSyntaxException, BareBonesCompilerException
	{
		super(Lines, currentLine, Variables);
		
		if(!CurrentLineParts[0].equalsIgnoreCase("while")) throw new BareBonesCompilerException("The compiler isn't working, shouldn't be creating a while statement");
		if(CurrentLineParts.length != 5) throw new BareBonesSyntaxException("Expecting four arguments for while, variableName not 0 do");
		if(!CurrentLineParts[2].equalsIgnoreCase("not") || !CurrentLineParts[3].equalsIgnoreCase("0") || !CurrentLineParts[4].equalsIgnoreCase("do")) throw new BareBonesSyntaxException("Expecting \"variableName not 0 do\" for while statement");
	
		variableName = CurrentLineParts[1];
	}
	
	@Override
	public void executeStatment() 
	{
		
	}

}
