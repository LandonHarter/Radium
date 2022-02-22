package Radium.Components;

import Radium.Color;
import Radium.Component;
import Radium.EventSystem.EventSystem;
import Radium.EventSystem.Events.Event;
import Radium.EventSystem.Events.EventType;
import Radium.Math.Vector.Vector2;
import Radium.Math.Vector.Vector3;
import Radium.Scripting.*;
import Radium.System.FileExplorer;
import RadiumEditor.Annotations.RunInEditMode;
import RadiumEditor.Console;
import RadiumEditor.EditorGUI;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.ArrayList;
import java.util.List;

public class NodeScriptManager extends Component {

    private transient List<NodeScript> scripts = new ArrayList<>();
    private List<String> scriptPaths = new ArrayList<>();

    private float buttonPadding = 20;

    public NodeScriptManager() {
        name = "Script Manager";
        submenu = "Scripting";
    }

    @Override
    public void Start() {
        for (NodeScript script : scripts) {
            for (ScriptingNode node : script.nodes) {
                node.action = NodeAction.ActionFromType(node);
                node.start = NodeAction.StartFromType(node);
                node.update = NodeAction.UpdateFromType(node);
                node.display = NodeAction.DisplayFromType(node);
            }

            script.Start();
        }
    }

    @Override
    public void Update() {
        for (NodeScript script : scripts) {
            script.Update();
        }
    }

    @Override
    public void OnAdd() {
        ReloadScripts();
    }

    @Override
    public void GUIRender() {
        if (ImGui.button("Refresh")) {
            ReloadScripts();
        }
        ImGui.sameLine();
        if (ImGui.treeNodeEx("Scripts")) {
            for (int i = 0; i < scripts.size(); i++) {
                NodeScript script = scripts.get(i);

                if (ImGui.button("Remove")) {
                    scripts.remove(i);
                }
                ImGui.sameLine();
                if (ImGui.treeNodeEx(script.name)) {
                    for (NodeScriptProperty property : script.properties) {
                        RenderProperty(property);
                    }

                    ImGui.treePop();
                }
            }

            ImGui.treePop();
        }

        ImGui.setCursorPosX(buttonPadding);
        if (ImGui.button("Add Script", ImGui.getWindowWidth() - (buttonPadding * 2), 25)) {
            String path = FileExplorer.Choose("script");
            if (path != null) {
                LoadScript(path);
            }
        }
    }

    private void ReloadScripts() {
        scripts.clear();

        int pathSize = scriptPaths.size();
        for (int i = 0; i < pathSize; i++) {
            String value = scriptPaths.get(i);
            scriptPaths.remove(i);

            LoadScript(value);
        }
    }

    private void LoadScript(String path) {
        NodeScript script = NodeScript.Load(path);
        script.gameObject = gameObject;

        for (ScriptingNode node : script.nodes) {
            node.gameObject = gameObject;
        }

        scripts.add(script);
        scriptPaths.add(path);

        Nodes.NodePlay = true;
        EventSystem.Trigger(null, new Event(EventType.Play));
        EventSystem.Trigger(null, new Event(EventType.Stop));
        Console.Clear(false);
        Nodes.NodePlay = false;
    }

    private void RenderProperty(NodeScriptProperty property) {
        property.FixType();

        if (property.type == Integer.class) {
            property.value = EditorGUI.DragInt(property.name, (int)property.value);
        } else if (property.type == Float.class) {
            property.value = EditorGUI.DragFloat(property.name, (float)property.value);
        } else if (property.type == Boolean.class) {
            property.value = EditorGUI.Checkbox(property.name, (boolean)property.value);
        } else if (property.type == String.class) {
            property.value = EditorGUI.InputString(property.name, (String)property.value);
        } else if (property.type == Vector2.class) {
            property.value = EditorGUI.DragVector2(property.name, (Vector2)property.value);
        } else if (property.type == Vector3.class) {
            property.value = EditorGUI.DragVector3(property.name, (Vector3)property.value);
        } else if (property.type == Color.class) {
            property.value = EditorGUI.ColorField(property.name, (Color)property.value);
        }
    }

}
