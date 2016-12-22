package io.github.ingvarjackal.wars.engine;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.ingvarjackal.wars.buildings.*;
import io.github.ingvarjackal.wars.furries.*;
import io.github.ingvarjackal.wars.humans.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private final static int GRADIENT_PREFIX = 5000;

    private final int XSIZE;
    private final int YSIZE;

    private final Unit[][] units;
    private final Building[][] buildings;

    private final int[][] swordsmanGradient;
    private final int[][] infanrtyfurGradient;
    private final int[][] arbalesterGradient;
    private final int[][] archerGradient;
    private final int[][] peasantGradient;
    private final int[][] graizerGradient;
    private final int[][] griffonGradient;
    private final int[][] wereGradient;

    public World(Building[][] buildings) {
        XSIZE = buildings.length;
        YSIZE = buildings[0].length;
        units = new Unit[XSIZE][YSIZE];
        this.buildings = buildings;
        swordsmanGradient = new int[XSIZE][YSIZE];
        infanrtyfurGradient = new int[XSIZE][YSIZE];
        arbalesterGradient = new int[XSIZE][YSIZE];
        archerGradient = new int[XSIZE][YSIZE];
        peasantGradient = new int[XSIZE][YSIZE];
        graizerGradient = new int[XSIZE][YSIZE];
        griffonGradient = new int[XSIZE][YSIZE];
        wereGradient = new int[XSIZE][YSIZE];
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

    private void feedAndSpawn() {
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
                units[unit.x()][unit.y()].redHp(1);
                unit.player().food = 0;
            }
        }

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
                humanPlayerSpawn.owner().chivalry,
                humanPlayerSpawn.owner().heavyInfantry,
                humanPlayerSpawn.owner().infantry,
                humanPlayerSpawn.owner().peasants,
                100)) {
            case 0: { // archers
                if (humanPlayerSpawn.owner().food >= Archer.cost) {
                    putClosest(humanPlayerSpawn.x(), humanPlayerSpawn.y(), new Archer(humanPlayerSpawn.owner()), units, buildings);
                    humanPlayerSpawn.owner().food -= Archer.cost;
                    break;
                }
            }
            case 1: { // chivalry
                if (humanPlayerSpawn.owner().food >= Griffon.cost) {
                    putClosest(humanPlayerSpawn.x(), humanPlayerSpawn.y(), new Griffon(humanPlayerSpawn.owner()), units, buildings);
                    humanPlayerSpawn.owner().food -= Griffon.cost;
                    break;
                };
            }
            case 2: { // heavyInfantry
                break;
            }
            case 3: { // infantry
                if (humanPlayerSpawn.owner().food >= Swordsman.cost) {
                    putClosest(humanPlayerSpawn.x(), humanPlayerSpawn.y(), new Swordsman(humanPlayerSpawn.owner()), units, buildings);
                    humanPlayerSpawn.owner().food -= Swordsman.cost;
                    break;
                }
            }
            case 4: { // peasants
                if (humanPlayerSpawn.owner().food >= Peasant.cost) {
                    putClosest(humanPlayerSpawn.x(), humanPlayerSpawn.y(), new Peasant(humanPlayerSpawn.owner()), units, buildings);
                    humanPlayerSpawn.owner().food -= Peasant.cost;
                    break;
                }
            }
            case 5: { // nothing
                break;
            }
        }

        switch (getBiasedRandom(furryPlayerSpawn.owner().archers,
                furryPlayerSpawn.owner().chivalry,
                furryPlayerSpawn.owner().heavyInfantry,
                furryPlayerSpawn.owner().infantry,
                furryPlayerSpawn.owner().peasants,
                100)) {
            case 0: { // archers
                if (furryPlayerSpawn.owner().food >= Arbalester.cost) {
                    putClosest(furryPlayerSpawn.x(), furryPlayerSpawn.y(), new Arbalester(furryPlayerSpawn.owner()), units, buildings);
                    furryPlayerSpawn.owner().food -= Arbalester.cost;
                    break;
                }
            }
            case 1: { // chivalry
                if (furryPlayerSpawn.owner().food >= Were.cost) {
                    putClosest(furryPlayerSpawn.x(), furryPlayerSpawn.y(), new Were(furryPlayerSpawn.owner()), units, buildings);
                    furryPlayerSpawn.owner().food -= Were.cost;
                    break;
                };
            }
            case 2: { // heavyInfantry
                break;
            }
            case 3: { // infantry
                if (furryPlayerSpawn.owner().food >= Infantryfur.cost) {
                    putClosest(furryPlayerSpawn.x(), furryPlayerSpawn.y(), new Infantryfur(furryPlayerSpawn.owner()), units, buildings);
                    furryPlayerSpawn.owner().food -= Infantryfur.cost;
                    break;
                }
            }
            case 4: { // peasants
                if (furryPlayerSpawn.owner().food >= Graizer.cost) {
                    putClosest(furryPlayerSpawn.x(), furryPlayerSpawn.y(), new Graizer(furryPlayerSpawn.owner()), units, buildings);
                    furryPlayerSpawn.owner().food -= Graizer.cost;
                    break;
                }
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
                    if (furry == null) furry = findClosest(building.x(), building.y(), 1, Hero.class, units);
                    if (furry == null) furry = findClosest(building.x(), building.y(), 1, Infantryfur.class, units);
                    if (furry == null) furry = findClosest(building.x(), building.y(), 1, Were.class, units);

                    Object human = findClosest(building.x(), building.y(), 1, Swordsman.class, units);
                    if (human == null) human = findClosest(building.x(), building.y(), 1, Archer.class, units);
                    if (human == null) human = findClosest(building.x(), building.y(), 1, Griffon.class, units);
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
                        if (building.owner() != null) {
                            building.owner().food += 10;
                        }
                        if (furry == null && human != null) {
                            building.owner(humanPlayerSpawn.owner());
                        }
                        if (furry != null && human == null) {
                            building.owner(furryPlayerSpawn.owner());
                        }
                    }
                }
                if (units[j][i] != null) {
                    Unit unit = units[j][i];
                    if (unit instanceof Peasant || unit instanceof Graizer) {
                        int bonus = 0;
                        Building farm = findClosest(unit.x(), unit.y(), 3, Farm.class, buildings);
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
            if (unit instanceof Arbalester) moveArchers(unit, arbalesterGradient, units, buildings);
            if (unit instanceof Archer) moveArchers(unit, archerGradient, units, buildings);
            if (unit instanceof Peasant) moveInfantry(unit, peasantGradient, units, buildings);
            if (unit instanceof Graizer) moveInfantry(unit, graizerGradient, units, buildings);
            if (unit instanceof Griffon) moveChivalry(unit, griffonGradient, units, buildings);
            if (unit instanceof Were) moveChivalry(unit, wereGradient, units, buildings);
        }
    }

    private static void moveInfantry(Unit unit, int[][] gradient, Unit[][] units, Building[][] buildings) {
        int upScore = getSafe(unit.x(), unit.y() - 1, gradient);
        int downScore = getSafe(unit.x(), unit.y() + 1, gradient);
        int leftScore = getSafe(unit.x() - 1, unit.y(), gradient);
        int rightScore = getSafe(unit.x() + 1, unit.y(), gradient);
        int stayScore = getSafe(unit.x(), unit.y(), gradient);

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

    private static void moveArchers(Unit unit, int[][] gradient, Unit[][] units, Building[][] buildings) {
        int n = getSafe(unit.x(), unit.y() - 1, gradient);
        int s = getSafe(unit.x(), unit.y() + 1, gradient);
        int w = getSafe(unit.x() - 1, unit.y(), gradient);
        int e = getSafe(unit.x() + 1, unit.y(), gradient);
        int stayScore = getSafe(unit.x(), unit.y(), gradient);

        Unit swUnit = getSafe(unit.x() - 1, unit.y() + 1, units);
        Unit seUnit = getSafe(unit.x() + 1, unit.y() + 1, units);
        Unit nwUnit = getSafe(unit.x() - 1, unit.y() - 1, units);
        Unit neUnit = getSafe(unit.x() + 1, unit.y() - 1, units);
        Unit ssUnit = getSafe(unit.x(), unit.y() + 2, units);
        Unit nnUnit = getSafe(unit.x(), unit.y() - 2, units);
        Unit wwUnit = getSafe(unit.x() - 2, unit.y(), units);
        Unit eeUnit = getSafe(unit.x() + 2, unit.y(), units);

        int sw = swUnit != null && swUnit.player != unit.player ? 1000 : 0;
        int se = seUnit != null && seUnit.player != unit.player ? 1000 : 0;
        int nw = nwUnit != null && nwUnit.player != unit.player ? 1000 : 0;
        int ne = neUnit != null && neUnit.player != unit.player ? 1000 : 0;
        int ss = ssUnit != null && ssUnit.player != unit.player ? 1000 : 0;
        int nn = nnUnit != null && nnUnit.player != unit.player ? 1000 : 0;
        int ww = wwUnit != null && wwUnit.player != unit.player ? 1000 : 0;
        int ee = eeUnit != null && eeUnit.player != unit.player ? 1000 : 0;


        if (sw+se+nw+ne+ss+nn+ww+ee != 0) {
            switch (getBiasedRandom(sw, se, nw, ne, ss, nn, ww, ee)) {
                case 0: { // sw
                    if (sw == 1000) attack(unit, swUnit, 0.4f, units, buildings);
                }
                case 1: { // se
                    if (se == 1000) attack(unit, seUnit,0.4f, units, buildings);
                }
                case 2: { // nw
                    if (nw == 1000) attack(unit, nwUnit,0.4f, units, buildings);
                }
                case 3: { // ne
                    if (ne == 1000) attack(unit, neUnit,0.4f, units, buildings);
                }
                case 4: { // ss
                    if (ss == 1000) attack(unit, ssUnit,0.4f, units, buildings);
                }
                case 5: { // nn
                    if (nn == 1000) attack(unit, nnUnit,0.4f, units, buildings);
                }
                case 6: { // ww
                    if (ww == 1000) attack(unit, wwUnit,0.4f, units, buildings);
                }
                case 7: { // ee
                    if (ee == 1000) attack(unit, eeUnit,0.4f, units, buildings);
                }
            }
        } else {
            switch (getBiasedRandom(n, s, w, e, stayScore)) {
                case 0: { // n
                    if (moveUnit(unit, unit.x(), unit.y() - 1, units, buildings)) break;
                }
                case 1: { // s
                    if (moveUnit(unit, unit.x(), unit.y() + 1, units, buildings)) break;
                }
                case 2: { // w
                    if (moveUnit(unit, unit.x() - 1, unit.y(), units, buildings)) break;
                }
                case 3: { // e
                    if (moveUnit(unit, unit.x() + 1, unit.y(), units, buildings)) break;
                }
                case 4: { // stayScore
                    break;
                }
            }
        }
    }

    private static void moveChivalry(Unit unit, int[][] gradient, Unit[][] units, Building[][] buildings) {
        int n = getSafe(unit.x(), unit.y() - 1, gradient);
        int s = getSafe(unit.x(), unit.y() + 1, gradient);
        int w = getSafe(unit.x() - 1, unit.y(), gradient);
        int e = getSafe(unit.x() + 1, unit.y(), gradient);

        int sw = getSafe(unit.x() - 1, unit.y() + 1, gradient);
        int se = getSafe(unit.x() + 1, unit.y() + 1, gradient);
        int nw = getSafe(unit.x() - 1, unit.y() - 1, gradient);
        int ne = getSafe(unit.x() + 1, unit.y() - 1, gradient);
        int ss = getSafe(unit.x(), unit.y() + 2, gradient);
        int nn = getSafe(unit.x(), unit.y() - 2, gradient);
        int ww = getSafe(unit.x() - 2, unit.y(), gradient);
        int ee = getSafe(unit.x() + 2, unit.y(), gradient);
        int stayScore = getSafe(unit.x(), unit.y(), gradient);

        switch (getBiasedRandom(n, s, w, e, sw, se, nw, ne, ss, nn, ww, ee, stayScore)) {
            case 0: { // n
                if (moveUnit(unit, unit.x(), unit.y() - 1, units, buildings)) break;
            }
            case 1: { // s
                if (moveUnit(unit, unit.x(), unit.y() + 1, units, buildings)) break;
            }
            case 2: { // w
                if (moveUnit(unit, unit.x() - 1, unit.y(), units, buildings)) break;
            }
            case 3: { // e
                if (moveUnit(unit, unit.x() + 1, unit.y(), units, buildings)) break;
            }
            case 4: { // sw
                if (moveUnit(unit, unit.x() - 1, unit.y() + 1, units, buildings)) break;
            }
            case 5: { // se
                if (moveUnit(unit, unit.x() + 1, unit.y() + 1, units, buildings)) break;
            }
            case 6: { // nw
                if (moveUnit(unit, unit.x() - 1, unit.y() - 1, units, buildings)) break;
            }
            case 7: { // ne
                if (moveUnit(unit, unit.x() + 1, unit.y() - 1, units, buildings)) break;
            }
            case 8: { // ss
                if (moveUnit(unit, unit.x(), unit.y() + 2, units, buildings)) break;
                else if (moveUnit(unit, unit.x(), unit.y() + 1, units, buildings)) break;
            }
            case 9: { // nn
                if (moveUnit(unit, unit.x(), unit.y() - 2, units, buildings)) break;
                else if (moveUnit(unit, unit.x(), unit.y() - 1, units, buildings)) break;
            }
            case 10: { // ww
                if (moveUnit(unit, unit.x(), unit.y() - 2, units, buildings)) break;
                else if (moveUnit(unit, unit.x(), unit.y() - 1, units, buildings)) break;
            }
            case 11: { // ee
                if (moveUnit(unit, unit.x(), unit.y() + 2, units, buildings)) break;
                else if (moveUnit(unit, unit.x(), unit.y() + 1, units, buildings)) break;
            }
            case 12: { // stayScore
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
                    return swap(unit, unit2, units);
                }
            }
            if (unit instanceof Infantryfur) {
                if (unit2 instanceof Graizer ||
                        unit2 instanceof Arbalester) {
                    return swap(unit, unit2, units);
                }
            }
            if (unit instanceof Archer) {
                if (unit2 instanceof Peasant) {
                    return swap(unit, unit2, units);
                }
            }
            if (unit instanceof Arbalester) {
                if (unit2 instanceof Peasant) {
                    return swap(unit, unit2, units);
                }
            }
            return false;
        } else {
            return attack(unit, unit2, units, buildings);
        }
    }

    private static boolean swap(Unit unit, Unit unit2, Unit[][] units) {
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

    private static boolean attack(Unit unit, Unit unit2, Unit[][] units, Building[][] buildings) {
        return attack(unit, unit2, 0f, units, buildings);
    }

    private static boolean attack(Unit unit, Unit unit2, float bonus, Unit[][] units, Building[][] buildings) {
        Building tile = getSafe(unit2.x(), unit2.y(), buildings);
        if (tile != null && tile instanceof Forest) bonus = 0.25f;
        if (tile != null && tile instanceof River) bonus = -0.75f;
        tile = findClosest(unit2.x(), unit2.y(), 2, Castle.class, buildings);
        if (tile != null && unit2.player().equals(tile.owner())) bonus = 0.5f;
        if (ThreadLocalRandom.current().nextFloat() > Math.min(0.99f, Math.max(0.01f, unit2.evasion() + bonus))) {
            if (unit2.redHp(unit.str()) < 0) {
                units[unit2.x()][unit2.y()] = null;
            }
        }
        return true;
    }

    /* GRADIENTS
    */

    private void calculateGradients() {
        zeroGragients();
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                if (buildings[j][i] != null) {
                    if (buildings[j][i] instanceof Capital) updateCapitalGradients((Capital) buildings[j][i]);
                    if (buildings[j][i] instanceof Castle) updateCastleGradients((Castle) buildings[j][i]);
                    if (buildings[j][i] instanceof Farm) updateFarmGradients((Farm) buildings[j][i]);
                    if (buildings[j][i] instanceof Forest) updateForestGradients((Forest) buildings[j][i]);
                    if (buildings[j][i] instanceof River) updateRiverGradients((River) buildings[j][i]);
                    if (buildings[j][i] instanceof Mountain) updateMountainGradients((Mountain) buildings[j][i]);
                }
                if (units[j][i] != null) {
                    if (units[j][i] instanceof Swordsman) updateSwordsmanGradients((Swordsman) units[j][i]);
                    if (units[j][i] instanceof Infantryfur) updateInfantryfurGradients((Infantryfur) units[j][i]);
                    if (units[j][i] instanceof Arbalester) updateArbalesterGradients((Arbalester) units[j][i]);
                    if (units[j][i] instanceof Archer) updateArcherGradients((Archer) units[j][i]);
                    if (units[j][i] instanceof Graizer) updateGraizerGradients((Graizer) units[j][i]);
                    if (units[j][i] instanceof Peasant) updatePeasantGradients((Peasant) units[j][i]);
                    if (units[j][i] instanceof Griffon) updateGriffonGradients((Griffon) units[j][i]);
                    if (units[j][i] instanceof Were) updateWereGradients((Were) units[j][i]);
                }
            }
        }
    }

    private void zeroGragients() {
        for (int i = 0; i < YSIZE; i++) {
            for (int j = 0; j < XSIZE; j++) {
                swordsmanGradient[j][i] = GRADIENT_PREFIX;
                infanrtyfurGradient[j][i] = GRADIENT_PREFIX;
                arbalesterGradient[j][i] = GRADIENT_PREFIX;
                archerGradient[j][i] = GRADIENT_PREFIX;
                peasantGradient[j][i] = GRADIENT_PREFIX;
                graizerGradient[j][i] = GRADIENT_PREFIX;
            }
        }
    }

    private void updateCapitalGradients(Capital capital) {
        if (capital.owner().furries) {
            makeGradient(capital.x(), capital.y(), 4, swordsmanGradient);
            makeGradient(capital.x(), capital.y(), 4, archerGradient);
            makeGradient(capital.x(), capital.y(), 4, griffonGradient);
        } else {
            makeGradient(capital.x(), capital.y(), 4, infanrtyfurGradient);
            makeGradient(capital.x(), capital.y(), 4, arbalesterGradient);
            makeGradient(capital.x(), capital.y(), 4, wereGradient);
        }
    }

    private void updateMountainGradients(Mountain mountain) {
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, infanrtyfurGradient);
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, arbalesterGradient);
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, swordsmanGradient);
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, archerGradient);
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, peasantGradient);
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, graizerGradient);
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, griffonGradient);
        makeGradient(mountain.x(), mountain.y(), -100, -100, 1, wereGradient);
    }

    private void updateRiverGradients(River river) {
        makeGradient(river.x(), river.y(), -50, -25, 2, infanrtyfurGradient);
        makeGradient(river.x(), river.y(), -50, -25, 2, arbalesterGradient);
        makeGradient(river.x(), river.y(), -50, -25, 2, swordsmanGradient);
        makeGradient(river.x(), river.y(), -50, -25, 2, archerGradient);
        makeGradient(river.x(), river.y(), -50, -25, 2, peasantGradient);
        makeGradient(river.x(), river.y(), -50, -25, 2, graizerGradient);
    }

    private void updateForestGradients(Forest forest) {
        makeGradient(forest.x(), forest.y(), 2, 2, 1, infanrtyfurGradient);
        makeGradient(forest.x(), forest.y(), 2, 2, 1, arbalesterGradient);
        makeGradient(forest.x(), forest.y(), 2, 2, 1, swordsmanGradient);
        makeGradient(forest.x(), forest.y(), 2, 2, 1, archerGradient);
        makeGradient(forest.x(), forest.y(), 2, 2, 1, peasantGradient);
        makeGradient(forest.x(), forest.y(), 2, 2, 1, graizerGradient);
        makeGradient(forest.x(), forest.y(), 2, 2, 1, griffonGradient);
        makeGradient(forest.x(), forest.y(), 2, 2, 1, wereGradient);
    }


    private void updateCastleGradients(Castle castle) {
        if (castle.owner == null) {
            makeGradient(castle.x(), castle.y(), 20, 2, 10, swordsmanGradient);
            makeGradient(castle.x(), castle.y(), 20, 2, 10, archerGradient);
            makeGradient(castle.x(), castle.y(), 20, 2, 10, infanrtyfurGradient);
            makeGradient(castle.x(), castle.y(), 20, 2, 10, arbalesterGradient);
            makeGradient(castle.x(), castle.y(), 40, 2, 20, griffonGradient);
            makeGradient(castle.x(), castle.y(), 40, 2, 20, wereGradient);
        } else {
            if (castle.owner().furries) {
                makeGradient(castle.x(), castle.y(), 10, 2, 5, infanrtyfurGradient);
                makeGradient(castle.x(), castle.y(), 20, 4, 5, arbalesterGradient);
                makeGradient(castle.x(), castle.y(), 45, 3, 15, swordsmanGradient);
                makeGradient(castle.x(), castle.y(), 45, 3, 15, archerGradient);
                makeGradient(castle.x(), castle.y(), 30, 3, 10, griffonGradient);
                makeGradient(castle.x(), castle.y(), 10, 2, 2, wereGradient);
            } else {
                makeGradient(castle.x(), castle.y(), 10, 2, 5, swordsmanGradient);
                makeGradient(castle.x(), castle.y(), 20, 4, 5, archerGradient);
                makeGradient(castle.x(), castle.y(), 45, 3, 15, infanrtyfurGradient);
                makeGradient(castle.x(), castle.y(), 45, 3, 15, arbalesterGradient);
                makeGradient(castle.x(), castle.y(), 30, 3, 10, wereGradient);
                makeGradient(castle.x(), castle.y(), 10, 2, 2, griffonGradient);
            }
        }
    }

    private void updateFarmGradients(Farm farm) {
        makeGradient(farm.x(), farm.y(), 10, 1, 10, swordsmanGradient);
        makeGradient(farm.x(), farm.y(), 10, 1, 10, archerGradient);
        makeGradient(farm.x(), farm.y(), 10, 1, 10, infanrtyfurGradient);
        makeGradient(farm.x(), farm.y(), 10, 1, 10, arbalesterGradient);
        makeGradient(farm.x(), farm.y(), 40, 1, 40, wereGradient);
        makeGradient(farm.x(), farm.y(), 40, 1, 40, griffonGradient);

        if (farm.owner != null) {
            if (farm.owner.furries) {
                makeGradient(farm.x(), farm.y(), -40, -1, 40, wereGradient);
                makeGradient(farm.x(), farm.y(), 4, graizerGradient);
            } else {
                makeGradient(farm.x(), farm.y(), -40, -1, 40, griffonGradient);
                makeGradient(farm.x(), farm.y(), 4, peasantGradient);
            }
        }
    }

    private void updateGriffonGradients(Griffon griffon) {
        makeGradient(griffon.x(), griffon.y(), 80, 10, 6, infanrtyfurGradient);
        makeGradient(griffon.x(), griffon.y(), 50, 10, 5, arbalesterGradient);
        makeGradient(griffon.x(), griffon.y(), 50, 10, 5, wereGradient);
        makeGradient(griffon.x(), griffon.y(), -40, -20, 2, arbalesterGradient);
        makeGradient(griffon.x(), griffon.y(), -50, -25, 2, graizerGradient);
    }

    private void updateWereGradients(Were were) {
        makeGradient(were.x(), were.y(), 80, 10, 6, swordsmanGradient);
        makeGradient(were.x(), were.y(), 50, 10, 5, archerGradient);
        makeGradient(were.x(), were.y(), 50, 10, 5, griffonGradient);
        makeGradient(were.x(), were.y(), -40, -20, 2, archerGradient);
        makeGradient(were.x(), were.y(), -50, -25, 2, peasantGradient);
    }

    private void updateSwordsmanGradients(Swordsman swordsman) {
        makeGradient(swordsman.x(), swordsman.y(), 10, 1, 10, swordsmanGradient);
        makeGradient(swordsman.x(), swordsman.y(), 50, 10, 5, infanrtyfurGradient);
        makeGradient(swordsman.x(), swordsman.y(), 80, 10, 6, arbalesterGradient);
        makeGradient(swordsman.x(), swordsman.y(), -40, -20, 2, arbalesterGradient);
        makeGradient(swordsman.x(), swordsman.y(), -50, -25, 2, graizerGradient);
        makeGradient(swordsman.x(), swordsman.y(), 50, 10, 5, wereGradient);
    }

    private void updateInfantryfurGradients(Infantryfur infantryfur) {
        makeGradient(infantryfur.x(), infantryfur.y(), 10, 1, 10, infanrtyfurGradient);
        makeGradient(infantryfur.x(), infantryfur.y(), 50, 10, 5, swordsmanGradient);
        makeGradient(infantryfur.x(), infantryfur.y(), 50, 10, 5, griffonGradient);
        makeGradient(infantryfur.x(), infantryfur.y(), 80, 10, 6, archerGradient);
        makeGradient(infantryfur.x(), infantryfur.y(), -40, -20, 2, archerGradient);
        makeGradient(infantryfur.x(), infantryfur.y(), -50, -25, 2, peasantGradient);
    }

    private void updateArbalesterGradients(Arbalester arbalester) {
        makeGradient(arbalester.x(), arbalester.y(), 80, 10, 6, griffonGradient);
        makeGradient(arbalester.x(), arbalester.y(), 40, 4, 10, infanrtyfurGradient);
        makeGradient(arbalester.x(), arbalester.y(), 80, 10, 5, swordsmanGradient);
        makeGradient(arbalester.x(), arbalester.y(), 50, 10, 6, archerGradient);
        makeGradient(arbalester.x(), arbalester.y(), -75, -25, 3, peasantGradient);
    }

    private void updateArcherGradients(Archer archer) {
        makeGradient(archer.x(), archer.y(), 80, 10, 6, wereGradient);
        makeGradient(archer.x(), archer.y(), 30, 3, 10, swordsmanGradient);
        makeGradient(archer.x(), archer.y(), 80, 10, 5, infanrtyfurGradient);
        makeGradient(archer.x(), archer.y(), 50, 10, 6, arbalesterGradient);
        makeGradient(archer.x(), archer.y(), -75, -25, 3, graizerGradient);
    }

    private void updateGraizerGradients(Graizer graizer) {
        makeGradient(graizer.x(), graizer.y(), 120, 10, 10, griffonGradient);
        makeGradient(graizer.x(), graizer.y(), 100, 10, 10, archerGradient);
        makeGradient(graizer.x(), graizer.y(), 100, 10, 10, swordsmanGradient);
    }

    private void updatePeasantGradients(Peasant peasant) {
        makeGradient(peasant.x(), peasant.y(), 120, 10, 10, wereGradient);
        makeGradient(peasant.x(), peasant.y(), 100, 10, 10, arbalesterGradient);
        makeGradient(peasant.x(), peasant.y(), 100, 10, 10, infanrtyfurGradient);
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
        long min = Integer.MAX_VALUE;
        for (long value : probabilities) {
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
