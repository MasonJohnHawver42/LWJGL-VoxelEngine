package SGE.User.Input;

import SGE.User.Output.Window;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

public class Keyboard {

    public Keyboard(Window window) {
        win = window;
        keys = new HashMap<Integer, Key>();

        glfwSetKeyCallback(win.getHandle(), (handle, key_code, scancode, action, mods) -> {
            if (handle == win.getHandle() ) {
                Key key = getKey(key_code);
                switch (action) {
                    case GLFW_PRESS:
                        key.pressed = true;
                        key.down = false;
                        key.released = false;
                        break;

                    case GLFW_REPEAT:
                        key.pressed = false;
                        key.down = true;
                        key.released = false;
                        break;

                    case GLFW_RELEASE:
                        key.pressed = false;
                        key.down = false;
                        key.released = true;
                        break;
                }
            }
        });
    }


    public Key getKey(int key_code) {
        if ( keys.containsKey(key_code) ) { return keys.get(key_code); }
        else {
            Key nk = new Key();
            keys.put(key_code, nk);
            return nk;
        }
    }

    public void update() {
        for (Map.Entry<Integer,Key> entry : keys.entrySet()) {
            Key key = entry.getValue();
            if (key.pressed) { key.pressed = false; key.down = true; key.released = false; }
            if (key.released) { key.pressed = false; key.down = false; key.released = false; }
        }
    }

    Map<Integer, Key> keys;
    Window win;

    public class Key {
        private Key() {
            pressed = false;
            released = false;
            down = false;
        }

        public boolean pressed;
        public boolean released;
        public boolean down;
    }
}