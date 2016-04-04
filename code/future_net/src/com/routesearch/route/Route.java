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
import com.Model.Searcher;
import com.Model.Topo;
import com.filetool.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public final class Route {
    //路径信息
//    private static short[][] data;
//    private static short[][] routenum;
    //线路信息
    private static Topo[][] topo;
    //所有点
    private static Point[] points;
    //起点
    private static short start;
    //终点
    private static short end;
    //特殊点
    private static short[] pass;
    //计时
    public static boolean timeout = false;

    private static Searcher currentminpoint = null;

    private static MinValueHeap minValueHeap;

    /**
     * 你需要完成功能的入口
     *
     * @author XXX
     * @version V1
     * @since 2016-3-4
     */
    public static String searchRoute(String graphContent, String condition) {
        //计时器
        SetTimer();
        //格式化路线
        FormatData(graphContent);
        //格式化条件
        FormatCondition(condition);
        //格式化优先级
        FormatGrade();
        LogUtil.printLog("Format");
        //建立最小堆
        minValueHeap = MinValueHeap.getInstance();
        findMinValueRoute();
        return FormatResult();
    }

    private static void SetTimer() {
        Timer timer = new Timer();
        timer.schedule(new Task(), 9 * 1000 + 800);
    }

    private static void FormatGrade() {
        for (short pas : pass) {
            points[pas].setGrade(40);
            for (Point point : points[pas].getPrevious()) {
                point.setGrade(6);
                for (Point point1 : point.getPrevious()) {
                    point1.setGrade(5);
                    for (Point point2 : point1.getPrevious()) {
                        point2.setGrade(4);
                        for (Point point3 : point2.getPrevious()) {
                            point3.setGrade(3);
                            for (Point point4 : point3.getPrevious()) {
                                point4.setGrade(2);
                                for (Point point5 : point4.getPrevious()) {
                                    point5.setGrade(1);
                                    for (Point point6 : point5.getPrevious()) {
                                        point6.setGrade(0.9);
                                        for (Point point7 : point6.getPrevious()) {
                                            point7.setGrade(0.8);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        points[end].setGrade(100);
//        for (Point point : points[end].getPrevious()) {
//            point.setGrade(0.2);
//            for (Point point1 : point.getPrevious()) {
//                point1.setGrade(0.3);
//                for (Point point2 : point1.getPrevious()) {
//                    point2.setGrade(0.6);
//                    for (Point point3 : point2.getPrevious()) {
//                        point3.setGrade(0.65);
//                        for (Point point4 : point3.getPrevious()) {
//                            point4.setGrade(0.7);
//                            for (Point point5 : point4.getPrevious()) {
//                                point5.setGrade(0.75);
//                                for (Point point6 : point5.getPrevious()) {
//                                    point6.setGrade(0.8);
//                                    for (Point point7 : point6.getPrevious()) {
//                                        point7.setGrade(0.8);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
    }

    /**
     * 格式化结果输出
     *
     * @return
     */
    private static String FormatResult() {
        if (currentminpoint == null) {
            System.out.println("NA");
            return "NA";
        }
        Searcher pre = currentminpoint.getPrevious();
        Searcher current = currentminpoint;
        StringBuffer result = new StringBuffer();
        while (pre != null) {
            result.insert(0, topo[pre.getPointID()][current.getPointID()].getLinkId());
            current = pre;
            pre = current.getPrevious();
            if (pre != null)
                result.insert(0, "|");
        }
        System.out.println(result.toString() + "\n cost:" + currentminpoint.getTotalValue());
        return result.toString();
    }

    private static void findMinValueRoute() {
        Searcher startpoint = new Searcher(start, 0, 0, null);
        findnextPointList(startpoint);
        while (!minValueHeap.isHeapEmpty()) {
            Searcher searcher = minValueHeap.popMin();
            findnextPointList(searcher);
            if (timeout) {
                break;
            }
            //int second = LogUtil.getTimeUsed().get(Calendar.SECOND);
            //if (second >= 9)break;
//            minValueHeap.insert();
        }
    }

    private static void findnextPointList(Searcher parent) {
        for (int i = 0; i < topo[0].length; i++) {
            int value = topo[parent.getPointID()][i].getCost();
            if (value != 0) {
                int bestvalue = (int) (value - points[i].getGrade() * 1);
                Searcher searcher = new Searcher(i, parent.getTotalValue() + value, parent
                        .getTotalBestValue() + bestvalue, parent);
                //判断这个点是不是结尾
                if (searcher.getPointID() == end) {
                    //有没有通过所有特殊点
                    if (hasallspecialpoint(searcher)) {
                        //如果通过了所有特殊点 它是不是最短路径 如果不是则丢弃
                        if (currentminpoint == null) {
                            currentminpoint = searcher;
                            LogUtil.printLog("First route");
                            FormatResult();
                        } else if (searcher.getTotalValue() < currentminpoint.getTotalValue()) {
                            currentminpoint = searcher;
                            FormatResult();
                        }
                    }
                } else {
                    if (haspassedpoint(searcher)) {
                        continue;
                    }
                    //判断这个点如果超出了最短路径则丢弃
                    if (currentminpoint != null) {
                        if (searcher.getTotalValue() < currentminpoint.getTotalValue()) {
                            minValueHeap.insert(searcher);
//                            searchers.add(searcher);
                        }
                    } else {
                        minValueHeap.insert(searcher);
//                        searchers.add(searcher);
                    }
                }
            }
        }
    }

    /**
     * 判断是否重复经过某点
     *
     * @param searcher
     * @return
     */
    private static boolean haspassedpoint(Searcher searcher) {
        int pointId = searcher.getPointID();
        searcher = searcher.getPrevious();
        while (searcher != null) {
            if (searcher.getPointID() == pointId)
                return true;
            searcher = searcher.getPrevious();
        }
        return false;
    }

    /**
     * 是否通过所有特殊点
     *
     * @param searcher
     * @return
     */
    private static boolean hasallspecialpoint(Searcher searcher) {
        int passnumber = pass.length;
        while (!(searcher == null || passnumber == 0)) {
            for (int i = 0; i < pass.length; i++) {
                if (searcher.getPointID() == pass[i]) {
                    passnumber--;
                    break;
                }
            }
            searcher = searcher.getPrevious();
        }
        if (passnumber == 0) return true;
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

    /**
     * 生成路线信息
     *
     * @param graphContent
     */
    public static void FormatData(String graphContent) {
        String[] routes = graphContent.split("\\n");
        int size = 0;
        //找到最大的路径值
        for (String route : routes) {
            String[] info = route.split(",");
            int start = Integer.parseInt(info[1]);
            int end = Integer.parseInt(info[2]);
            if (size < start) {
                size = start;
            }
            if (size < end) {
                size = end;
            }
        }
        points = new Point[size + 1];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(i);
        }
        topo = new Topo[size + 1][size + 1];
        for (int i = 0; i < topo.length; i++) {
            for (int j = 0; j < topo.length; j++) {
                topo[i][j] = new Topo();
            }
        }
        for (String route : routes) {
            String[] info = route.split(",");
            Topo data = new Topo(Short.parseShort(info[0]), Short.parseShort(info[3]));
            topo[Integer.parseInt(info[1])][Integer.parseInt(info[2])] = data;
            points[Integer.parseInt(info[2])].getPrevious().add(points[Integer.parseInt(info[1])]);
        }
    }

    private static class Task extends TimerTask {

        @Override
        public void run() {
            System.out.println("time out !!!!!!!!");
            timeout = true;
        }
    }
}