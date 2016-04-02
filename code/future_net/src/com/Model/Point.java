package com.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2016/4/2.
 */
public class Point {
    //点ID
    private int pointID;
    //优先级
    private double grade;
    //前面的点
    private List<Point> previous;

    public Point(int pointID) {
        this.pointID = pointID;
        this.previous = new ArrayList<>();
        this.grade = 0;
    }

    public int getPointID() {
        return pointID;
    }

    public void setPointID(int pointID) {
        this.pointID = pointID;
    }

    public List<Point> getPrevious() {
        return previous;
    }

    public void setPrevious(List<Point> previous) {
        this.previous = previous;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        if(this.grade < grade) {
            this.grade = grade;
        }
    }
}
