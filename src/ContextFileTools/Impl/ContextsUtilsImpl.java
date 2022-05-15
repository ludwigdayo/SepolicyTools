package ContextFileTools.Impl;

import ContextFileTools.ContextsUtils;
import Utils.FilePathUtils;
import Utils.Impl.FilePathUtilsImpl;
import Utils.Impl.LineUtilsImpl;
import Utils.Impl.LoggerImpl;
import Utils.Impl.StreamHelperImpl;
import Utils.LineUtils;
import Utils.StreamHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class ContextsUtilsImpl implements ContextsUtils {

    LoggerImpl logger = new LoggerImpl();

    LineUtils lineUtils = new LineUtilsImpl();

    private final StreamHelper streamHelper = new StreamHelperImpl();

    /**
     * 把文件读取到字符串数组
     *
     * @param inPutPath 输入文件路径
     * @return 结果
     */
    public String[] fileToString(String inPutPath) {
        BufferedReader bufferReader = streamHelper.getBufferReader(inPutPath);
        TreeSet<String> treeSet = new TreeSet<>();

        String line = null;
        try {
            while ((line = bufferReader.readLine()) != null) {
                treeSet.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] text = new String[treeSet.size()];
        treeSet.toArray(text);

        try {
            bufferReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }


    /**
     * 从Contexts获取标签
     *
     * @param Contexts 文件完整路径
     * @return 结果
     */
    @Override
    public String[] getLabelFromContexts(String Contexts) {

        StreamHelperImpl streamHelper = new StreamHelperImpl();
        BufferedReader bufferReader = streamHelper.getBufferReader(Contexts);
        TreeSet<String> resultSet = new TreeSet<>();
        String[] result = null;

        if (bufferReader == null) return null;

        String line = null;
        try {
            while ((line = bufferReader.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("u:object_r:")) continue;

                int start = line.indexOf("u:object_r:") + "u:object_r:".length();
                int end = line.lastIndexOf(":s0");

                resultSet.add(line.substring(start, end));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        streamHelper.close(bufferReader);

        result = new String[resultSet.size()];
        resultSet.toArray(result);
        return result;
    }

    /**
     * 得到dir下以*_contexts命名的文件名
     *
     * @param dir 文件夹
     * @return 结果
     */
    private String[] getContextsFileList(String dir) {
        ArrayList<String> resultList = new ArrayList<>();
        String[] result = null;

        File d = new File(dir);
        if (!d.isDirectory()) {
            logger.println(dir + "不是文件夹");
        }

        File[] files = d.listFiles();
        for (File file : files) {
            if (file.getName().endsWith("_contexts")) {
                resultList.add(file.getName());
            }
        }

        result = new String[resultList.size()];
        resultList.toArray(result);

        return result;
    }

    /**
     * 读取全部标签
     *
     * @param dir 工作文件夹
     * @return 结果
     */
    @Override
    public String[] getAllLabels(String dir) {
        TreeSet<String> resultSet = new TreeSet<>();
        String[] result = null;
        String[] contextsFileList = null;
        FilePathUtils filePathUtils = new FilePathUtilsImpl();

        // 得到文件列表
        contextsFileList = getContextsFileList(dir);

        for (String file : contextsFileList) {
            String[] labelFromContexts = getLabelFromContexts(filePathUtils.catPath(dir, file));
            resultSet.addAll(Arrays.asList(labelFromContexts));
        }

        result = new String[resultSet.size()];
        resultSet.toArray(result);

        return result;
    }

    /**
     * 清除多余空格 删除空行 删除注释 排序
     *
     * @param content contexts文件内容
     * @return 结果
     */
    @Override
    public String[] formatAllLine(String[] content) {
        String[] results;
        TreeSet<String> treeSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        for (String line : content) {
            if (line.startsWith("#")) continue;
            if (line.isEmpty()) continue;

            line = line.replace("\t", " ");
            line = lineUtils.deleteDuplicateSpace(line);
            treeSet.add(line);
        }

        results = new String[treeSet.size()];
        treeSet.toArray(results);
        return results;
    }
}
