package io.github.ingvarjackal.wars.engine;

public class Player {
    public final boolean furries;
    public final String name;

    public boolean offensive;
    public int peasants; // 0..100
    public int infantry; // 0..100
    public int heavyInfantry; // 0..100
    public int archers; // 0..100
    public int chivalry; // 0..100

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return name != null ? name.equals(player.name) : player.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
