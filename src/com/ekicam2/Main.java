package com.ekicam2;

import com.ekicam2.Engine.Engine;

public class Main {

    public static void main(String[] args) {
        Engine Engine = new Engine();
        if(Engine.Init())
        {
            Engine.Run();

            Engine.Terminate();
        }
    }
}