package araujo.jordan.andvr.engine.utils;

/**
 * Created by jordan on 12/12/17.
 */

public class UIBlocker {

    private boolean tasksComplete;

    public synchronized void waitProcess() {

        tasksComplete = false;
        while (!tasksComplete) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void processComplete() {
        tasksComplete = true;
        notify();
    }
}
