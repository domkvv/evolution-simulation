package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChartBox extends VBox {

    private final SimulationEngine engine;
    private final DoubleChartBox populationChart;
    private final SingleChartBox averageEnergyChart;
    private final SingleChartBox averageLifespanChart;
    private final SingleChartBox averageChildrenNo;
    private final GenotypesChart genotypesChart;
    private final VBox dominantGenotype;

    public ChartBox(SimulationEngine engine) {
        super();
        this.engine = engine;
        this.populationChart = new DoubleChartBox(engine, "animals number", "plants number", "time", engine.getMap().getAnimalsNumber(),
                engine.getMap().getPlantsNumber());
        this.averageEnergyChart = new SingleChartBox(engine, "average animal energy", "time", engine.getMap().getAverageEnergy());
        this.averageLifespanChart = new SingleChartBox(engine, "average animal lifespan", "time", engine.getMap().getAverageLifespan());
        this.averageChildrenNo = new SingleChartBox(engine, "average children number", "time", engine.getMap().getAverageChildren());
        this.genotypesChart = new GenotypesChart(engine);
        this.dominantGenotype = new VBox(new Text("dominant genotype:"), new Text(engine.getMap().getDominantGenotype()));

        this.dominantGenotype.setSpacing(5);
        VBox allCharts = new VBox(this.populationChart, this.averageEnergyChart, this.averageLifespanChart, this.averageChildrenNo,
                this.genotypesChart, this.dominantGenotype);
        allCharts.setSpacing(10);
        allCharts.setAlignment(Pos.CENTER);
        this.getChildren().add(allCharts);
    }

    public void updateCharts() {
        this.averageEnergyChart.updateChart(this.engine.getMap().getAverageEnergy());
        this.populationChart.updateChart(this.engine.getMap().getAnimalsNumber(), this.engine.getMap().getPlantsNumber());
        this.averageLifespanChart.updateChart(this.engine.getMap().getAverageLifespan());
        this.averageChildrenNo.updateChart(this.engine.getMap().getAverageChildren());
        this.genotypesChart.updateChart();
        this.dominantGenotype.getChildren().clear();
        this.dominantGenotype.getChildren().add(new VBox(new Text("dominant genotype:"), new Text(this.engine.getMap().getDominantGenotype())));
    }


}
