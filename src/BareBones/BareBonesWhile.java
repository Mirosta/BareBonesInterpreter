package BareBones;
import java.util.ArrayList;
import java.util.HashMap;


public class BareBonesWhile extends BareBonesStatement 
{

	String variableName;
	ArrayList<BareBonesStatement> innerStatements = new ArrayList<BareBonesStatement>();
	
	public BareBonesWhile(String[] Lines, String[] CurrentLineParts, LineReference currentLine, HashMap<String, Integer> Variables) throws BareBonesSyntaxException, BareBonesCompilerException
	{
		super(Lines, currentLine, Variables);
		
		if(!CurrentLineParts[0].equalsIgnoreCase("while")) throw new BareBonesCompilerException("The compiler isn't working, shouldn't be creating a while statement");
		if(CurrentLineParts.length != 5) throw new BareBonesSyntaxException("Expecting four arguments for while, variableName not 0 do");
		if(!CurrentLineParts[2].equalsIgnoreCase("not") || !CurrentLineParts[3].equalsIgnoreCase("0") || !CurrentLineParts[4].equalsIgnoreCase("do")) throw new BareBonesSyntaxException("Expecting \"variableName not 0 do\" for while statement");
	
		variableName = CurrentLineParts[1];
		
		
		String lineToRead = "";
		boolean hasFinished = false;
		
		do
		{
			currentLine.increment();
			
			if(currentLine.getLineNumber() >= Lines.length)
			{
				currentLine.setLineNumber(Lines.length-1);
				throw new BareBonesSyntaxException("End expected, but EOF reached");
			}
			
			lineToRead = Lines[currentLine.getLineNumber()];
			hasFinished = BareBonesInterpreter.removeWhitespace(lineToRead).equalsIgnoreCase("end");
			
			if(!hasFinished)
			{
				innerStatements.add(BareBonesInterpreter.InterpretLine(Lines, currentLine, Variables));
			}
		} while(!hasFinished);
	}
	
	@Override
	public void executeStatment(LineReference currentLineNumber) throws BareBonesRuntimeException 
	{
		int startNumber = currentLineNumber.getLineNumber();
		
		while(getVariable(variableName) > 0)
		{
			currentLineNumber.setLineNumber(startNumber);
			for(BareBonesStatement curStatement : innerStatements)
			{
				curStatement.executeStatment(currentLineNumber);
				currentLineNumber.increment();
			}
		}
		currentLineNumber.increment();
	}

}
