package npv.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Created by nick on 13.03.17.
 */
public class PlanDataCounter {
    private ArrayList<QueueData> queues;
    LinkedHashMap<Integer, ArrayList<PlanData>> plans = new LinkedHashMap<>();

    public PlanDataCounter(ArrayList<QueueData> queueData) {
        this.queues = queueData;
    }

    public void count() throws Exception{
        int periodsTillEnd = calculateTimeScaleForQueues();
        for (int queueNumber = 0; queueNumber < queues.size(); queueNumber++) {
            ArrayList<PlanData> plansPerQueue = new ArrayList<>();
            int sumOfTime = 0;
            for (MiniProjectData miniProject : queues.get(queueNumber).getMiniProjects()) {
                PlanData planData = new PlanData(miniProject.getPeriodI());
                int time = miniProject.getTime();
                sumOfTime += time;
                if (plansPerQueue.size() == 0) {
                    //for 1st MP
                    for (int i = 0; i < time; i++) {
                        planData.addToMiniProjectProfit(0);
                    }
                    for (int j = time + 1; j <= periodsTillEnd; j++) {
                        planData.addToMiniProjectProfit(miniProject.getIncome());
                    }
                    plansPerQueue.add(planData);
                } else {
                    for (int i = 0; i < sumOfTime; i++) {
                        planData.addToMiniProjectProfit(0);
                    }
                    for (int j = sumOfTime + 1; j<= periodsTillEnd; j++) {
                        planData.addToMiniProjectProfit(miniProject.getIncome());
                    }
                    plansPerQueue.add(planData);
                }
            }
            plans.put(queueNumber, plansPerQueue);
        }
        printQueueToConsole();
    }

    private int calculateTimeScaleForQueues() {
        Integer result = 0;
        int sum = 0;
        ArrayList<Integer> timeSumPerQueue = new ArrayList<>();

        for (QueueData queue : queues) {
            for (MiniProjectData miniProject : queue.getMiniProjects()) {
                sum += miniProject.getTime();
            }
            timeSumPerQueue.add(sum);
            sum = 0;
        }
        result = Collections.max(timeSumPerQueue);
        result++;
        //+1 in order to get at least the minimal profit from the last MP in queue, before ending MultiProject
        System.out.println("Timescale for queues: " + result);
        return result;
    }

    public void printQueueToConsole(){
        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i < queues.size(); i++) {
            ArrayList<PlanData> planPerQueue = plans.get(i);
            int index = i;
            resultString.append("Queue #" + index++ + "\n");
            for (PlanData planData : planPerQueue) {
                resultString.append(planData.getMiniProjectNumber() + "\t");
                for (Integer period : planData.getProfitByMiniProject()) {
                    resultString.append(period + " ");
                }
                resultString.append("\n");
            }
            resultString.append("\n\n");
        }
        System.out.println(resultString.toString());
    }
}
