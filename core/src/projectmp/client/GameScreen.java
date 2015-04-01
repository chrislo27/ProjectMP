package projectmp.client;

import projectmp.common.Main;
import projectmp.common.entity.Entity;
import projectmp.common.entity.EntityPlayer;
import projectmp.common.packet.Packet5PlayerPosUpdate;
import projectmp.common.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class GameScreen extends Updateable {

	public GameScreen(Main m) {
		super(m);

		renderer = new WorldRenderer(main, world);
	}
	
	public static long latency = System.currentTimeMillis();

	public World world;
	public WorldRenderer renderer;
	
	public EntityPlayer player;
	
	private Packet5PlayerPosUpdate playerUpdate = new Packet5PlayerPosUpdate();

	@Override
	public void render(float delta) {
		renderer.renderWorld();
		main.batch.setProjectionMatrix(main.camera.combined);
		renderer.renderHUD();

		main.batch.begin();
		main.font.draw(main.batch, "x, y: " + player.x + ", "
				+ player.y, 5, Main.convertY(100));
		main.batch.end();
	}

	@Override
	public void renderUpdate() {
		playerInput();
		
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.client.close();
			player = null;
			Main.logger.info("Connection closed");
			Main.ERRORMSG.setMessage("Disconnected from server: client closed connection");
			main.setScreen(Main.ERRORMSG);
		}

		renderer.camera.clamp();
		
		for(Entity e : world.entities){
			e.clientRenderUpdate();
		}
	}

	public void newWorld(World world) {
		this.world = world;
		renderer.world = world;
		renderer.camera.setWorld(world);
		
	}

	@Override
	public void tickUpdate() {
		if(player != null){
			if(main.client.isConnected()){
				player.tickUpdate();
				playerUpdate.username = Main.username;
				playerUpdate.x = player.x;
				playerUpdate.y = player.y;
				
				main.client.sendUDP(playerUpdate);
			}
		}
	}
	
	private void playerInput(){
		if(player == null || !main.client.isConnected()) return;
		
		if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			player.moveLeft();
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			player.moveRight();
		}
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			player.moveUp();
		}
		if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			player.moveDown();
		}
	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {

	}

}
