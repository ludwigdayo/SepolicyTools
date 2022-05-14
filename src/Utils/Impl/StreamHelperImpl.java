package Utils.Impl;

import Utils.StreamHelper;

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
}
