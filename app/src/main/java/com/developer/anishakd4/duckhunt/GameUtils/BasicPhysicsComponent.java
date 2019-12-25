package com.developer.anishakd4.duckhunt.GameUtils;

import com.developer.anishakd4.duckhunt.Utils.Vector2D;

public class BasicPhysicsComponent {

    public Vector2D forward;

    public BasicPhysicsComponent() {
        this( new Vector2D(0.0f, 0.0f));
    }

    public BasicPhysicsComponent(Vector2D forward) {
        this.forward = forward;
    }

    public void setForward(Vector2D forward) {
        this.forward = forward;
    }

    public Vector2D getForward() {
        return forward;
    }

    public void update(GameObject gameObject) {
        updatePosition(gameObject);
    }

    private void updatePosition(GameObject gameObject) {
        gameObject.position.add(forward);
    }
}
