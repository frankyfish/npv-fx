/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data.algorithms;

import java.util.Comparator;
import npv.data.QueueData;

/**
 *
 * @author nick
 */
public class DeltaComparator implements Comparator<QueueData> {

    @Override
    public int compare(QueueData o1, QueueData o2) {
        return o1.getDelta().compareTo(o2.getDelta());
    }
    
}
