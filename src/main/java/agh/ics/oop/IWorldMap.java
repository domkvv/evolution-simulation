package agh.ics.oop;

public interface IWorldMap {

    boolean canMoveTo(Vector2d position);

    Object objectAt(Vector2d position);

    void placeAnimalAtRandomPosition();

    void addToGenotypes(Animal animal);

    void removeDeadAnimals(SimulationEngine engine);

    void moveAnimals();

    void feedAnimals();

    void reproduceAnimals(SimulationEngine engine);

    void placePlants();

    boolean inJungle(Vector2d position);

    boolean isJungleFull();

    boolean isMapFull();

    int getAnimalsNumber();

    int getPlantsNumber();

    double getAverageEnergy();

    double getAverageLifespan();

    double getAverageChildren();

    String getDominantGenotype();

    void doMagic();

    String toString();

}
