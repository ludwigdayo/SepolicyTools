package GeneralFilesTools.Impl;


import GeneralFilesTools.SepolicyFileUtils;
import Gui.SepolicyToolsGUI;
import Utils.Impl.FilePathUtilsImpl;
import Utils.Impl.LoggerImpl;
import Utils.Impl.StreamHelperImpl;
import Utils.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class SepolicyFileUtilsImpl extends SepolicyLineUtilsImpl implements SepolicyFileUtils {

    private static Logger logger = new LoggerImpl();

    public static void main(String[] args) {
        new SepolicyFileUtilsImpl().autoFormatFile("in.te", "out.te");
    }

    /**
     * 格式化一个文件
     * 利用treeSet的特性给文件的行排序
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    @Override
    public void autoFormatFile(String inPutPath, String outPutPath) {
        TreeSet<String> treeSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals(o2)) return 0;

                if (o1.startsWith("type ")) {
                    return -1;
                }

                if (o2.startsWith("type ")) {
                    return 1;
                }

                return o2.compareTo(o1);
            }
        }); // 存放全部行，将自动排序

        StreamHelperImpl streamHelper = new StreamHelperImpl();

        treeSet.addAll(Arrays.asList(readAllLineFromTEFile(inPutPath)));

        String[] strings = new String[treeSet.size()]; // 结果
        treeSet.toArray(strings); // 取出

        logger.println("格式化行...");
        formatAllLine(strings); // 格式化行
        logger.println("行格式化完成");

        streamHelper.writeToFile(strings, outPutPath);
    }

    @Override
    public String[] readAllLineFromTEFile(String file) {
        ArrayList<String> arrayList = new ArrayList<>();
        StreamHelperImpl streamHelper = new StreamHelperImpl();
        String[] result = null;

        BufferedReader bufferReader = streamHelper.getBufferReader(file);
        try {
            String line;
            while ((line = bufferReader.readLine()) != null) {

                // deal with function
                if (line.contains("(") && !line.contains(")")) {
                    String buffer = null;
                    do {
                        buffer = bufferReader.readLine();
                        buffer = formatLine(buffer);

                        if (buffer != null && !buffer.isEmpty()) line += "\r\n" + buffer;
                    } while (buffer != null && !(buffer.contains(")") && !buffer.contains("(")));
                }

                arrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } streamHelper.close(bufferReader);

        result = new String[arrayList.size()];
        arrayList.toArray(result);

        return result;
    }

    /**
     * 格式化每一行
     */
    @Override
    public void formatAllLine(String[] content) {

        for (int i = 0; i < content.length; i++) {
            content[i] = formatLine(content[i]);
        }
    }
}
