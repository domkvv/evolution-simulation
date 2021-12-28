package agh.ics.oop;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Plant implements IMapElement{
    private Vector2d position;

    public Plant(Vector2d position) {
        this.position = position;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public Image setOrientationImage() throws FileNotFoundException {
        return new Image(new FileInputStream("src/main/resources/plant.png"));
    }

    @Override // del
    public String toString() {
        return this.position.toString();
    }
}
