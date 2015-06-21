package projectmp.common.error;


public class InvalidPacketException extends RuntimeException{

	public InvalidPacketException(String reason){
		super(reason);
	}
	
}
