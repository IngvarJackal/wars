package io.github.ingvarjackal.wars.humans;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.Unit;

public class Peasant extends Unit {
    public final static int cost = 11;

    private int hp = 2;

    protected Peasant(Player player) {
        super(player);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw((Texture) WarsServer.assetManager.get("archer.png"), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
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
        return 0.1f;
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
