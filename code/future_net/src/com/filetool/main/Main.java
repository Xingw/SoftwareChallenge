package com.filetool.main;

import com.filetool.util.FileUtil;
import com.filetool.util.LogUtil;
import com.routesearch.route.Route;

/**
 * 工具入口
 *
 * @author
 * @version v1.0
 * @since 2016-3-1
 */
public class Main {
    public static String test = "E:\\Code\\Java\\Softwarechallenge-x\\more\\case\\case50-10" +
            "\\case0\\";

    public static void main(String[] args) {
        args = new String[3];
        args[0] = test + "topo.csv";
        args[1] = test + "demand.csv";
        args[2] = test + " sample_result.csv";
        if (args.length != 3) {
            System.err.println("please input args: graphFilePath, conditionFilePath, resultFilePath");
            return;
        }
        String graphFilePath = args[0];
        String conditionFilePath = args[1];
        String resultFilePath = args[2];

        LogUtil.printLog("Begin");

        // 读取输入文件
        String graphContent = FileUtil.read(graphFilePath, null);
        String conditionContent = FileUtil.read(conditionFilePath, null);

        // 功能实现入口
        String resultStr = Route.searchRoute(graphContent, conditionContent);

        // 写入输出文件
        FileUtil.write(resultFilePath, resultStr, false);

        LogUtil.printLog("End");

    }

}
