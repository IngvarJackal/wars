package io.github.ingvarjackal.wars;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.buildings.*;
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
        assetManager.load("forest.png", Texture.class);
        assetManager.load("river.png", Texture.class);
        assetManager.load("plain.png", Texture.class);
        assetManager.load("mountain.png", Texture.class);
        assetManager.load("were.png", Texture.class);
        assetManager.load("griffon.png", Texture.class);
        assetManager.finishLoading();
        human = new Player(false, "Human");
        furry = new Player(true, "Furry");
        human.infantry = 30;
        human.archers = 20;
        human.chivalry = 5;
        human.peasants = 2;
        furry.infantry = 30;
        furry.archers = 20;
        furry.chivalry = 5;
        furry.peasants = 2;
        world = new World(readBuildings(
                 ".....t...........rr...........\n" +
                        ".1...........c....r....t......\n" +
                        "........t..t..................\n" +
                        "t....t..t.............mmtm.f..\n" +
                        "..f....t..t.....r.......mmm...\n" +
                        "......t..tt....rrr...........m\n" +
                        "..t.t.....t...r.rr..........m.\n" +
                        ".......t..............t.......\n" +
                        ".............c......t...t.....\n" +
                        ".tm.mmm...rrr...ttt..t...t.t..\n" +
                        "m.tm.m....rrrr...t.t..t....f..\n" +
                        "....m....r......t.t.t...t.....\n" +
                        "...t..........................\n" +
                        ".f...r..........c...........2.\n" +
                        "....t.rr............t.....t..."));
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

	private Building[][] readBuildings(String input) {
        String[] lines = input.split("\n");
        Building[][] buildings = new Building[lines[0].length()][lines.length];
        for (int i = 0; i < lines.length; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                switch (lines[i].charAt(j)) {
                    case '.': { // Plain
                        buildings[j][i] = new Plain(j, i); break;
                    } case '1': { // Human player spawn
                        buildings[j][i] = new Capital(human, j, i); break;
                    } case '2': { // Furry player spawn
                        buildings[j][i] = new Capital(furry, j, i); break;
                    } case 't': { // Forest
                        buildings[j][i] = new Forest(j, i); break;
                    } case 'm': { // Mountain
                        buildings[j][i] = new Mountain(j, i); break;
                    } case 'r': { // River
                        buildings[j][i] = new River(j, i); break;
                    } case 'c': { // Castle
                        buildings[j][i] = new Castle(null, j, i); break;
                    } case 'f': { // Farm
                        buildings[j][i] = new Farm(null, j, i); break;
                    }
                }
            }
        }
        return buildings;
    }
}
