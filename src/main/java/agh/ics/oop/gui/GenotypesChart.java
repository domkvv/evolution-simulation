package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;

public class GenotypesChart extends VBox {
    private PieChart chart;
    private SimulationEngine engine;

    public GenotypesChart(SimulationEngine engine){
        super();
        this.engine = engine;
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(String genotype : engine.getMap().genotypes.keySet()){
            pieChartData.add(new PieChart.Data(genotype, engine.getMap().genotypes.get(genotype)));
        }
        chart = new PieChart(pieChartData);
        chart.setAnimated(false);
        chart.setLegendVisible(true);
        chart.setLabelsVisible(true);
        chart.setLegendSide(Side.RIGHT);
        this.getChildren().add(chart);

    }

    public void updateChart(){
        chart.getData().clear();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        for(String genotype : engine.getMap().genotypes.keySet()){
            pieChartData.add(new PieChart.Data(genotype, engine.getMap().genotypes.get(genotype)));
        }
        chart.setData(pieChartData);
        chart.setAnimated(false);
        chart.setLabelsVisible(true);
        chart.setLegendVisible(true);
        chart.setLegendSide(Side.RIGHT);
    }

}
