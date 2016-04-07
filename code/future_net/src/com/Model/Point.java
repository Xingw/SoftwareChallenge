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
    //后面的点
    private List<Point> next;
    //是否特殊点
    private boolean special;

    private Linker out;
    private Linker in;
    private int prespecial;
    private List<Integer> Link;

    public Point(int pointID) {
        this.pointID = pointID;
        this.previous = new ArrayList<>();
        this.next = new ArrayList<>();
        this.grade = 0;
        this.special = false;
        this.prespecial = -1;
    }

    public List<Integer> getLink() {
        return Link;
    }

    public void setLink(List<Integer> link) {
        Link = link;
    }

    public int getPrespecial() {
        return prespecial;
    }

    public void setPrespecial(int prespecial) {
        this.prespecial = prespecial;
    }

    public List<Point> getNext() {
        return next;
    }

    public void setNext(List<Point> next) {
        this.next = next;
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
    public boolean isSpecial() {
        return special;
    }
    public void setSpecial(boolean special) {
        this.special = special;
    }

    public void setLinker(Linker linker){
        if(linker.isOut()){
            out = linker;
        }else {
            in = linker;
        }
    }

    public Linker getLinker(boolean out){
        if(!out){
            return this.out;
        }else{
            return this.in;
        }
    }
}
