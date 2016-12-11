package io.github.ingvarjackal.wars.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface Unit {
    void draw(SpriteBatch spriteBatch);
    int hp();
    int cost();
    float evasion(); // 0..1
    Player player();
    int x();
    int y();
    void x(int x);
    void y(int y);
    int str();
    void move(int x, int y);
    int redHp(int damage);
}
