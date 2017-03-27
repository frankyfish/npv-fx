/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data.algorithms;

import java.util.ArrayList;
import npv.data.MiniProjectData;
import npv.data.QueueData;

/**
 *
 * @author nick
 */
public interface Algorithm<T> {
    
    public ArrayList<QueueData> sort(ArrayList<MiniProjectData> list);

    public ArrayList<QueueData> getQueues();
    
}
