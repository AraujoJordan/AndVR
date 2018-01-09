package araujo.jordan.andvr.engine;

import android.os.Build;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import araujo.jordan.andvr.engine.entity.Camera;
import araujo.jordan.andvr.engine.entity.Entity;
import araujo.jordan.andvr.engine.entity.Light;
import araujo.jordan.andvr.engine.resources.GameResources;


/**
 * Created by arauj on 24/02/2017.
 */
public class VREngine {

    public GameResources resouces;
    public GameUpdates updates;
    public Camera camera;
    public Light light;
    public VrActivity vrAct;
    public boolean runningEngine;
    private ArrayList<Entity> entities;

//    private ArrayList<List<Entity>> taskEntities = new ArrayList<>();
//    private final int CPU_THREADS = Runtime.getRuntime().availableProcessors();
//    private static List<RunComponentsAsync> tasks = new ArrayList<>();
//
//    private UIBlocker uiBlocker = new UIBlocker();
//    private boolean entitiesChange = false;

    public VREngine(VrActivity activity, GameResources resources, GameUpdates gameUpdates) {
        activity.setup(this);
        entities = new ArrayList<>();

        this.resouces = resources;
        this.updates = gameUpdates;
        this.vrAct = activity;
    }

    public Camera getCamera() {
        return camera;
    }

    public void pause() {
        runningEngine = false;
        showSystemUI();
    }

    public void play() {
        runningEngine = true;
        hideSystemUI();
    }

    public void finish() {
        runningEngine = false;
        showSystemUI();
    }

    public boolean isRunning() {
        return runningEngine;
    }

    private void hideSystemUI() {
        View decorView = vrAct.getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN); // hide status bar
        }

    }

    private void showSystemUI() {
        View decorView = vrAct.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public void loadIntoOpenGL() {
        Log.v("VREngine", "loadIntoOpenGL()");
        for (Entity entity : entities) {
            if (entity.getModel3D() != null)
                entity.getModel3D().initTriangles();
        }
    }

    public void addEntity(Entity entity) {
        if (entity instanceof Light) {
            light = (Light) entity;
            return;
        }
        if (entity instanceof Camera) {
            camera = (Camera) entity;
            return;
        }
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Update the engine components (like physics, boxColisor, etc)
     * This will not draw anything on screen
     */
    public void engineUpdates() {
//        Log.v(getClass().getSimpleName(), "engineUpdates()");

        updates.gameFrame();

//        long initialTIme = new Date().getTime();

//        float elementsPerThread = entities.size() / CPU_THREADS;
//        float restOfElements = entities.size() % CPU_THREADS;


//        if (elementsPerThread < 1f) {// SINGLE_CORE

        for (Entity entity : entities)
            entity.run(this);

//        } else {// MULTI_CORE
//
//            if(taskEntities.isEmpty()) { //List has been changed
//                Log.e("INITLIST","taskEntities is empty");
//                for (int i = 1; i <= CPU_THREADS; i++)
//                    taskEntities.add(entities.subList(
//                            (int)((i * elementsPerThread) - elementsPerThread),
//                            (int)(i * elementsPerThread)));
//                taskEntities.add(entities.subList(
//                        (int)(CPU_THREADS * elementsPerThread),
//                        (int)((CPU_THREADS * elementsPerThread) + restOfElements)));
//            }

//            for (List<Entity> listOfEntities : taskEntities) {
//                RunComponentsAsync runComponentsAsync = new RunComponentsAsync(this, listOfEntities);
//                runComponentsAsync.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                tasks.add(runComponentsAsync);
//            }
//            uiBlocker.waitProcess();

//            Log.e("TIMESPENT","Time: "+(new Date().getTime() - initialTIme));
    }
//    }

//    public boolean allTasksComplete() {
//        try {
//            for (int i = 0; i < tasks.size(); i++) {
//                RunComponentsAsync componentsAsync = tasks.get(i);
//                if (componentsAsync.processComplete)
//                    tasks.remove(componentsAsync);
//                else
//                    return false;
//            }
//        } catch (IndexOutOfBoundsException | NullPointerException err) { //Someone already did task complete
//            return true;
//        }
//
//        if(entitiesChange) {
//            entitiesChange = false;
//            taskEntities.clear();
//        }
////        tasks.clear();
//        return true;
//    }

    public void draw() {
        for (Entity entity : entities)
            entity.draw(this);
        if (camera.getPhysics() != null)
            camera.getPhysics().run(this);
    }

    public void addCamera(Camera camera) {
        this.camera = camera;
    }

    public void addLight(Light light) {
        this.light = light;
    }

    public interface GameUpdates {
        void gameFrame();
    }

//    private class RunComponentsAsync extends AsyncTask<Void, Void, Void> {
//
//        private boolean processComplete; //can't use asyncTask status due to block ui on wait()
//        private VREngine engine;
//        private List<Entity> list;
//
//        public RunComponentsAsync(VREngine engine, List<Entity> list) {
//            this.engine = engine;
//            this.list = list;
//            processComplete = false;
//        }
//
//        @Override
//        protected Void doInBackground(Void... var) {
//            for (Entity ent : list)
//                ent.run(engine);
//            processComplete = true;
//            if (allTasksComplete()) {
////                uiBlocker.processComplete();
//            }
//            return null;
//        }
//    }

}
