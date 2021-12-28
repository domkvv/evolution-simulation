import agh.ics.oop.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IWorldMapTest {
    @Test
    void inJungleTest1() {
        IWorldMap map = new WorldMap(10, 12, 12, 0.5, 100, 10, 10);
        Vector2d position = new Vector2d(6, 6);
        Vector2d position2 = new Vector2d(1, 6);
        assertEquals(true, map.inJungle(position));
        assertEquals(false, map.inJungle(position2));
    }

    @Test
    void inJungleTest2() {
        IWorldMap map = new WorldMap(10, 150, 15, 0.35, 100, 10, 10);
        Vector2d position = new Vector2d(0, 0);
        Vector2d position2 = new Vector2d(75, 7);
        assertEquals(false, map.inJungle(position));
        assertEquals(true, map.inJungle(position2));
    }

    @Test
    void isFullTest(){
        WorldMap map = new WorldMap(0, 4, 4, 0.33, 100, 10, 10);
        map.getPlantLists().put(new Vector2d(1,1), new Plant(new Vector2d(1, 1)));
        map.getPlantLists().put(new Vector2d(1,2), new Plant(new Vector2d(1, 2)));
        map.getPlantLists().put(new Vector2d(2,1), new Plant(new Vector2d(2, 1)));
        map.getPlantLists().put(new Vector2d(2,2), new Plant(new Vector2d(2, 2)));
        assertEquals(true, map.isJungleFull());
        assertEquals(false, map.isSteppeFull());
    }

    @Test
    void canMoveToTest(){
        WorldMap map = new WorldMap(12, 5, 14, 0.22, 150, 12, 12);
        Animal animal = new Animal(map, new Vector2d(0, 0));
        animal.setOrientation(MapDirection.WEST);
        assertEquals(false, map.canMoveTo(animal.getPosition().add(animal.getOrientation().toUnitVector())));
        assertEquals(true, map.canMoveTo(animal.getPosition().subtract(animal.getOrientation().toUnitVector())));
    }

    @Test
    void BoundaryTest1(){
        WorldMap map = new RolledMap(12, 5, 14, 0.22, 150, 12, 12);
        Animal animal = new Animal(map, new Vector2d(4, 0));
        animal.setOrientation(MapDirection.SOUTH_EAST);
        animal.move();
        assertTrue(animal.getPosition().equals(new Vector2d(4, 0))
                   || animal.getPosition().equals(new Vector2d(0, 1))
                   || animal.getPosition().equals(new Vector2d(3, 13)) );
    }

    @Test
    void BoundaryTest2(){
        WorldMap map = new LimitedMap(12, 5, 14, 0.22, 150, 12, 12);
        Animal animal = new Animal(map, new Vector2d(4, 0));
        animal.setOrientation(MapDirection.SOUTH_EAST);
        animal.move();
        assertEquals(new Vector2d(4, 0), animal.getPosition());
    }


    @Test
    void AnimalPlacingTest(){
        WorldMap map = new WorldMap(20, 150, 15, 0.35, 100, 10, 10);
        boolean flag = true;
        for(Vector2d i : map.getAnimalLists().keySet()){
            if(map.getAnimalLists().get(i).size() > 1){
                flag = false;
                break;
            }
        }
        assertEquals(true, flag);

    }

}
