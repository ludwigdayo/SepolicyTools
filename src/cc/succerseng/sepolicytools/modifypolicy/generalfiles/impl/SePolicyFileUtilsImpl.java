package cc.succerseng.sepolicytools.modifypolicy.generalfiles.impl;

import cc.succerseng.sepolicytools.modifypolicy.generalfiles.SePolicyFileUtils;
import cc.succerseng.sepolicytools.modifypolicy.generalfiles.SePolicyLineUtils;
import cc.succerseng.sepolicytools.utils.Logger;
import cc.succerseng.sepolicytools.utils.impl.LoggerImpl;
import cc.succerseng.sepolicytools.utils.impl.StreamHelperImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class SePolicyFileUtilsImpl implements SePolicyFileUtils {
    private final SePolicyLineUtils sepolicyLineUtils = new SePolicyLineUtilsImpl();
    private final StreamHelperImpl streamHelper = new StreamHelperImpl();
    private static final Logger logger = new LoggerImpl();

    public static void main(String[] args) {
        new SePolicyFileUtilsImpl().autoFormatFile("in.te", "out.te");
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
                        buffer = sepolicyLineUtils.formatLine(buffer);

                        if (buffer != null && !buffer.isEmpty()) line += "\r\n" + buffer;
                    } while (buffer != null && !(buffer.contains(")") && !buffer.contains("(")));
                }

                arrayList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        streamHelper.close(bufferReader);

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
            content[i] = sepolicyLineUtils.formatLine(content[i]);
        }
    }
}
