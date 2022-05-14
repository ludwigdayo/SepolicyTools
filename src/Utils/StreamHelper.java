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
}
