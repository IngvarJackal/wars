package io.github.ingvarjackal.wars.humans;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.Unit;

public class Swordsman implements Unit {

    public final static int cost = 10;

    private int hp = 4;
    private int x;
    private int y;
    private final Player player;

    public Swordsman(Player player) {
        this.player = player;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw((Texture) WarsServer.assetManager.get("swordsman.png"), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
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
        return 0.2f;
    }

    @Override
    public Player player() {
        return player;
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    @Override
    public void x(int x) {
        this.x = x;
    }

    @Override
    public void y(int y) {
        this.y = y;
    }

    @Override
    public int str() {
        return 2;
    }

    @Override
    public void move(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int redHp(int damage) {
        hp -= damage;
        return hp;
    }
}
