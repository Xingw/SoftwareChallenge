package com.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2016/4/6.
 */
public class LinkSpecialHeap {
    private static List<Linker> searcherList;
    private static LinkSpecialHeap single;

    public LinkSpecialHeap() {
        this.searcherList = new ArrayList<>();
    }

    public static LinkSpecialHeap getInstance(){
        if (single == null){
            single = new LinkSpecialHeap();
        }
        return single;
    }

    public static void popUseless(Linker existLinker,Linker preLinker){
        for (Linker linker : searcherList) {
            if ((linker.getParentID() == existLinker.getParentID() && linker.isOut() ==
                    existLinker.isOut()) || (linker.getParentID() == preLinker.getParentID() && linker.isOut() ==
                    preLinker.isOut())){
                searcherList.remove(linker);
            }
        }
    }

    public static Linker popMin(){
        Linker searcher = searcherList.get(0);
        searcherList.remove(0);
        return searcher;
    }

    public static List<Linker> getSearcherList() {
        return searcherList;
    }

    public static void setSearcherList(List<Linker> searcherList) {
        LinkSpecialHeap.searcherList = searcherList;
    }

    public boolean isHeadEmpty() {
        return searcherList.isEmpty();
    }
}