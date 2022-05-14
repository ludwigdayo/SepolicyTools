package ContextFileTools.Impl;

import ContextFileTools.fileContextsFormat;
import Utils.AdbUtils;
import Utils.Impl.AdbUtilsImpl;
import Utils.Impl.StreamHelperImpl;
import Utils.StreamHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class fileContextsFormatImpl implements fileContextsFormat {

    private StreamHelper streamHelper = new StreamHelperImpl();

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

    @Override
    public String[] getPathList(String[] content) {
        return new String[0];
    }

    @Override
    public String[] formatAllLine(String[] content) {
        return new String[0];
    }

    @Override
    public String[] cleanNotExistedLine(String[] content) {
        AdbUtils adbUtils = new AdbUtilsImpl();
        String[] pathList = getPathList(content);
        ArrayList<String> strings = new ArrayList<>();
        String[] result = null;

        for (String line : pathList) {
            if (!isRemain(line)) {
                boolean existed = adbUtils.isExisted(line);
                if (!existed) continue; // 如果不是要特殊处理的且又不存在就不复制
            }
            strings.add(line);
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
}
