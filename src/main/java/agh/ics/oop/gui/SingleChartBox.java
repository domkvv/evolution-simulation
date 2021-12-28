package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

public class SingleChartBox extends VBox {
    protected final NumberAxis xAxis = new NumberAxis();
    protected final NumberAxis yAxis = new NumberAxis();
    protected final LineChart<Number,Number> chart = new LineChart<Number,Number>(xAxis,yAxis);
    protected final XYChart.Series series = new XYChart.Series();
    public final SimulationEngine engine;
    public double yDataToAdd;

    public SingleChartBox(SimulationEngine engine, String chartName, String xAxisName, double yDataToAdd){
        super();
        xAxis.setLabel(xAxisName);
        series.setName(chartName);
        this.engine = engine;
        this.yDataToAdd = yDataToAdd;
        series.getData().add(new XYChart.Data(engine.getDayNumber(), yDataToAdd));
        chart.getData().add(series);
        this.getChildren().add(chart);
    }

    public void updateChart(double yDataToAdd){
        XYChart.Data newData = new XYChart.Data(engine.getDayNumber(), yDataToAdd);
        this.yDataToAdd = yDataToAdd;
        series.getData().add(newData);
    }


}
