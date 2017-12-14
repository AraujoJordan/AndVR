package araujo.jordan.andvr;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import araujo.jordan.andvr.engine.resources.GameResources;
import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.VrActivity;
import araujo.jordan.andvr.engine.draw.Color;
import araujo.jordan.andvr.engine.entity.Camera;
import araujo.jordan.andvr.engine.entity.Entity;
import araujo.jordan.andvr.engine.entity.components.Transformation;
import araujo.jordan.andvr.engine.entity.components.model3d.Model3D;
import araujo.jordan.andvr.engine.math.Vector3D;

public class GameActivity extends VrActivity implements VREngine.GameUpdates {

    private VREngine gameEngine;
    private Camera camera;
    private GameResources resources;

    public int fps = 0;
    private Timer timer;

    ArrayList<Entity> birdsListForTrans;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        resources = new GameResources();
        resources.addOBJ(this, "bird", "bird-flat.obj");

        gameEngine = new VREngine(this, resources, this);

        camera = new Camera("mainCamera");
        camera.getTransformation().setTranslation(0, 0f, 0f);
        gameEngine.addCamera(camera);

        birdsListForTrans = addBirds(50);

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

    int increment = 0;

    @Override
    public void gameFrame() {
        fps++;
        increment++;
        Vector3D rot3D = birdsListForTrans.get(0).getTransformation().getRotation();
        rot3D.xyz[0] = increment;
        rot3D.xyz[1] = increment;
        rot3D.xyz[2] = increment;

        for (Entity entity : birdsListForTrans) {
            entity.getTransformation().setRotation(rot3D);
        }
    }

    public ArrayList<Entity> addBirds(int birds) {

        ArrayList<Entity> birdsList = new ArrayList<>();
        Random rand = new Random();
        final int max = 10;
        final int min = -10;

        for (int i = 0; i < birds; i++) {
            Entity bird = new Entity("bird" + i);
            bird.addComponent(new Transformation(
                    rand.nextInt((max - min) + 1) + min,
                    rand.nextInt((max - min) + 1) + min,
                    rand.nextInt((max - min) + 1) + min));
            bird.addComponent(new Model3D("bird", gameEngine,
                    new Color(rand.nextFloat(),
                            rand.nextFloat(),
                            rand.nextFloat(),
                            1f)));
            gameEngine.addEntity(bird);
            birdsList.add(bird);
        }
        return birdsList;
    }
}
