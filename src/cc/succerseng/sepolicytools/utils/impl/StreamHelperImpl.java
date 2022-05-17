package cc.succerseng.sepolicytools.utils.impl;

import cc.succerseng.sepolicytools.utils.StreamHelper;

import java.io.*;

public class StreamHelperImpl implements StreamHelper {
    @Override
    public BufferedReader getBufferReader(String path) {

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bufferedReader;
    }

    @Override
    public BufferedWriter getBufferWriter(String path, boolean append) {

        BufferedWriter bufferedWriter = null;

        // 创建文件夹
        new File(new File(path).getParent()).mkdirs();

        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path, append)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bufferedWriter;
    }

    @Override
    public void closeIO(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (bufferedReader != null) bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(BufferedWriter bufferedWriter) {
        try {
            if (bufferedWriter != null) bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeToFile(String[] content, String file) {
        BufferedWriter bufferWriter = getBufferWriter(file, false);

        try {
            for (int i = 0; i < content.length; i++) {
                bufferWriter.write(content[i]);

                // 最后一行不要回车
                if (i < content.length - 1) {
                    bufferWriter.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        close(bufferWriter);
    }
}
