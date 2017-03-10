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
            result.append("Queue #" + queueNumber + "\n");
            queues.get(i).getMiniProjects().forEach(miniProjectData -> result.append(miniProjectData.getPeriodI()+"\n"));
            result.append("=============\n");
        }
        return result.toString();
    }

}
