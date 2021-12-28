package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.VBox;

public class SingleChartBox extends VBox {

    protected final NumberAxis xAxis = new NumberAxis();
    protected final NumberAxis yAxis = new NumberAxis();
    protected final LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
    protected final XYChart.Series series = new XYChart.Series();
    protected final SimulationEngine engine;
    protected double yDataToAdd;

    public SingleChartBox(SimulationEngine engine, String chartName, String xAxisName, double yDataToAdd) {
        super();
        this.xAxis.setLabel(xAxisName);
        this.series.setName(chartName);
        this.engine = engine;
        this.yDataToAdd = yDataToAdd;
        this.series.getData().add(new XYChart.Data(engine.getDayNumber(), yDataToAdd));
        this.chart.getData().add(this.series);
        this.chart.setMaxSize(300, 230);
        this.getChildren().add(this.chart);
    }

    public void updateChart(double yDataToAdd) {
        XYChart.Data newData = new XYChart.Data(this.engine.getDayNumber(), yDataToAdd);
        this.yDataToAdd = yDataToAdd;
        this.series.getData().add(newData);
        this.chart.setMaxSize(300, 230);

    }


}
