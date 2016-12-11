package io.github.ingvarjackal.wars.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Building {
    int x();
    int y();
    void draw(SpriteBatch spriteBatch);
    Player owner();
    void owner(Player owner);
}
