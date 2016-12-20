package io.github.ingvarjackal.wars.buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.WarsServer;
import io.github.ingvarjackal.wars.engine.Building;
import io.github.ingvarjackal.wars.engine.Player;

public class Farm extends Building{
    public Farm(Player owner, int x, int y) {
        this.x = x;
        this.y = y;
        this.owner = owner;
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (owner == null) {
            spriteBatch.draw(WarsServer.assetManager.get("farm.png", Texture.class), (x+1)*17, Gdx.graphics.getHeight()-(y+1)*17);
        } else {
            if (owner.furries)
                spriteBatch.draw(WarsServer.assetManager.get("furryfarm.png", Texture.class), (x + 1) * 17, Gdx.graphics.getHeight() - (y + 1) * 17);
            else
                spriteBatch.draw(WarsServer.assetManager.get("humanfarm.png", Texture.class), (x + 1) * 17, Gdx.graphics.getHeight() - (y + 1) * 17);
        }
    }
}
