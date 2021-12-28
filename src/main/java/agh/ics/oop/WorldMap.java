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

    public MapDirection[] directions = MapDirection.values();
    public ArrayList<Animal> animals = new ArrayList<Animal>();
    public ArrayList<Animal> deadAnimals = new ArrayList<>();
    public ArrayList<Plant> plants = new ArrayList<>();
    public HashMap<Vector2d, Plant> plantLists = new HashMap<>();
    public HashMap<Vector2d, ArrayList<Animal>> animalLists = new HashMap<>();
    public Set<Vector2d> animalPositions = animalLists.keySet();
    public Set<Vector2d> plantPositions = plantLists.keySet();

    public ArrayList<Animal> animalOffsprings = new ArrayList<>();
    public HashMap<String, Integer> genotypes = new HashMap<String, Integer>();

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
        if (animalLists.containsKey(position)) {
            return animalLists.get(position).get(0);
        }

        if (plantLists.containsKey(position)) {
            return plantLists.get(position);
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
        Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(0, width),
                ThreadLocalRandom.current().nextInt(0, height));
        while (objectAt(position) instanceof Animal) {
            position = new Vector2d(ThreadLocalRandom.current().nextInt(0, width),
                    ThreadLocalRandom.current().nextInt(0, height));
        }
        Animal newAnimal = new Animal(this, position);
        newAnimal.setEnergy(startEnergy);
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
        for (int i = jLowerLeft.x; i <= jUpperRight.x; i++) {
            for (int j = jLowerLeft.y; j <= jUpperRight.y; j++) {
                if (objectAt(new Vector2d(i, j)) == null) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean isMapFull() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
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
        return false;
    }

    @Override
    public void removeDeadAnimals(SimulationEngine engine) {
        if (animals.size() != 0) {
            for (Animal animal : animals) {
                if (animal.getEnergy() <= 0) {
                    animal.setDeathDate(engine.getDayNumber());
                    deadAnimals.add(animal);
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
        animals.removeIf(e -> e.getEnergy() <= 0);

    }

    @Override
    public void moveAnimals() {
        if (animals.size() != 0) {
            for (Animal animal : animals) {
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
        for (Vector2d i : plantPositions) {
            if (animalLists.containsKey(i)) {
                ArrayList<Animal> list = animalLists.get(i);
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
        plants.removeIf(e -> animalLists.containsKey(e.getPosition()));
        plantLists.keySet().removeIf(e -> animalLists.containsKey(e));
    }

    @Override
    public void reproduceAnimals(SimulationEngine engine) {
        for (Vector2d i : animalPositions) {
            if (animalLists.get(i).size() >= 2) {
                ArrayList<Animal> list = animalLists.get(i);
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
                Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(jLowerLeft.x, jUpperRight.x + 1),
                        ThreadLocalRandom.current().nextInt(jLowerLeft.y, jUpperRight.y + 1));
                if (inJungle(position) && objectAt(position) == null) {
                    newJunglePlants += 1;
                    Plant junglePlant = new Plant(position);
                    this.plantLists.put(position, junglePlant);
                    this.plants.add(junglePlant);
                }
            }
        }
        if (!isMapFull()) {
            while (newSteppePlants == 0) {
                Vector2d position = new Vector2d(ThreadLocalRandom.current().nextInt(0, width),
                        ThreadLocalRandom.current().nextInt(0, height));
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
        if (animals.size() != 0) {
            for (Animal animal : animals) {
                sumEnergies += animal.getEnergy();
            }
            return sumEnergies / ((double) animals.size());
        }
        return 0;
    }

    @Override
    public double getAverageLifespan() {
        if (deadAnimals.size() != 0) {
            double sumLifespans = 0;
            for (Animal animal : deadAnimals) {
                sumLifespans += (animal.getDeathDate() - animal.getBornDate() + 1);
            }
            return sumLifespans / ((double) deadAnimals.size());
        }
        return 0;
    }

    @Override
    public double getAverageChildren() {
        if (animals.size() != 0) {
            return animalOffsprings.size() / ((double) animals.size());
        }
        return 0;
    }

    @Override
    public String getDominantGenotype() {
        String dominantGenotype = "";
        int maxCount = 0;
        for (String genotype : genotypes.keySet()) {
            if (genotypes.get(genotype) >= maxCount) {
                maxCount = genotypes.get(genotype);
                dominantGenotype = genotype;
            }
        }
        return dominantGenotype;
    }

    @Override
    public void doMagic() {
        if (animals.size() == 5) {
            ArrayList<Animal> magicAnimals = new ArrayList<>();
            for (Animal animal : animals) {
                Vector2d newPosition = new Vector2d(ThreadLocalRandom.current().nextInt(0, width),
                        ThreadLocalRandom.current().nextInt(0, height));
                while (objectAt(newPosition) != null) {
                    newPosition = new Vector2d(ThreadLocalRandom.current().nextInt(0, width),
                            ThreadLocalRandom.current().nextInt(0, height));
                }

                Animal magicAnimal = new Animal(this, newPosition);
                ArrayList<Integer> newGenes = IntStream.range(0, 32).mapToObj(i -> animal.getGenes().get(i)).sorted().collect(Collectors.toCollection(ArrayList::new));
                magicAnimal.setGenes(newGenes);
                magicAnimal.setEnergy(startEnergy);
                animalLists.put(newPosition, new ArrayList<>());
                animalLists.get(newPosition).add(magicAnimal);
                addToGenotypes(magicAnimal);
                magicAnimals.add(magicAnimal);
            }
            animals.addAll(magicAnimals);
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
}
