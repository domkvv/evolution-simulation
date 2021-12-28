package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.scene.layout.VBox;

public class SpyingBox extends VBox {
    private SimulationEngine engine;

    public SpyingBox(SimulationEngine engine) {
        super();
        this.engine = engine;
    }

}
