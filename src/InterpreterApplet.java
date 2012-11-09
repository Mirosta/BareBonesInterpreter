import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import BareBones.BareBonesCompilerException;
import BareBones.BareBonesInterpreter;
import BareBones.BareBonesRuntimeException;
import BareBones.BareBonesStatement;
import BareBones.BareBonesSyntaxException;


public class InterpreterApplet extends Applet 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	
	ArrayList<BareBonesStatement> program = new ArrayList<BareBonesStatement>();
	HashMap<String, Integer> variables = new HashMap<String, Integer>();
	
	ArrayList<Integer> linePositions = new ArrayList<Integer>(); 
	
	TextArea output;
	JEditorPane input;
	JScrollPane inputScroll;
	
	Button runButton;
	Button pauseButton;
	Button stepButton;
	
	List variableList;
	
	Thread runProgram;
	Thread programMonitor;
	
	Runnable runProgramRunnable;
	Runnable programMonitorRunnable;
	
	@Override
	public void init()
	{
		variables = new HashMap<String, Integer>();
	}

	@Override
	public void start()
	{
		final Graphics graphics;
		
		SetupUI();
		SetupListeners();
		SetupThreads();
		
		graphics = this.getGraphics();
		
		TimerTask task = new TimerTask() 
		{
			public void run()
			{
				Paint(graphics);
			}
		};
		
		timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 0, 16);
	}
	
	void SetupUI()
	{
		this.setLayout(null);
		
		runButton = new Button("Run");
		pauseButton = new Button("Pause");
		stepButton = new Button("Step");
		
		variableList = new List();
		
		input = new JEditorPane();
		inputScroll = new JScrollPane(input);
		output = new TextArea();
		
		runButton.setBounds(10,10, 80, 18);
		pauseButton.setBounds(100, 10, 80, 18);
		stepButton.setBounds(190, 10, 80, 18);
		
		variableList.setBounds(500, 10, 120, 480);
		
		inputScroll.setBounds(10, 38, 480, 221);
		output.setBounds(10, 269, 480, 221);
		
		output.setEditable(false);
		pauseButton.setEnabled(false);
		
		add(runButton);
		add(pauseButton);
		add(stepButton);
		add(variableList);
		add(inputScroll);
		add(output);
	}
	
	void SetupListeners()
	{
		ActionListener runClickListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent action) 
			{
				onRunClick();
			}	
		};
		
		ActionListener pauseClickListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent action) 
			{
				onPauseClick();
			}	
		};
		
		ActionListener stepClickListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent action)
			{
				onStepClick();
			}
		};
		
		DocumentListener inputTextListener = new DocumentListener()
		{
			@Override
			public void changedUpdate(DocumentEvent e) 
			{
				InputChanged();		
			}

			@Override
			public void insertUpdate(DocumentEvent e) 
			{
				InputChanged();	
			}

			@Override
			public void removeUpdate(DocumentEvent e) 
			{
				InputChanged();
			}
			
		};
		
		input.getDocument().addDocumentListener(inputTextListener);
		
		runButton.addActionListener(runClickListener);
		pauseButton.addActionListener(pauseClickListener);
		stepButton.addActionListener(stepClickListener);
	}
	
	void SetupThreads()
	{
		runProgramRunnable = new Runnable()
		{
			@Override
			public void run() 
			{
				input.setEditable(false);
				runButton.setEnabled(false);
				pauseButton.setEnabled(true);
				runProgram();
				
				UpdateVariables();
				Highlighter inputHighlighter = input.getHighlighter();
				inputHighlighter.removeAllHighlights();
				
				pauseButton.setEnabled(false);
				pauseButton.setLabel("Pause");
				programMonitor.interrupt();
				runButton.setEnabled(true);
				input.setEditable(true);
				
				outputLine("");
			}
			
		};
		
		programMonitorRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				monitorProgram();
			}
		};
		
		runProgram = new Thread(runProgramRunnable);
		programMonitor = new Thread(programMonitorRunnable);
	}
	
	void outputLine(String text)
	{
		output.append(text + "\r\n");
	}
	
	public void monitorProgram()
	{
		do
		{
			try 
			{
				Thread.sleep(5000);
			}
			catch (InterruptedException e) 
			{
				return;
			}
		} while(BareBonesInterpreter.isPaused());
		
		if(runProgram.isAlive())
		{
			outputLine("Program running for too long, killing");
			runProgram.interrupt();
		}
	}
	
	public void runProgram()
	{
		outputLine("Interpreting...");
		program.clear();
		variables.clear();
		
		try 
		{
			program = BareBonesInterpreter.InterpretString(input.getText(), variables);
		} 
		catch (BareBonesSyntaxException e) 
		{
			outputLine("Syntax Error:");
			outputLine(e.getMessage());
			outputLine("On Statement " + (e.getLineNumber() + 1));
			
			return;
		} 
		catch (BareBonesCompilerException e) 
		{
			outputLine("Compiler Error:");
			outputLine(e.getMessage());
			outputLine("On Statement " + (e.getLineNumber() + 1));
			
			return;
		}
		
		outputLine("Done");
		outputLine("Running...");
		
		try 
		{
			BareBonesInterpreter.RunProgram(program);
		}
		catch (BareBonesRuntimeException e) 
		{
			outputLine("Runtime Error:");
			outputLine(e.getMessage());
			outputLine("On Statement " + (e.getLineNumber() + 1));
		}
		catch (InterruptedException e)
		{
			outputLine("Program interrupted, exiting");
			return;
		}
		
		outputLine("Done");
	}
	
	public void onRunClick()
	{
		if(runProgram.isAlive()) BareBonesInterpreter.unpause();
		else
		{
			runProgram = new Thread(runProgramRunnable);
			runProgram.start();
			programMonitor = new Thread(programMonitorRunnable);
			programMonitor.start();
		}
		
	}
	
	public void onPauseClick()
	{
		if(BareBonesInterpreter.isPaused())
		{
			pauseButton.setLabel("Pause");
			BareBonesInterpreter.unpause();
			
			input.select(0, 0);
		}
		else
		{
			pauseButton.setLabel("Resume");
			BareBonesInterpreter.pause();
			
			try 
			{
				Thread.sleep(16);
			}
			catch (InterruptedException e) 
			{
			}
			
			UpdateVariables();
		}
	}
	
	public void onStepClick()
	{
		if(runProgram.isAlive())
		{	
			synchronized(BareBonesInterpreter.syncObject)
			{
				BareBonesInterpreter.syncObject.notifyAll();
			}
			try 
			{
				Thread.sleep(16);
			} 
			catch (InterruptedException e) 
			{
			}
			
			UpdateVariables();
		}
		else
		{
			BareBonesInterpreter.pause();
			pauseButton.setLabel("Resume");
			runProgram = new Thread(runProgramRunnable);
			runProgram.start();
			programMonitor = new Thread(programMonitorRunnable);
			programMonitor.start();
			
			variableList.removeAll();
			
			try 
			{
				Thread.sleep(16);
			} 
			catch (InterruptedException e) 
			{
			}
			if(runProgram.isAlive()) UpdateVariables();
		}
	}
	
	public void RecalculateLines()
	{
		linePositions.clear();
		int outputIndex = 0;
		
		for(int i =0; i < input.getText().length();i++)
		{
			if(input.getText().charAt(i) == ';') linePositions.add(outputIndex);
			if(input.getText().charAt(i) == '\r') outputIndex--;
			
			outputIndex++;
		}
	}
	
	public void InputChanged()
	{
		RecalculateLines();
	}
	
	public void UpdateVariables()
	{
		synchronized(variables)
		{
			variableList.removeAll();
		
			for(String key : variables.keySet())
			{
				int val = variables.get(key);
				
				variableList.add(key + ": " + val);
			}
		}
		
		int lineNo = BareBonesInterpreter.syncObject.getLineNumber();
		
		int start, end;
		if(lineNo >= linePositions.size()) 
		{
			start = linePositions.get(linePositions.size()-1) + 1;
			end = input.getText().length();
		}
		else
		{
			if(lineNo == 0) start = 0;
			else start = linePositions.get(lineNo-1)+1;
			
			start = getNextNonWhitespace(start);
			
			end = linePositions.get(lineNo)+1;
		}
		
		Highlighter inputHighlighter = input.getHighlighter();
		try 
		{
			inputHighlighter.removeAllHighlights();
			inputHighlighter.addHighlight(start, end, new DefaultHighlighter.DefaultHighlightPainter(Color.yellow));
		}
		catch (Exception e) 
		{
		}
	}
	
	int getNextNonWhitespace(int start)
	{
		char curChar;
		int offset = -1;
		int retOffset = -1;
		
		do
		{
			offset ++;
			retOffset ++;
			
			if(start + offset >= input.getText().length()) return retOffset-1;
			curChar = input.getText().charAt(start + offset);
		
			if(curChar == '\r') retOffset --;
		}
		while(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t');
		
		return start + retOffset;
	}
	
	public void Paint(Graphics graphics)
	{
	}
	
}
