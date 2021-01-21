package SGE.User;

import SGE.User.Assets.AssetManager;
import SGE.User.Input.Keyboard;
import SGE.User.Input.Mouse;
import SGE.User.Output.Window;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class User {
    public User(Window win, AssetManager am) {
        window = win;

        mouse = new Mouse(window);
        keyboard = new Keyboard(window);

        assetManager = am;
    }

    public void updateInput() {
        keyboard.update();
        glfwPollEvents();
        mouse.update();
    }

    public void terminate() {
        window.terminate();
        assetManager.terminate();
    }

    public Window window;

    //io
    public Mouse mouse;
    public Keyboard keyboard;

    //Assets
    public AssetManager assetManager;
}
