package SGE.User.Output;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public Window(String title, int w, int h) {

        width = w;
        height = h;

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if ( handle == NULL ) { throw new RuntimeException("Failed to create the GLFW window"); }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);

        glfwShowWindow(handle);

        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
    }

    //getters

    public long getHandle() { return handle; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public float getAspectRatio() { return (float)width / (float)height; }

    //modders

    public void clear(float r, float g, float b) {
        glClearColor(r, g, b, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void display() { glfwSwapBuffers(handle); }

    public void terminate() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);
    }

    private int width, height;

    private final long handle;
}
