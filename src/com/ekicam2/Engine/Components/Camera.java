package com.ekicam2.Engine.Components;

import com.ekicam2.Engine.Components.Transform;
import org.joml.Matrix4f;

public class Camera {
    private com.ekicam2.Engine.Components.Transform Transform = new Transform();

    Matrix4f View = new Matrix4f()
            .lookAt(0.0f, 0.0f, 0.0f,
                    0.0f, 0.0f, 150.0f,
                    0.0f, 1.0f, 0.0f);
    Matrix4f Proj = new Matrix4f().perspective((float) Math.toRadians(90.0f), 1.0f, 1.01f, 1000.0f);

    public Matrix4f GetViewProjection() {
        Matrix4f Returner = new Matrix4f();
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
