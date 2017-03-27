package npv.data.algorithms;

import npv.data.MiniProjectData;
import npv.data.QueueData;

import java.util.ArrayList;

/**
 * Created by atsus on 2/2/2017.
 */
public class AlgorithmII<T> implements Algorithm<T> {

    private Integer T = 0;//T

    ArrayList<QueueData> queues = new ArrayList<>();
    ArrayList<MiniProjectData> miniProjectsList;

    QueueData queue1 = new QueueData();// todo may be redo
    QueueData queue2 = new QueueData();// in order to sort not only
    QueueData queue3 = new QueueData();// in 3 queues

    @Override
    public ArrayList<QueueData> sort(ArrayList<MiniProjectData> objectsList) {
        this.miniProjectsList = objectsList;
        queues.add(queue1);
        queues.add(queue2);
        queues.add(queue3);

        miniProjectsList.sort(new TimeComparator());//sorting by time (bigger the first)
        System.out.println("Results of sorting by time:");
        for (int i = 0; i < objectsList.size(); i++) {
            System.out.print(objectsList.get(i).getTime() + " ");
        }

        Integer sumOfTime = 0;
        for (MiniProjectData miniProjectData : objectsList) {
            sumOfTime = sumOfTime + miniProjectData.getTime();
        }

        T = Math.round(sumOfTime / queues.size());
        System.out.println("T= " + T);

        queue1.setDelta(T);
        queue2.setDelta(T);
        queue3.setDelta(T);

        sortByAlgorithm(objectsList.size());
        return queues;
    }

    @Override
    public ArrayList<QueueData> getQueues() {
        return queues;
    }

    private void sortByAlgorithm(int numberOfMiniProjects) {
        int k = 0;
        boolean addedToQueue;
        for (int i = 0; i < numberOfMiniProjects; i++) {
            addedToQueue = false;
            while (!addedToQueue) {
                for (int j = k; j < queues.size(); j++) {
                    QueueData queue = queues.get(j);
                    if (queue.getDelta() >= miniProjectsList.get(i).getTime()) {
                        queue.addToQueue(miniProjectsList.get(i));
                        queue.setDelta(queue.getDelta() - miniProjectsList.get(i).getTime());
                        addedToQueue = true;
                        if (k == queues.size() - 1){
                            k = 0;
                        } else {
                            k++;
                        }
                        break;
                    } else if (k == queues.size() - 1) {
                        System.out.println("Reached last queue switching to the 1st");
                        k = 0;
                        break;
                    } else {
                        k++;
                    }
                }
            }
        }
        for (QueueData queue:queues) {
            queue.getMiniProjects().sort(new FactorKComparator());
        }
        printQueueToConsole();
    }

    private void printQueueToConsole () {
        for (int i = 0; i < queues.size(); i++) {
            System.out.println("Queue #" + i);
            System.out.print("Da = " + queues.get(i).getDelta() + "\n");
            for (int j = 0; j < queues.get(i).getMiniProjects().size(); j++) {
                System.out.print("MiniProject: " + queues.get(i).getMiniProjects().get(j).getPeriodI()
                        + " | " + queues.get(i).getMiniProjects().get(j).getTime() + " \n");
            }
        }
    }
}
