package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.scene.chart.XYChart;

public class DoubleChartBox extends SingleChartBox {

    public double yDataToAdd2;
    protected final XYChart.Series series2 = new XYChart.Series();

    public DoubleChartBox(SimulationEngine engine, String seriesName, String seriesName2, String xAxisName, double yDataToAdd, double yDataToAdd2) {
        super(engine, seriesName, xAxisName, yDataToAdd);
        this.yDataToAdd2 = yDataToAdd2;
        this.series2.setName(seriesName2);
        this.series2.getData().add(new XYChart.Data(engine.getDayNumber(), yDataToAdd2));
        this.chart.getData().add(this.series2);
        this.getChildren().clear();
        this.getChildren().add(this.chart);
    }

    public void updateChart(double yDataToAdd, double yDataToAdd2) {
        XYChart.Data newData = new XYChart.Data(this.engine.getDayNumber(), yDataToAdd);
        this.yDataToAdd = yDataToAdd;
        this.series.getData().add(newData);
        XYChart.Data newData2 = new XYChart.Data(this.engine.getDayNumber(), yDataToAdd2);
        this.yDataToAdd2 = yDataToAdd2;
        this.series2.getData().add(newData2);
    }

}
