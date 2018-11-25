package com.ekicam2.Engine.Resources;

abstract class IResource<T> {
    String Name = new String();
    String Path = new String();


    T LoadFromFile(String InPath) {
        Path = InPath;

        return null;
    }

    public abstract void Free();
}
