package npv.data.utils;

import npv.data.QueueData;

import java.util.ArrayList;

/**
 * Created by nick on 10.03.17.
 */
public class QueueDataUtils {

    public static String getStringOfQueueData (ArrayList<QueueData> queues) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < queues.size(); i++) {
            int queueNumber = i + 1;
            result.append("Queue #" + queueNumber + ": ");
            queues.get(i).getMiniProjects().forEach(miniProjectData -> result.append(miniProjectData.getPeriodI() + " "));
            result.append("\n");
        }
        return result.toString();
    }

}
