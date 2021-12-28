package agh.ics.oop;

public class RolledMap extends WorldMap {

    public RolledMap(int animalsNo, int width, int height, double jungleRatio, double startEnergy, double moveEnergy, double plantEnergy) {
        super(animalsNo, width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy);
    }

    @Override
    public String toString() {
        return "RolledMap";
    }
}
