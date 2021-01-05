package SGE.Renderer;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transform {

    public Transform() {

        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);
        scale = 1;

        transform = new Matrix4f();
    }

    public Vector3f getPosition() { return position; }
    public Vector3f getRotation() { return rotation; }

    public Matrix4f getTransform() {
        transform.identity().translate(position).
                rotateX(-rotation.x).
                rotateY(-rotation.y).
                rotateZ(-rotation.z).
                scale(scale);
        return transform;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public void move(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void rotate(float x, float y, float z) {
        rotation.x += x;
        rotation.y += y;
        rotation.z += z;
    }

    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public final Vector3f position, rotation;
    public float scale;

    private final Matrix4f transform;
}
