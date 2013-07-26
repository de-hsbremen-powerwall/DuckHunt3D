package de.hsbremen.powerwall.duckhunt3d;

import java.util.ArrayList;
import java.util.List;

import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioNode;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix3f;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.system.AppSettings;
import com.jme3.ui.Picture;

import de.hsbremen.powerwall.duckhunt3d.objects.Duck;
import de.hsbremen.powerwall.duckhunt3d.objects.Player;
import de.hsbremen.powerwall.duckhunt3d.objects.PowerwallCamera;
import de.hsbremen.powerwall.duckhunt3d.objects.ShootingRange;

/**
 * 
 * @author Hendrik
 * 
 */
public class DuckHunt3D extends SimpleApplication {

	// graphicnodes
	private Node shootables;
	private Node hudNode;
	private Node cursorNode;
	private Node menuNode;

	// audionodes
	private AudioNode audioGun;
	private AudioNode audioReload;
	private AudioNode audioHit;
	private AudioNode audioInsertShell;

	// 3D camera set-up
	private PowerwallCamera pCam;

	// lists
	public static List<Duck> duckList = new ArrayList<Duck>();
	public static List<Player> playerList = new ArrayList<Player>();
	public static List<BitmapText> hudTexts = new ArrayList<BitmapText>();
	public static List<Node> bulletNodes = new ArrayList<Node>();

	// stuff
	private Vector3f origin;
	private float globalTimer;
	private float roundTime = 10;
	private float currentRoundTime = roundTime;
	float rotateX = 1.0f;

	private Boolean firstRound = true;
	Boolean draw = false;
	String drawString = ("Unentschieden zwischen ");

	BitmapText winnerText;

	Spatial target;

	private enum GameState {
		MENU, RUNNING, END
	};

	private GameState currentState = GameState.MENU;

