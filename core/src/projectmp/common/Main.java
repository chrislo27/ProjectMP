package projectmp.common;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import projectmp.client.ClientListener;
import projectmp.client.animation.Animation;
import projectmp.client.animation.LoopingAnimation;
import projectmp.client.transition.Transition;
import projectmp.client.transition.TransitionScreen;
import projectmp.common.util.AssetMap;
import projectmp.common.util.CaptureStream;
import projectmp.common.util.CaptureStream.Consumer;
import projectmp.common.util.Difficulty;
import projectmp.common.util.GameException;
import projectmp.common.util.Logger;
import projectmp.common.util.MathHelper;
import projectmp.common.util.MemoryUtils;
import projectmp.common.util.ScreenshotFactory;
import projectmp.common.util.Splashes;
import projectmp.common.util.Utils;
import projectmp.common.util.render.Gears;
import projectmp.common.util.render.Shaders;
import projectmp.common.util.version.VersionGetter;
import projectmp.server.ServerListener;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

/**
 * 
 * Main class, think of it like slick's Main class
 *
 */
public class Main extends Game implements Consumer {

	public OrthographicCamera camera;

	public SpriteBatch batch;
	public SpriteBatch maskRenderer;
	public SpriteBatch blueprintrenderer;

	public ShapeRenderer shapes;

	public FrameBuffer buffer;
	public FrameBuffer buffer2;

	public BitmapFont font;
	public BitmapFont arial;

	private static Color rainbow = new Color();
	private static Color inverseRainbow = new Color();

	public static final String version = "v0.1.0-alpha";
	public static String githubVersion = null;

	public AssetManager manager;

	public static AssetLoadingScreen ASSETLOADING = null;
	public static MainMenuScreen MAINMENU = null;
	public static TransitionScreen TRANSITION = null;
	public static MiscLoadingScreen MISCLOADING = null;
	public static SettingsScreen SETTINGS = null;

	public static Texture filltex;

	public ShaderProgram maskshader;
	public ShaderProgram blueprintshader;
	public ShaderProgram toonshader;
	public ShaderProgram greyshader;
	public ShaderProgram warpshader;
	public ShaderProgram blurshader;
	public ShaderProgram defaultShader;
	public ShaderProgram invertshader;
	public ShaderProgram swizzleshader;

	public HashMap<String, Animation> animations = new HashMap<String, Animation>();
	public HashMap<String, Texture> textures = new HashMap<String, Texture>();

	private CaptureStream output;
	private PrintStream printstrm;
	private JFrame consolewindow;
	private JTextArea consoletext;
	private JScrollPane conscrollPane;

	public Client client;
	public Server server;
	
	/**
	 * used for storing progress, level data etc
	 */
	public Preferences progress;

	public static final int TICKS = 20;
	public static final int MAX_FPS = 60;
	private int[] lastFPS = new int[5];
	private float deltaUntilTick = 0;
	private float totalSeconds = 0f;

	public static Gears gears;

	/**
	 * use this rather than Gdx.app.log
	 */
	public static Logger logger;

	public Main(Logger l) {
		super();
		logger = l;
	}

	@Override
	public void create() {
		defaultShader = SpriteBatch.createDefaultShader();
		progress = getPref("progress");
		Gdx.graphics.setTitle(getTitle() + " - " + Splashes.getRandomSplash());
		redirectSysOut();

		for (int i = 0; i < lastFPS.length; i++) {
			lastFPS[i] = 0;
		}

		ShaderProgram.pedantic = false;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT);
		batch = new SpriteBatch();
		batch.enableBlending();
		maskRenderer = new SpriteBatch();
		maskRenderer.enableBlending();
		blueprintrenderer = new SpriteBatch();
		manager = new AssetManager();
		font = new BitmapFont(Gdx.files.internal("fonts/couriernewbold.fnt"),
				Gdx.files.internal("fonts/couriernewbold.png"), false);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.setMarkupEnabled(true);

		arial = new BitmapFont();
		arial.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Pixmap pix = new Pixmap(1, 1, Format.RGBA8888);
		pix.setColor(Color.WHITE);
		pix.fill();
		filltex = new Texture(pix);
		pix.dispose();

		shapes = new ShapeRenderer();

