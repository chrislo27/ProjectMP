package projectmp.common.registry;

import projectmp.common.registry.handler.IErrorLogWriter;
import projectmp.common.registry.handler.StandardErrorLogWriter;

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
		addWriter(new StandardErrorLogWriter());
	}

	public void addWriter(IErrorLogWriter writer){
		writers.add(writer);
	}
	
	public String createErrorLog(String consoleOutput){
		StringBuilder builder = new StringBuilder();
		
		for(int i = 0; i < writers.size; i++){
			writers.get(i).appendToStart(builder);
		}
		
		builder.append(consoleOutput);
		
		for(int i = 0; i < writers.size; i++){
			writers.get(i).appendToEnd(builder);
		}
		
		return builder.toString();
	}
	
}
