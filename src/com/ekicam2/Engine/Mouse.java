package com.ekicam2.Engine;

import org.joml.Vector2i;

public class Mouse {
    private Vector2i CurrentCursorPos;

    private Vector2i CursorPressedPos;
    private Vector2i CursorReleasedPos;

    private boolean bLeftPressed = false;
    private boolean bRightPressed = false;

    public void SetCursorPressedPos(Vector2i InCursorPressedPos) {
        CursorPressedPos = InCursorPressedPos;
    }
    public Vector2i GetCursorPressedPos() { return CursorPressedPos; }

    public void SetCursorReleasedPos(Vector2i cursorReleasedPos) { CursorReleasedPos = cursorReleasedPos; }
    public Vector2i GetCursorReleasedPos() { return CursorReleasedPos; }

    public void SetLeftButtonState(boolean Pressed) { bLeftPressed = Pressed; }
    public void SetRightButtonState(boolean Pressed) { bRightPressed = Pressed; }
}
