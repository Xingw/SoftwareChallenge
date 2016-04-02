package com.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2016/4/1.
 */
public class Topo {
    //路径号
    short LinkId;
    //消费
    short cost;
    //前一个点
    List<Topo> previous;
    public Topo(short linkId, short cost) {
        LinkId = linkId;
        this.cost = cost;
        previous = new ArrayList<>();
    }

    public Topo() {
    }

    public List<Topo> getPrevious() {
        return previous;
    }

    public void setPrevious(List<Topo> previous) {
        this.previous = previous;
    }

    public short getLinkId() {
        return LinkId;
    }

    public void setLinkId(short linkId) {
        LinkId = linkId;
    }

    public short getCost() {
        return cost;
    }

    public void setCost(short cost) {
        this.cost = cost;
    }

}
