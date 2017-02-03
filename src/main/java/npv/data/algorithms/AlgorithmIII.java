package npv.data.algorithms;

import npv.data.MiniProjectData;
import npv.data.QueueData;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by atsus on 2/3/2017.
 */
public class AlgorithmIII<T> implements Algorithm<T>{

    private Integer T = 0;//T

    ArrayList<QueueData> queues = new ArrayList<>();
    ArrayList<MiniProjectData> miniProjectsList;

    QueueData queue1 = new QueueData();// todo may be redo
    QueueData queue2 = new QueueData();// in order to sort not only
    QueueData queue3 = new QueueData();// in 3 queues

    @Override
    public void sort(ArrayList<MiniProjectData> objectsList) {
        this.miniProjectsList = objectsList;
        queues.add(queue1);
        queues.add(queue2);
        queues.add(queue3);

        miniProjectsList.sort(new FactorKComparator());

        queues.forEach(queueData -> queueData.setDelta(0));
        sortByAlgorithm(objectsList.size());
    }

    private void sortByAlgorithm (int miniProjectsNumber) {
        for (int i = 0; i < miniProjectsNumber; i++) {
            QueueData queueWithMinDelta = findMinInQueue();
            MiniProjectData currentMiniProject = miniProjectsList.get(i);
            queueWithMinDelta.addToQueue(currentMiniProject);
            Integer queueDelta = queueWithMinDelta.getDelta();
            queueWithMinDelta.setDelta(queueDelta + currentMiniProject.getTime());
        }
        printQueueToConsole();
    }

    private QueueData findMinInQueue () {
        return Collections.min(queues, new DeltaComparator());
    }

    private void printQueueToConsole () {
        for (int i = 0; i < queues.size(); i++) {
            System.out.println("Queue #" + i);
            for (int j = 0; j < queues.get(i).getMiniProjects().size(); j++) {
                System.out.print("MiniProject: " + queues.get(i).getMiniProjects().get(j).getPeriodI()
                        + " | " + queues.get(i).getMiniProjects().get(j).getTime() + " \n");
            }
        }
    }
}
