import agh.ics.oop.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovingTest {

        @Test
        void MovingTest1() {
            WorldMap map = new WorldMap(10, 10, 10, 0.5, 100, 10, 10);
            Vector2d position = new Vector2d(2, 2);
            Animal movingAnimal = new Animal(map, position);
            movingAnimal.setOrientation(MapDirection.NORTH);
            assertEquals(MapDirection.EAST, movingAnimal.getOrientation().next().next());
            assertEquals(new Vector2d(2, 3), movingAnimal.getPosition().subtract(MapDirection.NORTH.toUnitVector()));
        }

        @Test
        void MovingTest2() {
            WorldMap map = new WorldMap(10, 150, 160, 0.5, 100, 10, 10);
            Vector2d position = new Vector2d(56, 78);
            Animal movingAnimal = new Animal(map, position);
            movingAnimal.setOrientation(MapDirection.SOUTH);
            assertEquals(MapDirection.SOUTH_EAST, movingAnimal.getOrientation().previous());
            assertEquals(new Vector2d(56, 79), movingAnimal.getPosition().add(MapDirection.SOUTH.toUnitVector()));
        }

    }

