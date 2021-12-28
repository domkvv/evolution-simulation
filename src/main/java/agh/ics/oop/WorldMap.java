package agh.ics.oop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WorldMap implements IWorldMap {
    private final Vector2d jLowerLeft;
    private final Vector2d jUpperRight;

    private final int width;
    private final int height;
    private final double startEnergy;
    private final double moveEnergy;
    private final double plantEnergy;

    private final MapDirection[] directions = MapDirection.values();
    private final ArrayList<Animal> animals = new ArrayList<Animal>();
    private final ArrayList<Animal> deadAnimals = new ArrayList<>();
    private final ArrayList<Plant> plants = new ArrayList<>();
    private final ArrayList<Animal> animalOffsprings = new ArrayList<>();
    private final HashMap<Vector2d, Plant> plantLists = new HashMap<>();
    private final HashMap<Vector2d, ArrayList<Animal>> animalLists = new HashMap<>();
    private final HashMap<String, Integer> genotypes = new HashMap<String, Integer>();
    private final Set<Vector2d> animalPositions = animalLists.keySet();
    private final Set<Vector2d> plantPositions = plantLists.keySet();

    public WorldMap(int animalsNo, int width, int height, double jungleRatio, double startEnergy, double moveEnergy, double plantEnergy) {
        this.width = width;
        this.height = height;
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;

        for (int i = 0; i < animalsNo; i++) {
            placeAnimalAtRandomPosition();
        }

        double oneDimRatio = Math.sqrt(jungleRatio / (1 + jungleRatio));
        this.jLowerLeft = new Vector2d((int) Math.round((width * (1 - oneDimRatio)) / 2), (int) Math.round((height * (1 - oneDimRatio)) / 2));
        this.jUpperRight = jLowerLeft.add(new Vector2d((int) Math.round(oneDimRatio * width) - 1, (int) Math.round(oneDimRatio * height) - 1));

    }

    @Override
    public Object objectAt(Vector2d position) {
        if (this.animalLists.containsKey(position)) {
            return this.animalLists.get(position).get(0);
        }

        if (this.plantLists.containsKey(position)) {
            return this.plantLists.get(position);
        }
        return null;
    }

    @Override
    public void addToGenotypes(Animal animal) {
        if (!this.genotypes.containsKey(animal.getGenes().toString())) {
            this.genotypes.put(animal.getGenes().toString(), 1);
        } else {
            int countGen = this.genotypes.get(animal.getGenes().toString());
            countGen += 1;
            this.genotypes.remove(animal.getGenes().toString());
            this.genotypes.put(animal.getGenes().toString(), countGen);
        }
    }

    @Override
    public void placeAnimalAtRandomPosition() {
        Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(0, this.width),
                ThreadLocalRandom.current().nextInt(0, this.height));
        while (objectAt(position) instanceof Animal) {
            position = new Vector2d(ThreadLocalRandom.current().nextInt(0, this.width),
                    ThreadLocalRandom.current().nextInt(0, this.height));
        }
        Animal newAnimal = new Animal(this, position);
        newAnimal.setEnergy(this.startEnergy);
        this.animals.add(newAnimal);
        this.animalLists.put(newAnimal.getPosition(), new ArrayList<Animal>());
        this.animalLists.get(newAnimal.getPosition()).add(newAnimal);
        addToGenotypes(newAnimal);
    }

    @Override
    public boolean inJungle(Vector2d position) {
        return (position.x >= this.jLowerLeft.x && position.x <= this.jUpperRight.x && position.y >= this.jLowerLeft.y && position.y <= this.jUpperRight.y);
    }

    @Override
    public boolean isJungleFull() {
        for (int i = this.jLowerLeft.x; i <= this.jUpperRight.x; i++) {
            for (int j = this.jLowerLeft.y; j <= this.jUpperRight.y; j++) {
                if (objectAt(new Vector2d(i, j)) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isSteppeFull() {
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                Vector2d position = new Vector2d(i, j);
                if (objectAt(position) == null && !inJungle(position)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.x >= 0 && position.x < this.width && position.y >= 0 && position.y < this.height;
    }

    @Override
    public void removeDeadAnimals(SimulationEngine engine) {
        if (this.animals.size() != 0) {
            for (Animal animal : this.animals) {
                if (animal.getEnergy() <= 0) {
                    animal.setDeathDate(engine.getDayNumber());
                    this.deadAnimals.add(animal);
                    int countGen = this.genotypes.get(animal.getGenes().toString()) - 1;
                    this.genotypes.remove(animal.getGenes().toString());
                    if (countGen > 0) {
                        this.genotypes.put(animal.getGenes().toString(), countGen);
                    }

                    this.animalLists.get(animal.getPosition()).remove(animal);
                    if (this.animalLists.get(animal.getPosition()).size() == 0) {
                        this.animalLists.remove(animal.getPosition());
                    }
                }
            }
        }
        this.animals.removeIf(e -> e.getEnergy() <= 0);

    }

    @Override
    public void moveAnimals() {
        if (this.animals.size() != 0) {
            for (Animal animal : this.animals) {
                Vector2d prevPosition = animal.getPosition();
                animal.move();
                if (!prevPosition.equals(animal.getPosition())) {
                    this.animalLists.get(prevPosition).remove(animal);
                    if (this.animalLists.get(prevPosition).size() == 0) {
                        this.animalLists.remove(prevPosition);
                    }
                    if (!this.animalLists.containsKey(animal.getPosition())) {
                        this.animalLists.put(animal.getPosition(), new ArrayList<>());
                    }
                    this.animalLists.get(animal.getPosition()).add(animal);
                }
            }
        }
    }

    @Override
    public void feedAnimals() {
        for (Vector2d i : this.plantPositions) {
            if (this.animalLists.containsKey(i)) {
                ArrayList<Animal> list = this.animalLists.get(i);
                list.sort((a, b) -> (int) (b.getEnergy() - a.getEnergy()));
                double maxEnergy = list.get(0).getEnergy();
                int j = 1;
                while (j < animalLists.get(i).size() && list.get(j).getEnergy() == maxEnergy) {
                    j += 1;
                }
                double OnePortion = plantEnergy / ((double) j);
                j = 0;
                while (list.get(j).getEnergy() == maxEnergy) {
                    list.get(j).setEnergy(list.get(j).getEnergy() + OnePortion);
                }
            }
        }
        this.plants.removeIf(e -> this.animalLists.containsKey(e.getPosition()));
        this.plantLists.keySet().removeIf(e -> this.animalLists.containsKey(e));
    }

    @Override
    public void reproduceAnimals(SimulationEngine engine) {
        for (Vector2d i : this.animalPositions) {
            if (this.animalLists.get(i).size() >= 2) {
                ArrayList<Animal> list = this.animalLists.get(i);
                list.sort((a, b) -> (int) (b.getEnergy() - a.getEnergy()));
                Animal parentOne = list.get(0);
                Animal parentTwo = list.get(1);
                Animal babyAnimal = parentOne.reproduce(parentTwo);
                if (babyAnimal != null) {
                    babyAnimal.setBornDate(engine.getDayNumber());
                    this.animalLists.get(i).add(babyAnimal);
                    this.animals.add(babyAnimal);
                    this.animalOffsprings.add(babyAnimal);
                    addToGenotypes(babyAnimal);
                }
            }
        }
    }

    @Override
    public void placePlants() {
        int newJunglePlants = 0;
        int newSteppePlants = 0;
        if (!isJungleFull()) {
            while (newJunglePlants == 0) {
                Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(this.jLowerLeft.x, this.jUpperRight.x + 1),
                        ThreadLocalRandom.current().nextInt(this.jLowerLeft.y, this.jUpperRight.y + 1));
                if (inJungle(position) && objectAt(position) == null) {
                    newJunglePlants += 1;
                    Plant junglePlant = new Plant(position);
                    this.plantLists.put(position, junglePlant);
                    this.plants.add(junglePlant);
                }
            }
        }
        if (!isSteppeFull()) {
            while (newSteppePlants == 0) {
                Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(0, this.width),
                        ThreadLocalRandom.current().nextInt(0, this.height));
                if (!inJungle(position) && objectAt(position) == null) {
                    newSteppePlants += 1;
                    Plant steppePlant = new Plant(position);
                    this.plantLists.put(position, steppePlant);
                    this.plants.add(steppePlant);
                }
            }
        }

    }

    @Override
    public int getAnimalsNumber() {
        return this.animals.size();
    }

    @Override
    public int getPlantsNumber() {
        return this.plants.size();
    }

    @Override
    public double getAverageEnergy() {
        double sumEnergies = 0;
        if (this.animals.size() != 0) {
            for (Animal animal : this.animals) {
                sumEnergies += animal.getEnergy();
            }
            return sumEnergies / ((double) this.animals.size());
        }
        return 0;
    }

    @Override
    public double getAverageLifespan() {
        if (this.deadAnimals.size() != 0) {
            double sumLifespans = 0;
            for (Animal animal : this.deadAnimals) {
                sumLifespans += (animal.getDeathDate() - animal.getBornDate() + 1);
            }
            return sumLifespans / ((double) this.deadAnimals.size());
        }
        return 0;
    }

    @Override
    public double getAverageChildren() {
        if (this.animals.size() != 0) {
            return this.animalOffsprings.size() / ((double) this.animals.size());
        }
        return 0;
    }

    @Override
    public String getDominantGenotype() {
        String dominantGenotype = "";
        int maxCount = 0;
        for (String genotype : this.genotypes.keySet()) {
            if (this.genotypes.get(genotype) >= maxCount) {
                maxCount = this.genotypes.get(genotype);
                dominantGenotype = genotype;
            }
        }
        return dominantGenotype;
    }

    @Override
    public void doMagic() {
        if (this.animals.size() == 5) {
            ArrayList<Animal> magicAnimals = new ArrayList<>();
            for (Animal animal : this.animals) {
                Vector2d newPosition = new Vector2d(ThreadLocalRandom.current().nextInt(0, this.width),
                        ThreadLocalRandom.current().nextInt(0, this.height));
                while (objectAt(newPosition) != null) {
                    newPosition = new Vector2d(ThreadLocalRandom.current().nextInt(0, this.width),
                            ThreadLocalRandom.current().nextInt(0, this.height));
                }

                Animal magicAnimal = new Animal(this, newPosition);
                ArrayList<Integer> newGenes = IntStream.range(0, 32).mapToObj(i -> animal.getGenes().get(i)).sorted().collect(Collectors.toCollection(ArrayList::new));
                magicAnimal.setGenes(newGenes);
                magicAnimal.setEnergy(this.startEnergy);
                this.animalLists.put(newPosition, new ArrayList<>());
                this.animalLists.get(newPosition).add(magicAnimal);
                addToGenotypes(magicAnimal);
                magicAnimals.add(magicAnimal);
            }
            this.animals.addAll(magicAnimals);
        }

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public double getMoveEnergy() {
        return this.moveEnergy;
    }

    public double getStartEnergy() {
        return this.startEnergy;
    }

    public HashMap<String, Integer> getGenotypes() {
        return this.genotypes;
    }

    public MapDirection[] getDirections() {
        return this.directions;
    }

    public ArrayList<Animal> getAnimals() {
        return this.animals;
    }

    public HashMap<Vector2d, ArrayList<Animal>> getAnimalLists() {
        return this.animalLists;
    }

    public HashMap<Vector2d, Plant> getPlantLists() {
        return this.plantLists;
    }
}
