package io.github.ingvarjackal.wars.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Building;

public class Plain extends Building {
    public Plain(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(WarsServer.assetManager.get("plain.png", Texture.class), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
    }
}
