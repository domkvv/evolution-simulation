import agh.ics.oop.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Vector2dTest {
    @Test
    void equalsTest() {
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0));
        assertEquals(new Vector2d(-2147483648, 2147483647), new Vector2d(-2147483648, 2147483647));
        assertEquals(new Vector2d(1, -1), new Vector2d(1, -1));
    }

    @Test
    void addTest() {
        assertEquals(new Vector2d(2, -2), new Vector2d(-1, -3).add(new Vector2d(3, 1)));
        assertEquals(new Vector2d(-5, 0), new Vector2d(0, -3).add(new Vector2d(-5, 3)));
        assertEquals(new Vector2d(15, 4), new Vector2d(5, -1).add(new Vector2d(10, 5)));
    }

    @Test
    void subtractTest() {
        assertEquals(new Vector2d(19, -2), new Vector2d(19, -3).subtract(new Vector2d(0, -1)));
        assertEquals(new Vector2d(-4, 13), new Vector2d(-9, 13).subtract(new Vector2d(-5, 0)));
        assertEquals(new Vector2d(1, 6), new Vector2d(7, -16).subtract(new Vector2d(6, -22)));

    }

}


