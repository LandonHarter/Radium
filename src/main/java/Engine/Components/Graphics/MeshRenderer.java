package Engine.Components.Graphics;

import Engine.Component;
import Engine.Graphics.Renderers.Renderer;
import Engine.Graphics.Renderers.UnlitRenderer;
import Engine.Graphics.Texture;

public class MeshRenderer extends Component {

    private transient Renderer renderer;

    public MeshRenderer() {
        icon = new Texture("EngineAssets/Editor/Icons/meshrenderer.png").textureID;
        renderer = new UnlitRenderer();
    }
    public MeshRenderer(Renderer renderer) {
        icon = new Texture("EngineAssets/Editor/Icons/meshrenderer.png").textureID;
        this.renderer = renderer;
    }

    @Override
    public void Start() {

    }

    @Override
    public void Update() {
        renderer.Render(gameObject);
    }

    @Override
    public void GUIRender() {

    }

}
