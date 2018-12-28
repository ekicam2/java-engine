package com.ekicam2.Engine.GameFoundation.Objects;

import com.ekicam2.Engine.GameFoundation.Components.Drawable;
import com.ekicam2.Engine.GameFoundation.Components.Transform;

public class DrawableObject extends GameObject {
    public DrawableObject() {
        super((int)System.currentTimeMillis());
    }

    public Drawable DrawableComponent = new Drawable();
    public Transform Transform = new Transform();
}
