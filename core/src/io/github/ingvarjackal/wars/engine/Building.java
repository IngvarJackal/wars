package io.github.ingvarjackal.wars.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Building {
    protected int x;
    protected int y;
    protected Player owner;

    public Building() {

    }

    public final int x() {
        return x;
    }

    public final int y() {
        return y;
    }

    public final Player owner() {
        return owner;
    }

    public final void owner(Player owner) {
        this.owner = owner;
    }

    public abstract void draw(SpriteBatch spriteBatch);
}
