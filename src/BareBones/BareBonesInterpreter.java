package BareBones;

import java.util.ArrayList;
import java.util.HashMap;

public class BareBonesInterpreter 
{
	public static BareBonesStatement InterpretLine(String[] Lines, LineReference currentLine, HashMap<String, Integer> Variables) throws BareBonesSyntaxException, BareBonesCompilerException
	{
		BareBonesStatement returnStatement = null;
		
		String workingLine = Lines[currentLine.getLineNumber()];
		
		workingLine = removeWhitespace(workingLine);
		
		String[] currentLineParts = workingLine.split(" ");
		
		if(currentLineParts[0].equalsIgnoreCase("clear")) returnStatement = new BareBonesClear(Lines, currentLineParts, currentLine, Variables);
		else if(currentLineParts[0].equalsIgnoreCase("incr")) returnStatement = new BareBonesIncr(Lines, currentLineParts, currentLine, Variables);
		else if(currentLineParts[0].equalsIgnoreCase("decr")) returnStatement = new BareBonesDecr(Lines, currentLineParts, currentLine, Variables);
		else if(currentLineParts[0].equalsIgnoreCase("while")) returnStatement = new BareBonesWhile(Lines, currentLineParts, currentLine, Variables);
		else throw new BareBonesSyntaxException("Statement not valid, unknown command " + currentLineParts[0]);
		
		return returnStatement;
	}
	
	public static ArrayList<BareBonesStatement> InterpretString(String bareBonesCode, HashMap<String, Integer> Variables) throws BareBonesSyntaxException, BareBonesCompilerException
	{
		ArrayList<BareBonesStatement> statements = new ArrayList<BareBonesStatement>();
		String[] lines = bareBonesCode.split(";");
		
		LineReference currentLine = new LineReference(0);
		
		while(currentLine.getLineNumber() < lines.length)
		{
			try 
			{
				statements.add(InterpretLine(lines, currentLine, Variables));
			}
			catch(BareBonesSyntaxException syntaxEx)
			{
				syntaxEx.setLineNumber(currentLine);
				throw syntaxEx;
			}
			catch(BareBonesCompilerException compilerEx)
			{
				compilerEx.setLineNumber(currentLine);
				throw compilerEx;
			}
			
			currentLine.increment();
		}
		
		return statements;
	}
	
	public static void RunProgram(ArrayList<BareBonesStatement> statements) throws BareBonesRuntimeException
	{
		LineReference currentLineNumber = new LineReference(0);
		
		for(BareBonesStatement statement : statements)
		{
			try
			{
				statement.executeStatment(currentLineNumber);
				
				currentLineNumber.increment();
			}
			catch(BareBonesRuntimeException e)
			{
				e.setLineNumber(currentLineNumber);
				throw e;
			}
		}
	}
	
	public static String removeWhitespace(String line)
	{
		String workingLine = line;
		
		workingLine = workingLine.replaceAll("\t", " ");
		workingLine = workingLine.replaceAll("  ", " ");
		workingLine = workingLine.replaceAll("\r", "");
		workingLine = workingLine.replaceAll("\n", "");
		workingLine = workingLine.trim();
		
		return workingLine;
	}
}
