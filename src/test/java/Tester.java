import SGE.Renderer.*;
import SGE.User.User;
import SGE.User.Output.Window;
import VoxelGameEngine.Chunk;
import VoxelGameEngine.VVMesh;
import org.joml.Vector2d;
import org.lwjgl.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;


public class Tester {

    //basic io
    private User user;

    //render
    private ShaderProgram sprog;
    private Camera cam;

    //object
    private Chunk chunk;

    Texture texture;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        sprog.terminate();
        user.terminate();

        // Terminate GLFW and free the error callback
        glfwTerminate();
    }

    private void init() {
        if ( !glfwInit() ) { throw new IllegalStateException("Unable to initialize GLFW"); }

        try {
            Window win = new Window("Test", 1200, 1200);
            user = new User(win);

            sprog = new ShaderProgram();

            String assets = "/home/mason/IdeaProjects/Test/src/main/assets/";
            sprog.add( new Shader(assets + "Shaders/vertexVV.glsl", GL_VERTEX_SHADER) );
            sprog.add( new Shader(assets + "Shaders/fragmentVV.glsl", GL_FRAGMENT_SHADER) );

            sprog.link();

            sprog.createUniform("cam_mat");
           // sprog.createUniform("trans_mat");

            sprog.createUniform("texture_sampler");


            cam = new Camera(win);
            cam.getTrans().setPosition(0, 0, 0);

            texture = new Texture(assets + "Textures/grassblock.png");

            chunk = new Chunk(texture);
            System.out.println("s");
            chunk.createMesh();
            System.out.println("e");

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

                //output

                sprog.bind();

                sprog.setUniform("cam_mat", cam.getCameraMatrix());

                sprog.setUniform("texture_sampler", 0);

                chunk.mesh.render();

                sprog.unbind();

                user.window.display();

                last = glfwGetTime();

                //input

                user.updateInput();
            }
        }
    }

    public static void main(String[] args) { new Tester().run(); }

}