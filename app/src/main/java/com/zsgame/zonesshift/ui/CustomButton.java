package com.zsgame.zonesshift.ui;


import android.graphics.RectF;
import android.view.MotionEvent;

public class CustomButton {

    private final RectF hitbox;

    private boolean pushed;

    public CustomButton(float x, float y, float width, float height) {
        hitbox = new RectF(x, y, x + width, y + height);
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public boolean isPushed() {
        return pushed;
    }

    public void setPushed(boolean pushed) {
        this.pushed = pushed;
    }

    public boolean isIn(MotionEvent e) {
        return getHitbox().contains(e.getX(), e.getY());
    }
}
