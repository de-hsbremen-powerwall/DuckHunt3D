package de.hsbremen.powerwall.duckhunt3d.objects;


import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import com.jme3.texture.Texture;

/**
 * 
 * @author Hendrik
 *
 */
public class Duck {
	
	private Geometry duckGeo;
	private Quad duckQuad;
	
	private Texture cycle1;
	private Texture cycle2;
	private Texture cycle3;
	private Texture die1;
	private Texture die2;
	
	private Material duckMat;
	private Material duckMat2;
	private Material duckMat3;
	private Material dieMat;
	private Material dieMat2;
	
	private int bounty;
	private Vector3f pos;
	private Vector3f speed;
	
	private boolean duckAlive = true;
	
	// TODO add behaviour like changing directions
	/**
	 * creates a new Duck
	 * @param shootablesNode Node with all collidable duckObjects
	 * @param duckList duckList for adding ducks
	 * @param assetManager assetManager to load Materials
	 * @param p position of duck
	 */
	public Duck(Node shootablesNode, List<Duck> duckList, AssetManager assetManager,Vector3f p){
		// iniate variables
		duckQuad = new Quad(256/100,256/100);
		duckGeo = new Geometry("Quad", duckQuad);
		this.pos = p;
		this.speed = (new Vector3f(0,0,0));
		
		// assign all Materials unshaded
		duckMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		duckMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		duckMat3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		dieMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		dieMat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
			
		// find out if it comes from the right or left and which kind it is : red, blue, green
		int defineDuck = (int) Math.round(MathRandom(0.5, 3.4));
		boolean fromLeft = false;
		
		if(pos.x < 0){
			fromLeft = true;
		}else if (pos.x > 0){
			fromLeft = false;
		}
	
		//change material according to define Duck
		switch(defineDuck){
		case 1:
			if (fromLeft){
				cycle1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dblue.png");
				cycle2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dblue2.png");
				cycle3 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dblue3.png");
				die1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/bdie.png");
				die2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/bdie2.png");
				setSpeed((float) MathRandom(1.0, 1.3)/50,0,0);
			}else{
				cycle1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dbluer.png");
				cycle2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dbluer2.png");
				cycle3 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dbluer3.png");
				die1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/bdier.png");
				die2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/bdier2.png");
				setSpeed(-(float) MathRandom(1.0, 1.3)/50,0,0);
			}
			this.bounty = 1;
			duckGeo.setName("blue");
			break;
		case 2:
			if (fromLeft){
				cycle1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dgreen.png");
				cycle2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dgreen2.png");
				cycle3 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dgreen3.png");
				die1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/gdie.png");
				die2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/gdie2.png");
				setSpeed((float) MathRandom(1.5, 1.8)/50,0,0);
			}else{
				cycle1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dgreenr.png");
				cycle2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dgreenr2.png");
				cycle3 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dgreenr3.png");
				die1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/gdier.png");
				die2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/gdier2.png");
				setSpeed(-(float) MathRandom(1.5, 1.8)/50,0,0);
			}
			this.bounty = 2;
			duckGeo.setName("green");
			break;
		case 3:
			if(fromLeft){
				cycle1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dred.png");
				cycle2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dred2.png");
				cycle3 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dred3.png");
				die1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/rdie.png");
				die2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/rdie2.png");
				setSpeed((float) MathRandom(2.0, 2.5)/50,0,0);
			}else{
				cycle1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dredr.png");
				cycle2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dredr2.png");
				cycle3 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/dredr3.png");
				die1 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/rdier.png");
				die2 = assetManager.loadTexture("de/hsbremen/powerwall/duckhunt3d/assets/rdier2.png");
				setSpeed(-(float) MathRandom(2.0, 2.5)/50,0,0);
			}
			this.bounty = 3;
			duckGeo.setName("red");
			break;
		}
		
		//set textures
		duckMat.setTexture("ColorMap", cycle1);
		duckMat2.setTexture("ColorMap", cycle2);
		duckMat3.setTexture("ColorMap", cycle3);
		dieMat.setTexture("ColorMap", die1);
		dieMat2.setTexture("ColorMap", die2);
		
		//set transparency of material
		duckMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		duckMat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		duckMat3.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		dieMat.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		dieMat2.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		
		//set transparency of geometry
		duckGeo.setQueueBucket(Bucket.Transparent);
		duckGeo.setLocalTranslation(pos);

		//set first material
		duckGeo.setMaterial(duckMat);
		
		//add duck
		duckList.add(this);
		shootablesNode.attachChild(duckList.get(duckList.size()-1).getDuckGeo());
		
		//TODO remove this when debugging is finished
		System.out.println("Duck created at " + this.pos + " Total Ducks: " + duckList.size());
	}
	
	/**
	 * cycles through all materials
	 */
	public void cycle() {
		if(duckAlive){
			if(duckGeo.getMaterial().equals(duckMat3)){
				duckGeo.setMaterial(duckMat);
			}else if(duckGeo.getMaterial().equals(duckMat)){
				duckGeo.setMaterial(duckMat2);
			}else if(duckGeo.getMaterial().equals(duckMat2)){
				duckGeo.setMaterial(duckMat3);
			}
		}
	}
	
	/**
	 * makes duck no longer shootable and shows die animation
	 * @param shootables node with ducks
	 * @param duckList list with ducks
	 */
	public void die(Node shootables, List<Duck> duckList) {
		
		duckAlive = false;
		duckGeo.setMaterial(dieMat);
		this.setSpeed(0, 0, 0);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				duckGeo.setMaterial(dieMat2);
				setSpeed(0, -0.2f, 0);
			}
		}, 200);
	}

	private double MathRandom(double low, double high) {
		double random;
		random = Math.random() *(high-low) + low;	
		return random;
	}
	
	//Getter&Setter
	public Geometry getDuckGeo() {
		return duckGeo;
	}

	public int getBounty() {
		return bounty;
	}

	public Vector3f getPos() {
		return pos;
	}

	public void setPos(Vector3f p) {
		pos.x += p.x;
		pos.y += p.y;
		pos.z += p.z;
		duckGeo.setLocalTranslation(pos);
	}

	public Vector3f getSpeed() {
		return speed;
	}

	public void setSpeed(float x, float y, float z) {
		speed.x = x;
		speed.y = y;
		speed.z = z;
	}
	
	public boolean isAlive(){
		return duckAlive;
	}	
}
