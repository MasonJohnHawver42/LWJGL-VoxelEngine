import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    public Window(String title, int w, int h) {

        width = w;
        height = h;

        // Configure GLFW
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE); // the window will be resizable

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if ( handle == NULL ) { throw new RuntimeException("Failed to create the GLFW window"); }

        glfwMakeContextCurrent(handle);
        glfwSwapInterval(1);

        glfwShowWindow(handle);

    }

    //getters

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    //modders

    public void clear(int r, int g, int b) {
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

    private long handle;
}
