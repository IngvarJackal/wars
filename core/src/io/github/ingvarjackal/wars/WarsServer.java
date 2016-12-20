package io.github.ingvarjackal.wars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.buildings.Capital;
import io.github.ingvarjackal.wars.buildings.Castle;
import io.github.ingvarjackal.wars.buildings.Farm;
import io.github.ingvarjackal.wars.engine.Building;
import io.github.ingvarjackal.wars.engine.Player;
import io.github.ingvarjackal.wars.engine.World;

public class WarsServer extends ApplicationAdapter {
    private final static float FREQ = 0.01f; // sec

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
        assetManager.load("infantryfur.png", Texture.class);
        assetManager.load("arbalester.png", Texture.class);
        assetManager.load("archer.png", Texture.class);
        assetManager.load("castle.png", Texture.class);
        assetManager.load("furrycastle.png", Texture.class);
        assetManager.load("humancastle.png", Texture.class);
        assetManager.load("graizer.png", Texture.class);
        assetManager.load("peasant.png", Texture.class);
        assetManager.load("farm.png", Texture.class);
        assetManager.load("humanfarm.png", Texture.class);
        assetManager.load("furryfarm.png", Texture.class);
        assetManager.finishLoading();
        human = new Player(false, "Human");
        furry = new Player(true, "Furry");
        human.infantry = 10;
        human.archers = 5;
        human.peasants = 2;
        furry.infantry = 10;
        furry.archers = 5;
        furry.peasants = 2;
        world = new World(new Building[][]{
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, new Capital(human,1, 1), null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null, null ,null,null,null,null,null,new Castle(null, 16 ,2),null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,new Farm(null, 17, 6),null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, new Farm(null, 3, 15), null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, new Castle(null, 2 ,16), null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,new Capital(furry, 18, 17),null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null},
                {null, null, null, null, null, null, null, null, null, null,null,null,null,null,null,null,null,null,null,null}
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
