package SGE.User.Input;

import SGE.User.Output.Window;
import org.joml.Vector2d;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {

    public Mouse(Window window) {
        win = window;

        pos = new Vector2d(0, 0);
        focused = false;

        glfwSetCursorPosCallback(win.getHandle(), (windowHandle, xpos, ypos) -> {
            pos.x = xpos;
            pos.y = ypos;
        });
        glfwSetCursorEnterCallback(win.getHandle(), (windowHandle, entered) -> {
            focused = entered;
        });
    }

    public Vector2d getPos() { return pos; }

    public void setPos(int x, int y) {
        glfwSetCursorPos(win.getHandle(),x,y);
        pos.set(x, y);
    }

    public void update() {}

    private final Window win;

    private final Vector2d pos;

    public boolean focused;

}
