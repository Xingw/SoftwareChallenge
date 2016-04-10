/**
 * 实现代码文件
 *
 * @author XXX
 * @version V1.0
 * @since 2016-3-4
 */
package com.routesearch.route;

import com.Model.InsertSpecialHeap;
import com.Model.LinkSpecialHeap;
import com.Model.Linker;
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

    public static int maxValue = 2000;

    private static Searcher currentminpoint = null;

    private static MinValueHeap minValueHeap;
    //方向 true为正 false为反
    private static boolean direction = true;

    private static LinkSpecialHeap linkSpecialHeap;
    private static InsertSpecialHeap insertSpecialHeap;

    private static List<List<Integer>> route = new ArrayList<>();
    private static boolean linkover = false;
    private static boolean needreturn = false;

    /**
     * 你需要完成功能的入口
     *
     * @author XXX
     * @version V1
     * @since 2016-3-4
     */
    public static String searchRoute(String graphContent, String condition) {
        //计时器
        //SetTimer();
        //格式化路线
        FormatData(graphContent, direction);
        //格式化条件
        FormatCondition(condition, direction);
        //格式化优先级
        //FormatGrade();
        LogUtil.printLog("Format");
        //建立最小堆
        //minValueHeap = MinValueHeap.getInstance();
        //findMinValueRoute();
        LinkSpecialPoint();
        return FormatLinkResult();
    }

    private static String FormatLinkResult() {
        List<Integer> list = points[start].getLinker(false).getPoints();
        StringBuffer result = new StringBuffer();
        if (direction) {
            for (int i = 1; i < list.size(); i++) {
                result.append(topo[list.get(i - 1)][list.get(i)].getLinkId());
                if (i != list.size() - 1)
                    result.append("|");
            }

        } else {
            for (int i = 1; i < list.size(); i++) {
                result.insert(0, topo[list.get(i - 1)][list.get(i)].getLinkId());
                if (i != list.size() - 1)
                    result.insert(0, "|");
            }
        }
        System.out.println(result.toString());
        return result.toString();
    }

    private static String FormatResult(List<Integer> integers) {
        StringBuffer result = new StringBuffer();
        if (direction) {
            for (int i = integers.size() - 2; i >= 0; i--) {
                result.insert(0, topo[integers.get(i + 1)][integers.get(i)].getLinkId());
                if (i != 0)
                    result.insert(0, "|");
            }
        } else {
            for (int i = 1; i < integers.size(); i++) {
                result.insert(0, topo[integers.get(i - 1)][integers.get(i)].getLinkId());
                if (i != integers.size() - 1)
                    result.insert(0, "|");
            }
        }
        System.out.println(result.toString());
        return result.toString();
    }

    private static void SetTimer() {
        Timer timer = new Timer();
        timer.schedule(new Task(), 9 * 1000 + 800);
    }

    private static void FormatGrade() {
//        if (points.length < 500) {
//            maxValue = 290;
//        } else {
//            maxValue = 600;
//        }
        for (short pas : pass) {
            points[pas].setSpecial(true);
            points[pas].setGrade(100);
            for (Point point : points[pas].getPrevious()) {
                RecursionGrade(point, 10, Math.pow(20, 1d / (10)));
            }
        }
        points[end].setGrade(100);
        RecursionGrade(points[end], 10, Math.pow(20, 1d / (10)));
    }

    private static void RecursionGrade(Point point, int count, double num) {
        if (count == 0) return;
        point.setGrade(Math.pow(num, count) + 5);
        count--;
        for (Point point1 : point.getPrevious()) {
            RecursionGrade(point1, count, num);
        }
    }


    /**
     * 格式化结果输出
     *
     * @return
     */
    private static String FormatResult(boolean direction) {
        if (currentminpoint == null) {
            System.out.println("NA");
            return "NA";
        }
        Searcher pre = currentminpoint.getPrevious();
        Searcher current = currentminpoint;
        StringBuffer result = new StringBuffer();
        while (pre != null) {
            if (direction) {
                result.insert(0, topo[pre.getPointID()][current.getPointID()].getLinkId());
                current = pre;
                pre = current.getPrevious();
                if (pre != null)
                    result.insert(0, "|");
            } else {
                result.append(topo[pre.getPointID()][current.getPointID()].getLinkId());
                current = pre;
                pre = current.getPrevious();
                if (pre != null)
                    result.append("|");
            }
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
                double bestvalue = value - points[i].getGrade();
//                if (bestvalue<=0 && !points[i].isSpecial())bestvalue = points[i].getGrade()/100;
                if (points[i].isSpecial()) {
                    minValueHeap.clean();
//                    bestvalue =value - points[i].getGrade();
                } else {
//                    bestvalue = value * (1-points[i].getGrade()/100);
                }
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
                            FormatResult(direction);
                        } else if (searcher.getTotalValue() < currentminpoint.getTotalValue()) {
                            LogUtil.printLog("other route");
                            currentminpoint = searcher;
                            FormatResult(direction);
                        }
                    }
                } else {
                    if (haspassedpoint(searcher)) {
                        continue;
                    }
                    if (searcher.getTotalValue() > maxValue) {
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
    public static void FormatCondition(String condition, boolean direction) {
        String[] Info = condition.split(",");
        if (direction) {
            start = Short.parseShort(Info[0]);
            end = Short.parseShort(Info[1]);
        } else {
            start = Short.parseShort(Info[1]);
            end = Short.parseShort(Info[0]);
        }
        //截取掉换行符
        Info[2] = Info[2].substring(0, Info[2].length() - 1);
        String[] passes = Info[2].split("\\|");
        pass = new short[passes.length];
        for (int i = 0; i < passes.length; i++) {
            pass[i] = Short.parseShort(passes[i]);
        }
        for (short pas : pass) {
            points[pas].setSpecial(true);
        }
    }

    /**
     * 生成路线信息
     *
     * @param graphContent
     */
    public static void FormatData(String graphContent, boolean direction) {
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
            if (direction) {
                Topo data = new Topo(Short.parseShort(info[0]), Short.parseShort(info[3]));
                topo[Integer.parseInt(info[1])][Integer.parseInt(info[2])] = data;
                points[Integer.parseInt(info[2])].getPrevious().add(points[Integer.parseInt(info[1])]);
                points[Integer.parseInt(info[1])].getNext().add(points[Integer.parseInt(info[2])]);
            } else {
                Topo data = new Topo(Short.parseShort(info[0]), Short.parseShort(info[3]));
                topo[Integer.parseInt(info[2])][Integer.parseInt(info[1])] = data;
                points[Integer.parseInt(info[1])].getPrevious().add(points[Integer.parseInt
                        (info[2])]);
                points[Integer.parseInt(info[2])].getNext().add(points[Integer.parseInt(info[1])]);
            }
        }
    }

//    private static void DFSarithmetic(){
//        Searcher startpoint = new Searcher(start,0,0,null);
//        for (Point point : points[start].getNext()) {
//            Searcher searcher = new Searcher(point.getPointID(),0, 0,startpoint);
//            RecursionDFS(point,searcher);
//        }
//    }
//
//    private static void RecursionDFS(Point point,Searcher previous) {
//        if (currentminpoint != null || point.getNext().size() == 0) return;
//        for (Point point1 : point.getNext()) {
//            Searcher searcher = new Searcher(point1.getPointID(),0, 0,previous);
//            if(searcher.getPointID() == end){
//                if(hasallspecialpoint(searcher)){
//                    currentminpoint = searcher;
//                    return;
//                }
//                continue;
//            }
//            if (haspassedpoint(searcher)) continue;
//            RecursionDFS(point1,searcher);
//        }
//    }

    private static void LinkSpecialPoint() {
        linkSpecialHeap = LinkSpecialHeap.getInstance();
        points[start].setLinkstate(true);
        points[start].setLinker(new Linker(start));
        points[end].setLinkstate(true);
        points[end].setLinker(new Linker(end));
        initLinker(start, true);
        initLinker(end, false);
        for (short pas : pass) {
            initLinker(pas, true);
            initLinker(pas, false);
        }

        while (!linkSpecialHeap.isHeadEmpty()) {
            if (hasPointRoute(points[start].getLinker(false).getPoints(), end)) break;
            Linker searcher = linkSpecialHeap.popMin();
            findnextLinkPoint(searcher);
        }
        //如果已经连成的一条路没有包括所有的特殊点
        while (hasPointRoute(points[start].getLinker(false).getPoints(), end)) {
            linkSpecialHeap.cleanall();
            List<Integer> lonelypoint = new ArrayList<>();
            List<Integer> removespecial = findallspecialpoints();
            for (short pas : pass) {
                boolean skip = false;
                for (Integer integer : removespecial) {
                    if (integer == pas)skip = true;
                }
                if (!skip)lonelypoint.add((int)pas);
            }
            Point lonepoint = points[lonelypoint.get(0)];
            findnearRoute(lonepoint);
        }
    }

    private static void findnearRoute(Point lonepoint) {
        Integer ID = lonepoint.getPointID();
        List<Linker> preSpecial = new ArrayList<>();
        List<Linker> nextSpecial = new ArrayList<>();
        for (Point point : lonepoint.getPrevious()) {
            Linker linker = new Linker(lonepoint.getPointID(), point.getPointID(),
                    false, new ArrayList<Integer>());
            linker.add(lonepoint.getPointID());
            linker.add(point.getPointID());
            if ((point.isSpecial() || point.getPointID() == start)
                    && point.isLinkstate()) {
                //如果这个特殊点已经连接了，说明是线上的点
                preSpecial.add(linker);
            } else {
                if(point.getPointID() == end) continue;
                linkSpecialHeap.getSearcherList().add(linker);
            }
        }
        for (Point point : lonepoint.getNext()) {
            Linker linker = new Linker(lonepoint.getPointID(), point.getPointID(),
                    true, new ArrayList<Integer>());
            linker.add(lonepoint.getPointID());
            linker.add(point.getPointID());
            if ((point.isSpecial() || point.getPointID() == end)
                    && point.isLinkstate()) {
                //如果这个特殊点已经连接了，说明是线上的点
                nextSpecial.add(linker);
                //连接成功直接返回
                if(checkAndInsert(preSpecial, nextSpecial, lonepoint))return;
            } else {
                if(point.getPointID() == start) continue;
                linkSpecialHeap.getSearcherList().add(linker);
            }
        }
        //邻接特殊点不符合要求则开始广搜
        while (!linkSpecialHeap.isHeadEmpty()) {
            Linker previous = linkSpecialHeap.popMin();
            if(previous.isOut()) {
                List<Point> pointList=points[previous.getPointID()].getNext();
                for (Point point : pointList) {
                    Linker linker = new Linker(previous.getPointID(), point.getPointID(),
                            false,previous.getPoints());
                    linker.add(point.getPointID());
                    if ((point.isSpecial() || point.getPointID() == start)
                            && point.isLinkstate()) {
                        //如果这个特殊点已经连接了，说明是线上的点
                        preSpecial.add(linker);
                        if(checkAndInsert(preSpecial, nextSpecial, lonepoint))return;
                    } else {
                        if (point.getPointID() == end) continue;
                        linkSpecialHeap.getSearcherList().add(linker);
                    }
                }
            }else{
                List<Point> pointList=points[previous.getPointID()].getPrevious();
                for (Point point : pointList) {
                    Linker linker = new Linker(previous.getPointID(), point.getPointID(),
                            true, previous.getPoints());
                    linker.add(point.getPointID());
                    if ((point.isSpecial() || point.getPointID() == end)
                            && point.isLinkstate()) {
                        //如果这个特殊点已经连接了，说明是线上的点
                        nextSpecial.add(linker);
                        //连接成功直接返回
                        if(checkAndInsert(preSpecial, nextSpecial, lonepoint))return;
                    } else {
                        if(point.getPointID() == start) continue;
                        linkSpecialHeap.getSearcherList().add(linker);
                    }
                }
            }
        }
    }

    private static boolean checkAndInsert(List<Linker> preSpecial, List<Linker> nextSpecial, Point lonepoint) {
        if (preSpecial.size() > 0 && nextSpecial.size() > 0) {
            Point prePoint = null;
            Point nextPoint = null;
            Linker preLinker = null;
            Linker nextLinker = null;
            if (lonepoint.getPrespecial() != -1 && lonepoint.getNextspecial() != -1) {
                for (Linker pre : preSpecial) {
                    for (Linker next : nextSpecial) {
                        if (!(pre.getPointID() == lonepoint.getPrespecial() && next.getPointID()
                                == lonepoint.getNextspecial())) {
                            if(pre.getPointID() == next.getPointID())continue;
                            preLinker = pre;
                            nextLinker = next;
                            prePoint = points[pre.getPointID()];
                            nextPoint = points[next.getPointID()];
                            break;
                        }
                    }
                }
                if (prePoint == null || nextPoint == null) return false;
            } else {
                preLinker = preSpecial.get(0);
                nextLinker = nextSpecial.get(0);
                if(preLinker.getPointID() == nextLinker.getPointID())return false;
                prePoint = points[preLinker.getPointID()];
                nextPoint = points[nextLinker.getPointID()];
            }
            //断开连接
            List<Integer> removeList = new ArrayList<>();
            removeList.addAll(prePoint.getLinker(false).getPoints());
            removeList.remove(0);
            prePoint.getLinker(false).getPoints().removeAll(removeList);
            Point removePoint = prePoint;
            while (removePoint.getPrespecial() != -1) {
                removePoint = points[removePoint.getPrespecial()];
                removePoint.getLinker(false).getPoints().removeAll(removeList);
            }
            //清除连接状态
            Point middlePoint = prePoint;
            while (prePoint.getNextspecial() != -1 && middlePoint.getNextspecial()!=nextPoint.getPointID()) {
                middlePoint = points[middlePoint.getNextspecial()];
                middlePoint.setLinkstate(false);
            }

            //接路——前
            List<Integer> routeList = new ArrayList<>();
            nextLinker.getPoints().remove(nextLinker.getPoints().size() - 1);
            routeList.addAll(nextLinker.getPoints());
            routeList.addAll(nextPoint.getLinker(false).getPoints());
            Linkpoints(routeList);
            nextLinker.setPoints(routeList);
            lonepoint.setLinker(nextLinker,true);
            lonepoint.setNextspecial(nextPoint.getPointID());
            lonepoint.setPrespecial(-1);
            nextPoint.setPrespecial(lonepoint.getPointID());
            SavePointToSpecialPoint(lonepoint, nextPoint);
            //接路——后
            routeList = new ArrayList<>();
            preLinker.getPoints().remove(0);
            routeList.addAll(prePoint.getLinker(false).getPoints());
            routeList.addAll(preLinker.getPoints());
            Linkpoints(routeList);
            preLinker.setPoints(routeList);
            prePoint.setLinker(preLinker,true);
            prePoint.setNextspecial(lonepoint.getPointID());
            lonepoint.setPrespecial(prePoint.getPointID());
            SavePointToSpecialPoint(prePoint, lonepoint);
            return true;
        }
        return false;
    }

    private static List<Integer> findallspecialpoints() {
        List<Integer> special = new ArrayList<>();
        Point point = points[end];
        while (point.getPrespecial() != -1) {
            point = points[point.getPrespecial()];
            special.add(point.getPointID());
        }
        special.remove(special.size()-1);
        return special;
    }

    private static void initLinker(int ID, boolean out) {
        if (out) {
            if (points[ID].getNextspecial() != -1) return;
            for (Point point : points[ID].getNext()) {
                Linker linker = new Linker(ID, point.getPointID(),
                        true, new ArrayList<Integer>());
                linker.add(ID);
                linker.add(point.getPointID());
                if (point.isSpecial()) {
                    //如果这个特殊点没有连接，那就连接他
                    if (point.getPrespecial() == -1 && point.getNextspecial() != ID) {
                        if (hasRingRoute(points[ID], points[point.getPointID()])) continue;
                        points[ID].setLinker(linker);
                        point.setPrespecial(ID);
                        points[ID].setNextspecial(point.getPointID());
                        point.setLinkstate(true);
                        points[ID].setLinkstate(true);
                        SavePointToSpecialPoint(points[ID], points[point.getPointID()]);
                        points[ID].cleanLinkernext();
                        linkSpecialHeap.popUseless(ID);
                        break;
                    }
                } else {
                    //特殊点不放置linker
                    if (point.getPointID() == start || point.getPointID() == end || point
                            .isSpecial() || point.isLinkstate())
                        continue;
                    Linker existLinker = point.getLinker(linker.isOut());
                    if (existLinker != null) {
                        if (hasRingRoute(points[ID], points[point.getPointID()])) continue;
                        //与其他的特殊点的遍历器相交
                        List<Integer> routeList = new ArrayList<>();
                        List<Linker> removeList = linkSpecialHeap.popUseless(existLinker, linker);
                        linker.getPoints().remove(1);
                        routeList.addAll(linker.getPoints());
                        routeList.addAll(existLinker.getPoints());
                        linker.setPoints(routeList);
                        Linkpoints(routeList);
                        Point outpoint = points[linker.getParentID()];
                        outpoint.setLinker(linker);
                        outpoint.setLinkstate(true);
                        SavePointToSpecialPoint(linker, existLinker);
                        outpoint.setNextspecial(existLinker.getParentID());
                        points[existLinker.getParentID()].setPrespecial(linker.getParentID());
                        points[existLinker.getParentID()].setLinkstate(true);
                        CleanPointLinker(removeList);
                        break;
                    }
                    point.setLinker(linker);
                    linkSpecialHeap.getSearcherList().add(linker);
                }
            }
        } else {
            if (points[ID].getPrespecial() != -1) return;
            for (Point point : points[ID].getPrevious()) {
                Linker linker = new Linker(ID, point.getPointID(),
                        false, new ArrayList<Integer>());
                linker.add(ID);
                linker.add(point.getPointID());
                if (point.isSpecial()) {
                    if (point.getNextspecial() == -1 && point.getPrespecial() != ID) {
                        if (hasRingRoute(points[point.getPointID()], points[ID])) continue;
                        point.setLinker(linker);
                        point.setNextspecial(ID);
                        point.setLinkstate(true);
                        points[ID].setLinkstate(true);
                        points[ID].setPrespecial(point.getPointID());
                        SavePointToSpecialPoint(points[point.getPointID()], points[ID]);
                        points[ID].cleanLinkerpre();
                        linkSpecialHeap.popUseless(ID);
                        break;
                    }
                } else {
                    if (point.getPointID() == start || point.getPointID() == end || point
                            .isSpecial() || point.isLinkstate())
                        continue;
                    Linker existLinker = point.getLinker(linker.isOut());
                    if (existLinker != null) {
                        if (hasRingRoute(points[point.getPointID()], points[ID])) continue;
                        List<Integer> routeList = new ArrayList<>();
                        List<Linker> removeList = linkSpecialHeap.popUseless(existLinker, linker);
                        linker.getPoints().remove(0);
                        routeList.addAll(existLinker.getPoints());
                        routeList.addAll(linker.getPoints());
                        existLinker.setPoints(routeList);
                        Linkpoints(routeList);
                        Point outpoint = points[existLinker.getParentID()];
                        outpoint.setLinker(existLinker);
                        outpoint.setLinkstate(true);
                        SavePointToSpecialPoint(existLinker, linker);
                        outpoint.setNextspecial(linker.getParentID());
                        points[linker.getParentID()].setPrespecial(existLinker.getParentID());
                        points[linker.getParentID()].setLinkstate(true);
                        CleanPointLinker(removeList);
                        break;
                    }
                    point.setLinker(linker);
                    linkSpecialHeap.getSearcherList().add(linker);
                }
            }
        }
    }

    private static boolean hasRingRoute(Point out, Point in) {
        int recordout = out.getNextspecial();
        int recordin = in.getPrespecial();
        out.setNextspecial(in.getPointID());
        in.setPrespecial(out.getPointID());
        Point test = out;
        //防止成环
        List<Integer> passedpoint = new ArrayList<>();
        passedpoint.add(test.getPointID());
        while (test.getPrespecial() != -1) {
            test = points[test.getPrespecial()];
            for (Integer integer : passedpoint) {
                if (integer == test.getPointID()) {
                    out.setNextspecial(recordout);
                    in.setPrespecial(recordin);
                    return true;
                }
            }
            passedpoint.add(test.getPointID());
        }
        in.setPrespecial(recordin);
        out.setNextspecial(recordout);
        return false;
    }

    private static boolean hasPointRoute(List<Integer> route, short id) {
        if (hasallspecialpoint(route)) return false;
        for (Integer integer : route) {
            if (id == integer) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSpecialPoint(int pointID) {
        for (short pas : pass) {
            if (pointID == pas) return true;
        }
        return false;
    }

    private static void findnextLinkPoint(Linker previous) {
        Point prepoint = points[previous.getPointID()];
        //清除当前点记录
        prepoint.cleanLinker(previous);
        List<Point> pointList;
        if (previous.isOut()) {
            pointList = prepoint.getNext();
        } else {
            pointList = prepoint.getPrevious();
        }
        for (Point point : pointList) {
            if (point.getPointID() == start || point.getPointID() == end || point.isSpecial())
                continue;
            Linker linker = new Linker(previous.getParentID(), point.getPointID(), previous.isOut(),
                    previous.getPoints());
            linker.add(point.getPointID());
            Linker existLinker = point.getLinker(previous.isOut());
            if (existLinker != null) {
                //与其他的特殊点的遍历器相交
                List<Integer> routeList = new ArrayList<>();
                List<Linker> removeList = linkSpecialHeap.popUseless(existLinker, previous);
                if (previous.isOut()) {
                    if (hasRingRoute(points[previous.getParentID()], points[existLinker.getParentID()]))
                        continue;
                    routeList.addAll(previous.getPoints());
                    routeList.addAll(existLinker.getPoints());
                    previous.setPoints(routeList);
                    Linkpoints(routeList);
                    Point outpoint = points[previous.getParentID()];
                    outpoint.setLinker(previous);
                    SavePointToSpecialPoint(previous, existLinker);
                    outpoint.setNextspecial(existLinker.getParentID());
                    points[existLinker.getParentID()].setPrespecial(previous.getParentID());
                    CleanPointLinker(removeList);
                    break;
                } else {
                    if (hasRingRoute(points[existLinker.getParentID()], points[previous.getParentID()]))
                        continue;
                    routeList.addAll(existLinker.getPoints());
                    routeList.addAll(previous.getPoints());
                    existLinker.setPoints(routeList);
                    Linkpoints(routeList);
                    Point outpoint = points[existLinker.getParentID()];
                    outpoint.setLinker(existLinker);
                    SavePointToSpecialPoint(existLinker, previous);
                    outpoint.setNextspecial(previous.getParentID());
                    points[previous.getParentID()].setPrespecial(existLinker.getParentID());
                    CleanPointLinker(removeList);
                    break;
                }
            }
            point.setLinker(linker);
            linkSpecialHeap.getSearcherList().add(linker);
        }
    }

    private static void CleanPointLinker(List<Linker> removeList) {
        for (Linker linker1 : removeList) {
            points[linker1.getPointID()].cleanLinker();
        }
    }

    private static void SavePointToSpecialPoint(Linker outLinker, Linker inLinker) {
        Point inpoint = points[inLinker.getParentID()];
        Point outpoint = points[outLinker.getParentID()];
        SavePointToSpecialPoint(outpoint, inpoint);
    }

    private static void SavePointToSpecialPoint(Point outpoint, Point inpoint) {
        if (inpoint.getLinker(false) == null) {
            inpoint.setLinker(new Linker(inpoint.getPointID()));
        }
        //如果in节点前面有路径则向后传递
        if (inpoint.getNextspecial() != -1) {
            List<Integer> points = new ArrayList<>();
            points.addAll(inpoint.getLinker(true).getPoints());
            points.remove(0);
            outpoint.getLinker(false).getPoints().addAll(points);
        }
        //如果out节点后面有路径则向后传递
        if (outpoint.getPrespecial() != -1) {
            Point prepoint = outpoint;
            List<Integer> pointsList = new ArrayList<>();
            pointsList.addAll(outpoint.getLinker(true).getPoints());
            pointsList.remove(0);
            while (prepoint.getPrespecial() != -1) {
                prepoint = points[prepoint.getPrespecial()];
                prepoint.getLinker(false).getPoints().addAll(pointsList);
            }
        }
    }

    private static void Linkpoints(List<Integer> routeList) {
        for (Integer integer : routeList) {
            points[integer].setLinkstate(true);
        }
    }

//    private static boolean InsertListToRoute(List<Integer> pointList) {
//        int Linkstart = pointList.get(0);
//        int Linkend = pointList.get(pointList.size() - 1);
//        if (route.size() != 0) {
//            if(hasRepeatPoint(pointList)){
//                return false;
//            }
//            boolean insert = false;
//            for (List<Integer> list : route) {
//                //头尾相同则拼接
//                if (list.get(0) == Linkend) {
//                    pointList.remove(pointList.size() - 1);
//                    pointList.addAll(list);
//                    route.remove(list);
//                    route.add(pointList);
//                    insert = true;
//                    break;
//                } else if (list.get(list.size() - 1) == Linkstart) {
//                    pointList.remove(0);
//                    list.addAll(pointList);
//                    insert = true;
//                    break;
//                }
//            }
//            if(!insert) route.add(pointList);
//            sortRoute();
//            if (hasallspecialpoint(route.get(0))) {
//                linkover = true;
//            }
//        } else {
//            route.add(pointList);
//        }
//        return true;
//    }

//    private static void sortRoute() {
//        List<List<Integer>> insertList = new ArrayList<>();
//        List<List<Integer>> removeList = new ArrayList<>();
//        for (int i = 0; i < route.size(); i++) {
//            for (int k = route.size() - i; k < route.size(); k++) {
//                if(k == i) continue;
//                List<Integer> route1 = route.get(i);
//                List<Integer> route2 = route.get(k);
//                if (route1.get(0) == route2.get(route2.size()-1)) {
//                    removeList.add(route1);
//                    removeList.add(route2);
//                    route2.remove(route2.size() - 1);
//                    route1.addAll(route2);
//                    insertList.add(route1);
//                    break;
//                } else if (route1.get(route1.size()-1) == route2.get(0)) {
//                    removeList.add(route1);
//                    removeList.add(route2);
//                    route2.remove(0);
//                    route1.addAll(route2);
//                    insertList.add(route1);
//                    break;
//                }
//            }
//        }
//        if(removeList.size()!=0){
//            for (List<Integer> integers : removeList) {
//                route.remove(integers);
//            }
//            route.addAll(insertList);
//        }
//    }

//    private static boolean hasRepeatPoint(List<Integer> pointList) {
//        if(pointList.size() == 2){
//            int a = pointList.get(0);
//            int b = pointList.get(1);
//            for (List<Integer> integers : route) {
//                if(integers.size() == 2){
//                    if(a == integers.get(1) && b == integers.get(0))return true;
//                }
//            }
//        }
//        for (int i = 0; i < pointList.size(); i++) {
//            for (List<Integer> integers : route) {
//                //去掉头和尾
//                for (int i1 = 1; i1 < integers.size()-1; i1++) {
//                    //是否重复经过某点
//                    if(pointList.get(i) == integers.get(i)) {
//                        points[pointList.get(i)].cleanLinker();
//                        needreturn = true;
//                        return true;
//                    }
//                }
//                //是否成环
//                if(integers.get(0) == pointList.get(pointList.size() -1) && pointList.get(0) ==
//                        integers.get(integers.size() -1)) {
//                    needreturn = true;
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    private static boolean hasallspecialpoint(List<Integer> integers) {
        int passnum = pass.length + 2;
        for (Integer integer : integers) {
            for (short pas : pass) {
                if (integer == pas || integer == start || integer == end) {
                    passnum--;
                    break;
                }
            }
            if (passnum == 0) return true;
        }
        return false;
    }

    private static class Task extends TimerTask {

        @Override
        public void run() {
            System.out.println("time out !!!!!!!!");
            timeout = true;
        }
    }


}