package io.github.ingvarjackal.wars.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Building;
import io.github.ingvarjackal.wars.engine.Player;

public class Capital extends Building {
    public int hp = 10;

    public Capital(Player owner, int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (owner.furries) spriteBatch.draw(WarsServer.assetManager.get("furrycapital.png", Texture.class), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
        else spriteBatch.draw(WarsServer.assetManager.get("capital.png", Texture.class), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
    }
}
