package com.ekicam2.Engine.Editor;

import com.ekicam2.Engine.Engine;
import com.ekicam2.Engine.InputHandler;

public class Editor {
    private Engine Engine;
    private ObjectPicker ScenePicker;
    private InputHandler InputHandler;


    public Editor(Engine InEngine) {
        Engine = InEngine;
        InputHandler = new EditorInputHandler(Engine);
        ScenePicker = new ObjectPicker(Engine);
    }

    public InputHandler GetInputHandler() { return InputHandler; }
    public ObjectPicker GetScenePicker() { return ScenePicker; }
}
