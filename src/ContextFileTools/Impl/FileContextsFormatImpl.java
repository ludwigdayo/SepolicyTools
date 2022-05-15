package ContextFileTools.Impl;

import ContextFileTools.FileContextsFormat;
import Utils.AdbUtils;
import Utils.Impl.AdbUtilsImpl;
import Utils.Impl.LoggerImpl;
import Utils.Impl.StreamHelperImpl;
import Utils.Logger;
import Utils.StreamHelper;

import java.io.BufferedWriter;
import java.util.ArrayList;

public class FileContextsFormatImpl implements FileContextsFormat {
    Logger logger = new LoggerImpl();

    private final StreamHelper streamHelper = new StreamHelperImpl();

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
    public String[] cleanNotExistedLine(String[] content) {
        AdbUtils adbUtils = new AdbUtilsImpl();
        String[] pathList = getPathList(content);
        ArrayList<String> strings = new ArrayList<>();
        String[] result = null;

        for (String line : pathList) {

            if (!isRemain(line) && !adbUtils.isExisted(line)) {
                logger.println("忽略" + line);
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
        BufferedWriter bufferWriter = null;
        String[] text = null;

        text = new ContextsUtilsImpl().fileToString(inPutPath);

        logger.println("清除无用行...");
        text = cleanNotExistedLine(text);

        logger.println("整理所有行...");
        text = new ContextsUtilsImpl().formatAllLine(text);

        logger.println("写入文件...");
        streamHelper.writeToFile(text, outPutPath);
    }

    public static void main(String[] args) {
        new FileContextsFormatImpl().autoFormatFileContexts("file_contexts", "out.txt");
    }
}
