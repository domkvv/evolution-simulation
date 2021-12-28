package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ChartBox extends VBox {

    private final SimulationEngine engine;
    private final DoubleChartBox populationChart;
    private final SingleChartBox averageEnergyChart;
    private final SingleChartBox averageLifespanChart;
    private final SingleChartBox averageChildrenNo;
    private final GenotypesChart genotypesChart;
    private final HBox dominantGenotype;
    private Statistics statistics;

    public ChartBox(SimulationEngine engine){
        super();
        this.engine = engine;
        this.populationChart = new DoubleChartBox(engine, "animals number", "plants number", "time", engine.getMap().getAnimalsNumber(),
                engine.getMap().getPlantsNumber());
        this.averageEnergyChart = new SingleChartBox(engine, "average animal energy", "time", engine.getMap().getAverageEnergy());
        this.averageLifespanChart = new SingleChartBox(engine, "average animal lifespan", "time", engine.getMap().getAverageLifespan());
        this.averageChildrenNo = new SingleChartBox(engine, "average children number", "time", engine.getMap().getAverageChildren());
        this.genotypesChart = new GenotypesChart(engine);
        this.dominantGenotype =new HBox(new Text("dominant genotype:"), new Text(engine.getMap().getDominantGenotype()) );

        dominantGenotype.setSpacing(5);
        dominantGenotype.setAlignment(Pos.CENTER);
        VBox allCharts = new VBox(populationChart, averageEnergyChart, averageLifespanChart, averageChildrenNo, genotypesChart, dominantGenotype);
        allCharts.setSpacing(10);
        this.getChildren().add(allCharts);
    }

    public void updateCharts(){
        averageEnergyChart.updateChart(engine.getMap().getAverageEnergy());
        populationChart.updateChart(engine.getMap().getAnimalsNumber(), engine.getMap().getPlantsNumber());
        averageLifespanChart.updateChart(engine.getMap().getAverageLifespan());
        averageChildrenNo.updateChart(engine.getMap().getAverageChildren());
        genotypesChart.updateChart();
        dominantGenotype.getChildren().clear();
        dominantGenotype.getChildren().add(new HBox(new Text("dominant genotype:"), new Text(engine.getMap().getDominantGenotype())));

    }


}
