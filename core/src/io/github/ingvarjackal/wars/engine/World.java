package io.github.ingvarjackal.wars.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.buildings.*;
import io.github.ingvarjackal.wars.furries.*;
import io.github.ingvarjackal.wars.humans.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private final static int GRADIENT_PREFIX = 100;

    private final int XSIZE;
    private final int YSIZE;

    private final Unit[][] units;
    private final Building[][] buildings;

    private final int[][] swordsmanGradient;
    private final int[][] infanrtyfurGradient;

    public World(Building[][] buildings) {
        XSIZE = buildings.length;
        YSIZE = buildings[0].length;
        units = new Unit[XSIZE][YSIZE];
        this.buildings = buildings;
        swordsmanGradient = new int[XSIZE][YSIZE];
        infanrtyfurGradient = new int[XSIZE][YSIZE];;
    }

    public void draw(SpriteBatch spriteBatch) {
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                if (buildings[j][i] != null) buildings[j][i].draw(spriteBatch);
                if (units[j][i] != null) units[j][i].draw(spriteBatch);
            }
        }
    }

    public void makeStep() {
        calculateGradients();
        moveAndAttack();
        feedAndSpawn();
        takeAndConquer();
    }

    /* FEEDING
    */
    private Capital furryPlayerSpawn;
    private Capital humanPlayerSpawn;

    private void feedAndSpawn() {/*
        List<Coordinate> coordinates = new ArrayList<Coordinate>(YSIZE * XSIZE);
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                if (units[j][i] != null) coordinates.add(new Coordinate(j, i));
            }
        }
        Collections.shuffle(coordinates);

        for (Coordinate coordinate : coordinates) {
            Unit unit = units[coordinate.x][coordinate.y];
            unit.player().food -= unit.cost() / 4;
            if (unit.player().food < 0) {
                units[unit.x()][unit.y()] = null;
                unit.player().food = 0;
            }
        }*/

        if (furryPlayerSpawn == null || humanPlayerSpawn == null) {
            for (int i = 0; i < YSIZE; i++) {
                for (int j = 0; j < XSIZE; j++) {
                    if (buildings[j][i] != null) {
                        if (buildings[j][i] instanceof Capital) {
                            if (buildings[j][i].owner().furries) {
                                furryPlayerSpawn = (Capital) buildings[j][i];
                            } else {
                                humanPlayerSpawn = (Capital) buildings[j][i];
                            }
                        }
                    }
                }
            }
        }

        switch (getBiasedRandom(humanPlayerSpawn.owner().archers,
                humanPlayerSpawn.owner().cavalary,
                humanPlayerSpawn.owner().heavyInfantry,
                humanPlayerSpawn.owner().infantry,
                humanPlayerSpawn.owner().peasants,
                10)) {
            case 0: { // archers

            }
            case 1: { // cavalary

            }
            case 2: { // heavyInfantry

            }
            case 3: { // infantry
                if (humanPlayerSpawn.owner().food >= Swordsman.cost) {
                    putClosest(humanPlayerSpawn.x(), humanPlayerSpawn.y(), new Swordsman(humanPlayerSpawn.owner()), units, buildings);
                    humanPlayerSpawn.owner().food -= Swordsman.cost;
                }
            }
            case 4: { // peasants

            }
            case 5: { // nothing
                break;
            }
        }

        switch (getBiasedRandom(furryPlayerSpawn.owner().archers,
                furryPlayerSpawn.owner().cavalary,
                furryPlayerSpawn.owner().heavyInfantry,
                furryPlayerSpawn.owner().infantry,
                furryPlayerSpawn.owner().peasants,
                10)) {
            case 0: { // archers

            }
            case 1: { // cavalary

            }
            case 2: { // heavyInfantry

            }
            case 3: { // infantry
                if (furryPlayerSpawn.owner().food >= Infantryfur.cost) {
                    putClosest(furryPlayerSpawn.x(), furryPlayerSpawn.y(), new Infantryfur(furryPlayerSpawn.owner()), units, buildings);
                    furryPlayerSpawn.owner().food -= Infantryfur.cost;
                }
            }
            case 4: { // peasants

            }
            case 5: { // nothing
                break;
            }
        }
    }


    /* CONQUERING
    */
    private void takeAndConquer() {
        humanPlayerSpawn.owner().food += 15;
        furryPlayerSpawn.owner().food += 15;
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                if (buildings[j][i] != null) {
                    Building building = buildings[j][i];
                    Object furry = findClosest(building.x(), building.y(), 1, Arbalester.class, units);
                    if (furry == null) furry = findClosest(building.x(), building.y(), 1, Halberd.class, units);
                    if (furry == null) furry = findClosest(building.x(), building.y(), 1, Infantryfur.class, units);
                    if (furry == null) furry = findClosest(building.x(), building.y(), 1, Were.class, units);

                    Object human = findClosest(building.x(), building.y(), 1, Swordsman.class, units);
                    if (human == null) human = findClosest(building.x(), building.y(), 1, Archer.class, units);
                    if (human == null) human = findClosest(building.x(), building.y(), 1, Knight.class, units);
                    if (human == null) human = findClosest(building.x(), building.y(), 1, Champion.class, units);
                    if (building instanceof Capital) {
                        if (building.owner().furries && furry == null && human != null) {
                            ((Capital) building).hp -= 1;
                        }
                        if (!building.owner().furries && furry != null && human == null) {
                            ((Capital) building).hp -= 1;
                        }
                    }
                    if (building instanceof Farm || building instanceof Castle) {
                        if (building.owner() == null) {
                            if (furry == null && human != null) {
                                building.owner(humanPlayerSpawn.owner());
                            }
                            if (furry != null && human == null) {
                                building.owner(furryPlayerSpawn.owner());
                            }
                        } else {
                            building.owner().food += 10;
                        }
                    }
                }
                if (units[j][i] != null) {
                    Unit unit = units[j][i];
                    if (unit instanceof Peasant || unit instanceof Graizer) {
                        int bonus = 0;
                        Building farm = (Building) findClosest(unit.x(), unit.y(), 3, Farm.class, buildings);
                        if (farm != null) {
                            if (unit.player().equals(farm.owner())) {
                                bonus += 4;
                            }
                            bonus += 4;
                        }
                        unit.player().food += 4 + bonus;
                    }
                }
            }
        }
    }


    /* MOVEMENT
    */

    private void moveAndAttack() {
        List<Coordinate> coordinates = new ArrayList<Coordinate>(YSIZE * XSIZE);
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                if (units[j][i] != null) coordinates.add(new Coordinate(j, i));
            }
        }
        Collections.shuffle(coordinates);

        for (Coordinate coordinate : coordinates) {
            Unit unit = units[coordinate.x][coordinate.y];
            if (unit instanceof Swordsman) moveInfantry(unit, swordsmanGradient, units, buildings);
            if (unit instanceof Infantryfur) moveInfantry(unit, infanrtyfurGradient, units, buildings);
        }
    }

    private static void moveInfantry(Unit unit, int[][] swordsmanGradient, Unit[][] units, Building[][] buildings) {
        int upScore = getSafe(unit.x(), unit.y() - 1, swordsmanGradient);
        int downScore = getSafe(unit.x(), unit.y() + 1, swordsmanGradient);
        int leftScore = getSafe(unit.x() - 1, unit.y(), swordsmanGradient);
        int rightScore = getSafe(unit.x() + 1, unit.y(), swordsmanGradient);
        int stayScore = getSafe(unit.x(), unit.y(), swordsmanGradient);

        switch (getBiasedRandom(upScore, downScore, leftScore, rightScore, stayScore)) {
            case 0: { // upScore
                if (moveUnit(unit, unit.x(), unit.y() - 1, units, buildings)) break;
            }
            case 1: { // downScore
                if (moveUnit(unit, unit.x(), unit.y() + 1, units, buildings)) break;
            }
            case 2: { // leftScore
                if (moveUnit(unit, unit.x() - 1, unit.y(), units, buildings)) break;
            }
            case 3: { // rightScore
                if (moveUnit(unit, unit.x() + 1, unit.y(), units, buildings)) break;
            }
            case 4: { // stayScore
                break;
            }
        }
    }

    private static boolean moveUnit(Unit unit, int x, int y, Unit[][] units, Building[][] buildings) {;
        Building building = getSafe(x, y, buildings);
        if (building != null &&
                (building instanceof Capital || building instanceof Farm || building instanceof Mountain || building instanceof Castle)) {
            return false;
        }
        Unit unit2 = getSafe(x, y, units);
        if (unit2 == null) {
            if (setSafe(x, y, unit, units)) {
                units[unit.x()][unit.y()] = null;
                unit.x(x);
                unit.y(y);
                return true;
            } else {
                return false;
            }
        } else {
            return attackOrSwap(unit, unit2, units, buildings);
            //return false;
        }
    }

    private static boolean attackOrSwap(Unit unit, Unit unit2, Unit[][] units, Building[][] buildings) {
        if (unit.player().equals(unit2.player())) {
            if (unit instanceof Swordsman) {
                if (unit2 instanceof Peasant ||
                        unit2 instanceof Archer) {
                    if (setSafe(unit2.x(), unit2.y(), unit, units)) {
                        units[unit.x()][unit.y()] = unit2;
                        int x2 = unit2.x();
                        int y2 = unit2.y();
                        unit2.x(unit.x());
                        unit2.y(unit.y());
                        unit.x(x2);
                        unit.y(y2);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        } else {
            float bonus = 0f;
            Building tile = getSafe(unit2.x(), unit2.y(), buildings);
            if (tile != null && tile instanceof Forest) bonus = 0.15f;
            tile = (Building) findClosest(unit2.x(), unit2.y(), 2, Castle.class, buildings);
            if (tile != null && unit2.player().equals(tile.owner())) bonus = 0.25f;
            if (ThreadLocalRandom.current().nextFloat() > unit2.evasion() + bonus) {
                if (unit2.redHp(unit.str()) < 0) {
                    units[unit2.x()][unit2.y()] = null;
                }
            }
            return true;
        }
    }

    /* GRADIENTS
    */

    private void calculateGradients() {
        zeroGragients();
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                if (buildings[j][i] != null) {
                    if (buildings[j][i] instanceof Capital) updateCapitalGradients((Capital) buildings[j][i]);
                }
                if (units[j][i] != null) {
                    if (units[j][i] instanceof Swordsman) updateSwordsmanGradients((Swordsman) units[j][i]);
                }
                if (units[j][i] != null) {
                    if (units[j][i] instanceof Infantryfur) updateInfantryfurGradients((Infantryfur) units[j][i]);
                }
            }
        }
    }

    private void zeroGragients() {
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                swordsmanGradient[j][i] = GRADIENT_PREFIX;
                infanrtyfurGradient[j][i] = GRADIENT_PREFIX;
            }
        }
        /*for (int i = 0; i < YSIZE; i++) {
            swordsmanGradient[0][i] = -GRADIENT_PREFIX;
            swordsmanGradient[XSIZE - 1][i] = -GRADIENT_PREFIX;
        }

        for (int i = 0; i < XSIZE; i++) {
            swordsmanGradient[i][0] = -GRADIENT_PREFIX;
            swordsmanGradient[i][YSIZE - 1] = -GRADIENT_PREFIX;
        }*/
    }

    private void updateCapitalGradients(Capital capital) {
        if (capital.owner().furries) {
            makeGradient(capital.x(), capital.y(), 2, swordsmanGradient);
        } else {
            makeGradient(capital.x(), capital.y(), 2, infanrtyfurGradient);
        }
    }

    private void updateSwordsmanGradients(Swordsman swordsman) {
        makeGradient(swordsman.x(), swordsman.y(), 50, 10, 5, infanrtyfurGradient);
    }

    private void updateInfantryfurGradients(Infantryfur infantryfur) {
        makeGradient(infantryfur.x(), infantryfur.y(), 50, 10, 5, swordsmanGradient);
    }

    private void makeGradient(int xStart, int yStart, int decay, int[][] gradient) {
        makeGradient(xStart, yStart, GRADIENT_PREFIX, decay, Integer.MAX_VALUE, gradient);
    }

    private void makeGradient(int xStart, int yStart, int value, int decay, int limit, int[][] gradient) {
        boolean changed = addSafe(xStart, yStart, value, gradient);
        for (int i = 1; changed && i < limit ; i++) {
            changed = false;
            for (int x = 0; x <= i; x++) {
                changed |= addSafe(xStart+x, yStart+i-x, value-i*decay, gradient);
                changed |= addSafe(xStart-x, yStart-i+x, value-i*decay, gradient);
            }
            for (int x = 1; x < i; x++) {
                changed |= addSafe(xStart-x, yStart+i-x, value-i*decay, gradient);
                changed |= addSafe(xStart+x, yStart-i+x, value-i*decay, gradient);
            }
        }
    }

    /* UTILS
    */

    private static boolean addSafe(int x, int y, int value, int[][] gradient) {
        if (x>= 0 && y >= 0 && x < gradient.length && y < gradient[0].length) gradient[x][y] += value;
        else return false;
        return true;
    }

    private static <T> boolean setSafe(int x, int y, T value, T[][] array) {
        if (x>= 0 && y >= 0 && x < array.length && y < array[0].length) array[x][y] = value;
        else return false;
        return true;
    }

    private static void putClosest(int xStart, int yStart, Unit value, Unit[][] array, Building[][] buildings) {
        boolean changed = tryToPut(xStart, yStart, value, array, buildings); if (changed) return;
        for (int i = 1; !changed; i++) {
            changed = false;
            for (int x = 0; x <= i; x++) {
                changed |= tryToPut(xStart+x, yStart+i-x, value, array, buildings); if (changed) return;
                changed |= tryToPut(xStart-x, yStart-i+x, value, array, buildings); if (changed) return;
            }
            for (int x = 1; x < i; x++) {
                changed |= tryToPut(xStart-x, yStart+i-x, value, array, buildings); if (changed) return;
                changed |= tryToPut(xStart+x, yStart-i+x, value, array, buildings); if (changed) return;
            }
        }
    }

    private static boolean tryToPut(int x, int y, Unit value, Unit[][] units, Building[][] buildings) {
        Unit unit = getSafe(x, y, units);
        Building building = getSafe(x, y, buildings);
        if (unit != null) return false;
        if (building != null &&
                (building instanceof Castle ||
                building instanceof Capital ||
                building instanceof Mountain ||
                building instanceof River ||
                building instanceof Farm)) {
            return false;
        }
        if (setSafe(x, y, value, units)) {
            value.x(x);
            value.y(y);
            return true;
        } else {
            return false;
        }
    }

    private static int getSafe(int x, int y, int[][] gradient) {
        if (x>= 0 && y >= 0 && x < gradient.length && y < gradient[0].length) return gradient[x][y];
        else return GRADIENT_PREFIX;
    }

    private static <T> T getSafe(int x, int y, T[][] array) {
        if (x>= 0 && y >= 0 && x < array.length && y < array[0].length) return array[x][y];
        else return null;
    }

    private static <T> T findClosest(int xStart, int yStart, int length, Class<? extends T> c, T[][] array) {
        T unit = getSafe(xStart, yStart, array); if (unit != null && unit.getClass().equals(c)) return unit;
        for (int i = 1; i <= length; i++) {
            for (int x = 0; x <= i; x++) {
                unit = getSafe(xStart+x, yStart+i-x, array); if (unit != null && unit.getClass().equals(c)) return unit;
                unit = getSafe(xStart-x, yStart-i+x, array); if (unit != null && unit.getClass().equals(c)) return unit;
            }
            for (int x = 1; x < i; x++) {
                unit = getSafe(xStart-x, yStart+i-x, array); if (unit != null && unit.getClass().equals(c)) return unit;
                unit = getSafe(xStart+x, yStart-i+x, array); if (unit != null && unit.getClass().equals(c)) return unit;
            }
        }
        return null;
    }

    @SafeVarargs
    private static int getBiasedRandom(int... probabilities) {
        int min = Integer.MAX_VALUE;
        for (int value : probabilities) {
            if (value < min) min = value;
        }
        min -= 5;

        long sum = 0;
        for (long value : probabilities) {
            sum += (value - min) * (value - min);
        }
        long result = ThreadLocalRandom.current().nextLong(0, sum + 1);
        long shift = 0;
        for (int i = 0; i < probabilities.length; i++) {
            if (result <= (probabilities[i] - min) * (probabilities[i] - min) + shift) {
                return i;
            } else {
                shift += (probabilities[i] - min) * (probabilities[i] - min);
            }
        }
        throw new AssertionError();
    }
}
