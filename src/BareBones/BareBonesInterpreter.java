package BareBones;

import java.util.ArrayList;
import java.util.HashMap;

public class BareBonesInterpreter 
{
	private static boolean isPaused;
	
	public static LineReference syncObject = new LineReference(0);
	
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
	
	public static void RunProgram(ArrayList<BareBonesStatement> statements) throws BareBonesRuntimeException, InterruptedException
	{
		RunProgram(statements, new LineReference(0));
	}
	
	public static void RunProgram(ArrayList<BareBonesStatement> statements, LineReference startLine) throws BareBonesRuntimeException, InterruptedException
	{
		LineReference currentLineNumber = startLine;
		
		for(BareBonesStatement statement : statements)
		{
			try
			{
				if(isPaused())
				{
					synchronized(syncObject)
					{
						synchronized(currentLineNumber)
						{
							syncObject = currentLineNumber;
							syncObject.wait();
						}
					}
				}
				
				statement.executeStatment(currentLineNumber);
				
				currentLineNumber.increment();
			}
			catch(BareBonesRuntimeException e)
			{
				e.setLineNumber(currentLineNumber);
				throw e;
			}
			catch(InterruptedException e)
			{
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
	
	public static synchronized void pause()
	{
		isPaused = true;
	}
	
	public static synchronized void unpause()
	{
		isPaused = false;
		synchronized(syncObject)
		{
			syncObject.notifyAll();
		}
	}
	
	public static synchronized boolean isPaused()
	{
		return isPaused;
	}
}
