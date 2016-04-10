package com.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2016/4/10.
 */
public class InsertSpecialHeap {
    private static List<Linker> inList;
    private static List<Linker> outList;
    private static boolean step;
    private static InsertSpecialHeap single;

    public InsertSpecialHeap() {
        this.inList = new ArrayList<>();
        this.outList = new ArrayList<>();
        step = true;
    }

    public static InsertSpecialHeap getInstance(){
        if (single == null){
            single = new InsertSpecialHeap();
        }
        return single;
    }

    public static Linker popMin(){
        if((step && inList.size() !=0) || outList.size() ==0) {
            Linker searcher = inList.get(0);
            inList.remove(0);
            return searcher;
        }else {
            Linker searcher = outList.get(0);
            outList.remove(0);
            return searcher;
        }
    }

    public static boolean isEmpty(){
        if(inList.size() ==0 && outList.size() ==0) return true;
        return false;
    }

    public static List<Linker> getInList() {
        return inList;
    }

    public static void setInList(List<Linker> inList) {
        InsertSpecialHeap.inList = inList;
    }

    public static List<Linker> getOutList() {
        return outList;
    }

    public static void setOutList(List<Linker> outList) {
        InsertSpecialHeap.outList = outList;
    }
}
