package io.github.ingvarjackal.wars.furries;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.Unit;

public class Graizer extends Unit {
    protected Graizer(Player player) {
        super(player);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {

    }

    @Override
    public int hp() {
        return 0;
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public float evasion() {
        return 0;
    }

    @Override
    public int str() {
        return 0;
    }

    @Override
    public int redHp(int damage) {
        return 0;
    }
}
