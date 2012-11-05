import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class Interpreter extends Applet 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Timer timer;
	
	HashMap<String, Integer> variables;
	
	Image doubleBuffer;
	Graphics doubleBufferGraphics;
	
	Canvas textViewer;
	
	String fileData = "test output";
	
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
		
		graphics = textViewer.getGraphics();
		
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
		
		textViewer = new Canvas();
		
		textViewer.setBounds(10, 30, 250, 250);
		
		doubleBuffer = createImage(textViewer.getWidth(), textViewer.getHeight());
		doubleBufferGraphics = doubleBuffer.getGraphics();
		
		add(textViewer);
	}
	
	@Override
	public void update(Graphics graphics) 
    { 
         Paint(textViewer.getGraphics()); 
    } 
	
	public void Paint(Graphics graphics)
	{
		doubleBufferGraphics.setColor(Color.red);
		doubleBufferGraphics.fillRect(0, 0, textViewer.getWidth(), textViewer.getHeight());
		
		doubleBufferGraphics.setColor(Color.black);
		doubleBufferGraphics.drawString(fileData, 10, 0);
		
		//graphics.setColor(Color.white);
		graphics.drawImage(doubleBuffer, 0, 0, textViewer);
	}
	
}
