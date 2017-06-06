package npv.fx.controllers;

/**
 * Created by nick on 06.06.17.
 */

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import npv.data.PlanData;

import java.util.*;


// TODO: use date for x-axis
public class GanttChartSample extends Application {

    private LinkedHashMap<Integer, ArrayList<PlanData>> plans;

    public GanttChartSample(LinkedHashMap<Integer, ArrayList<PlanData>> p) {
        this.plans = p;
    }

    @Override
    public void start(Stage stage) {

        stage.setTitle("Gantt Chart Sample");

        String[] machines = new String[]{"Queue#1", "Queue#2", "Queue#3"};

        String [] queuesNames = new String[plans.keySet().size()];
        Object[] keys = plans.keySet().toArray();
        for (int i=0; i < queuesNames.length; i++) {
            queuesNames[i] = String.valueOf(keys[i]);
        }

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChart<Number, String> chart = new GanttChart<Number, String>(xAxis, yAxis);
        xAxis.setLabel("");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);

        yAxis.setLabel("");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(machines)));

        chart.setTitle("Machine Monitoring");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);
        String machine;

        ArrayList<PlanData> planDatas = plans.get(0);
        XYChart.Series series1 = new XYChart.Series();
        for (int i=0; i < planDatas.size(); i++) {
            for (PlanData planData : planDatas) {
                series1.getData().add(new XYChart.Data(i, "Queue#1", new GanttChart.ExtraData(planData.getMiniProjectNumber(), "status-red")));
            }
        }

        chart.getData().addAll(series1);

//        chart.getStylesheets().add(getClass().getResource("resources/npv/fx/styles/ganttchart.css").toExternalForm());

        Scene scene = new Scene(chart, 620, 350);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}