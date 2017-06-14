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
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import npv.data.PlanData;
import npv.data.PlanDataCounter;
import npv.fx.GUIManager;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

import static npv.fx.GUIConstants.APP_ICON;


public class GanttChartSample extends Application {

    private LinkedHashMap<Integer, ArrayList<PlanData>> plans;
    private String urlToCss;
    private List<String> magicColors = new ArrayList<String>(Arrays.asList(
            "status-red", "status-green", "status-blue", "status-orange",
            "status-silver", "status-light-blue", "status-flying-banana", "status-morning-sky"));
    private String lastMagicColor;

    public GanttChartSample(LinkedHashMap<Integer, ArrayList<PlanData>> p, String url) {
        this.plans = p;
        this.urlToCss = url;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Queues Graphs");
        stage.getIcons().add(new Image(GUIManager.class.getResourceAsStream(APP_ICON)));
        String[] machines = buildQueuesNames();
        ArrayUtils.reverse(machines);//to make ordering from top to bottom

        Integer[] queuesNames = new Integer[plans.keySet().size()];
        Object[] keys = plans.keySet().toArray();
        for (int i = 0; i < queuesNames.length; i++) {
            queuesNames[i] = Integer.valueOf(keys[i].toString());
        }
        ArrayUtils.reverse(queuesNames);//to make ordering from top to bottom

        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();

        final GanttChart<Number, String> chart = new GanttChart<Number, String>(xAxis, yAxis);
        xAxis.setLabel("Time");
        xAxis.setTickLabelFill(Color.CHOCOLATE);
        xAxis.setMinorTickCount(4);
        xAxis.setTickLabelGap(4);

        yAxis.setLabel("Queue №");
        yAxis.setTickLabelFill(Color.CHOCOLATE);
        yAxis.setTickLabelGap(10);
        yAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(machines)));

        chart.setTitle("Queues Graphs");
        chart.setLegendVisible(false);
        chart.setBlockHeight(50);

        for (int i = 0; i < queuesNames.length; i++) {
            ArrayList<PlanData> planDatas = plans.get(queuesNames[i]);
            XYChart.Series series1 = new XYChart.Series();
            int posOnX = 0;
            for (int j = 0; j < planDatas.size(); j++) {
                PlanData planData = planDatas.get(j);
                if (!planData.getMiniProjectNumber()
                        .equals(PlanDataCounter.FAKE_MINIPROJECT_NUMBER_FOR_R)) {
                    series1.getData().add(
                            new XYChart.Data(posOnX, //j
                                    machines[i],
                                    new GanttChart.ExtraData(planData.getMiniProjectTime(), askMagicKaleidoscope())));
                    posOnX += planData.getMiniProjectTime();
                }
            }
            chart.getData().add(series1);
        }
        //todo revert elements in chart
        chart.getStylesheets().add(urlToCss);
        Scene scene = new Scene(chart, 820, 350);
        stage.setScene(scene);
        stage.show();
    }

    private String[] buildQueuesNames() {
        String[] queuesNames = new String[plans.keySet().size()];
        Object[] keys = plans.keySet().toArray();
        for (int i = 0; i < queuesNames.length; i++) {
            Integer number = Integer.valueOf((keys[i].toString()));
            number++;
            queuesNames[i] = String.valueOf("Queue#" + number.toString());
        }
        return queuesNames;
    }

    private String askMagicKaleidoscope() {
        if (lastMagicColor == null) {
            lastMagicColor = magicColors.get(0);
            return lastMagicColor;
        } else {
            int lastUsedColorId = magicColors.indexOf(lastMagicColor);
            if (lastUsedColorId != magicColors.size() - 1) {
                lastMagicColor = magicColors.get(lastUsedColorId + 1);
                return lastMagicColor;
            } else {
                String color = lastMagicColor;
                lastMagicColor = null;
                return color;
            }

        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}