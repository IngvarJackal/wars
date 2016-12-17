package io.github.ingvarjackal.wars.furries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.Unit;

public class Arbalester extends Unit {
    public final static int cost = 8;

    private int hp = 2;

    public Arbalester(Player player) {
        super(player);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw((Texture) WarsServer.assetManager.get("arbalester.png"), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
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
