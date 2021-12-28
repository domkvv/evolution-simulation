package agh.ics.oop;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.Collections;

public class Animal implements IMapElement {

    private Vector2d position;
    private MapDirection orientation;
    private double energy;
    private ArrayList<Integer> genes = new ArrayList<>();
    private final ArrayList<Animal> offsprings = new ArrayList<>();
    private int bornDate;
    private int deathDate;
    private final WorldMap map;

    public Animal(WorldMap map, Vector2d position) {
        for (int i = 0; i < 32; i++) {
            this.genes.add(ThreadLocalRandom.current().nextInt(0, 7));
        }
        Collections.sort(this.genes);
        this.position = position;
        this.map = map;
        this.orientation = this.map.getDirections()[new Random().nextInt(MapDirection.values().length)];
        this.bornDate = 0;
    }

    public void move() {
        Random rand = new Random();
        int nextMove = this.genes.get(rand.nextInt(this.genes.size()));

        Vector2d newPosition = this.position;
        switch (nextMove) {
            case 0 -> newPosition = this.position.add(this.orientation.toUnitVector());
            case 1 -> this.orientation = this.orientation.next();
            case 2 -> this.orientation = this.orientation.next().next();
            case 3 -> this.orientation = this.orientation.next().next().next();
            case 4 -> newPosition = this.position.subtract(this.orientation.toUnitVector());
            case 5 -> this.orientation = this.orientation.previous().previous().previous();
            case 6 -> this.orientation = this.orientation.previous().previous();
            case 7 -> this.orientation = this.orientation.previous();
        }

        if (this.map.canMoveTo(newPosition)) {
            this.position = newPosition;
        } else if (this.map instanceof RolledMap) {
            this.position = new Vector2d((newPosition.x + map.getWidth()) % (map.getWidth()), (newPosition.y + map.getHeight()) % (map.getHeight()));
        }
        this.energy -= this.map.getMoveEnergy();
    }

    public Animal reproduce(Animal that) {
        if (this.energy >= 0.5 * this.map.getStartEnergy() && that.energy >= 0.5 * this.map.getStartEnergy()) {
            int fromParentOne = (int) (32 * this.energy / (this.energy + that.energy));
            int side = ThreadLocalRandom.current().nextInt(0, 2);
            Animal babyAnimal = new Animal(this.map, this.getPosition());
            babyAnimal.genes = new ArrayList<>();

            if ((side == 0 && this.energy >= that.energy) || (side == 1 && this.energy <= that.energy)) {
                for (int i = 0; i < fromParentOne; i++) {
                    babyAnimal.genes.add(this.genes.get(i));
                }
                for (int i = fromParentOne; i < 32; i++) {
                    babyAnimal.genes.add(that.genes.get(i));
                }
            } else {
                for (int i = 32 - fromParentOne; i < 32; i++) {
                    babyAnimal.genes.add(this.genes.get(i));
                }
                for (int i = 0; i < 32 - fromParentOne; i++) {
                    babyAnimal.genes.add(that.genes.get(i));
                }
            }
            Collections.sort(babyAnimal.genes);
            babyAnimal.energy = this.energy * 0.25 + that.energy * 0.25;
            this.energy *= 0.75;
            that.energy *= 0.75;
            this.offsprings.add(babyAnimal);
            that.offsprings.add(babyAnimal);
            return babyAnimal;
        }
        return null;
    }

    @Override
    public String getImagePath() {
        String imageFolder;
        if (this.energy <= 0) imageFolder = "0";
        else if (this.energy <= 25) imageFolder = "0_25";
        else if (this.energy <= 50) imageFolder = "25_50";
        else if (this.energy <= 75) imageFolder = "50_75";
        else imageFolder = "75_100";

        String imageFile;
        switch (this.orientation.ordinal()) {
            case 0 -> imageFile = "up";
            case 1 -> imageFile = "rightup";
            case 2 -> imageFile = "right";
            case 3 -> imageFile = "rightdown";
            case 4 -> imageFile = "down";
            case 5 -> imageFile = "leftdown";
            case 6 -> imageFile = "left";
            case 7 -> imageFile = "leftup";
            default -> imageFile = "";
        }
        return "src/main/resources/" + imageFolder + "/" + imageFile + ".png";
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    public MapDirection getOrientation() {
        return this.orientation;
    }

    public double getEnergy() {
        return this.energy;
    }

    public ArrayList<Integer> getGenes() {
        return this.genes;
    }

    public int getBornDate() {
        return this.bornDate;
    }

    public int getDeathDate() {
        return this.deathDate;
    }

    public void setOrientation(MapDirection orientation) {
        this.orientation = orientation;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public void setGenes(ArrayList<Integer> genes) {
        this.genes = genes;
    }

    public void setBornDate(int bornDate) {
        this.bornDate = bornDate;
    }

    public void setDeathDate(int deathDate) {
        this.deathDate = deathDate;
    }


}