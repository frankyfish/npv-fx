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
    int periodsTillEnd;

    public PlanDataCounter(ArrayList<QueueData> queueData) {
        this.queues = queueData;
        this.periodsTillEnd = calculateTimeScaleForQueues();
    }

    public void count() throws Exception {
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
                    for (int j = sumOfTime + 1; j <= periodsTillEnd; j++) {
                        planData.addToMiniProjectProfit(miniProject.getIncome());
                    }
                    plansPerQueue.add(planData);
                }
            }
            plans.put(queueNumber, plansPerQueue);
        }
        calculateRFlow();
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

    //officially: following method is ugliest ever
    private void calculateRFlow() throws Exception{
        for (int queueNumber = 0; queueNumber < queues.size(); queueNumber++) {
            PlanData rFlow = new PlanData(-42); //  Life, The Universe, and Everything
            ArrayList<PlanData> currentQueuePlan = plans.get(queueNumber);
            for (int i = 0; i < periodsTillEnd; i++) {
                Integer sumPerPeriod = 0;
                for (PlanData planData : currentQueuePlan) {
                    sumPerPeriod += planData.getProfitByMiniProject().get(i);
                }
                rFlow.addToMiniProjectProfit(sumPerPeriod);
                if (i == periodsTillEnd - 1) {
                    currentQueuePlan.add(rFlow);
                }
            }
        }
    }

    public void printQueueToConsole() {
        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i < queues.size(); i++) {
            ArrayList<PlanData> planPerQueue = plans.get(i);
            int index = i + 1;
            resultString.append("Queue #" + index + "\n");
            for (PlanData planData : planPerQueue) {
                Integer miniProjectName = planData.getMiniProjectNumber();
                if (miniProjectName != -42) {
                    resultString.append(miniProjectName + "\t");
                } else {
                    resultString.append("" + "\t");
                }
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
