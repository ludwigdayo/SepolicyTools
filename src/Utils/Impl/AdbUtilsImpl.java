package Utils.Impl;

import Utils.AdbUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AdbUtilsImpl implements AdbUtils {
    private final static int WINDOWS = 0;
    private final static int LINUX = 1;
    private final static int OTHER = 2;

    /**
     * 获取系统类型
     */
    private static int getSystemType() {
        String property = System.getProperty("os.name");
        if (property.contains("Windows")) {
            return WINDOWS;
        } else if (property.contains("Linux")) {
            return LINUX;
        } else {
            return OTHER;
        }
    }

    /**
     * 根据系统类型 确定adb工具的路径~
     */
    private static String getADB() {
        int systemType = getSystemType();
        String adbPath = null;
        switch (systemType) {
            case WINDOWS:
                adbPath = "platform-tools/windows/adb.exe";
                break;
            case LINUX:
                adbPath = "platform-tools/linux/adb";
                break;
        }
        return adbPath;
    }

    /**
     * 执行shell命令
     */
    public String[] shell(String command) {
        Process exec = null;
        try {
            exec = Runtime.getRuntime().exec(getADB() + " shell " + command);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            ArrayList<String> arrayList = new ArrayList<>();
            String buffer = null;
            while ((buffer = bufferedReader.readLine()) != null) {
                arrayList.add(buffer);
            }

            String[] strings = new String[arrayList.size()];
            arrayList.toArray(strings);

            return strings;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 判断文件是否存在
     */
    public boolean isExisted(String path) {
        String[] s = shell("num=$(ls -l " + path + " 2>/dev/null | wc -l); if [ $num -ge 1 ]; then echo true; else echo false;fi 2>/dev/null");
        if (s != null && s[0].equals("true")) {
            return true;
        }
        return false;
    }

    /**
     * 测试
     * 需要连接手机
     */
    public static void main(String[] args) {
        System.out.println(new AdbUtilsImpl().isExisted("/system")); // true
        System.out.println(new AdbUtilsImpl().isExisted("/succerseng")); // false
    }

}
