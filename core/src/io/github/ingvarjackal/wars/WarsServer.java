package io.github.ingvarjackal.wars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.buildings.Capital;
import io.github.ingvarjackal.wars.engine.Building;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.World;

public class WarsServer extends ApplicationAdapter {
    private final static float FREQ = 0.1f; // sec

	public static AssetManager assetManager;
	SpriteBatch batch;
    Player human;
    Player furry;
    World world;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
        assetManager = new AssetManager();
        assetManager.load("capital.png", Texture.class);
        assetManager.load("furrycapital.png", Texture.class);
        assetManager.load("swordsman.png", Texture.class);
        assetManager.finishLoading();
        human = new Player(false, "Human");
        furry = new Player(true, "Furry");
        human.infantry = 10;
        world = new World(new Building[][]{
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, new Capital(furry, 2, 2), null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, new Capital(human, 8, 8), null},
                {null, null, null, null, null, null, null, null, null, null},
        });
	}

    private float timePassed = 0f;
    private void makeStep(float delta) {
        timePassed += delta;
        if (timePassed >= FREQ) {
            timePassed = 0f;
            world.makeStep();
        }
    }

	@Override
	public void render () {
        makeStep(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
        world.draw(batch);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
        assetManager.dispose();
	}
}
