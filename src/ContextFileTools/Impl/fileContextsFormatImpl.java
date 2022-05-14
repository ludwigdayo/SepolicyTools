package ContextFileTools.Impl;

import ContextFileTools.fileContextsFormat;
import Utils.AdbUtils;
import Utils.Impl.AdbUtilsImpl;
import Utils.Impl.LineUtilsImpl;
import Utils.Impl.StreamHelperImpl;
import Utils.LineUtils;
import Utils.StreamHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.logging.Logger;

public class fileContextsFormatImpl implements fileContextsFormat {
    LineUtils lineUtils = new LineUtilsImpl();

    private final StreamHelper streamHelper = new StreamHelperImpl();

    Logger logger = Logger.getLogger(fileContextsFormatImpl.class.getName());

    /**
     * 把文件读取到字符串数组
     *
     * @param inPutPath 输入文件路径
     * @return 结果
     */
    private String[] fileToString(String inPutPath) {
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
     * 判断是不是要特殊保留的路径
     */
    private boolean isRemain(String line) {
        if (line.startsWith("/proc")) return true;
        if (line.startsWith("/data")) return true;

        return false;
    }

    /**
     * 截取路径
     * 根据路径后的空格判断
     * 忽略注释
     *
     * @param content file_contexts文件内容
     * @return 结果
     */
    @Override
    public String[] getPathList(String[] content) {
        ArrayList<String> arrayList = new ArrayList<>();
        String[] results;

        for (String line : content) {
            if (line.startsWith("#")) {
                continue;
            }

            if (!line.contains(" ") || !line.contains("/")) {
                continue;
            }

            line = line.substring(0, line.indexOf(" "));
            arrayList.add(line);
        }

        results = new String[arrayList.size()];
        arrayList.toArray(results);
        return results;
    }

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

            line = line.replace("\t", " ");
            line = lineUtils.deleteDuplicateSpace(line);
            treeSet.add(line);
        }

        results = new String[treeSet.size()];
        treeSet.toArray(results);
        return results;
    }

    @Override
    public String[] cleanNotExistedLine(String[] content) {
        AdbUtils adbUtils = new AdbUtilsImpl();
        String[] pathList = getPathList(content);
        ArrayList<String> strings = new ArrayList<>();
        String[] result = null;

        for (String line : pathList) {

            if (!isRemain(line) && !adbUtils.isExisted(line)) {
                System.out.println(line);
                continue; // 如果不是要特殊处理的且又不存在就不复制
            }

            for (String reallyLine : content) {
                if (reallyLine.contains(line)) {
                    strings.add(reallyLine);
                }
            }
        }

        result = new String[strings.size()];
        strings.toArray(result);
        return result;
    }

    @Override
    public void autoFormatFileContexts(String inPutPath, String outPutPath) {
        BufferedWriter bufferWriter = streamHelper.getBufferWriter(outPutPath, false);
        String[] text = null;

        text = fileToString(inPutPath);
        text = cleanNotExistedLine(text);
        text = formatAllLine(text);

        try {
            for (String line : text) {
                bufferWriter.write(line);
                bufferWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new fileContextsFormatImpl().autoFormatFileContexts("file_contexts", "out.txt");
    }
}
