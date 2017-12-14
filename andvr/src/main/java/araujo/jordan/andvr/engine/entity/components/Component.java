package araujo.jordan.andvr.engine.entity.components;

import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.entity.Entity;

/**
 * Created by arauj on 05/03/2017.
 */
public abstract class Component {

    public Entity parentEntity;

    public Component(Entity entity) {
        this.parentEntity = entity;
    }

    public Component() {}

    public abstract void run(VREngine engine);
}
