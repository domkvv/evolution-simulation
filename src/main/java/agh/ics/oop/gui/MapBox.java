package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class MapBox extends HBox {

    public Button toggleButton = new Button("play/pause");
    private final GridPane grid = new GridPane();
    private final SimulationEngine engine;
    private final ChartBox chart;
    private final Button exportButton = new Button("export statistics to .csv");
    private final VBox buttons = new VBox(grid, toggleButton, exportButton);
    private final HashMap<String, Image> pictures = new HashMap<>();

    public MapBox(WorldMap map, SimulationEngine engine) throws FileNotFoundException {
        super();
        this.engine = engine;
        makeMap(map);
        this.chart = new ChartBox(engine);
        concatenateEverything();
    }

    public void makeMap(WorldMap map) throws FileNotFoundException {
        this.grid.setGridLinesVisible(true);

        for (int i = 0; i < map.getWidth(); i++) {
            this.grid.getColumnConstraints().add(new ColumnConstraints(35));
        }
        for (int i = 0; i < map.getHeight(); i++) {
            this.grid.getRowConstraints().add(new RowConstraints(35));
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
                    ElementImageBox vbox = new ElementImageBox((IMapElement) object, this.pictures);
                    pane = new StackPane(backPane, vbox);
                } else {
                    pane = new StackPane(backPane);
                }
                pane.setAlignment(Pos.CENTER);
                this.grid.add(pane, i, j);
            }
        }
    }

    public void updateMap(WorldMap map) throws FileNotFoundException {
        this.grid.setGridLinesVisible(false);
        this.grid.getChildren().clear();
        this.grid.getColumnConstraints().clear();
        this.grid.getRowConstraints().clear();
        makeMap(map);
        this.chart.updateCharts();
        concatenateEverything();
    }

    public void concatenateEverything() {
        VBox newButtons;
        if (this.engine.isMagicStatement()) {
            Text showMagicStatement = new Text("magic is happening");
            this.buttons.getChildren().clear();
            newButtons = new VBox(this.grid, this.toggleButton, this.exportButton, showMagicStatement);
        } else {
            this.buttons.getChildren().clear();
            newButtons = new VBox(this.grid, this.toggleButton, this.exportButton);
        }
        newButtons.setAlignment(Pos.CENTER);
        newButtons.setSpacing(15);
        this.buttons.getChildren().add(newButtons);
        this.buttons.setAlignment(Pos.CENTER);
        HBox mapBox = new HBox(this.chart, this.buttons);
        mapBox.setSpacing(10);
        mapBox.setAlignment(Pos.CENTER);
        this.toggleButton.setOnAction(e -> {
            this.engine.toggleSimulation();
        });

        this.exportButton.setOnAction(e -> {
            if (this.engine.isPaused()) {
                try {
                    this.engine.exportStatistics();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(mapBox);
    }

}
