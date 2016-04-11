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
    private boolean linkstate = false;
    private int prespecial;
    private int nextspecial;

    public Point(int pointID) {
        this.pointID = pointID;
        this.previous = new ArrayList<>();
        this.next = new ArrayList<>();
        this.grade = 0;
        this.special = false;
        this.prespecial = -1;
        this.nextspecial = -1;
    }


    public int getNextspecial() {
        return nextspecial;
    }

    public void setNextspecial(int nextspecial) {
        this.nextspecial = nextspecial;
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
        if(special){
            out = linker;
        }else if(linker.isOut()){
            out = linker;
        }else {
            in = linker;
        }
    }

    public void cleanLinker(Linker linker){
        if(linker.isOut()){
            out = null;
        }else {
            in = null;
        }
    }

    public Linker getLinker(boolean out){
        if(special){
            return this.out;
        }
        if(!out){
            return this.out;
        }else{
            return this.in;
        }
    }

    public void cleanLinker() {
        out = null;
        in = null;
    }

    public boolean isLinkstate() {
        return linkstate;
    }

    public void setLinkstate(boolean linkstate) {
        this.linkstate = linkstate;
    }

    public void cleanLinkernext() {
        for (Point point : next) {
            if (point.isSpecial() || point.isLinkstate())continue;
            point.cleanLinker();
        }
    }

    public void cleanLinkerpre() {
        for (Point previou : previous) {
            if (previou.isSpecial() || previou.isLinkstate())continue;
            previou.cleanLinker();
        }
    }

    public void setLinker(Linker nextLinker, boolean b) {
        out = nextLinker;
    }
}
