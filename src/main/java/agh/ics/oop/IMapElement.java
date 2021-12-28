package agh.ics.oop;

import java.io.FileNotFoundException;

public interface IMapElement {
    Vector2d getPosition();
    String getImagePath() throws FileNotFoundException;
}
