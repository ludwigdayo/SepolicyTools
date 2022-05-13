package FormatTools;

/**
 * 对一个te文件的操作
 */
public interface SepolicyFileUtils extends SepolicyLineUtils {

    /**
     * 对行的排序
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    void sortLines(String inPutPath, String outPutPath);

    /**
     * 格式化每一行
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    void formatAllLine(String inPutPath,String outPutPath);

    /**
     * 自动格式化文件
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    void autoFormatFile(String inPutPath, String outPutPath);

}
