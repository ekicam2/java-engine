package com.ekicam2;

import org.joml.*;

public class Transform {
    public final static Vector3f UP = new Vector3f(0.0f, 0.0f, 1.0f);
    public final static Vector3f RIGHT = new Vector3f(0.0f, 1.0f, 0.0f);
    public final static Vector3f FORWARD = new Vector3f(1.0f, 0.0f, 0.0f);

    //TODO: scale maybe :P
    private Vector3f Position = new Vector3f();
    private Quaternionf Rotation = new Quaternionf();
    private boolean bDirty = true;

    private Matrix4f Model = new Matrix4f();

    public void SetPosition(Vector3f InPosition) { Position = InPosition; }
    public Vector3f GetPosition() { return Position; }

    public void SetRotation(Vector3f InRotation){
        var X = new Quaternionf().fromAxisAngleDeg(UP, InRotation.x);
        var Y = new Quaternionf().fromAxisAngleDeg(RIGHT, InRotation.y);
        var Z = new Quaternionf().fromAxisAngleDeg(FORWARD, InRotation.z);

        Rotation = Z.mul(Y).mul(X);
        bDirty = true;
    }
    public Quaternionf GetRotation() { return Rotation; }

    public Matrix4f GetModel() {
        if(bDirty) {
            Recalculate();
        }

        return Model;
    }

    protected void Recalculate() {
        Model.transform(new Vector4f(Position, 1.0f)).rotate(Rotation);
        bDirty = false;
    }

}
