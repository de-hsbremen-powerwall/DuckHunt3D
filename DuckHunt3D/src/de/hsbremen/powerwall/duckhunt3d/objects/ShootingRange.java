package de.hsbremen.powerwall.duckhunt3d.objects;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;

/**
 * 
 * @author Hendrik
 * 
 */
public class ShootingRange {

	// Geometry
	private Geometry grassLightGeo;
	private Geometry grassDarkGeo;
	private Geometry bigTreeGeo;
	private Geometry smallTreeGeo;
	private Geometry floorGeo;

	// Materials
	private Material grassDarkMat;
	private Material grassLightMat;
	private Material bigTreeMat;
	private Material smallTreeMat;
	private Material floorMat;

	// Quads
	private Quad quadMeshGrassLight;
	private Quad quadMeshGrassDark;
	private Quad quadMeshBigTree;
	private Quad quadMeshSmallTree;
	private Quad quadMeshFloor;

	// Lightning
	private DirectionalLight dl;

	/**
	 * creates a shooting range
	 * 
	 * @param rootNode
	 *            rootNode for adding graphics
	 * @param assetManager
	 *            assetManager for loading Materials
	 */
	public ShootingRange(Node rootNode, AssetManager assetManager) {
		dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(Vector3f.UNIT_XYZ.negate());

		// Setting up stage

		// Earth(same layer as Grass Light)
		quadMeshFloor = new Quad(2560 / 136, 136 / 136);
		floorGeo = new Geometry("Quad", quadMeshFloor);

		floorMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		floorMat.setTexture(
				"ColorMap",
				assetManager
						.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/earth.png"));
		floorMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		floorGeo.setQueueBucket(Bucket.Transparent);
		floorGeo.setMaterial(floorMat);
		floorGeo.setLocalTranslation(new Vector3f(-9f, -2.8f, 6.501f));

		// Grass Light(Foreground)
		quadMeshGrassLight = new Quad(2560 / 136, 136 / 136);
		grassLightGeo = new Geometry("Quad", quadMeshGrassLight);

		grassLightMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		grassLightMat
				.setTexture(
						"ColorMap",
						assetManager
								.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/grassLight.png"));
		grassLightMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		grassLightGeo.setQueueBucket(Bucket.Transparent);
		grassLightGeo.setMaterial(grassLightMat);
		grassLightGeo.setLocalTranslation(new Vector3f(-9f, -2.1f, 6.5f));

		// Grass Dark (Background)
		quadMeshGrassDark = new Quad(2560 / 136, 136 / 136);
		grassDarkGeo = new Geometry("Quad", quadMeshGrassDark);

		grassDarkMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		grassDarkMat
				.setTexture(
						"ColorMap",
						assetManager
								.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/grassDark.png"));
		grassDarkMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		grassDarkGeo.setQueueBucket(Bucket.Transparent);
		grassDarkGeo.setMaterial(grassDarkMat);
		grassDarkGeo.setLocalTranslation(new Vector3f(-9f, -2.1f, 6.2f));

		// Big Tree (Behind Dark Grass)
		quadMeshBigTree = new Quad(264 / 50, 471 / 90);
		bigTreeGeo = new Geometry("Quad", quadMeshBigTree);

		bigTreeMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		bigTreeMat
				.setTexture(
						"ColorMap",
						assetManager
								.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/treeBig.png"));
		bigTreeMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		bigTreeGeo.setQueueBucket(Bucket.Transparent);
		bigTreeGeo.setMaterial(bigTreeMat);
		bigTreeGeo.setLocalTranslation(new Vector3f(-10f, -2.15f, 5.0f));

		// Small Tree (Behind Dark Grass)
		quadMeshSmallTree = new Quad(122 / 40, 124 / 60);
		smallTreeGeo = new Geometry("Quad", quadMeshSmallTree);

		smallTreeMat = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		smallTreeMat
				.setTexture(
						"ColorMap",
						assetManager
								.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/treeSmall.png"));
		smallTreeMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		smallTreeGeo.setQueueBucket(Bucket.Transparent);
		smallTreeGeo.setMaterial(smallTreeMat);
		smallTreeGeo.setLocalTranslation(new Vector3f(6f, -1.85f, 5.0f));

		// attach all objects to scene
		rootNode.addLight(dl);
		rootNode.attachChild(grassLightGeo);
		rootNode.attachChild(grassDarkGeo);
		rootNode.attachChild(floorGeo);
		rootNode.attachChild(bigTreeGeo);
		rootNode.attachChild(smallTreeGeo);
	}
}
