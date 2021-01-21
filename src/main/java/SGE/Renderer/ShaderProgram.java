package SGE.Renderer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;

import java.util.HashMap;
import java.util.Map;

import SGE.User.Assets.Asset;
import org.joml.Matrix4f;
import org.joml.Vector3i;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {

    public ShaderProgram() throws Exception {
        id = glCreateProgram();
        if (id == 0) { throw new Exception("Could not create SGE.User.Input.User.Renderer.Shader"); }

        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(id, uniformName);
        //if (uniformLocation < 0) { throw new Exception("Could not find uniform:" + uniformName); }
        uniforms.put(uniformName, uniformLocation);
    }

    public void setUniform(String uniformName, Matrix4f value) {
        // Dump the matrix into a float buffer
        try (MemoryStack stack = MemoryStack.stackPush()) {
            glUniformMatrix4fv(uniforms.get(uniformName), false, value.get(stack.mallocFloat(16)));
        }
    }

    public void setUniform(String uniformName, int x, int y, int z) {
        glUniform3i(uniforms.get(uniformName), x, y, z);
    }

    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    public void add(Asset.Shader shader) {

        glAttachShader(this.id, shader.id);

        switch (shader.type) {
            case GL_VERTEX_SHADER:
                vertex = shader; break;

            case GL_GEOMETRY_SHADER:
                geometry = shader; break;

            case GL_FRAGMENT_SHADER:
                fragment = shader;
        }
    }

    public void link() throws Exception {
        glLinkProgram(id);

        if (glGetProgrami(id, GL_LINK_STATUS) == 0) { throw new Exception("Error linking SGE.User.Input.User.Renderer.Shader code: " + glGetProgramInfoLog(id, 1024)); }
        if (vertex != null && vertex.id != 0) { glDetachShader(this.id, vertex.id); }
        if (geometry != null && geometry.id != 0) { glDetachShader(this.id, geometry.id); }
        if (fragment != null && fragment.id != 0) { glDetachShader(this.id, fragment.id); }

        glValidateProgram(id);
        if (glGetProgrami(id, GL_VALIDATE_STATUS) == 0) { System.err.println("Warning validating SGE.User.Input.User.Renderer.Shader code: " + glGetProgramInfoLog(id, 1024)); }
    }

    public void bind() { glUseProgram(id); }

    public void unbind() { glUseProgram(0); }

    public void terminate() { unbind(); if (id != 0) { glDeleteProgram(id); } }


    private Asset.Shader vertex, geometry, fragment;

    private final Map<String, Integer> uniforms;

    private final int id;
}
