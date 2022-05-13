package FormatTools.impl;

import FormatTools.SepolicyFileUtils;

import java.io.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class SepolicyFileUtilsImpl extends SepolicyLineUtilsImpl implements SepolicyFileUtils {

    /**
     * 格式化一个文件
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    @Override
    public void formatFile(String inPutPath, String outPutPath) {

        String tmpPath = outPutPath + "/tmp";
        formatAllLine(inPutPath, tmpPath);
        sortLines(tmpPath, outPutPath);
        new File(tmpPath).delete();
    }

    /**
     * 利用treeSet的特性给文件的行排序
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    @Override
    public void sortLines(String inPutPath, String outPutPath) {

        TreeSet<String> treeSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        }); // 存放全部行，将自动排序

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inPutPath)));
        } catch (FileNotFoundException e) {
            System.out.println("输入文件未找到");
            return;
        }

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutPath, false)));
        } catch (FileNotFoundException e) {
            System.out.println("输出文件打不开");
            return;
        }

        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                treeSet.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> iterator = treeSet.iterator();
        while (iterator.hasNext()) {
            try {
                bufferedWriter.write(iterator.next());
                bufferedWriter.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 格式化每一行
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    @Override
    public void formatAllLine(String inPutPath, String outPutPath) {

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inPutPath)));
        } catch (FileNotFoundException e) {
            System.out.println("输入文件未找到");
            return;
        }

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutPath, false)));
        } catch (FileNotFoundException e) {
            System.out.println("输出文件打不开");
            return;
        }

        try {
            // 从输入取出一行，格式化后放入输出
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                String s = formatLine(line);
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            System.out.println("格式化失败");
        }

        try {
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SepolicyFileUtilsImpl().formatFile("in.te","out.te");
    }
}
