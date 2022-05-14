package Utils;

public interface FilePathUtils {
    /**
     * 根据不同系统拼接路径
     * @param dir 文件夹
     * @param fileName 文件
     * @return 结果
     */
    String catPath(String dir, String fileName);
}
