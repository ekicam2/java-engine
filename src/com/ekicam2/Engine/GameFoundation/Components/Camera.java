package com.ekicam2.Engine.GameFoundation.Components;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    public Transform Transform;

    public Camera() {
        View = new Matrix4f().lookAt(0.0f, 0.0f, 0.0f,
    0.0f, 150.0f, 0.0f,
            Transform.UP.x, Transform.UP.y, Transform.UP.z);

        Proj = new Matrix4f().perspective((float) Math.toRadians(90.0f), 1.0f, 1.0f, 1000.0f);
        Transform = new Transform();
    }

    Matrix4f View;
    Matrix4f Proj;

    public Matrix4f GetViewProjection() {
        Matrix4f Returner = new Matrix4f();

        View = new Matrix4f()
                .lookAt(Transform.GetPosition().x, Transform.GetPosition().y, Transform.GetPosition().z,
                        Transform.GetForwardVector().x * 100.0f, Transform.GetForwardVector().y * 100.0f, Transform.GetForwardVector().z * 100.0f,
                        Transform.UP.x, Transform.UP.y, Transform.UP.z);

        View = View.rotate((float)Math.toRadians(Transform.GetRotation().x), new Vector3f(Transform.RIGHT));
        View = View.rotate((float)Math.toRadians(Transform.GetRotation().y), new Vector3f(Transform.FORWARD));
        View = View.rotate((float)Math.toRadians(Transform.GetRotation().z), new Vector3f(Transform.UP));

        Proj.mul(View, Returner);
        return Returner;
    }

    public Matrix4f GetProjectionMatrix() {
        return new Matrix4f(Proj);
    }

    public Matrix4f GetViewMatrix() {
        return new Matrix4f(View);
    }

}
