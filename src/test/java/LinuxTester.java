import SGE.User.Assets.Asset;
import SGE.User.Assets.AssetManager;
import SGE.Renderer.*;
import SGE.User.User;
import SGE.User.Output.Window;
import VGE.Vorld;
import org.joml.Vector2d;
import org.lwjgl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;


public class LinuxTester {

    //basic io
    private User user;

    //render
    private Camera cam;

    //object
    private Vorld world;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        user.terminate();

        // Terminate GLFW and free the error callback
        glfwTerminate();
    }

    private void init() {
        if ( !glfwInit() ) { throw new IllegalStateException("Unable to initialize GLFW"); }

        try {
            Window win = new Window("Test", 1200, 1200);
            AssetManager am = new AssetManager("/home/mason/IdeaProjects/Test/src/main/assets/");
            user = new User(win, am);

            user.assetManager.load("Shaders/vertexVV.glsl", new Asset.Shader(GL_VERTEX_SHADER));
            user.assetManager.load("Shaders/fragmentVV.glsl", new Asset.Shader(GL_FRAGMENT_SHADER));
            user.assetManager.load("Textures/grassblock.png", new Asset.Texture());

            ShaderProgram sprog = new ShaderProgram();

            Asset.Shader shad = (Asset.Shader) user.assetManager.get("Shaders/vertexVV.glsl");
            System.out.println(user.assetManager.assets);
            sprog.add( (Asset.Shader) user.assetManager.get("Shaders/vertexVV.glsl") );
            sprog.add( (Asset.Shader) user.assetManager.get("Shaders/fragmentVV.glsl") );

            sprog.link();

            sprog.createUniform("cam_mat");
            sprog.createUniform("chunk_pos");
            sprog.createUniform("texture_sampler");

            world = new Vorld(sprog, (Asset.Texture)user.assetManager.get("Textures/grassblock.png"));

            world.loadChunk(0, 0, 1);
            world.loadChunk(1, 0, 1);
            world.loadChunk(0, 1, 1);
            world.loadChunk(1, 1, 1);

            world.loadChunk(0, 0, 0);
            world.loadChunk(1, 0, 0);
            world.loadChunk(0, 1, 0);
            world.loadChunk(1, 1, 0);

            cam = new Camera(win);
            cam.getTrans().setPosition(0, 0, 0);

        } catch (Exception e) { e.printStackTrace(); }

    }

    private void loop() {

        double last = glfwGetTime();
        double delta;
        double fps = 1 / 60.0;

        user.mouse.setPos(600, 600);


        while ( !user.keyboard.getKey(GLFW_KEY_ESCAPE).released ) {

            delta = glfwGetTime() - last;

            if ( delta > fps ) {
                user.window.clear(0.0f, 0.0f, 0.0f);

                //update

                Vector2d md = user.mouse.getPos();

                float sensitivity = (float)Math.PI / 500.0f;
                cam.trans.rotate((float)(md.y - 600) * sensitivity,(float)(md.x - 600) * sensitivity, 0 );

                user.mouse.setPos(600, 600);

                if (user.keyboard.getKey(GLFW_KEY_W).down) {
                    float rot = cam.trans.rotation.y - (float)(Math.PI / 2);
                    cam.trans.move((float)(Math.cos(rot) * 10 * delta), 0, (float)(Math.sin(rot) * 10 * delta));

                }
                if (user.keyboard.getKey(GLFW_KEY_S).down) {
                    float rot = cam.trans.rotation.y + (float)(Math.PI / 2);
                    cam.trans.move((float)(Math.cos(rot) * 10 * delta), 0, (float)(Math.sin(rot) * 10 * delta));
                }

                if (user.keyboard.getKey(GLFW_KEY_D).down) {
                    float rot = cam.trans.rotation.y;
                    cam.trans.move((float)(Math.cos(rot) * 10 * delta), 0, (float)(Math.sin(rot) * 10 * delta));
                }
                if (user.keyboard.getKey(GLFW_KEY_A).down) {
                    float rot = cam.trans.rotation.y - (float)Math.PI;
                    cam.trans.move((float)(Math.cos(rot) * 10 * delta), 0, (float)(Math.sin(rot) * 10 * delta));
                }

                if (user.keyboard.getKey(GLFW_KEY_SPACE).down) {
                    cam.trans.move(0, 10 * (float)delta, 0);
                }
                if (user.keyboard.getKey(GLFW_KEY_C).down) {
                    cam.trans.move(0, -10 * (float)delta, 0);
                }

                int xp = (int)cam.trans.position.x / 31;
                int yp = (int)cam.trans.position.y / 31;
                int zp = (int)cam.trans.position.z / 31;

                int[][] diffs = { {0, 0, 0} };

                //output

                world.render(cam);

                user.window.display();

                last = glfwGetTime();

                //input

                user.updateInput();
            }
        }
    }

    public static void main(String[] args) { new LinuxTester().run(); }
}
