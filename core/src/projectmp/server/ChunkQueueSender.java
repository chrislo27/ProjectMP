package projectmp.server;

import projectmp.common.Main;
import projectmp.common.packet.PacketEndChunkTransfer;
import projectmp.common.packet.PacketSendChunk;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.util.TcpIdleSender;


public class ChunkQueueSender extends TcpIdleSender {

	Array<PacketSendChunk> chunks;
	Connection connection;
	private boolean finalChunkSent = false;
	
	public ChunkQueueSender(Array<PacketSendChunk> list, Connection c){
		chunks = list;
		connection = c;
	}
	
	
	@Override
	protected Object next() {
		if(chunks.size <= 0){
			if(!finalChunkSent){
				finalChunkSent = true;
				return new PacketEndChunkTransfer();
			}else{
				return null;
			}
		}
		return chunks.pop();
	}

}
