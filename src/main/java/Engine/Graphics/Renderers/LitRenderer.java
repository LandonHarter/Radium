package Engine.Graphics.Renderers;

import Editor.Console;
import Engine.Components.Graphics.MeshFilter;
import Engine.Graphics.Framebuffer.DepthFramebuffer;
import Engine.Graphics.Shader;
import Engine.Graphics.Shadows.Shadows;
import Engine.Objects.GameObject;
import Engine.Variables;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public final class LitRenderer extends Renderer {

    @Override
    public void Initialize() {
        shader = new Shader("EngineAssets/Shaders/Lit/vert.glsl", "EngineAssets/Shaders/Lit/frag.glsl");
    }

    @Override
    public void SetUniforms(GameObject gameObject) {
        gameObject.GetComponent(MeshFilter.class).SentMaterialToShader(shader);
        shader.SetUniform("cameraPosition", Variables.DefaultCamera.gameObject.transform.position);
    }

}
