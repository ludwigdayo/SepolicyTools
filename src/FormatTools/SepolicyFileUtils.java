package FormatTools;

/**
 * 对一个te文件的操作
 */
public interface SepolicyFileUtils {
    /**
     * 格式化每一行
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    void formatFile(String inPutPath, String outPutPath);

    /**
     * 对行的排序
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    void sortLines(String inPutPath, String outPutPath);
}
