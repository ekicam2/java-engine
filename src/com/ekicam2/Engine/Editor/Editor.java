package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.Inputs.IInputHandler;
import com.ekicam2.Engine.GameFoundation.Objects.GameObject;

public class Editor {
    private Engine Engine;
    public Engine GetEngine() { return Engine; }

    ObjectPicker ScenePicker;
    private IInputHandler IInputHandler;
    //temp here
    private GameObject CurrentSelectedModel = null;
    public GameObject GetSelectedObject() {
        return CurrentSelectedModel;
    }

    public Editor(Engine InEngine) {
        Engine = InEngine;
        IInputHandler = new EditorIInputHandler(Engine);
        ((EditorIInputHandler) IInputHandler).Editor = this;
        //ScenePicker = new ObjectPicker(this);
    }

    private void SelectObject(int ObjectID) {
        if(ObjectID != 0){

        } else {
            CurrentSelectedModel = null;
        }
    }

    public IInputHandler GetInputHandler() { return IInputHandler; }
}
