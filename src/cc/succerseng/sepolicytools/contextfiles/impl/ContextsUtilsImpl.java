package cc.succerseng.sepolicytools.contextfiles.impl;

import cc.succerseng.sepolicytools.contextfiles.ContextsUtils;
import cc.succerseng.sepolicytools.utils.FilePathUtils;
import cc.succerseng.sepolicytools.utils.LineUtils;
import cc.succerseng.sepolicytools.utils.Logger;
import cc.succerseng.sepolicytools.utils.StreamHelper;
import cc.succerseng.sepolicytools.utils.impl.FilePathUtilsImpl;
import cc.succerseng.sepolicytools.utils.impl.LineUtilsImpl;
import cc.succerseng.sepolicytools.utils.impl.LoggerImpl;
import cc.succerseng.sepolicytools.utils.impl.StreamHelperImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class ContextsUtilsImpl implements ContextsUtils {

    private final StreamHelper streamHelper = new StreamHelperImpl();
    Logger logger = new LoggerImpl();
    LineUtils lineUtils = new LineUtilsImpl();

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
     * 得到dir下不以te或_contexts结尾文件列表
     *
     * @param dir 文件夹
     * @return 结果
     */
    private String[] getOtherFileList(String dir) {
        ArrayList<String> resultList = new ArrayList<>();
        String[] result = null;

        File d = new File(dir);
        if (!d.isDirectory()) {
            logger.println(dir + "不是文件夹");
        }

        File[] files = d.listFiles();
        for (File file : files) {
            if (!file.getName().endsWith(".te") && !file.getName().endsWith("_contexts")) {
                resultList.add(file.getName());
            }
        }

        result = new String[resultList.size()];
        resultList.toArray(result);

        return result;
    }

    /**
     * 得到dir下以*_contexts命名的文件名列表
     * 除了file_contexts
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
            if (file.getName().endsWith("_contexts") && !file.getName().equals("file_contexts")) {
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
        String[] otherFileList = null;
        FilePathUtils filePathUtils = new FilePathUtilsImpl();

        // 得到文件中的标签
        contextsFileList = getContextsFileList(dir);
        for (String file : contextsFileList) {
            String[] labelFromContexts = getLabelFromContexts(filePathUtils.catPath(dir, file));
            if (labelFromContexts != null) {
                resultSet.addAll(Arrays.asList(labelFromContexts));
            }
        }

        // 得到不规则文件中的标签
        otherFileList = getOtherFileList(dir);
        for (String file : otherFileList) {
            String[] labelFromOtherFile = getLabelFromContexts(filePathUtils.catPath(dir, file));
            if (labelFromOtherFile != null) {
                resultSet.addAll(Arrays.asList(labelFromOtherFile));
            }
        }

        result = new String[resultSet.size()];
        resultSet.toArray(result);

        return result;
    }

    @Override
    public void autoFormatAllContext(String dir) {
        String[] contextsFileList = getContextsFileList(dir);
        FilePathUtils filePathUtils = new FilePathUtilsImpl();
        StreamHelper streamHelper = new StreamHelperImpl();

        logger.println("发现" + contextsFileList.length + "个contexts文件");

        for (String file : contextsFileList) {
            String[] content = fileToString(filePathUtils.catPath(dir, file));
            content = formatAllLine(content);
            streamHelper.writeToFile(content, filePathUtils.catPath(dir, file));
        }
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
