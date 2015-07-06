package projectmp.common.registry;

import projectmp.common.registry.handler.IErrorLogWriter;
import projectmp.common.registry.handler.SpecsErrorLogWriter;
import projectmp.common.registry.handler.StockErrorLogWriter;

import com.badlogic.gdx.utils.Array;

/**
 * appends things to the error log
 * 
 *
 */
public class ErrorLogRegistry {
	
	private static ErrorLogRegistry instance;

	private ErrorLogRegistry() {
	}

	public static ErrorLogRegistry instance() {
		if (instance == null) {
			instance = new ErrorLogRegistry();
			instance.loadResources();
		}
		return instance;
	}

	private Array<IErrorLogWriter> writers = new Array<>();
	
	private void loadResources() {
		addWriter(new SpecsErrorLogWriter());
		addWriter(new StockErrorLogWriter());
	}

	public void addWriter(IErrorLogWriter writer){
		writers.add(writer);
	}
	
	public String createErrorLog(String consoleOutput){
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < writers.size; i++){
			if(i > 0) builder.append("\n");
			writers.get(i).appendToStart(builder);
			builder.append("\n");
		}
		
		builder.append("\n");
		builder.append(consoleOutput);
		builder.append("\n");
		
		for(int i = 0; i < writers.size; i++){
			builder.append("\n");
			writers.get(i).appendToEnd(builder);
			builder.append("\n");
		}
		
		builder.append("\n");
		
		return builder.toString();
	}
	
}
