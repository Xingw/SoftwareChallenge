/**
 * 实现代码文件
 *
 * @author XXX
 * @version V1.0
 * @since 2016-3-4
 */
package com.routesearch.route;

import com.Model.MinValueHeap;
import com.Model.Point;

import java.util.ArrayList;
import java.util.List;

import static com.filetool.util.Util.FormatData;

public final class Route {
    //路径信息
    private static short[][] data;
    private static short[][] routenum;
    //起点
    private static short start;
    //终点
    private static short end;
    //特殊点
    private static short[] pass;

    private static Point currentminpoint = null;

    private static MinValueHeap minValueHeap;

    /**
     * 你需要完成功能的入口
     *
     * @author XXX
     * @version V1
     * @since 2016-3-4
     */
    public static String searchRoute(String graphContent, String condition) {
        short[][][]  result= FormatData(graphContent);
        data = result[0];
        routenum = result[1];
        FormatCondition(condition);
        minValueHeap = MinValueHeap.getInstance();
        findMinValueRoute();
        return FormatResult();
    }

    /**
     * 格式化结果输出
     * @return
     */
    private static String FormatResult() {
        if(currentminpoint == null) return "NA";
        Point pre = currentminpoint.getPrevious();
        Point current = currentminpoint;
        StringBuffer result = new StringBuffer();
        while (pre !=null){
            result.insert(0,routenum[pre.getPointID()][current.getPointID()]);
            current = pre;
            pre = current.getPrevious();
            if(pre !=null)
                result.insert(0,"|");
        }
        return result.toString();
    }

    private static void findMinValueRoute() {
        Point startpoint = new Point(start, 0, null, null);
        startpoint.setNextPoints(findnextPointList(startpoint));
        while (!minValueHeap.isHeapEmpty()) {
            Point point=minValueHeap.popMin();
            point.setNextPoints(findnextPointList(point));
//            minValueHeap.insert();
        }
    }

    private static List<Point> findnextPointList(Point parent) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < data[0].length; i++) {
            int value = data[parent.getPointID()][i];
            if (value != 0) {
                Point point = new Point(i, parent.getTotalValue() + value, null, parent);
                //判断这个点是不是结尾
                if (point.getPointID() == end) {
                    //有没有通过所有特殊点
                    if (hasallspecialpoint(point)) {
                        //如果通过了所有特殊点 它是不是最短路径 如果不是则丢弃
                        if (currentminpoint == null) {
                            currentminpoint = point;
                        } else if (point.getTotalValue() < currentminpoint.getTotalValue()) {
                            currentminpoint = point;
                        }
                    }
                } else {
                    if(haspassedpoint(point)){
                        continue;
                    }
                    //判断这个点如果超出了最短路径则丢弃
                    if (currentminpoint != null) {
                        if (point.getTotalValue() < currentminpoint.getTotalValue()) {
                            minValueHeap.insert(point);
                            points.add(point);
                        }
                    } else {
                        minValueHeap.insert(point);
                        points.add(point);
                    }
                }
            }
        }
        return points;
    }

    /**
     * 判断是否重复经过某点
     * @param point
     * @return
     */
    private static boolean haspassedpoint(Point point) {
        int pointId = point.getPointID();
        point = point.getPrevious();
        while (point !=null){
            if(point.getPointID() == pointId)
                return true;
            point = point.getPrevious();
        }
        return false;
    }

    /**
     * 是否通过所有特殊点
     * @param point
     * @return
     */
    private static boolean hasallspecialpoint(Point point) {
        int passnumber = pass.length;
        while (!(point==null || passnumber==0)){
            for (int i = 0; i < pass.length; i++) {
                if(point.getPointID() == pass[i])
                {
                    passnumber--;
                    break;
                }
            }
            point = point.getPrevious();
        }
        if (passnumber == 0)return true;
        return false;
    }

    /**
     * 格式化条件信息
     *
     * @param condition 条件字符串
     */
    public static void FormatCondition(String condition) {
        String[] Info = condition.split(",");
        start = Short.parseShort(Info[0]);
        end = Short.parseShort(Info[1]);
        //截取掉换行符
        Info[2] = Info[2].substring(0, Info[2].length() - 1);
        String[] passes = Info[2].split("\\|");
        pass = new short[passes.length];
        for (int i = 0; i < passes.length; i++) {
            pass[i] = Short.parseShort(passes[i]);
        }
    }


}