import org.lwjgl.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;


public class HelloWorld {

    // The window handle
    private Window win;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        if ( !glfwInit() ) { throw new IllegalStateException("Unable to initialize GLFW"); }
        win = new Window("Test", 500, 500);
    }

    private void loop() {
        GL.createCapabilities();

        while ( true ) {
            win.clear(255, 0, 0);
            win.display();

            glfwPollEvents();
        }
    }

    public static void main(String[] args) { new HelloWorld().run(); }

}