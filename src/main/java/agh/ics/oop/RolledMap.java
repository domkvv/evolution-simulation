package agh.ics.oop;

public class RolledMap extends WorldMap{


    public RolledMap(int animalsNo, int width, int height, double jungleRatio, double startEnergy, double moveEnergy, double plantEnergy) {
        super(animalsNo, width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        if (position.x >= 0 && position.x < getWidth() && position.y >= 0 && position.y < getHeight()) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "RolledMap";
    }
}
