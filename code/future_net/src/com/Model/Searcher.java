package com.Model;

import java.util.List;

/**
 * Created by Xingw on 2016/3/24.
 */
public class Searcher {
    private int pointID;
    private int totalValue;
    private int totalBestValue;
    //    private List<Searcher> nextSearchers;
    private int passednnum;
    private Searcher previous;

    public Searcher(int pointID, int totalValue, int totalBestValue, Searcher previous) {
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

    public int getPassednnum() {
        return passednnum;
    }

    public void setPassednnum(int passednnum) {
        this.passednnum = passednnum;
    }

    public int getTotalBestValue() {
        return totalBestValue;
    }

    public void setTotalBestValue(int totalBestValue) {
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

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public int getSortValue() {
        return (int)(totalBestValue - passednnum * 1);
    }
}
