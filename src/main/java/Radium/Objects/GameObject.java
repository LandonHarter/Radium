package Radium.Objects;

import RadiumEditor.Console;
import Radium.Application;
import Radium.Component;
import Radium.Math.Transform;
import Radium.SceneManagement.SceneManager;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements Cloneable {

    public String name = "New Game Object";
    public Transform transform;

    private GameObject storedGameObject;

    private List<Component> components = new ArrayList<>();

    public GameObject() {
        transform = new Transform();
        SceneManager.GetCurrentScene().gameObjectsInScene.add(this);
    }

    public GameObject(boolean instantiate) {
        transform = new Transform();
        if (instantiate) SceneManager.GetCurrentScene().gameObjectsInScene.add(this);
    }

    public void OnPlay() {
        storedGameObject = Clone();
    }

    public void OnStop() {
        if (storedGameObject == null) {
            Destroy();
            return;
        }

        name = storedGameObject.name;
        components = storedGameObject.components;
        transform = storedGameObject.transform;

        for (Component comp : components) {
            comp.Stop();
        }
    }

    public void Destroy() {
        Destroy(true);
    }

    public void Destroy(boolean clear) {
        for (int i = 0; i < components.size(); i++) {
            RemoveComponent(components.get(i).getClass());
        }
        components.clear();

        if (clear) {
            SceneManager.GetCurrentScene().gameObjectsInScene.remove(this);
        }
    }

    public <T extends Component> T GetComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (Exception e) {
                    Console.Error(e);
                }
            }
        }
        return null;
    }

    public Component AddComponent(Component component) {
        components.add(component);

        component.gameObject = this;
        component.OnAdd();

        if (Application.Playing) component.Start();

        return component;
    }

    public <T extends Component> void RemoveComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                c.OnRemove();

                components.remove(i);
            }
        }
    }

    public List<Component> GetComponents() {
        return components;
    }

    public boolean ContainsComponent(Class component) {
        return GetComponent(component) != null;
    }

    public GameObject Clone()
    {
        GameObject newGO = new GameObject(false);

        newGO.transform = new Transform();
        newGO.transform.position = transform.position;
        newGO.transform.rotation = transform.rotation;
        newGO.transform.scale = transform.scale;
        newGO.components = new ArrayList<>(components);

        newGO.name = new String(name);

        return newGO;
    }

}
