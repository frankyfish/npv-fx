/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data.algorithms;

import java.util.ArrayList;
import java.util.Collections;
import npv.data.MiniProjectData;
import npv.data.QueueData;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment;

/**
 *
 * @author nick
 */
public class AlgorithmI<T> implements Algorithm<T> {

    private Integer T = 0;//T

    ArrayList<QueueData> queues = new ArrayList<>();
    ArrayList<MiniProjectData> objectsList;

    QueueData queue1 = new QueueData();// todo may be redo
    QueueData queue2 = new QueueData();// in order to sort not only
    QueueData queue3 = new QueueData();// in 3 queues

    @Override
    public void sort(ArrayList<MiniProjectData> objList) {
        this.objectsList = objList;
        queues.add(queue1);
        queues.add(queue2);
        queues.add(queue3);

        objectsList.sort(new TimeComparator());//sorting by time (bigger the first)
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
        //maybe move following lines to sortByAlgorithm method to make it more clear
        queue1.addToQueue(objectsList.get(0));
        queue1.setDelta(queue1.getDelta() - objectsList.get(0).getTime());
        queue2.addToQueue(objectsList.get(1));
        queue2.setDelta(queue2.getDelta() - objectsList.get(1).getTime());
        queue3.addToQueue(objectsList.get(2));
        queue3.setDelta(queue3.getDelta() - objectsList.get(2).getTime());

        sortByAlgorithm();
    }

    private void sortByAlgorithm() {
        int k = 0;
        boolean isAddedToQueue;
        for (int i = 3; i < objectsList.size(); i++) {
            isAddedToQueue = false;
            while (!isAddedToQueue) {
                for (int j = k; j < queues.size(); j++) {
                    QueueData queue = queues.get(j);
                    if (queue.getDelta() >= objectsList.get(i).getTime()) {
                        queue.addToQueue(objectsList.get(i));//adding MP to queue
                        queue.setDelta(queue.getDelta() - objectsList.get(i).getTime());//calculating time for que after adding
                        k = j;
                        isAddedToQueue = true;
                        break;
                    } else if (j == queues.size() - 1 && checkTimeLeftInQueues(objectsList.get(i))) {
                        k = 0;
                        System.out.println("Switched queue loop to beginning for " + i + " miniproject");
                    } else if (j == queues.size() - 1 && !checkTimeLeftInQueues(objectsList.get(i))) {
                        //todo check, probably this part doesn't work
                        System.out.println("Trying to add time to the queue, for " + i + " miniproject");
                        ArrayList<QueueData> maxDeltaFromRestingQueues = queues;
                        QueueData queueWithMaxRestingDelta
                                = Collections.max(maxDeltaFromRestingQueues, new DeltaComparator());
                        Integer maxRestingDelta = queueWithMaxRestingDelta.getDelta();
                        System.out.println("Max resting delta = " + maxRestingDelta);

                        for (QueueData curQueue : queues) {
                            curQueue.setDelta(curQueue.getDelta()
                                    + objectsList.get(i).getTime() - maxRestingDelta);
                            //T = T +ti - △k* и возвращаемся к шагу 5.
                        }
                        k = 0;
                    }
                }
            }
        }
        for (QueueData queue:queues) {
            queue.getMiniProjects().sort(new FactorKComparator());
        }
        for (int i = 0; i < queues.size(); i++) {
            System.out.println("Queue #" + i);
            for (int j = 0; j < queues.get(i).getMiniProjects().size(); j++) {
                System.out.print("MiniProject: " + queues.get(i).getMiniProjects().get(j).getPeriodI()
                        + " | " + queues.get(i).getMiniProjects().get(j).getTime() + " \n");
            }
        }
    }

    private boolean checkTimeLeftInQueues(MiniProjectData mPD) {
        ArrayList<Integer> deltasFromQueues = new ArrayList<>();
        boolean enoughTimeInQueues = false;
        for (QueueData queue : queues) {
            deltasFromQueues.add(queue.getDelta());
        }

        for (Integer delta : deltasFromQueues) {
            if (delta >= mPD.getTime()) {
                enoughTimeInQueues = true;
            }
        }

        return enoughTimeInQueues;
    }

}
