package io.github.ingvarjackal.wars.engine;

public class Player {
    public final boolean furries;
    public final String name;

    public boolean offensive;
    public int peasants; // 0..100
    public int infantry; // 0..100
    public int heavyInfantry; // 0..100
    public int archers; // 0..100
    public int cavalary; // 0..100

    public int peasantsFlags;
    public int infantryFlags;
    public int heavyInfantryFlags;
    public int archersFlags;
    public int cavalaryFlags;

    public int food = 10;

    public Player(boolean furries, String name) {
        this.furries = furries;
        this.name = name;
    }
}
