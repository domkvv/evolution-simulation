package agh.ics.oop.gui;

import agh.ics.oop.IMapElement;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.Vector2d;
import agh.ics.oop.WorldMap;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class MapBox extends GridPane {
    public GridPane grid = new GridPane();
    public Button toggleButton = new Button("play/pause");
    private final SimulationEngine engine;
    private final ChartBox chart;
    private final Button exportButton = new Button("export statistics to .csv");
    private final VBox buttons = new VBox(grid, toggleButton, exportButton);
    private HashMap<String, Image> pictures = new HashMap<>();

    public MapBox(WorldMap map, SimulationEngine engine) throws FileNotFoundException {
        super();
        this.engine = engine;

        makeMap(map);
        this.chart = new ChartBox(engine);
        concatenateEverything();
    }

    public void makeMap(WorldMap map) throws FileNotFoundException {
        grid.setGridLinesVisible(true);

        for (int i = 0; i < map.getWidth(); i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(45));
        }
        for (int i = 0; i < map.getHeight(); i++) {
            grid.getRowConstraints().add(new RowConstraints(45));
        }

        for (int i = 0; i < map.getWidth(); i++) {
            for (int j = 0; j < map.getHeight(); j++) {
                Pane backPane = new Pane();
                if (map.inJungle(new Vector2d(i, j))) {
                    backPane.setBackground(new Background(new BackgroundFill(Color.DARKSEAGREEN, null, null)));
                } else {
                    backPane.setBackground(new Background(new BackgroundFill(Color.BEIGE, null, null)));
                }

                StackPane pane;
                Object object = map.objectAt(new Vector2d(i, j));
                if (object != null) {
                    ElementImageBox vbox = new ElementImageBox((IMapElement) object, pictures);
                    pane = new StackPane(backPane, vbox);
                } else {
                    pane = new StackPane(backPane);
                }
                pane.setAlignment(Pos.CENTER);
                grid.add(pane, i, j);
            }
        }

    }

    public void updateMap(WorldMap map) throws FileNotFoundException {
        grid.setGridLinesVisible(false);
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        makeMap(map);
        chart.updateCharts();
        concatenateEverything();
    }

    public void concatenateEverything() {
        VBox newButtons;
        if (engine.isMagicStatement()) {
            Text showMagicStatement = new Text("magic is happening");
            buttons.getChildren().clear();
            newButtons = new VBox(grid, toggleButton, exportButton, showMagicStatement);
        } else {
            buttons.getChildren().clear();
            newButtons = new VBox(grid, toggleButton, exportButton);
        }
        newButtons.setAlignment(Pos.CENTER);
        newButtons.setSpacing(15);
        buttons.getChildren().add(newButtons);
        buttons.setAlignment(Pos.CENTER);
        HBox hbox = new HBox(chart, buttons);
        hbox.setSpacing(15);
        hbox.setAlignment(Pos.CENTER);
        VBox mapBox = new VBox(hbox);
        mapBox.setAlignment(Pos.CENTER);
        mapBox.setSpacing(20);
        this.toggleButton.setOnAction(e -> {
            engine.toggleSimulation();
        });

        this.exportButton.setOnAction(e -> {
            try {
                engine.exportStatistics();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(mapBox);

    }

}