	/**
	 * create new Instances of DuckHunt3D
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		DuckHunt3D app = new DuckHunt3D();

		// set resolution for 2x720p as we use in our set-up
		settings.setResolution(1280 * 2, 720);
		settings.setBitsPerPixel(32);

		// limit fps
		settings.setFrameRate(60);

		// TODO change set-up to "one-Desktop for fullscreen support
		settings.setFullscreen(false);

		// change title for the moment
		settings.setTitle("DUCKHUNT 3D");

		// apply settings
		app.setSettings(settings);
		app.setShowSettings(false);

		app.start();
	}

	/**
	 * initiates all the important stuff
	 */
	@Override
	public void simpleInitApp() {

		// remove debugscreen
		setDisplayStatView(false);
		setDisplayFps(false);

		// create nodes
		shootables = new Node("Shootables");
		hudNode = new Node("Hud Node");
		cursorNode = new Node("Cursor Node");
		menuNode = new Node("Menu Node");

		// add node for ducks to root
		rootNode.attachChild(shootables);

		// set up cameras
		pCam = new PowerwallCamera(this, settings);
		pCam.attachScene(rootNode);
		pCam.setLocation(new Vector3f(0f, 0f, 12f));
		renderManager.removeMainView(viewPort);

		// init audio and controls
		initKeys();
		initAudio();

		// create shooting range
		new ShootingRange(this.rootNode, this.assetManager);

		// manually add 4 Players for testing
		// TODO add for every wiiMote a Player
		playerList.add(new Player(1));
		playerList.add(new Player(2));
		playerList.add(new Player(3));
		playerList.add(new Player(4));

		// TODO add HUD to both viewports if possible
		drawCrosshair();
		drawHud();

		// draw Timer for HUD
		hudTexts.add(new BitmapText(guiFont, false));
		hudTexts.get(playerList.size()).setSize(
				guiFont.getCharSet().getRenderedSize());
		hudTexts.get(playerList.size()).setColor(ColorRGBA.White);
		hudTexts.get(playerList.size()).setLocalTranslation(
				settings.getWidth() / 4
						- hudTexts.get(playerList.size()).getLineWidth(),
				settings.getHeight() - 2, 0);
		hudNode.attachChild(hudTexts.get(playerList.size()));

		// draw Menu
		// Load target model
		target = assetManager
				.loadModel("de/hsbremen/powerwall/duckhunt3d/assets/models/target.mesh.xml");
		target.scale(0.01f, 0.01f, 0.01f);
		target.setLocalTranslation(8.0f, -1.0f, 1.0f);
		target.rotate(1.50f, -0.70f, 0.0f);
		menuNode.attachChild(target);
		
		Spatial duckhunt3d = assetManager
				.loadModel("de/hsbremen/powerwall/duckhunt3d/assets/models/duckhunt3d.mesh.xml");
		duckhunt3d.scale(0.02f, 0.02f, 0.02f);
		duckhunt3d.setLocalTranslation(-2.0f, 0.8f, 0.0f);
		duckhunt3d.rotate(0.0f, 0.0f, 0.0f);
		menuNode.attachChild(duckhunt3d);

		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(8.0f, -3.0f, -15.0f));
		menuNode.addLight(sun);

	}

	/**
	 * draws a HUD for each Player in playerList
	 */
	private void drawHud() {
		for (int i = 0; i < playerList.size(); i++) {
			// create background for score and bullets (black)
			Picture guiBackgroundPic = new Picture("guiBack");
			guiBackgroundPic.setImage(assetManager,
					"de/hsbremen/powerwall/duckhunt3d/assets/black.png", false);
			guiBackgroundPic.setWidth(150);
			guiBackgroundPic.setHeight(50);
			guiBackgroundPic.setPosition(30 + i * 350, 0);
			hudNode.attachChild(guiBackgroundPic);

			// add a bullet node
			bulletNodes.add(new Node("Bullets"));

			// add bullets to node
			for (int j = 0; j < playerList.get(i).getBullets(); j++) {
				Picture bulletPic = new Picture("Bullet");
				bulletPic.setImage(assetManager,
						"de/hsbremen/powerwall/duckhunt3d/assets/bulletP"
								+ (i + 1) + ".png", true);
				bulletPic.setWidth(10);
				bulletPic.setHeight(15);
				bulletNodes.get(i).attachChild(bulletPic);
				bulletPic.setPosition(i * 350 + 50 + j * 20, 30);

				// attach bulletNode to hudNode
				hudNode.attachChild(bulletNodes.get(i));
			}

			// add text for score
			hudTexts.add(new BitmapText(guiFont, false));
			hudTexts.get(i).setSize(guiFont.getCharSet().getRenderedSize());
			hudTexts.get(i).setColor(ColorRGBA.White);
			hudTexts.get(i).setLocalTranslation(50 + i * 350,
					hudTexts.get(i).getLineHeight(), 0);

			// attach hudText to hudNode
			hudNode.attachChild(hudTexts.get(i));

			// attach hudNode to guiNode
			guiNode.attachChild(hudNode);
		}
	}

	/**
	 * initiates all audio files
	 */
	private void initAudio() {
		// gunshot
		audioGun = new AudioNode(assetManager,
				"de/hsbremen/powerwall/duckhunt3d/assets/sound/shoot.wav",
				false);
		audioGun.setLooping(false);
		audioGun.setVolume(2);
		rootNode.attachChild(audioGun);

		// reload
		audioReload = new AudioNode(assetManager,
				"de/hsbremen/powerwall/duckhunt3d/assets/sound/reload.wav",
				false);
		audioReload.setLooping(false);
		audioReload.setVolume(2);
		rootNode.attachChild(audioReload);

		// insert Shell while reloading
		audioInsertShell = new AudioNode(
				assetManager,
				"de/hsbremen/powerwall/duckhunt3d/assets/sound/insert_shell.wav",
				false);
		audioInsertShell.setLooping(false);
		audioInsertShell.setVolume(2);
		rootNode.attachChild(audioInsertShell);

		// hit duck
		audioHit = new AudioNode(assetManager,
				"de/hsbremen/powerwall/duckhunt3d/assets/sound/hit.wav", false);
		audioHit.setLooping(false);
		audioHit.setVolume(2);
		rootNode.attachChild(audioHit);
	}

	/**
	 * initiates controls and mapping
	 */
	private void initKeys() {
		inputManager.addMapping("Shoot", new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));
		inputManager.addListener(actionListener, "Shoot");

		inputManager.addMapping("reload", new KeyTrigger(KeyInput.KEY_R));
		inputManager.addListener(actionListener, new String[] { "reload" });
	}

	/**
	 * listener for controls
	 */
	private ActionListener actionListener = new ActionListener() {

		public void onAction(String name, boolean keyPressed, float tpf) {

			// reload
			if (name.equals("reload") && !keyPressed) {
				if (playerList.get(0).getShells() != 6
						&& playerList.get(0).getBullets() == 0) {
					audioInsertShell.playInstance();
					playerList.get(0).addShell();
				}
				if (playerList.get(0).getShells() == 6) {
					audioReload.playInstance();
					playerList.get(0).reload();
				}
			}

			// shoot
			if (name.equals("Shoot") && keyPressed) {
				Vector2f mouseCoords = inputManager.getCursorPosition();
				Ray ray = new Ray(cam.getWorldCoordinates(mouseCoords, 0), cam
						.getWorldCoordinates(mouseCoords, 1)
						.subtractLocal(cam.getWorldCoordinates(mouseCoords, 0))
						.normalizeLocal());

				if (currentState.equals(GameState.MENU)) {
					CollisionResults results = new CollisionResults();
					menuNode.updateModelBound();
					menuNode.updateGeometricState();
					menuNode.collideWith(ray, results);

					CollisionResult collision = results.getClosestCollision();

					if (collision != null) {
						currentState = GameState.RUNNING;
					}
					audioGun.playInstance();

				} else if (currentState.equals(GameState.RUNNING)) {

					CollisionResults results = new CollisionResults();
					shootables.updateModelBound();
					shootables.updateGeometricState();
					shootables.collideWith(ray, results);

					CollisionResult collision = results.getClosestCollision();
					// Vector3f contact = collision.getContactPoint();

					if (collision != null)
						for (int i = 0; i < duckList.size(); i++) {
							if (playerList.get(0).getBullets() != 0) {
								if (duckList.get(i).getDuckGeo()
										.equals(collision.getGeometry())) {
									if (duckList.get(i).isAlive()) {
										duckList.get(i).die(shootables,
												duckList);
										playerList.get(0).setScore(
												duckList.get(i).getBounty());
										audioHit.playInstance();
									}
								}
							}
						}
					if (playerList.get(0).getBullets() != 0) {
						audioGun.playInstance();
						playerList.get(0).setBullets(1);
					}
				}
			}
		};
	};

	/**
	 * @return returns a random position for a duck
	 */
	private Vector3f newDuckPos() {
		float y = (float) MathRandom(-2, 2);
		float z = (float) MathRandom(-5, 5);

		float x = (float) MathRandom(-100, 100);

		// determine whether the duck is outside or inside the screen
		// TODO find a better solution this looks like crap
		if (x < 0) {
			if (z < 0)
				x = 20;
			if (z < -1)
				x = 21;
			if (z < -2)
				x = 22;
			if (z < -3)
				x = 23;
			if (z < -4)
				x = 24;
			if (z > 0)
				x = 20;
			if (z > 1)
				x = 19;
			if (z > 2)
				x = 18;
			if (z > 3)
				x = 17;
			if (z > 4)
				x = 16;
		} else if (x > 0) {
			if (z < 0)
				if (z < -1)
					x = -21;
			if (z < -2)
				x = -22;
			if (z < -3)
				x = -23;
			if (z < -4)
				x = -24;
			if (z > 0)
				x = -20;
			if (z > 1)
				x = -19;
			if (z > 2)
				x = -18;
			if (z > 3)
				x = -17;
			if (z > 4)
				x = -16;
		}

		return (new Vector3f(x, y, z));
	}

	/**
	 * returns a random value between two doubles you define
	 * 
	 * @param double low
	 * @param double high
	 * @return double random
	 */
	private double MathRandom(double low, double high) {
		double random;
		random = Math.random() * (high - low) + low;
		return random;
	}

	/**
	 * paints a crosshair at pointers position
	 */
	private void drawCrosshair() {
		Picture crosshair = new Picture("Crosshair");
		crosshair.setImage(assetManager,
				"de/hsbremen/powerwall/duckhunt3d/assets/fadenkreuz.png", true);
		crosshair.setWidth(50);
		crosshair.setHeight(50);
		crosshair.setPosition(inputManager.getCursorPosition().x,
				inputManager.getCursorPosition().y);

		cursorNode.attachChild(crosshair);
		guiNode.attachChild(cursorNode);
	}

	/**
	 * main update cycle
	 */
	@Override
	public void simpleUpdate(float tpf) {

		// show crosshair at current cursor position
		cursorNode.setLocalTranslation(inputManager.getCursorPosition().x,
				inputManager.getCursorPosition().y, 0);

		switch (currentState) {
		case MENU:
			rootNode.attachChild(menuNode);
			if (target.getLocalRotation().getX() <= 0.60f) {
				rotateX *= -1;
			} else if (target.getLocalRotation().getX() >= 0.7f) {
				rotateX *= -1;
			}

			target.rotate(rotateX / 300, 0.0f, 0.0f);

			break;
		case RUNNING:
			rootNode.detachChild(menuNode);

			if (!firstRound)
				guiNode.detachChild(winnerText);

			if (currentRoundTime > 0) {
				currentRoundTime -= (tpf);

				// TODO change this to wiimotePositions

				for (int i = 0; i < playerList.size(); i++) {
					// get current score of all players in playerList and update
					// score in gui
					hudTexts.get(i).setText(
							"Player "
									+ (i + 1)
									+ ": "
									+ String.valueOf(playerList.get(i)
											.getScore()));
					// get bullets of all players in playerList and update
					// bullets
					// in gui
					// first "remove" all
					for (int j = 0; j < 6; j++) {
						bulletNodes.get(i).getChild(j).setLocalScale(0);
					}
					// then show all that are currently available
					for (int j = 0; j < playerList.get(i).getBullets(); j++) {
						bulletNodes.get(i).getChild(j).setLocalScale(10, 15, 1);
					}
				}

				hudTexts.get(playerList.size()).setText(
						String.valueOf((int) currentRoundTime));

				// set a timer
				globalTimer += Math.round(tpf * 50);

				// cycle duck animation
				if (globalTimer % 10 == 0) {
					for (int i = 0; i < duckList.size(); i++) {
						duckList.get(i).cycle();
					}
				}

				// create a new duck
				if (globalTimer % 100 == 0) {
					@SuppressWarnings("unused")
					Duck duck = new Duck(shootables, duckList, assetManager,
							newDuckPos());
				}

				// update duck positions
				for (int i = 0; i < duckList.size(); i++) {
					duckList.get(i).setPos(
							new Vector3f(duckList.get(i).getSpeed()));
					if (duckList.get(i).getPos().x > 30
							|| duckList.get(i).getPos().x < -30
							|| duckList.get(i).getPos().y < -30
							|| duckList.get(i).getPos().y > 30) {
						shootables.detachChildNamed(duckList.get(i)
								.getDuckGeo().getName());
						duckList.remove(i);
					}
				}
				rootNode.updateGeometricState();

			}

			if (currentRoundTime <= 0) {
				currentState = GameState.END;
			}
			break;
		case END:
			Player winner = playerList.get(0);
			int highestScore = 0;

			for (int i = 0; i < playerList.size(); i++) {
				if (playerList.get(i).getScore() > highestScore) {
					winner = playerList.get(i);
					highestScore = playerList.get(i).getScore();
				} else if (playerList.get(i).getScore() == highestScore) {
					draw = true;
					drawString += "Spieler " + playerList.get(i).getName() + " ";
				}
				//reset Game
				playerList.get(i).reload();
				playerList.get(i).setScore(-playerList.get(i).getScore());
			}

			winnerText = new BitmapText(guiFont);
			winnerText.setSize(guiFont.getCharSet().getRenderedSize());
			winnerText.setColor(ColorRGBA.White);
			
			if(!draw)
			winnerText.setText("Spieler " + winner.getName() + " hat gewonnen!");
			if(draw)
			winnerText.setText(drawString);
			
			winnerText.setLocalTranslation(
					settings.getWidth() / 4 - winnerText.getLineWidth() / 2,
					settings.getHeight() / 2 - winnerText.getLineHeight(), 0);
			guiNode.attachChild(winnerText);

			currentRoundTime = roundTime;
			draw = false;
			currentState = GameState.MENU;

			firstRound = false;
			break;
		}
	}
}
