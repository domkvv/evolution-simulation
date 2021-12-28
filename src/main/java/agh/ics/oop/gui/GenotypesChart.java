package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;

public class GenotypesChart extends VBox {

    private final PieChart chart;
    private final SimulationEngine engine;

    public GenotypesChart(SimulationEngine engine) {
        super();
        this.engine = engine;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (String genotype : this.engine.getMap().getGenotypesCounts().keySet()) {
            pieChartData.add(new PieChart.Data(genotype, this.engine.getMap().getGenotypesCounts().get(genotype)));
        }
        this.chart = new PieChart(pieChartData);
        this.chart.setAnimated(false);
        this.chart.setLegendVisible(true);
        this.chart.setLabelsVisible(true);
        this.chart.setLegendSide(Side.BOTTOM);
        this.chart.setTitle("Genotypes chart");
        this.chart.setMaxSize(300, 230);
        this.getChildren().add(this.chart);
    }

    public void updateChart() {
        this.chart.getData().clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for (String genotype : this.engine.getMap().getGenotypesCounts().keySet()) {
            pieChartData.add(new PieChart.Data(genotype, this.engine.getMap().getGenotypesCounts().get(genotype)));
        }
        this.chart.setData(pieChartData);
        this.chart.setAnimated(false);
        this.chart.setLabelsVisible(true);
        this.chart.setLegendVisible(true);
        this.chart.setLegendSide(Side.BOTTOM);
        this.chart.setTitle("Genotypes chart");
        this.chart.setMaxSize(300, 230);
    }

}
