package projectmp.common.packet;

import projectmp.client.ClientLogic;
import projectmp.common.Main;
import projectmp.common.block.Blocks;
import projectmp.common.chunk.Chunk;
import projectmp.common.tileentity.TileEntity;
import projectmp.server.ServerLogic;

import com.esotericsoftware.kryonet.Connection;

public class PacketSendChunk implements Packet {

	public String[] blockKeys;
	
	public int[][] blocks = new int[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
	public int[][] meta = new int[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];
	public TileEntity[][] tileEntities = new TileEntity[Chunk.CHUNK_SIZE][Chunk.CHUNK_SIZE];

	public int originx = 0;
	public int originy = 0;

	@Override
	public void actionServer(Connection connection, ServerLogic logic) {
	}

	@Override
	public void actionClient(Connection connection, ClientLogic logic) {
		for (int x = 0; x < Chunk.CHUNK_SIZE; x++) {
			for (int y = 0; y < Chunk.CHUNK_SIZE; y++) {
				logic.world.setBlock(Blocks.instance().getBlock(blockKeys[blocks[x][y]]), originx + x, originy
						+ y);
				logic.world.setMeta(meta[x][y], originx + x, originy + y);
				logic.world.setTileEntity(tileEntities[x][y], x, y);
			}
		}

		Main.WORLDGETTING.addPercent();
	}

}
