package Radium.Engine.Components.UI;

import Radium.Build;
import Radium.Engine.*;
import Radium.Engine.Color.Color;
import Radium.Engine.Input.Input;
import Radium.Engine.Math.Vector.Vector2;
import Radium.Editor.Annotations.HideInEditor;
import Radium.Editor.Viewport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import java.util.ArrayList;
import java.util.List;

public class Button extends Component {

    private Image image;

    @HideInEditor
    public boolean needToAdd = true;

    @HideInEditor
    public boolean isClicked = false;

    @JsonIgnore
    public PythonInterpreter interpreter;
    @JsonIgnore
    public PyObject buttonPy = null;

    public Button() {
        submenu = "UI";
        impact = PerformanceImpact.Low;

        description = "Clickable image that triggers an event";
        LoadIcon("button.png");
    }

    public void Update() {
        if (image == null) {
            image = gameObject.GetComponent(Image.class);
            if (image == null) {
                image = (Image)gameObject.AddComponent(new Image());
            }
        }

        if (Input.GetMouseButtonPressed(0)) {
            if (Application.Editor) {
                Vector2 mouse = Input.GetMousePosition();
                float x = InverseLerp(Viewport.position.x, Viewport.position.x + Viewport.size.x, 0, Window.width, mouse.x);
                float y = InverseLerp(Viewport.position.y, Viewport.position.y + Viewport.size.y, Window.height, 0,mouse.y);
                y = 1080 - y;

                Vector2 pos = (Vector2)image.position.clone();
                Vector2 size = (Vector2)image.size.clone();

                isClicked = IsHovering(pos, size, new Vector2(x, y));
            } else {
                Vector2 mouse = Input.GetMousePosition();
                float x = InverseLerp(0, Window.width, 0, Window.width, mouse.x);
                float y = InverseLerp(0,  Window.height, Window.height, 0,mouse.y);
                y = 1080 - y;

                Vector2 pos = (Vector2)image.position.clone();
                Vector2 size = (Vector2)image.size.clone();
                pos.x -= size.x / 2;

                isClicked = IsHovering(pos, size, new Vector2(x, y));
            }
        } else {
            isClicked = false;
        }

        if (isClicked && buttonPy != null) {
            buttonPy.__getattr__("callback").__call__(interpreter.get("ButtonEvent").__call__());
        }
    }

    private boolean IsHovering(Vector2 pos, Vector2 size, Vector2 mouse) {

        return mouse.x >= pos.x && mouse.x <= pos.x + size.x && mouse.y >= pos.y && mouse.y <= pos.y + size.y;
    }

    public void OnAdd() {
        if (!needToAdd) return;

        if (!gameObject.ContainsComponent(Text.class)) {
            Text text = (Text)gameObject.AddComponent(new Text("Text"));
            text.Position = new Vector2(880, 480);
            text.color = new Color(0, 0, 0);
            text.layerOrder = 1;
        }
        if (!gameObject.ContainsComponent(Image.class)) {
            image = (Image)gameObject.AddComponent(new Image());
            image.position = new Vector2(760, 410);
            image.size = new Vector2(400, 100);
        }
    }

    private static float InverseLerp(float imin, float imax, float omin, float omax, float v) {
        float t = (v - imin) / (imax - imin);
        float lerp = (1f - t) * omin + omax * t;

        return lerp;
    }

}
