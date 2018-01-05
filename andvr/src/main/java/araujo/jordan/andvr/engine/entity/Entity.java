package araujo.jordan.andvr.engine.entity;

import java.util.ArrayList;
import java.util.List;

import araujo.jordan.andvr.engine.VREngine;
import araujo.jordan.andvr.engine.entity.components.BoxCollision;
import araujo.jordan.andvr.engine.entity.components.Component;
import araujo.jordan.andvr.engine.entity.components.Physics;
import araujo.jordan.andvr.engine.entity.components.Texture;
import araujo.jordan.andvr.engine.entity.components.Transformation;
import araujo.jordan.andvr.engine.entity.components.model3d.Model3D;

/**
 * Created by arauj on 05/03/2017.
 */

public class Entity {
    public final String label;
    private List<Component> components;

    // COMPONENTS REFERENCE FOR OPTIMIZATION
    private Transformation transformation;
    private Model3D model3D;
    private Physics physics;
    private BoxCollision boxCollision;
    private Texture texture;

    public Entity() {
        label = getClass().getCanonicalName();
        components = new ArrayList<>();
    }

    public Entity(String label) {
        this.label = label;
        components = new ArrayList<>();
    }

    public List<Component> getComponents() {
        return components;
    }

    /**
     * Use an cache to delivery the component
     *
     * @return the component on cache
     */
    public Transformation getTransformation() {
        if (transformation != null)
            return transformation;
        for (Component component : components) {
            if (component instanceof Transformation) {
                transformation = (Transformation) component;
                return transformation;
            }
        }
        return null;
    }

    /**
     * Use an cache to delivery the component
     *
     * @return the component on cache
     */
    public Model3D getModel3D() {
        if (model3D != null)
            return model3D;
        for (Component component : components) {
            if (component instanceof Model3D) {
                model3D = (Model3D) component;
                return model3D;
            }
        }
        return null;
    }

    /**
     * Use an cache to delivery the component
     *
     * @return the component on cache
     */
    public BoxCollision getBoxCollision() {
        if (boxCollision != null)
            return boxCollision;
        for (Component component : components) {
            if (component instanceof BoxCollision) {
                boxCollision = (BoxCollision) component;
                return boxCollision;
            }
        }
        return null;
    }

    /**
     * Use an cache to delivery the component
     *
     * @return the component on cache
     */
    public Physics getPhysics() {
        if (physics != null)
            return physics;
        for (Component component : components) {
            if (component instanceof Physics) {
                physics = (Physics) component;
                return physics;
            }
        }
        return null;
    }

    /**
     * Use an cache to delivery the component
     *
     * @return the component on cache
     */
    public Texture getTexture() {
        if (texture != null)
            return texture;
        for (Component component : components) {
            if (component instanceof Texture) {
                texture = (Texture) component;
                return texture;
            }
        }
        return null;
    }

    /**
     * Remove the component from optimization variables (references) and from the list
     *
     * @param removeMe the variable that you want to be removed
     */
    public void removeComponent(Component removeMe) {
        //IS THIS NECESSARY TO CLEAR THE OBJECT REFERENCE? OR JUST REMOVE FROM LIST IT WILL BE NULL TOO?
        if (removeMe instanceof Transformation)
            transformation = null;
        if (removeMe instanceof Model3D)
            model3D = null;
        if (removeMe instanceof BoxCollision)
            boxCollision = null;
        if (removeMe instanceof Physics)
            physics = null;
        if (removeMe instanceof Texture)
            texture = null;

        //REMOVE FROM LIST TOO
        components.remove(removeMe);
    }

    public void run(VREngine engine) {
        for (Component component : components)
            if (!(component instanceof Model3D)) {
                component.run(engine);
            }
    }

    public void draw(VREngine engine) {
        if (getModel3D() != null)
            getModel3D().run(engine);
    }


    public void addComponent(Component component) {
        if (component.parentEntity == null)
            component.parentEntity = this;

        if (component instanceof Transformation)
            transformation = (Transformation) component;
        if (component instanceof Model3D)
            model3D = (Model3D) component;
        if (component instanceof BoxCollision)
            boxCollision = (BoxCollision) component;
        if (component instanceof Physics)
            physics = (Physics) component;

        components.add(component);
    }
}
