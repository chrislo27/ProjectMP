package projectmp.common;

import projectmp.common.packet.Packet;
import projectmp.common.packet.Packet0Handshake;

import com.esotericsoftware.kryo.Kryo;


public class ClassRegistration {

	public static void registerClasses(Kryo kryo){
		kryo.register(Packet.class);
		kryo.register(Packet0Handshake.class);
	}
	
}
