package projectmp.common;

import projectmp.client.Updateable;

/**
 * class to handle the ticking
 * 
 *
 */
public class Ticker extends Thread{

	private transient Main main;
	private int tickMsDelay;
	private int tickNanoDelay;
	
	public Ticker(Main main){
		super("Ticker");
		
		this.main = main;
		
		tickMsDelay = 1000 / Main.TICKS;
		tickNanoDelay = 1000000000 % Main.TICKS;
	}
	
	@Override
	public void run() {
		while(true){
			long nano = System.nanoTime();

			if (main.getScreen() != null) ((Updateable) main.getScreen()).tickUpdate();

			main.tickUpdate();

			main.lastTickDurationNano = System.nanoTime() - nano;
			
			main.totalTicksElapsed++;
			
			try {
				this.sleep(tickMsDelay, tickNanoDelay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
