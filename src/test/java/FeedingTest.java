import agh.ics.oop.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeedingTest {

    @Test
    void FeedingTest1() {
        WorldMap map = new WorldMap(10, 10, 10, 0.5, 10, 10, 10);
        Vector2d position = new Vector2d(2, 2);
        Animal eatingAnimal1 = new Animal(map, position);
        eatingAnimal1.setEnergy(40);
        Animal eatingAnimal2 = new Animal(map, position);
        eatingAnimal2.setEnergy(40);
        Animal eatingAnimal3 = new Animal(map, position);
        eatingAnimal3.setEnergy(40);
        Animal notEatingAnimal1 = new Animal(map, position);
        Animal notEatingAnimal2 = new Animal(map, position);
        if (!map.plantLists.containsKey(position)) {
            map.plantLists.put(position, new Plant(position));
        }
        if (map.animalLists.containsKey(position)) {
            map.animalLists.remove(position);
        }
        map.animalLists.put(position, new ArrayList<>());
        map.animalLists.get(position).add(eatingAnimal1);
        map.animalLists.get(position).add(notEatingAnimal1);
        map.animalLists.get(position).add(notEatingAnimal2);
        map.animalLists.get(position).add(eatingAnimal2);
        map.animalLists.get(position).add(eatingAnimal3);

        map.feedAnimals();
        double higherEnergy = 43 + (1 / (double) 3);
        assertEquals(higherEnergy, eatingAnimal1.getEnergy());

    }

    @Test
    void FeedingTest2() {
        WorldMap map = new WorldMap(10, 10, 10, 0.5, 10, 10, 15);
        Vector2d position = new Vector2d(2, 2);
        Animal eatingAnimal = new Animal(map, position);
        eatingAnimal.setEnergy(20);
        if (!map.plantLists.containsKey(position)) {
            map.plantLists.put(position, new Plant(position));
        }
        if (map.animalLists.containsKey(position)) {
            map.animalLists.remove(position);
        }
        map.animalLists.put(position, new ArrayList<>());
        map.animalLists.get(position).add(eatingAnimal);
        map.feedAnimals();
        assertEquals(35, eatingAnimal.getEnergy());

    }


}