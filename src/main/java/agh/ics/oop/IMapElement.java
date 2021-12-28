package agh.ics.oop;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;

public interface IMapElement {
    public Vector2d getPosition();
    public Image setOrientationImage() throws FileNotFoundException;
}
