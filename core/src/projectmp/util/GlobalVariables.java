package projectmp.util;

import java.util.HashMap;


public class GlobalVariables {

	
	private HashMap<String, String> strings = new HashMap<String, String>();
	private HashMap<String, Integer> ints = new HashMap<String, Integer>();
	
	public void setString(String key, String value){
		strings.put(key, value);
	}
	
	public String getString(String key){
		if(!strings.containsKey(key)){
			return "";
		}else{
			return strings.get(key);
		}
	}
	
	public void setInt(String key, int value){
		ints.put(key, value);
	}
	
	public int getInt(String key){
		if(!ints.containsKey(key)){
			return 0;
		}else{
			return ints.get(key);
		}
	}
	
	public void clear(){
		strings.clear();
		ints.clear();
	}
}
