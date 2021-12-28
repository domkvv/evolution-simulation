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
        this.fileData.append(header).append("\n");
        updateStatistics();
    }

    public void updateStatistics() throws IOException {
        this.SumAnimals += this.engine.getMap().getAnimalsNumber();
        this.SumPlants += this.engine.getMap().getPlantsNumber();
        this.SumEnergy += this.engine.getMap().getAverageEnergy();
        this.SumLifespan += this.engine.getMap().getAverageLifespan();
        this.SumChildren += this.engine.getMap().getAverageChildren();

        this.fileData.append(this.engine.getDayNumber()).append(",")
                .append(this.engine.getMap().getAnimalsNumber()).append(",")
                .append(this.engine.getMap().getPlantsNumber()).append(",")
                .append(this.engine.getMap().getAverageEnergy()).append(",")
                .append(this.engine.getMap().getAverageLifespan()).append(",")
                .append(this.engine.getMap().getAverageChildren()).append("\n")
        ;
    }

    public void exportToCSV() throws IOException {
        double days = this.engine.getDayNumber() + 1;
        String filename = this.engine.getMap().toString() + "_after_day_" + this.engine.getDayNumber() + "_statistics.csv";
        FileWriter fileWriter = new FileWriter(filename);
        fileWriter.append(this.fileData);
        if (days != 0) {
            fileWriter.append("average:").append(",")
                    .append(String.valueOf(this.SumAnimals / days)).append(",")
                    .append(String.valueOf(this.SumPlants / days)).append(",")
                    .append(String.valueOf(this.SumEnergy / days)).append(",")
                    .append(String.valueOf(this.SumLifespan / days)).append(",")
                    .append(String.valueOf(this.SumChildren / days)).append("\n");
        } else {
            fileWriter.append("average:").append(",")
                    .append(String.valueOf(this.SumAnimals))
                    .append(",").append(String.valueOf(this.SumPlants))
                    .append(",").append(String.valueOf(this.SumEnergy))
                    .append(",").append(String.valueOf(this.SumLifespan))
                    .append(",").append(String.valueOf(this.SumChildren))
                    .append("\n");
        }
        fileWriter.close();
    }

}
