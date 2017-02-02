/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data.algorithms;

import java.util.Comparator;
import npv.data.MiniProjectData;

/**
 *
 * @author nick
 */
public class FactorKComparator implements Comparator<MiniProjectData>{

    @Override
    public int compare(MiniProjectData o1, MiniProjectData o2) {
        return o1.getFactorK().compareTo(o2.getFactorK());
    }
    
}
