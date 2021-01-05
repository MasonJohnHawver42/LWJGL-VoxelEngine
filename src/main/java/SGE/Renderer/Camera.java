package SGE.Renderer;

import SGE.User.Output.Window;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    public float fov;
    public float aspect;

    public float zNear;
    public float zFar;

    public final Transform trans;

    public Camera(Window win) {
        fov = (float)Math.toRadians(90.0);
        aspect = win.getAspectRatio();

        zNear = 0.1f;
        zFar = 100f;

        trans = new Transform();
    }

    public Transform getTrans() { return trans; }

    public Matrix4f getViewMatrix() {
        Vector3f pos = trans.getPosition();
        Vector3f rot = trans.getRotation();

        Matrix4f view = new Matrix4f().identity();
        view.rotate(rot.x, new Vector3f(1, 0, 0))
                .rotate(rot.y, new Vector3f(0, 1, 0));
        view.translate(-pos.x, -pos.y, -pos.z);

        return view;
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().setPerspective(fov, aspect, zNear, zFar);
    }

    public Matrix4f getCameraMatrix() { return getProjectionMatrix().mul(getViewMatrix()); }
}
