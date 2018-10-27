package com.ekicam2;

import org.joml.Matrix4f;

public class Camera {
    private Transform Transform = new Transform();

    Matrix4f View = new Matrix4f()
            .lookAtLH(0.0f, 0.0f, -150.0f,
                    0.0f, 0.0f, 0.0f,
                    0.0f, 1.0f, 0.0f);
    Matrix4f Proj = new Matrix4f().perspectiveLH((float) Math.toRadians(5.0f), 1.0f, 1.01f, 1000.0f);

    Matrix4f GetViewProjection() {
        Matrix4f Returner = new Matrix4f();
        Proj.mul(View, Returner);
        return Returner;
    }
}
