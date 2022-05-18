package cc.succerseng.sepolicytools.utils.impl;

import cc.succerseng.sepolicytools.utils.AndroidBuildHelper;
import cc.succerseng.sepolicytools.utils.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class AndroidBuildHelperImpl implements AndroidBuildHelper {
    private final Logger logger = new LoggerImpl();

    @Override
    public boolean make(String rootDir, String deviceType, String target) {
        Process shell = null;
        try {
            shell = Runtime.getRuntime().exec("bash");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (shell == null) {
            logger.println("bash进程创建失败");
            return false;
        }

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(shell.getOutputStream()));
        try {
            bufferedWriter.write("cd " + rootDir);
            bufferedWriter.newLine();
            bufferedWriter.write("source build/envsetup.sh");
            bufferedWriter.newLine();
            bufferedWriter.write("lunch " + deviceType);
            bufferedWriter.newLine();
            bufferedWriter.write("make " + target);
            bufferedWriter.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStreamReader error = new InputStreamReader(shell.getErrorStream());
        InputStreamReader output = new InputStreamReader(shell.getInputStream());
        try {
            while (output.read() != -1) ;
            if (error.read() != -1) {
                logger.println("编译错误");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
