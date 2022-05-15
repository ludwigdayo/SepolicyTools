package Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * IO缓冲流的工具类
 */
public interface StreamHelper {

    BufferedReader getBufferReader(String path);

    BufferedWriter getBufferWriter(String path,boolean append);

    void closeIO(BufferedReader bufferedReader, BufferedWriter bufferedWriter);

    void close(BufferedReader bufferedReader);

    void close(BufferedWriter bufferedWriter);

    /**
     * 将content写到文件file
     * @param content 内容
     * @param file 文件完整路径
     */
    void writeToFile(String[] content,String file);
}
