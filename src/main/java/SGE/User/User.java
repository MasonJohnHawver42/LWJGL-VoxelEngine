package SGE.User;

import SGE.User.Input.Keyboard;
import SGE.User.Input.Mouse;
import SGE.User.Output.Window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class User {
    public User(Window win) {
        window = win;

        mouse = new Mouse(window);
        keyboard = new Keyboard(window);
    }

    public void updateInput() {
        keyboard.update();
        glfwPollEvents();
        mouse.update();
    }

    public void terminate() {
        window.terminate();
    }

    public Window window;

    //io
    public Mouse mouse;
    public Keyboard keyboard;
}
