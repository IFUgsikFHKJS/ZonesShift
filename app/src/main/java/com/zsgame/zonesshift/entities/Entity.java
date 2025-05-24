package com.zsgame.zonesshift.entities;

import android.graphics.PointF;
import android.graphics.RectF;

public abstract class Entity {

    protected RectF hitbox;
    protected int hitboxDirection = 0;

    public Entity(PointF pos, float width, float height){
        this.hitbox = new RectF(pos.x, pos.y, width, height);
    }

    public RectF getHitbox() {
        return hitbox;
    }

    public void turnHitbox(int direction) {
        // Get the center point of the hitbox
        float centerX = hitbox.centerX();
        float centerY = hitbox.centerY();

        switch (direction) {
            case 0: // Gravity pointing down
                float newWidth;
                float newHeight;

                if(hitboxDirection == 2 || hitboxDirection == 3){
                    newWidth = hitbox.height();
                    newHeight = hitbox.width();
                } else{
                    newWidth = hitbox.width();
                    newHeight = hitbox.height();
                }

                // Set hitbox dimensions as they are (no rotation)
                hitbox.set(centerX - newWidth / 2, centerY - newHeight / 2,
                        centerX + newWidth / 2, centerY + newHeight / 2);
                break;
            case 1: // Gravity pointing up
                // Set hitbox dimensions as they are (no rotation)
                hitbox.set(centerX - hitbox.width() / 2, centerY - hitbox.height() / 2,
                        centerX + hitbox.width() / 2, centerY + hitbox.height() / 2);
                break;
            case 2: // Gravity pointing right (90 degrees rotation counterclockwise)
                // Swap width and height and set new hitbox
                newWidth = hitbox.height();
                newHeight = hitbox.width();
                hitbox.set(centerX - newWidth / 2, centerY - newHeight / 2,
                        centerX + newWidth / 2, centerY + newHeight / 2);
                break;
            case 3: // Gravity pointing left (270 degrees rotation counterclockwise)
                // Swap width and height and set new hitbox
                float newWidthLeft = hitbox.height();
                float newHeightLeft = hitbox.width();
                hitbox.set(centerX - newWidthLeft / 2, centerY - newHeightLeft / 2,
                        centerX + newWidthLeft / 2, centerY + newHeightLeft / 2);
                break;
            default:
                // Handle invalid direction if necessary (e.g., do nothing)
                return;

        }

        hitboxDirection = direction;


    }
}
