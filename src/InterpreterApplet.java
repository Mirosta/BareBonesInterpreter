import java.applet.Applet;
import java.awt.Button;
import java.awt.Graphics;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

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
	
	TextArea output;
	TextArea input;
	Button runButton;
	
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
		
		input = new TextArea();
		output = new TextArea();
		
		runButton.setBounds(10,10, 80, 18);
		input.setBounds(10, 38, 480, 221);
		output.setBounds(10, 269, 480, 221);
		
		output.setEditable(false);
		
		add(runButton);
		add(input);
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
		
		runButton.addActionListener(runClickListener);
	}
	
	void outputLine(String text)
	{
		output.append(text + "\r\n");
	}
	
	public void onRunClick()
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
		
		outputLine("Done");
	}
	
	public void Paint(Graphics graphics)
	{
	}
	
}
