package agh.ics.oop;

public class Plant implements IMapElement {

    private Vector2d position;

    public Plant(Vector2d position) {
        this.position = position;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String getImagePath() {
        return "src/main/resources/plant.png";
    }

}
