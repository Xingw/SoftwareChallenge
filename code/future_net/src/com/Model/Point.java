package com.Model;

import java.util.List;

/**
 * Created by Xingw on 2016/3/24.
 */
public class Point {
    private int pointID;
    private int totalValue;
    private int totalBestValue;
    private List<Point> NextPoints;
    private Point previous;
    private int passednum;

    public Point(int pointID, int totalValue,int totalBestValue, List<Point> nextPoints, Point previous) {
        this.pointID = pointID;
        this.totalValue = totalValue;
        NextPoints = nextPoints;
        this.previous = previous;
        passednum = 0;
    }

    public int getTotalBestValue() {
        return totalBestValue;
    }

    public void setTotalBestValue(int totalBestValue) {
        this.totalBestValue = totalBestValue;
    }

    public Point() {
        pointID = -1;
    }

    public int getPointID() {
        return pointID;
    }

    public void setPointID(int pointID) {
        this.pointID = pointID;
    }

    public List<Point> getNextPoints() {
        return NextPoints;
    }

    public void setNextPoints(List<Point> nextPoints) {
        NextPoints = nextPoints;
    }

    public Point getPrevious() {
        return previous;
    }

    public void setPrevious(Point previous) {
        this.previous = previous;
    }

    public int getTotalValue(){
        return totalValue;
    }

    public void setTotalValue(int totalValue){
        this.totalValue=totalValue;
    }
}
