package engineTester;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.ModelTexture;

public class MainGameLoop {

	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		Loader loader = new Loader();
		
		
		RawModel model = OBJLoader.loadObjModel("modell", loader);
		TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("modellTexture")));
		ModelTexture texture = texturedModel.getTexture();
		//texture.setShineDamper(10);
		//texture.setReflectivity(1);
		
		List<Entity> allCubes = new ArrayList<Entity>();
		List<Float> rotX = new ArrayList<Float>();
		List<Float> rotY = new ArrayList<Float>();
		Random random = new Random();
		
		for(int i = 0; i < 200; i++){
			float x = random.nextFloat() * 100 -50;
			float y = random.nextFloat() * 100 -50;
			float z = random.nextFloat() * -300;
			rotX.add(random.nextFloat() * 4 - 2);
			rotY.add(random.nextFloat() * 4 - 2);
			allCubes.add(new Entity(texturedModel, new Vector3f(x,y,z), random.nextFloat() * 180f, random.nextFloat() * 180f, 0f, 1f));
		}
		
		Light light = new Light(new Vector3f(3000,2000,3000), new Vector3f(1,1,1));
		
		Camera camera = new Camera();
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()){
			camera.move();

			for(int i = 0; i < allCubes.size(); i++){
				allCubes.get(i).increaseRotation(rotX.get(i), rotY.get(i), 0f);
				renderer.processEntity(allCubes.get(i));
			}
			renderer.render(light, camera);
			DisplayManager.updateDisplay();
		}
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}

}
