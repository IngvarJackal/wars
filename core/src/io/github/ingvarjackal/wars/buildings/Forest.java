package io.github.ingvarjackal.wars.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Building;
import io.github.ingvarjackal.wars.engine.Player;

public class Forest extends Building {
    public Forest(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(WarsServer.assetManager.get("forest.png", Texture.class), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
    }
}
