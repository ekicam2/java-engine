package com.ekicam2.Engine.Rendering;

import com.ekicam2.Engine.Components.Camera;
import com.ekicam2.Engine.Components.Model;

import java.util.ArrayList;
import java.util.List;

class Scene {
    //TODO: split object from it's graphics interpretation
    private List<Model> Models = new ArrayList<>();
    private List<Camera> Cameras = new ArrayList<>();
    private int CurrentCameraIndex = 0;

    public void Update(float DeltaTime) {

    }

    public void Free() {
        Models.forEach((model -> model.Free()));
    }

    public int AddCamera(Camera CameraToAdd) {
        Cameras.add(CameraToAdd);
        return Cameras.size() - 1;
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

    public void AddModel(Model ModelToAdd) {
        Models.add(ModelToAdd);
    }

    public List<Model> GetModels() {
        return Models;
    }
}
