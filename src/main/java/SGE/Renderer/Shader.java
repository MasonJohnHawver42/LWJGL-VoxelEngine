package SGE.Renderer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {

    public Shader(String filename, int type) throws Exception {
        this.id = glCreateShader(type);
        this.type = type;
        String source = Shader.getSource(filename);
        if (id == 0) { throw new Exception("Error creating shader. Type: " + type); }

        glShaderSource(id, source);
        glCompileShader(id);

        if (glGetShaderi(id, GL_COMPILE_STATUS) == 0) { throw new Exception("Error compiling SGE.Shader code: " + glGetShaderInfoLog(id, 1024)); }
    }

    public final int id;
    public final int type;

    private static String getSource(String filename) throws Exception {
        Path path = Paths.get(filename);
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
