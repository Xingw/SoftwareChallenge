package com.filetool.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xingw on 2016/3/24.
 */
public class Util {
    public static short[][] FormatData(String graphContent){
        String[] routes=graphContent.split("\\n");
        String[] sizes=routes[routes.length-1].split(",");
        int size = Integer.parseInt(sizes[1]);
        short[][] data =new short[size+1][size+1];
        for (String route : routes) {
            String[] info=route.split(",");
            data[Integer.parseInt(info[1])][Integer.parseInt(info[2])] = Short.parseShort(info[3]);
        }
        return data;
    }
}
