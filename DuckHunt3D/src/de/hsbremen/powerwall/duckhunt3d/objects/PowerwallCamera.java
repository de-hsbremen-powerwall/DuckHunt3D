package de.hsbremen.powerwall.duckhunt3d.objects;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.system.AppSettings;

/**
 * 
 * @author benn0rDE, impmja 
 *
 */
public class PowerwallCamera {

	private SimpleApplication app;
	private AppSettings settings;
	private Camera leftCam, rightCam;
	private ViewPort leftView, rightView;
	private Vector3f location;
	private static final float offset = 0.1f;
	
	public PowerwallCamera(SimpleApplication app, AppSettings settings) {
		this.app = app;
		this.settings = settings;
		initCameras();
	}

	public void initCameras() {

		leftCam = new Camera(settings.getWidth(), settings.getHeight());
		rightCam = new Camera(settings.getWidth(), settings.getHeight());

		float aspect = (float)rightCam.getWidth() / rightCam.getHeight();
		
		leftCam.setFrustumPerspective(45f, aspect, 1f, 1000f);
		leftCam.setLocation(new Vector3f(offset, 0f, 10f));
		leftCam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
		leftCam.setViewPort(0f, 0.5f, 0f, 1f);
		
		rightCam.setFrustumPerspective(45f, aspect, 1f, 1000f);
		rightCam.setLocation(new Vector3f(-offset, 0f, 10f));
		rightCam.lookAt(new Vector3f(0f, 0f, 0f), Vector3f.UNIT_Y);
		rightCam.setViewPort(.5f, 1f, 0f, 1f);
		
		RenderManager renderManager = app.getRenderManager();
		
		leftView = renderManager.createMainView("LeftView", leftCam);
		leftView.setClearFlags(true, true, true);
		//leftView.setBackgroundColor(new ColorRGBA(63/255,191/255,1,1));
		
		rightView = renderManager.createMainView("RightView", rightCam);
		rightView.setClearFlags(true, true, true);
		//rightView.setBackgroundColor(new ColorRGBA(255/255,191/255,1,1));
	}
	
	public void lookAt(Vector3f position, Vector3f world) {
		leftCam.lookAt(position, world);
		rightCam.lookAt(position, world);
	}
	
	public void attachScene(Node scene) {
		leftView.attachScene(scene);
		rightView.attachScene(scene);
	}
	
	public void setLocation(Vector3f location) {
		this.location = location;
		leftCam.setLocation(this.location);
		rightCam.setLocation(this.location);
		leftCam.update();
		rightCam.update();
	}

	public Camera getLeftCam() {
		return leftCam;
	}

	public Camera getRightCam() {
		return rightCam;
	}
	
	public void update(float xPos,float yPos,float zPos){
		rightCam.setLocation(new Vector3f(xPos + rightCam.getLocation().x, yPos + rightCam.getLocation().y, zPos + rightCam.getLocation().z));
		leftCam.setLocation(new Vector3f(xPos + leftCam.getLocation().x, yPos + leftCam.getLocation().y, zPos + leftCam.getLocation().z));
	}


}
