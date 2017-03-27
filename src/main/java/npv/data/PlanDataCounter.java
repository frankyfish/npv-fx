package npv.data;

import java.util.ArrayList;

/**
 * Created by nick on 13.03.17.
 */
public class PlanDataCounter {
    private ArrayList<QueueData> queues;

    public PlanDataCounter(ArrayList<QueueData> queueData) {
        this.queues = queueData;
    }

    private void count() {
        StringBuilder resultString = new StringBuilder();
        ArrayList<MiniProjectData> miniProjects = new ArrayList<>();
        for (QueueData queue : queues) {
            miniProjects = queue.getMiniProjects();
            for (MiniProjectData miniProject : miniProjects) {
                int time = miniProject.getTime();
                for (int i = 0; i <= time; i++) {

                }
            }
        }
    }
}
