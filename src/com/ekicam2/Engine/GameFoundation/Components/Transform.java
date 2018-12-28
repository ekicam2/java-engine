package com.ekicam2.Engine.GameFoundation.Components;

import org.joml.*;

import java.lang.Math;

public class Transform {
    public final static Vector3f RIGHT = new Vector3f(1.0f, 0.0f, 0.0f);
    public final static Vector3f UP = new Vector3f(0.0f, 1.0f, 0.0f);
    public final static Vector3f FORWARD = new Vector3f(0.0f, 0.0f, 1.0f);

    private Matrix4f Model = new Matrix4f();

    private Vector3f Position = new Vector3f();
    // had to introduce this value because of strange bug around 89.98 deg
    // while using RotateBy on Y axis
    private Vector3f RotationHelper = new Vector3f();
    private Quaternionf Rotation = new Quaternionf();

    private boolean bDirty = false;


    public void SetPosition(Vector3f InPosition) { Position = InPosition; bDirty = true; }
    public Vector3f GetPosition() { return Position; }

    public void Translate(Vector3f InOffset) {
        Position.x += InOffset.x;
        Position.y += InOffset.y;
        Position.z += InOffset.z;

        bDirty = true;
    }

    public void SetRotation(Vector3f InRotation){
        var X = new Quaternionf().fromAxisAngleDeg(RIGHT, InRotation.x);
        var Y = new Quaternionf().fromAxisAngleDeg(UP, InRotation.y);
        var Z = new Quaternionf().fromAxisAngleDeg(FORWARD, InRotation.z);

        RotationHelper = InRotation;

        Rotation = Z.premul(Y).premul(X);
        bDirty = true;
    }

    public Vector3f GetRotation() {
        Vector3f RotationToReturn = new Vector3f();
        Rotation.getEulerAnglesXYZ(RotationToReturn);

        RotationToReturn.x = (float) Math.toDegrees(RotationToReturn.x);
        RotationToReturn.y = (float) Math.toDegrees(RotationToReturn.y);
        RotationToReturn.z = (float) Math.toDegrees(RotationToReturn.z);

        return RotationToReturn;
    }

    public void RotateBy(Vector3f RotationAngles) {
        RotationHelper.x += RotationAngles.x;
        RotationHelper.y += RotationAngles.y;
        RotationHelper.z += RotationAngles.z;
        SetRotation(RotationHelper);
    }

    public Matrix4f GetModel() {
        if(bDirty) {
            Recalculate();
        }

        return Model;
    }

    protected void Recalculate() {
        Model.setTranslation(Position);

        Vector3f RotationEuler = new Vector3f();
        Rotation.getEulerAnglesXYZ(RotationEuler);
        Model.setRotationXYZ(RotationEuler.x, RotationEuler.y, RotationEuler.z);

        bDirty = false;
    }

    public Vector3f GetForwardVector() {
        var Forward = Transform.FORWARD;

        Forward.rotateX((float)Math.toRadians(GetRotation().x));
        Forward.rotateY((float)Math.toRadians(GetRotation().y));
        Forward.rotateZ((float)Math.toRadians(GetRotation().z));

        return Forward.normalize();
    }

}
