package npv.data;

import java.util.*;

/**
 * Created by nick on 13.03.17.
 */
public class PlanDataCounter {
    public static final Integer FAKE_MINIPROJECT_NUMBER_FOR_R_SUM = -71;
    public static final Integer FAKE_MINIPROJECT_NUMBER_FOR_R = -42;

    private ArrayList<QueueData> queues;
    LinkedHashMap<Integer, ArrayList<PlanData>> plans = new LinkedHashMap<>();
    int periodsTillEnd;

    public PlanDataCounter(ArrayList<QueueData> queueData) {
        this.queues = queueData;
        this.periodsTillEnd = calculateTimeScaleForQueues();
    }
    //this method consumes MiniProjectData and produces PlanData
    public void count() throws Exception {
        for (int queueNumber = 0; queueNumber < queues.size(); queueNumber++) {
            ArrayList<PlanData> plansPerQueue = new ArrayList<>();
            int sumOfTime = 0;
            for (MiniProjectData miniProject : queues.get(queueNumber).getMiniProjects()) {
                PlanData planData = new PlanData(miniProject.getPeriodI(), miniProject.getGain() * -1, miniProject.getTime());
                int time = miniProject.getTime();
                sumOfTime += time;
                if (plansPerQueue.size() == 0) {
                    //for 1st MP
                    for (int i = 0; i < time; i++) {
                        planData.addToMiniProjectProfit(0.0);
                    }
                    for (int j = time + 1; j <= periodsTillEnd; j++) {
                        planData.addToMiniProjectProfit(miniProject.getIncome().doubleValue());
                    }
                    plansPerQueue.add(planData);
                } else {
                    for (int i = 0; i < sumOfTime; i++) {
                        planData.addToMiniProjectProfit(0.0);
                    }
                    for (int j = sumOfTime + 1; j <= periodsTillEnd; j++) {
                        planData.addToMiniProjectProfit(miniProject.getIncome().doubleValue());
                    }
                    plansPerQueue.add(planData);
                }
            }
            plans.put(queueNumber, plansPerQueue);
        }
        calculateRFlow();
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
    private void calculateRFlow() throws Exception {
        for (int queueNumber = 0; queueNumber < queues.size(); queueNumber++) {
            //fake MiniProject with sum
            PlanData rFlow = new PlanData(FAKE_MINIPROJECT_NUMBER_FOR_R, 0.0, 0); //  Life, The Universe, and Everything
            ArrayList<PlanData> currentQueuePlan = plans.get(queueNumber);
            for (int i = 0; i < periodsTillEnd; i++) {
                Double sumPerPeriod = 0.0;
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

    public String getStringRepresentation() {
        StringBuilder resultString = new StringBuilder();
        for (int i = 0; i < queues.size(); i++) {
            ArrayList<PlanData> planPerQueue = plans.get(i);
            int index = i + 1;
            resultString.append("Queue #" + index + "\n");
            for (PlanData planData : planPerQueue) {
                Integer miniProjectName = planData.getMiniProjectNumber();
                if (miniProjectName != FAKE_MINIPROJECT_NUMBER_FOR_R) {
                    resultString.append(miniProjectName + "\t");
                } else {
                    resultString.append("" + "\t");
                }
                for (Double period : planData.getProfitByMiniProject()) {
                    resultString.append(period + " ");
                }
                resultString.append("\n");
            }
            resultString.append("\n\n");
        }
        return resultString.toString();
    }

    public LinkedHashMap<Integer, ArrayList<PlanData>> getPlans() {
        return plans;
    }

    public static PlanData getSumOfRFlow(LinkedHashMap<Integer, ArrayList<PlanData>> plans) {
        List<Double> result
                = new ArrayList<>(plans.get(0).get(0).getProfitByMiniProject().size());
        for (Map.Entry<Integer, ArrayList<PlanData>> plan : plans.entrySet()) {
            for (PlanData miniProject : plan.getValue()) {
                if (miniProject.getMiniProjectNumber().equals(FAKE_MINIPROJECT_NUMBER_FOR_R)) {
                    ArrayList<Double> profit = miniProject.getProfitByMiniProject();
                    if (result.isEmpty()) {
                        result.addAll(profit);
                    } else {
                        for (int i = 0; i < profit.size(); i++) {
                            result.add(i, result.remove(i) + profit.get(i)); //sum of Rs from different queues
                        }
                    }
                }
            }
        }
        PlanData sumOfRFlow = new PlanData(FAKE_MINIPROJECT_NUMBER_FOR_R_SUM, new ArrayList<>(result));
        return sumOfRFlow;
    }

    //returns List of sum values per queues with according order
    public static List<Double> getMiniProjectsCostsPerQueue(LinkedHashMap<Integer, ArrayList<PlanData>> plans) {
        List<Double> sumOfMiniProjectsCostPerQueue = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<PlanData>> plan : plans.entrySet()) {
            Double sumOfCost = 0.0;
            for (PlanData planData : plan.getValue()) {
                sumOfCost += planData.getMiniProjectCost();
            }
            sumOfMiniProjectsCostPerQueue.add(sumOfCost);
        }
        return sumOfMiniProjectsCostPerQueue;
    }

    public static Double getCostOfAllQueues(LinkedHashMap<Integer, ArrayList<PlanData>> plans) {
        Double result = 0.0;
        List<Double> costs = getMiniProjectsCostsPerQueue(plans);
        for (Double cost : costs) {
            result += cost;
        }
        return result;
    }
}