		buffer = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT,
				true);
		buffer2 = new FrameBuffer(Format.RGBA8888, Settings.DEFAULT_WIDTH, Settings.DEFAULT_HEIGHT,
				true);
		
		client = new Client();
		client.addListener(new ClientListener());
		ClassRegistration.registerClasses(client.getKryo());
		client.start();
		server = new Server();
		ClassRegistration.registerClasses(server.getKryo());
		server.addListener(new ServerListener());
		server.start();

		maskshader = new ShaderProgram(Shaders.VERTDEFAULT, Shaders.FRAGBAKE);
		maskshader.begin();
		maskshader.setUniformi("u_texture1", 1);
		maskshader.setUniformi("u_mask", 2);
		maskshader.end();
		maskRenderer.setShader(maskshader);

		blueprintshader = new ShaderProgram(Shaders.VERTBLUEPRINT, Shaders.FRAGBLUEPRINT);
		blueprintshader.begin();
		blueprintshader.end();
		blueprintrenderer.setShader(blueprintshader);

		toonshader = new ShaderProgram(Shaders.VERTTOON, Shaders.FRAGTOON);
		greyshader = new ShaderProgram(Shaders.VERTGREY, Shaders.FRAGGREY);

		warpshader = new ShaderProgram(Shaders.VERTDEFAULT, Shaders.FRAGWARP);
		warpshader.begin();
		warpshader.setUniformf(warpshader.getUniformLocation("time"), totalSeconds);
		warpshader.setUniformf(warpshader.getUniformLocation("amplitude"), 1.0f, 1.0f);
		warpshader.setUniformf(warpshader.getUniformLocation("frequency"), 1.0f, 1.0f);
		warpshader.setUniformf(warpshader.getUniformLocation("speed"), 1f);
		warpshader.end();

		blurshader = new ShaderProgram(Shaders.VERTBLUR, Shaders.FRAGBLUR);
		blurshader.begin();
		blurshader.setUniformf("dir", 1f, 0f);
		blurshader.setUniformf("resolution", Gdx.graphics.getWidth());
		blurshader.setUniformf("radius", 2f);
		blurshader.end();

		invertshader = new ShaderProgram(Shaders.VERTINVERT, Shaders.FRAGINVERT);
		swizzleshader = new ShaderProgram(Shaders.VERTSWIZZLE, Shaders.FRAGSWIZZLE);

		loadUnmanagedAssets();
		loadAssets();

		Gdx.input.setInputProcessor(getDefaultInput());

		prepareStates();

		this.setScreen(ASSETLOADING);

		Gdx.app.postRunnable(new Thread("version checker") {

			public void run() {
				VersionGetter.instance().getVersionFromServer();
			}
		});
	}

	public void prepareStates() {
		ASSETLOADING = new AssetLoadingScreen(this);
		MAINMENU = new MainMenuScreen(this);
		TRANSITION = new TransitionScreen(this);
		MISCLOADING = new MiscLoadingScreen(this);
		SETTINGS = new SettingsScreen(this);
	}

	@Override
	public void dispose() {
		Settings.instance().save();

		batch.dispose();
		manager.dispose();
		font.dispose();
		arial.dispose();
		maskshader.dispose();
		blueprintshader.dispose();
		toonshader.dispose();
		warpshader.dispose();
		maskRenderer.dispose();
		blurshader.dispose();
		blueprintrenderer.dispose();
		invertshader.dispose();
		swizzleshader.dispose();
		shapes.dispose();

		buffer.dispose();
		buffer2.dispose();

		Iterator it = animations.entrySet().iterator();
		while (it.hasNext()) {
			((LoopingAnimation) ((Entry) it.next()).getValue()).dispose();
		}

		it = textures.entrySet().iterator();
		while (it.hasNext()) {
			((Texture) ((Entry) it.next()).getValue()).dispose();
		}

		// dispose screens
		ASSETLOADING.dispose();
		MAINMENU.dispose();
		TRANSITION.dispose();
		MISCLOADING.dispose();
		SETTINGS.dispose();
	}

	private void preRender() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearDepthf(1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);
		gears.update(1);
	}

	@Override
	public void render() {
		deltaUntilTick += Gdx.graphics.getRawDeltaTime();

		try {
			while (deltaUntilTick >= (1.0f / TICKS)) {
				if (getScreen() != null) ((Updateable) getScreen()).tickUpdate();
				tickUpdate();
				deltaUntilTick -= (1.0f / TICKS);
			}
			if (getScreen() != null) {
				((Updateable) getScreen()).renderUpdate();
			}

			preRender();
			super.render();
			postRender();

		} catch (Exception e) {
			e.printStackTrace();

			Gdx.files.local("crash/").file().mkdir();
			String date = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
			date.trim();
			FileHandle handle = Gdx.files.local("crash/crash-log_" + date + ".txt");
			handle.writeString(output.toString(), false);

			consoletext.setText(output.toString());
			resetSystemOut();
			System.out.println("\n\nThe game crashed. There is an error log at " + handle.path()
					+ " ; please send it to the game developer!\n");
			try {
				Thread.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();

			Gdx.app.exit();
			System.exit(1);
		}

	}

	private void postRender() {
		batch.begin();

		font.setColor(Color.WHITE);

		if (Settings.showFPS || Settings.debug) {
			font.draw(batch, "FPS: "
					+ (Gdx.graphics.getFramesPerSecond() <= (MAX_FPS / 4f) ? "[RED]"
							: (Gdx.graphics.getFramesPerSecond() <= (MAX_FPS / 2f) ? "[YELLOW]"
									: "")) + Gdx.graphics.getFramesPerSecond() + "[]", 5,
					Settings.DEFAULT_HEIGHT - 5);
		}
		if (Settings.debug) {
			font.setMarkupEnabled(false);
			font.draw(
					batch,
					"(avg of " + lastFPS.length + " sec: " + String.format("%.1f", getAvgFPS())
							+ ") " + Arrays.toString(lastFPS) + ", delta "
							+ Gdx.graphics.getDeltaTime(),
					5 + font.getSpaceWidth()
							+ (font.getBounds("FPS: " + Gdx.graphics.getFramesPerSecond()).width),
					Settings.DEFAULT_HEIGHT - 5);
			font.setMarkupEnabled(true);
		}

		if (this.getScreen() != null) {
			if (Settings.debug) ((Updateable) this.getScreen()).renderDebug(this.renderDebug());
		}
		batch.end();

		fpstimer += Gdx.graphics.getDeltaTime();
		if (fpstimer >= 1) {
			fpstimer--;
			int[] temp = lastFPS.clone();
			for (int i = 1; i < lastFPS.length; i++) {
				lastFPS[i] = temp[i - 1];
			}
			lastFPS[0] = Gdx.graphics.getFramesPerSecond();
		}

		totalSeconds += Gdx.graphics.getDeltaTime();

		warpshader.begin();
		warpshader.setUniformf(warpshader.getUniformLocation("time"), totalSeconds);
		warpshader.setUniformf(warpshader.getUniformLocation("amplitude"), 0.25f, 0.1f);
		warpshader.setUniformf(warpshader.getUniformLocation("frequency"), 10f, 5f);
		warpshader.setUniformf(warpshader.getUniformLocation("speed"), 2.5f);
		warpshader.end();

		inputUpdate();
	}

	private int renderDebug() {
		int offset = 0;
		if (getScreen() != null) offset = ((Updateable) getScreen()).getDebugOffset();
		if (MemoryUtils.getUsedMemory() > getMostMemory) getMostMemory = MemoryUtils
				.getUsedMemory();
		font.setColor(Color.WHITE);
		font.draw(batch, "version: " + Main.version
				+ (githubVersion.equals("") ? "" : "; github: " + Main.githubVersion), 5,
				Main.convertY(30 + offset));
		font.draw(batch, "Memory: "
				+ NumberFormat.getInstance().format(MemoryUtils.getUsedMemory()) + " KB / "
				+ NumberFormat.getInstance().format(MemoryUtils.getMaxMemory()) + " KB (max "
				+ NumberFormat.getInstance().format(getMostMemory) + " KB) ", 5,
				Main.convertY(45 + offset));
		font.draw(batch, "Available cores: " + MemoryUtils.getCores(), 5,
				Main.convertY(60 + offset));
		font.draw(batch, "OS: " + System.getProperty("os.name"), 5, Main.convertY(75 + offset));
		if (getScreen() != null) {
			font.draw(batch, "state: " + getScreen().getClass().getSimpleName(), 5,
					Main.convertY(90 + offset));
		} else {
			font.draw(batch, "state: null", 5, Main.convertY(90 + offset));
		}

		return 75 + 30 + offset;
	}

	public void inputUpdate() {
		if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			Settings.debug = !Settings.debug;
		} else if (Gdx.input.isKeyJustPressed(Keys.F5)) {
			ScreenshotFactory.saveScreenshot();
		}
		if (Settings.debug) { // console things -> alt + key
			if (((Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)))) {
				if (Gdx.input.isKeyJustPressed(Keys.C)) {
					if (consolewindow.isVisible()) {
						consolewindow.setVisible(false);
					} else {
						consolewindow.setVisible(true);
						conscrollPane.getVerticalScrollBar().setValue(
								conscrollPane.getVerticalScrollBar().getMaximum());
					}
				} else if (Gdx.input.isKeyJustPressed(Keys.Q)) {
					throw new GameException(
							"This is a forced crash caused by pressing ALT+Q while in debug mode.");
				} else if (Gdx.input.isKeyJustPressed(Keys.G)) {
					gears.reset();
				}

			}
		}
	}

	public void tickUpdate() {
		
	}

	private void loadAssets() {
		AssetMap.instance(); // load asset map namer thing
		Translator.instance();
		addColors();

		// missing
		manager.load(AssetMap.add("blockmissingtexture", "images/blocks/missing/missing.png"),
				Texture.class);
		manager.load(AssetMap.add("missingtexture", "images/missing.png"), Texture.class);

		// ui
		manager.load(AssetMap.add("spacekraken", "images/ui/misc.png"), Texture.class);
		manager.load(AssetMap.add("achievementui", "images/ui/achievement.png"), Texture.class);
		manager.load(AssetMap.add("guilanguage", "images/ui/button/language.png"), Texture.class);
		manager.load(AssetMap.add("guisettings", "images/ui/button/settings.png"), Texture.class);
		manager.load(AssetMap.add("guibg", "images/ui/button/bg.png"), Texture.class);
		manager.load(AssetMap.add("guislider", "images/ui/button/slider.png"), Texture.class);
		manager.load(AssetMap.add("guisliderarrow", "images/ui/button/sliderarrow.png"),
				Texture.class);
		manager.load(AssetMap.add("guiexit", "images/ui/button/exit.png"), Texture.class);
		manager.load(AssetMap.add("guiback", "images/ui/button/backbutton.png"), Texture.class);
		manager.load(AssetMap.add("guibgfalse", "images/ui/button/bgfalse.png"), Texture.class);
		manager.load(AssetMap.add("guibgtrue", "images/ui/button/bgtrue.png"), Texture.class);
		manager.load(AssetMap.add("detectionarrow", "images/ui/detection.png"), Texture.class);
		manager.load(AssetMap.add("guilevelselect", "images/ui/button/levelselect.png"),
				Texture.class);
		manager.load(AssetMap.add("guinextlevel", "images/ui/button/nextlevel.png"), Texture.class);
		manager.load(AssetMap.add("guiretry", "images/ui/button/retry.png"), Texture.class);
		manager.load("images/ui/damage/yourMother.png", Texture.class);
		manager.load("images/ui/damage/spikes.png", Texture.class);
		manager.load("images/ui/damage/generic.png", Texture.class);
		manager.load("images/ui/damage/fire.png", Texture.class);
		manager.load("images/ui/damage/electric.png", Texture.class);
		manager.load("images/ui/damage/theVoid.png", Texture.class);
		manager.load(AssetMap.add("playerdirectionarrow", "images/ui/player-arrow.png"),
				Texture.class);
		manager.load(AssetMap.add("exityes", "images/ui/exitsign/exityes.png"), Texture.class);
		manager.load(AssetMap.add("exitno", "images/ui/exitsign/exitno.png"), Texture.class);
		manager.load(AssetMap.add("objective-failed", "images/ui/objective/failed.png"), Texture.class);
		manager.load(AssetMap.add("objective-complete", "images/ui/objective/complete.png"), Texture.class);
		manager.load(AssetMap.add("objective-incomplete", "images/ui/objective/incomplete.png"), Texture.class);
		
		// particle
		manager.load(AssetMap.add("money", "images/particle/money.png"), Texture.class);
		manager.load(AssetMap.add("checkpoint", "images/particle/checkpoint.png"), Texture.class);
		manager.load(AssetMap.add("poof", "images/particle/poof.png"), Texture.class);
		manager.load(AssetMap.add("sparkle", "images/particle/sparkle.png"), Texture.class);
		manager.load(AssetMap.add("arrowup", "images/particle/arrow/up.png"), Texture.class);
		manager.load(AssetMap.add("arrowdown", "images/particle/arrow/down.png"), Texture.class);
		manager.load(AssetMap.add("arrowleft", "images/particle/arrow/left.png"), Texture.class);
		manager.load(AssetMap.add("arrowright", "images/particle/arrow/right.png"), Texture.class);
		manager.load(AssetMap.add("arrowcentre", "images/particle/arrow/centre.png"), Texture.class);
		manager.load(AssetMap.add("lasercube", "images/particle/lasercube.png"), Texture.class);
		manager.load(AssetMap.add("particlepower", "images/particle/power.png"), Texture.class);
		manager.load(AssetMap.add("magnetglow", "images/particle/magnetglow.png"), Texture.class);
		manager.load(AssetMap.add("airwhoosh", "images/particle/airwhoosh.png"), Texture.class);
		manager.load(AssetMap.add("particlecircle", "images/particle/circle.png"), Texture.class);
		manager.load(AssetMap.add("particlestar", "images/particle/star.png"), Texture.class);
		manager.load(AssetMap.add("particleshockwave", "images/particle/explosionshockwave.png"),
				Texture.class);
		manager.load(AssetMap.add("particleflash0", "images/particle/flash0.png"), Texture.class);
		manager.load(AssetMap.add("particleflash1", "images/particle/flash1.png"), Texture.class);
		manager.load(AssetMap.add("particleflash2", "images/particle/flash2.png"), Texture.class);
		manager.load(AssetMap.add("particleflash3", "images/particle/flash3.png"), Texture.class);
		manager.load(AssetMap.add("particleflame0", "images/particle/expflame0.png"), Texture.class);
		manager.load(AssetMap.add("particleflame1", "images/particle/expflame1.png"), Texture.class);
		manager.load(AssetMap.add("particleflame2", "images/particle/expflame2.png"), Texture.class);
		manager.load(AssetMap.add("particleflame3", "images/particle/expflame3.png"), Texture.class);
		manager.load(AssetMap.add("teleporterring", "images/particle/teleporterring.png"), Texture.class);

		// effects
		manager.load(AssetMap.add("effecticonblindness", "images/ui/effect/icon/blindness.png"),
				Texture.class);
		manager.load(AssetMap.add("effectoverlayblindness", "images/ui/effect/blindness.png"),
				Texture.class);

		// entities
		manager.load(AssetMap.add("player", "images/entity/player/player.png"), Texture.class);
		manager.load(AssetMap.add("playerGears", "images/entity/player/Copy of player.png"),
				Texture.class);
		manager.load(AssetMap.add("smallasteroid", "images/entity/smallasteroid.png"),
				Texture.class);
		manager.load(AssetMap.add("entityzaborinox", "images/entity/zaborinox.png"), Texture.class);
		manager.load(AssetMap.add("entitywhale", "images/entity/whale.png"), Texture.class);
		manager.load(AssetMap.add("entityball", "images/entity/ball.png"), Texture.class);

		// misc
		manager.load(AssetMap.add("vignette", "images/ui/vignette.png"), Texture.class);
		manager.load(AssetMap.add("entityshield", "images/entity/shield.png"), Texture.class);
		manager.load(AssetMap.add("levelselectbg", "images/levelselect/levelselectbg.png"),
				Texture.class);
		manager.load(AssetMap.add("levelselectdot", "images/levelselect/levelselectdot.png"),
				Texture.class);
		manager.load(AssetMap.add("levelselectdotgears", "images/levelselect/levelselectdotgears.png"),
				Texture.class);
		manager.load(AssetMap.add("levelselected", "images/levelselect/levelselected.png"),
				Texture.class);
		manager.load(AssetMap.add("glintsquare", "images/item/glintsquare.png"), Texture.class);
		manager.load(AssetMap.add("voidend", "images/voidend.png"), Texture.class);
		manager.load(AssetMap.add("circlegradient", "images/circlegradient.png"), Texture.class);
		manager.load(AssetMap.add("stunhalo", "images/stunhalo.png"), Texture.class);

		// level backgrounds
		manager.load(AssetMap.add("levelbgcity", "images/levelbg/city.png"), Texture.class);
		manager.load(AssetMap.add("levelbgcircuit", "images/levelbg/circuit.png"), Texture.class);
		

		// sfx
		manager.load(AssetMap.add("questcomplete", "sounds/questcomplete.ogg"), Sound.class);
		manager.load(AssetMap.add("switchsfx", "sounds/switch.ogg"), Sound.class);
		manager.load(AssetMap.add("voidambient", "sounds/ambient/void.ogg"), Sound.class);

		// music
		
	}

	private void loadUnmanagedAssets() {
		long timeTaken = System.currentTimeMillis();
		// misc

		// unmanaged textures
		textures.put("gear", new Texture("images/gear.png"));
		gears = new Gears(this);
		textures.put("switch", new Texture("images/blocks/switch/switch.png"));
		textures.put("switch_bg", new Texture("images/blocks/switch/switch_bg.png"));
		textures.put("timer", new Texture("images/blocks/timer/timer.png"));
		textures.put("timer_colour", new Texture("images/blocks/timer/timer_colour.png"));
		textures.put("toggle", new Texture("images/blocks/toggle/toggle.png"));
		textures.put("toggle_warning", new Texture("images/blocks/toggle/toggle_warning.png"));

		// animations
		animations.put("shine",
				new LoopingAnimation(0.1f, 20, "images/item/shine/shine.png", false));
		animations.put("portal", new LoopingAnimation(0.05f, 32, "images/blocks/portal/portal.png",
				true).setRegionTile(64, 64));
		animations.put("fire", new LoopingAnimation(0.05f, 30, "images/blocks/fire/fire.png", true)
				.setRegionTile(128, 128).setVertical(false));
		animations.put("fire-hud", new LoopingAnimation(0.05f, 8, "images/ui/fire-hudnomiddle.png",
				true).setRegionTile(864, 468).setVertical(false));
		animations.put("jumppad", new LoopingAnimation(0.25f, 4,
				"images/blocks/jumppad/jumppad.png", true).setRegionTile(64, 64));
		animations.put("accelerationpad", new LoopingAnimation(0.5f, 2,
				"images/blocks/accpad/accelerationpad.png", true).setRegionTile(64, 64)
				.setVertical(false));

		// load animations
		Iterator it = animations.entrySet().iterator();
		while (it.hasNext()) {
			((LoopingAnimation) ((Entry) it.next()).getValue()).load();
		}

		Main.logger.info("Finished loading all unmanaged assets and animations, took "
				+ (System.currentTimeMillis() - timeTaken) + " ms");
	}

	private void addColors() {
		Colors.put("VOID_PURPLE", new Color(123f / 255f, 0, 1, 1));

		// text related
		Colors.put("DANGER", new Color(1, 0, 0, 1)); // red
		Colors.put("OBJECT", new Color(1, 217 / 255f, 0, 1)); // yellow
		Colors.put("KEY", new Color(0, 204 / 255f, 0, 1)); // green
	}

	private static Vector2 unprojector = new Vector2(0, 0);

	public static int getInputX() {
		return (int) ((Gdx.input.getX()) * getScaleFactorX());
		// return ((int) (viewport.unproject(unprojector.set(Gdx.input.getX(),
		// Gdx.input.getY())).x));
	}

	public static int getInputY() {
		return (int) (Gdx.input.getY() * getScaleFactorY());
		// return viewport.getScreenHeight()
		// - ((int) (viewport.unproject(unprojector.set(Gdx.input.getX(),
		// Gdx.input.getY())).y));
	}

	public static float getScaleFactorX() {
		return (Gdx.graphics.getWidth() / Settings.DEFAULT_WIDTH);
	}

	public static float getScaleFactorY() {
		return ((Gdx.graphics.getHeight() * 1f) / Settings.DEFAULT_HEIGHT);
	}

	public Texture getCurrentShine() {
		return animations.get("shine").getCurrentFrame().getTexture();
	}

	public static String getTitle() {
		return (Translator.getMsg("gamename") + " " + Main.version);
	}

	@Override
	public void resize(int x, int y) {
//		viewport.update(x, y, false);
//		camera.setToOrtho(false, x, y);
	}

	public void redirectSysOut() {
		PrintStream ps = System.out;
		output = new CaptureStream(this, ps);
		printstrm = new PrintStream(output);
		resetConsole();
		System.setOut(printstrm);
	}

	public void resetConsole() {
		consolewindow = new JFrame();
		consolewindow.setTitle("Console for " + Translator.getMsg("gamename") + " " + Main.version);
		consolewindow.setVisible(false);
		consoletext = new JTextArea(40, 60);
		consoletext.setEditable(false);
		conscrollPane = new JScrollPane(consoletext);
		consolewindow.add(conscrollPane, null);
		consolewindow.pack();
	}

	public void resetSystemOut() {
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}

	@Override
	public void appendText(final String text) {
		consoletext.append(text);
		consoletext.setCaretPosition(consoletext.getText().length());
	}

	public void transition(Transition from, Transition to, Screen next) {
		TRANSITION.prepare(this.getScreen(), from, to, next);
		setScreen(TRANSITION);
	}

	public static Color getRainbow() {
		return getRainbow(1, 1);
	}

	public static Color getRainbow(float s) {
		return getRainbow(s, 1);
	}

	public static Color getRainbow(float s, float saturation) {
		return rainbow.set(
				Utils.HSBtoRGBA8888(
						(s < 0 ? 1.0f : 0)
								- MathHelper.getNumberFromTime(System.currentTimeMillis(),
										Math.abs(s)), saturation, 0.75f)).clamp();
	}

	public InputMultiplexer getDefaultInput() {
		InputMultiplexer plexer = new InputMultiplexer();
		plexer.addProcessor(new MainInputProcessor(this));
		return plexer;
	}

	private static Random random = new Random();

	public static Random getRandom() {
		return random;
	}

	public void fillRect(float x, float y, float width, float height) {
		batch.draw(filltex, x, y, width, height);
	}

	/**
	 * Directions to use
	 * 
	 * 1) Draw original texture (the base tex) on main batch - this renders the
	 * actual sprite
	 * 
	 * 2) end main batch, start itemRenderer
	 * 
	 * 3) call this method (base tex, mask) - prepares mask
	 * 
	 * 4) re-draw original texture (base tex) - draws the stencil (which uses
	 * base tex)
	 * 
	 * 5) end itemRenderer, begin main batch
	 * 
	 * 
	 * 
	 * @param mask
	 *            mask itself (generally base tex as well)
	 * @param tostencil
	 *            texture to cake on
	 */
	public static void useMask(Texture mask, Texture tostencil) {
		// bind mask to glActiveTexture(GL_TEXTURE2)
		mask.bind(2);

		// bind sprite to glActiveTexture(GL_TEXTURE1)
		tostencil.bind(1);

		// now we need to reset glActiveTexture to zero!!!! since sprite batch
		// does not do this for us
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}

	/**
	 * converts y-down to y-up
	 * 
	 * @param f
	 *            the num. of px down from the top of the screen
	 * @return the y-down conversion of input
	 */
	public static int convertY(float f) {
		return Math.round(Settings.DEFAULT_HEIGHT - f);
	}

	public void drawInverse(String s, float x, float y) {
		font.draw(batch, s, x - font.getBounds(s).width, y);
	}
	public void drawCentered(String s, float x, float y) {
		font.draw(batch, s, x - (font.getBounds(s).width / 2), y);
	}

	public void drawTextBg(String text, float x, float y) {
		batch.setColor(0, 0, 0, batch.getColor().a * 0.6f);
		fillRect(x, y, font.getBounds(text).width + 2, 17);
		font.draw(batch, text, x + 1, y + 15);
		batch.setColor(1, 1, 1, 1);
	}

	public void drawScaled(String text, float x, float y, float width, float padding) {
		if (font.getBounds(text).width + (padding * 2) > width) {
			font.setScale(width / (font.getBounds(text).width + (padding * 2)));
		}
		drawCentered(text, x, y);
		font.setScale(1);
	}

	private int totalavgFPS = 0;
	private float fpstimer = 0;

	public float getAvgFPS() {
		totalavgFPS = 0;
		for (int i = 0; i < lastFPS.length; i++) {
			totalavgFPS += lastFPS[i];
		}

		return ((totalavgFPS) / (lastFPS.length * 1f));
	}

	public int getMostMemory = MemoryUtils.getUsedMemory();

	public int getDifficulty() {
		return progress.getInteger("difficulty", Difficulty.NORMAL_ID);
	}

	/**
	 * basically appends "stray-" to the beginning of your preference
	 * 
	 * Preferences used: settings, achievements, progress
	 * 
	 * @param ref
	 * @return preferences
	 */
	public static Preferences getPref(String ref) {
		return Gdx.app.getPreferences("stray-" + ref);
	}

	public void setClearColor(int r, int g, int b) {
		Gdx.gl20.glClearColor(r / 255f, g / 255f, b / 255f, 1f);
	}

}
