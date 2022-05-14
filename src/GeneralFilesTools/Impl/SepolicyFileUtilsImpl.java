package GeneralFilesTools.Impl;


import GeneralFilesTools.SepolicyFileUtils;
import Utils.Impl.StreamHelperImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.TreeSet;

public class SepolicyFileUtilsImpl extends SepolicyLineUtilsImpl implements SepolicyFileUtils {

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
                return o2.compareTo(o1);
            }
        }); // 存放全部行，将自动排序

        StreamHelperImpl streamHelper = new StreamHelperImpl();
        BufferedReader bufferedReader = streamHelper.getBufferReader(inPutPath);
        BufferedWriter bufferedWriter = null;

        try {
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                treeSet.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] strings = new String[treeSet.size()]; // 结果
        treeSet.toArray(strings); // 取出

        formatAllLine(strings); // 格式化行

        bufferedWriter = streamHelper.getBufferWriter(outPutPath, false); // 打开输出文件并清空
        try {
            for (String line : strings) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        streamHelper.closeIO(bufferedReader, bufferedWriter);
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

    public static void main(String[] args) {
        new SepolicyFileUtilsImpl().autoFormatFile("in.te", "out.te");
    }
}
