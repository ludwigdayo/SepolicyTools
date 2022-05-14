package Utils.Impl;

import Utils.AdbUtils;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdbUtilsImpl implements AdbUtils {
    private final static int WINDOWS = 0;
    private final static int LINUX = 1;
    private final static int OTHER = 2;

    private static String allFileList = null;

    private static Logger logger = Logger.getLogger(AdbUtilsImpl.class.getName());

    /**
     * 获取系统类型
     */
    private static int getSystemType() {
        String property = System.getProperty("os.name");
        logger.log(Level.INFO, "系统类型" + property);
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
                //logger.log(Level.INFO, buffer);
                arrayList.add(buffer);

                // 丢掉错误输出，不然上面卡死！
                while (exec.getErrorStream().available() > 0) exec.getErrorStream().read();
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
     * 通过adb获取全部文件列表
     *
     * @return
     */
    public String getAllFileList() {
        if (allFileList == null) {
            logger.log(Level.INFO, "正在获取文件列表(至少需要几分钟时间)...");
            String[] shell = shell("ls -R");
            logger.log(Level.INFO, "正在处理结果");
            StringBuilder result = new StringBuilder();

            // 把“ls -R”得到的结果数组转成一个字符串 并给每一个文件加上完整路径
            int i = 0;
            while (i < shell.length - 1) {
                if (shell[i].isEmpty() && !shell[i + 1].isEmpty()) {
                    i++;
                    String line = shell[i]; // line 为文件夹路径
                    i++;
                    while (i < shell.length && !shell[i].isEmpty()) {
                        if (line.endsWith(":")) line = line.substring(0, line.lastIndexOf(":")); // 砍掉文件夹后面的冒号

                        result.append(line).append("/").append(shell[i]);// 拼接文件完整路径
                        result.append("\r\n");
                        i++;
                    }
                } else {
                    i++;
                }
            }

            allFileList = result.toString();
        }

        return allFileList;
    }

    /**
     * 判断文件是否存在
     */
    public boolean isExisted(String path) {
        String allFileList = getAllFileList();

        Pattern pattern = Pattern.compile(path);
        Matcher matcher = pattern.matcher(allFileList);
        return matcher.find();
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
