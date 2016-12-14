package io.github.ingvarjackal.wars.furries;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.Unit;

public class Arbalester extends Unit {
    public final static int cost = 8;

    private int hp = 2;

    protected Arbalester(Player player) {
        super(player);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {

    }

    @Override
    public int hp() {
        return hp;
    }

    @Override
    public int cost() {
        return cost;
    }

    @Override
    public float evasion() {
        return 0.3f;
    }

    @Override
    public int str() {
        return 1;
    }

    @Override
    public int redHp(int damage) {
        hp -= damage;
        return hp;
    }
}
