package com.Model;

import java.util.List;

/**
 * Created by Xingw on 2016/3/24.
 */
public class Point {
    int LinkID;
    List<Point> NextPoints;
    Point previous;

    public int getLinkID() {
        return LinkID;
    }

    public void setLinkID(int linkID) {
        LinkID = linkID;
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
}
