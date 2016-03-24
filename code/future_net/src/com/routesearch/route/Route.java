/**
 * 实现代码文件
 * 
 * @author XXX
 * @since 2016-3-4
 * @version V1.0
 */
package com.routesearch.route;

import java.util.List;

import static com.filetool.util.Util.FormatData;

public final class Route
{
    //路径信息
    private static short[][] data;
    //起点
    private static short start;
    //终点
    private static short end;
    //特殊点
    private static short[] pass;

    /**
     * 你需要完成功能的入口
     * 
     * @author XXX
     * @since 2016-3-4
     * @version V1
     */
    public static String searchRoute(String graphContent, String condition)
    {
        data=FormatData(graphContent);
        FormatCondition(condition);
        return "hello world!";
    }

    /**
     * 格式化条件信息
     * @param condition 条件字符串
     */
    public static void FormatCondition(String condition){
        String[] Info = condition.split(",");
        start = Short.parseShort(Info[0]);
        end = Short.parseShort(Info[1]);
        //截取掉换行符
        Info[2] = Info[2].substring(0,Info[2].length()-1);
        String[] passes = Info[2].split("\\|");
        pass = new short[passes.length];
        for (int i = 0; i < passes.length; i++) {
            pass[i] = Short.parseShort(passes[i]);
        }
    }
}