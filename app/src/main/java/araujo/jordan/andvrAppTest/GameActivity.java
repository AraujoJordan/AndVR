package araujo.jordan.andvrAppTest;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.VrActivity;
import araujo.jordan.andvr.engine.draw.Color;
import araujo.jordan.andvr.engine.entity.Camera;
import araujo.jordan.andvr.engine.entity.Entity;
import araujo.jordan.andvr.engine.entity.components.Texture;
import araujo.jordan.andvr.engine.entity.components.Transformation;
import araujo.jordan.andvr.engine.entity.components.model3d.Model3D;
import araujo.jordan.andvr.engine.math.Vector3D;
import araujo.jordan.andvr.engine.resources.GameResources;

public class GameActivity extends VrActivity implements VREngine.GameUpdates {

    public int fps = 0;
    ArrayList<Entity> entitiesListForTrans = new ArrayList<>();
    int increment = 0;
    private VREngine gameEngine;
    private Camera camera;
    private GameResources resources;
    private Timer timer;
    Random rand = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = new GameResources();
        resources.addOBJ(this, "cube", "porche.obj");
//        resources.addOBJ(this, "bird", "bird-flat.obj");
//        resources.addOBJ(this, "porche", "porche.obj");
        resources.addTexture("cube", R.drawable.porchejp);

        gameEngine = new VREngine(this, resources, this);

        camera = new Camera("mainCamera");
        camera.getTransformation().setTranslation(0, 0f, 0f);
        gameEngine.addCamera(camera);

        Entity superCar = new Entity("cube");
        superCar.addComponent(new Transformation(0, 0, -4));
        superCar.addComponent(new Model3D("cube", gameEngine,
                new Color(1f, 0f, 0f, 1f)));
        superCar.addComponent(new Texture(gameEngine, "cube"));
        gameEngine.addEntity(superCar);
        entitiesListForTrans.add(superCar);

//        entitiesListForTrans = addObjects(15);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.e("GameActivity", "FPS: " + fps);
                fps = 0;
            }
        }, 1000, 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public void gameFrame() {
        fps++;
        increment++;
        Vector3D rot3D = entitiesListForTrans.get(0).getTransformation().getRotation();
        rot3D.xyz[0] = ((float) increment)/10f;
        rot3D.xyz[1] = ((float) increment)/20f;
        rot3D.xyz[2] = ((float) increment)/30f;

        for (Entity entity : entitiesListForTrans) {
            entity.getTransformation().setRotation(rot3D);
        }

    }

    public ArrayList<Entity> addObjects(int many) {

        ArrayList<Entity> birdsList = new ArrayList<>();

        final int max = 10;
        final int min = -10;

        for (int i = 0; i < many; i++) {
            Entity bird = new Entity("bird" + i);
            bird.addComponent(new Transformation(
                    rand.nextInt((max - min) + 1) + min,
                    rand.nextInt((max - min) + 1) + min,
                    rand.nextInt((max - min) + 1) + min));

            float red = rand.nextFloat();
            red = (red+0.5f)>1.0?1.0f:red;

            float green = rand.nextFloat();
            green = (green+0.5f)>1.0?1.0f:green;

            float blue = rand.nextFloat();
            blue = (blue+0.5f)>1.0?1.0f:blue;

            bird.addComponent(new Model3D("porche", gameEngine,
                    new Color(red,green,blue,
                            1f)));
            gameEngine.addEntity(bird);
            birdsList.add(bird);
        }
        return birdsList;
    }
}
