package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;

import java.io.FileWriter;
import java.io.IOException;

public class Statistics {
    private final SimulationEngine engine;
    private final StringBuffer fileData = new StringBuffer();
    private int SumAnimals = 0;
    private int SumPlants = 0;
    private double SumEnergy = 0;
    private double SumLifespan = 0;
    private double SumChildren = 0;

    public Statistics(SimulationEngine engine) throws IOException {
        this.engine = engine;
        String header = "day,number of animals,number of plants,average energy,average lifespan,average children";
        fileData.append(header).append("\n");
        updateStatistics();
    }

    public void updateStatistics() throws IOException {
        SumAnimals += engine.getMap().getAnimalsNumber();
        SumPlants += engine.getMap().getPlantsNumber();
        SumEnergy += engine.getMap().getAverageEnergy();
        SumLifespan += engine.getMap().getAverageLifespan();
        SumChildren += engine.getMap().getAverageChildren();

        fileData.append(String.valueOf(engine.getDayNumber())).append(",")
                .append(String.valueOf(engine.getMap().getAnimalsNumber())).append(",")
                .append(String.valueOf(engine.getMap().getPlantsNumber())).append(",")
                .append(String.valueOf(engine.getMap().getAverageEnergy())).append(",")
                .append(String.valueOf(engine.getMap().getAverageLifespan()))
                .append(",").append(String.valueOf(engine.getMap().getAverageChildren())).append("\n")
        ;
    }

    public void exportToCSV() throws IOException {
        double days = engine.getDayNumber() + 1;
        String filename = engine.getMap().toString() + "_after_day_" + engine.getDayNumber() + "_statistics.csv";
        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.append(fileData);
        if (days != 0) {
            fileWriter.append("average:,").append(String.valueOf(SumAnimals / days)).append(",").append(String.valueOf(SumPlants / days)).append(",").append(String.valueOf(SumEnergy / days)).append(",").append(String.valueOf(SumLifespan / days)).append(",").append(String.valueOf(SumChildren / days)).append("\n");
        } else {
            fileWriter.append("average:,").append(String.valueOf(SumAnimals)).append(",").append(String.valueOf(SumPlants)).append(",").append(String.valueOf(SumEnergy)).append(",").append(String.valueOf(SumLifespan)).append(",").append(String.valueOf(SumChildren)).append("\n");
        }
        fileWriter.close();
    }

}
