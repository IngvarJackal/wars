package io.github.ingvarjackal.wars.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Unit {
    protected int x;
    protected int y;
    protected final Player player;

    protected Unit(Player player) {
        this.player = player;
    }

    abstract public void draw(SpriteBatch spriteBatch);
    abstract public int hp();
    abstract public int cost();
    abstract public float evasion(); // 0..1
    abstract public int str();
    abstract public int redHp(int damage);

    final public Player player() {
        return player;
    }

    final public int x() {
        return x;
    }

    final public int y() {
        return y;
    }

    final public void x(int x) {
        this.x = x;
    }

    final public void y(int y) {
        this.y = y;
    }
}
