package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App extends Application implements ISimulationEngineObserver {
    private RolledMap rolledMap;
    private LimitedMap limitedMap;

    private MapBox rolledMapBox;
    private MapBox limitedMapBox;

    private SimulationEngine engine1;
    private SimulationEngine engine2;

    public void queryToMaps(StartWindowBox startWindowBox) throws IOException {
        int animalsNo = Integer.parseInt(startWindowBox.animalsNoBox.textField.getText());
        int width = Integer.parseInt(startWindowBox.widthBox.textField.getText());
        int height = Integer.parseInt(startWindowBox.heightBox.textField.getText());
        double jungleRatio = Double.parseDouble(startWindowBox.jungleRatioBox.textField.getText());
        double startEnergy = Double.parseDouble(startWindowBox.startEnergyBox.textField.getText());
        double moveEnergy = Double.parseDouble(startWindowBox.moveEnergyBox.textField.getText());
        double plantEnergy = Double.parseDouble(startWindowBox.plantEnergyBox.textField.getText());
        int moveDelay = Integer.parseInt(startWindowBox.moveDelay.textField.getText());
        boolean isMagicRolledMap = startWindowBox.strategyRolledMap.isSelected();
        boolean isMagicLimitedMap = startWindowBox.strategyLimitedMap.isSelected();
        this.rolledMap = new RolledMap(animalsNo, width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy);
        this.limitedMap = new LimitedMap(animalsNo, width, height, jungleRatio, startEnergy, moveEnergy, plantEnergy);
        this.engine1 = new SimulationEngine(this.rolledMap, moveDelay, isMagicRolledMap);
        this.engine2 = new SimulationEngine(this.limitedMap, moveDelay, isMagicLimitedMap);
        this.engine1.addObserver(this);
        this.engine2.addObserver(this);
        Thread thread1 = new Thread(this.engine1);
        thread1.start();
        Thread thread2 = new Thread(this.engine2);
        thread2.start();
    }

    public void start(Stage primaryStage) throws FileNotFoundException {
        StartWindowBox startWindowBox = new StartWindowBox();
        Scene scene = new Scene(startWindowBox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        startWindowBox.goButton.setOnAction(e -> {
            try {
                queryToMaps(startWindowBox);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            try {
                rolledMapBox = new MapBox(rolledMap, engine1);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            try {
                limitedMapBox = new MapBox(limitedMap, engine2);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            HBox maps = new HBox(rolledMapBox, limitedMapBox);
            maps.setSpacing(20);
            maps.setAlignment(Pos.CENTER);

            primaryStage.setScene(new Scene(maps, 1920, 1200));
            primaryStage.show();
        });

    }

    @Override
    public void stop() {
        this.engine1.stop();
        this.engine2.stop();
    }

    @Override
    public void mapsChanged() {
        Platform.runLater(() -> {
            try {
                rolledMapBox.updateMap(this.rolledMap);
                limitedMapBox.updateMap(this.limitedMap);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });
    }

}

