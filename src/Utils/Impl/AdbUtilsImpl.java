package Utils.Impl;

import Utils.AboutSystem;
import Utils.AdbUtils;
import Utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdbUtilsImpl implements AdbUtils {
    private static String allFileList = null;

    Logger logger = new LoggerImpl();

    /**
     * 根据系统类型 确定adb工具的路径~
     */
    private static String getADB() {
        int systemType = new AboutSystemImpl().getSystemType();

        String adbPath = null;
        switch (systemType) {
            case AboutSystem.WINDOWS:
                adbPath = "platform-tools/windows/adb.exe";
                break;
            case AboutSystem.LINUX:
                adbPath = "platform-tools/linux/adb";
                break;
        }
        return adbPath;
    }

    /**
     * 测试
     * 需要连接手机
     */
    public static void main(String[] args) {
//        System.out.println(new AdbUtilsImpl().isExisted("/system")); // true
//        System.out.println(new AdbUtilsImpl().isExisted("/succerseng")); // false
//        System.out.println(new AdbUtilsImpl().getConnectedDeviceNum());
    }

    /**
     * 获得当前已经连接的设备数量
     *
     * @return 设备数量
     */
    int getConnectedDeviceNum() {
        int count = 0;

        String adb = getADB();
        Process exec = null;
        BufferedReader bufferedReader = null;
        try {
            exec = Runtime.getRuntime().exec(getADB() + " devices");
            bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

            // 丢掉错误输出
            while (exec.getErrorStream().available() > 0) {
                exec.getErrorStream().read();
            }

            // 统计数量
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                if (!buffer.isEmpty()) count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return count - 1;
    }

    /**
     * 执行shell命令
     */
    public String[] shell(String command) {
        Process exec = null;
        BufferedReader bufferedReader = null;
        try {
            exec = Runtime.getRuntime().exec(getADB() + " shell " + command);
            bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream()));

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
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 通过adb获取全部文件列表
     *
     * @return 结果
     */
    public String getAllFileList() {
        if (allFileList == null) {
            // 检查设备数量
            int num = getConnectedDeviceNum();
            if (num == 0) {
                logger.println("当前设备数量 " + num + " 请连接设备");
                return null;
            }

            if (num > 1) {
                logger.println("当前设备数量 " + num + " 请减少设备");
                return null;
            }

            logger.println("正在获取文件列表(至少需要几分钟时间)...");
            String[] shell = shell("ls -R");
            logger.println("正在处理结果");
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

        // 得不到列表默认存在
        if (allFileList == null) return true;

        Pattern pattern = Pattern.compile(path);
        Matcher matcher = pattern.matcher(allFileList);
        return matcher.find();
    }

}
