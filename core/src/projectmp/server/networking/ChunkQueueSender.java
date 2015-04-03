package projectmp.server.networking;

import projectmp.common.Main;
import projectmp.common.packet.Packet10EndChunkTransfer;
import projectmp.common.packet.Packet1Chunk;

import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.util.TcpIdleSender;


public class ChunkQueueSender extends TcpIdleSender {

	Array<Packet1Chunk> chunks;
	Connection connection;
	private boolean finalChunkSent = false;
	
	public ChunkQueueSender(Array<Packet1Chunk> list, Connection c){
		chunks = list;
		connection = c;
	}
	
	
	@Override
	protected Object next() {
		if(chunks.size <= 0){
			if(!finalChunkSent){
				finalChunkSent = true;
				return new Packet10EndChunkTransfer();
			}else{
				return null;
			}
		}
		return chunks.pop();
	}

}
