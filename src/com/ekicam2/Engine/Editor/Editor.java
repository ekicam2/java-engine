package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.IInputHandler;
import com.ekicam2.Engine.Rendering.Model;

public class Editor {
    private Engine Engine;
    private ObjectPicker ScenePicker;
    private IInputHandler IInputHandler;
    //temp here
    private Model CurrentSelectedModel = null;
    public Model GetSelectedObject() {
        return CurrentSelectedModel;
    }

    public Editor(Engine InEngine) {
        Engine = InEngine;
        IInputHandler = new EditorIInputHandler(Engine);
        ScenePicker = new ObjectPicker(Engine);
    }

    public void Update(float DeltaTime) {
    }

    public void SelectObject(int ObjectID) {
        if(ObjectID != 0){
            CurrentSelectedModel = Engine.GetCurrentScene().GetModels().get(0);
        } else {
            CurrentSelectedModel = null;
        }
    }

    public IInputHandler GetInputHandler() { return IInputHandler; }
    public ObjectPicker GetScenePicker() { return ScenePicker; }
}
