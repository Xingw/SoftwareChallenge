package com.Model;

import java.util.List;

/**
 * Created by Xingw on 2016/3/24.
 */
public class Searcher {
    private int pointID;
    private int parentID;
    private boolean out;
    private double totalValue;
    private double totalBestValue;
    //    private List<Searcher> nextSearchers;
    private int passednnum;
    private Searcher previous;

    public Searcher(int pointID, double totalValue, double totalBestValue, Searcher previous) {
        this.pointID = pointID;
        this.totalValue = totalValue;
        this.totalBestValue = totalBestValue;
        this.previous = previous;
        if (previous != null) {
            this.passednnum = previous.getPassednnum() + 1;
        } else {
            passednnum = 0;
        }
    }

    public Searcher(int parentID, int pointID, boolean out, Searcher previous) {
        this.parentID = parentID;
        this.pointID = pointID;
        this.out = out;
        this.previous = previous;
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

    public int getPassednnum() {
        return passednnum;
    }

    public void setPassednnum(int passednnum) {
        this.passednnum = passednnum;
    }

    public double getTotalBestValue() {
        return totalBestValue;
    }

    public void setTotalBestValue(double totalBestValue) {
        this.totalBestValue = totalBestValue;
    }

    public Searcher() {
        pointID = -1;
        passednnum = 0;
    }

    public int getPointID() {
        return pointID;
    }

    public void setPointID(int pointID) {
        this.pointID = pointID;
    }

    public Searcher getPrevious() {
        return previous;
    }

    public void setPrevious(Searcher previous) {
        this.previous = previous;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(double totalValue) {
        this.totalValue = totalValue;
    }

    public int getSortValue() {
        return (int)(totalBestValue - passednnum * 3);
    }
}
