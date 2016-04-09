package com.Model;

import java.util.List;

/**
 * Created by Xingw on 2016/4/7.
 */
public class Linker {
    private int pointID;
    private int parentID;
    private boolean out;
    //    private List<Searcher> nextSearchers;
    private List<Integer> points;

    public Linker(int parentID, int pointID, boolean out, List<Integer> points) {
        this.parentID = parentID;
        this.pointID = pointID;
        this.out = out;
        this.points = points;
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public boolean isOut() {
        return out;
    }

    public void setOut(boolean out) {
        this.out = out;
    }


    public int getPointID() {
        return pointID;
    }

    public void setPointID(int pointID) {
        this.pointID = pointID;
    }

    public List<Integer> getPoints() {
        return points;
    }

    public void setPoints(List<Integer> points) {
        this.points = points;
    }

    public void add(int id) {
        if (out){
            points.add(id);
        }else {
            points.add(0,id);
        }
    }
}
