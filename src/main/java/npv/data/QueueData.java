/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npv.data;

import java.util.ArrayList;

/**
 *
 * @author nick
 * Used in Algorithms
 */
public class QueueData {
    
    private ArrayList<MiniProjectData> miniProjects
            = new ArrayList<>();
    private Integer delta; //time

    public ArrayList<MiniProjectData> getMiniProjects() {
        return miniProjects;
    }

    public Integer getDelta() {
        return delta;
    }

    public void setMiniProjects(ArrayList<MiniProjectData> miniProjects) {
        this.miniProjects = miniProjects;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }
    
    public void addToQueue(MiniProjectData miniProject) {
        miniProjects.add(miniProject);
    }
}
