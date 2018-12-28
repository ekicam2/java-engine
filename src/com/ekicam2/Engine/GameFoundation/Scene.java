package com.ekicam2.Engine.GameFoundation;

import com.ekicam2.Engine.GameFoundation.Components.Camera;
import com.ekicam2.Engine.GameFoundation.Objects.DrawableObject;
import com.ekicam2.Engine.GameFoundation.Objects.GameObject;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private List<DrawableObject> GameObjects = new ArrayList<>();
    private List<Camera> Cameras = new ArrayList<>();
    private int CurrentCameraIndex = 0;

    public void Update(float DeltaTime) {
        for(var Object : GameObjects) {
            Object.Update(DeltaTime);
        }
    }

    public int AddCamera(Camera CameraToAdd) {
        Cameras.add(CameraToAdd);
        return Cameras.size() - 1;
    }

    public void AddDrawable(DrawableObject DrawableToAdd) {
        GameObjects.add(DrawableToAdd);
    }

    public List<DrawableObject> GetDrawables() {
        return GameObjects;
    }

    public void SetActiveCamera(int CameraIndex) {
        CurrentCameraIndex = CameraIndex;
    }

    public Camera GetCurrentCamera() {
        if(CurrentCameraIndex >= 0 && CurrentCameraIndex < Cameras.size()) {
            return Cameras.get(CurrentCameraIndex);
        }

        return null;
    }
}
