package io.github.ingvarjackal.wars.furries;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.Unit;

public class Infantryfur extends Unit{
    public final static int cost = 9;

    private int hp = 5;

    public Infantryfur(Player player) {
        super(player);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw((Texture) WarsServer.assetManager.get("infantryfur.png"), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
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
        return 2;
    }

    @Override
    public int redHp(int damage) {
        hp -= damage;
        return hp;
    }
}
