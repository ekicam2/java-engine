package com.ekicam2.Engine.Inputs;

import org.joml.Vector2i;

public class Mouse {
    private class ButtonState {
        private Mouse Owner = null;

        private boolean bPressed;
        private boolean bJustPressed;

        private boolean bCanCleanJustPressed;
        private Vector2i PressedPos;
        private boolean bJustReleased;

        private boolean bCanCleanJustReleased;
        private Vector2i ReleasedPos;

        ButtonState(Mouse InOwner) { Owner = InOwner; }

        public void SetPressed() { bPressed = true; bJustPressed = true; PressedPos = Owner.GetCursorPos(); }
        public void SetReleased() { bPressed = false; bJustReleased = true; ReleasedPos = Owner.GetCursorPos(); }

        public boolean IsPressed() { return bPressed; }
        public boolean JustPressed() { return bJustPressed; }
        public boolean JustReleased() { return bJustReleased; }

        public Vector2i GetPressedPos() { return PressedPos; }
        public Vector2i GetReleasedPos() { return ReleasedPos; }

        void Update() {
            if(bCanCleanJustPressed) {
                bJustPressed = false;
                bCanCleanJustPressed = false;
            }

            if(bCanCleanJustReleased) {
                bJustReleased = false;
                bCanCleanJustReleased = false;
            }

            if(bJustPressed) {
                bCanCleanJustPressed = true;
            }

            if(bJustReleased) {
                bCanCleanJustReleased = true;
            }
        }
    }

    private Vector2i CursorPos;

    private ButtonState Left = new ButtonState(this);
    private ButtonState Right = new ButtonState(this);

    public void SetCursorPos(Vector2i InCursorPos) { CursorPos = InCursorPos; }
    public Vector2i GetCursorPos() { return CursorPos; }

    public void SetLeftButtonState(boolean Pressed) {
        if(Pressed) {
            Left.SetPressed();
        } else {
            Left.SetReleased();
        }
    }
    public boolean IsLeftPressed() { return Left.IsPressed(); }
    public boolean IsLeftJustPressed() { return Left.JustPressed(); }
    public boolean IsLeftJustReleased() { return Left.JustReleased(); }

    public void SetRightButtonState(boolean Pressed) {
        if (Pressed) {
            Right.SetPressed();
        } else {
            Right.SetReleased();
        }
    }
    public boolean IsRightPressed() { return Right.IsPressed(); }
    public boolean IsRightJustPressed() { return Right.JustPressed(); }
    public boolean IsRightJustReleased() { return Right.JustReleased(); }

    public void Update(float DeltaTime) {
        Left.Update();
        Right.Update();
    }
}
