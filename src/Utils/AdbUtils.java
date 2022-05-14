package Utils;

/**
 * adb工具
 */
public interface AdbUtils {
    /**
     * 执行shell命令
     *
     * @param command 要用adb shell执行的命令
     * @return 输出结果
     */
    String[] shell(String command);

    /**
     * 判断文件是否存在
     *
     * @param path 文件在设备中的绝对路径
     * @return 返回是否存在
     */
    boolean isExisted(String path);
}
