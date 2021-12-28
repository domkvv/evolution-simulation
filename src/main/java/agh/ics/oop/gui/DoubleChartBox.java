package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.scene.chart.XYChart;

public class DoubleChartBox extends SingleChartBox{
    public double yDataToAdd2;
    protected final XYChart.Series series2 = new XYChart.Series();

    public DoubleChartBox(SimulationEngine engine, String seriesName, String seriesName2 , String xAxisName, double yDataToAdd, double yDataToAdd2) {
        super(engine, seriesName, xAxisName, yDataToAdd);
        this.yDataToAdd2 = yDataToAdd2;
        series2.setName(seriesName2);
        series2.getData().add(new XYChart.Data(engine.getDayNumber(), yDataToAdd2));
        chart.getData().add(series2);
//        chart.setPrefWidth(100);
//        chart.setPrefHeight(100);
        this.getChildren().clear();
        this.getChildren().add(chart);
    }

    public void updateChart(double yDataToAdd, double yDataToAdd2){
        XYChart.Data newData = new XYChart.Data(engine.getDayNumber(), yDataToAdd);
        this.yDataToAdd = yDataToAdd;
        series.getData().add(newData);
        XYChart.Data newData2 = new XYChart.Data(engine.getDayNumber(), yDataToAdd2);
        this.yDataToAdd2 = yDataToAdd2;
        series2.getData().add(newData2);
    }

}
