package cc.succerseng.sepolicytools.modifypolicy.generalfiles;

/**
 * 对一个te文件的操作
 */
public interface SePolicyFileUtils {

    /**
     * get all line from a file
     */
    String[] readAllLineFromTEFile(String file);

    /**
     * 格式化每一行
     *
     * @param content 需要格式化的内容
     */
    void formatAllLine(String[] content);

    /**
     * 自动格式化文件
     *
     * @param inPutPath  输入文件路径
     * @param outPutPath 输出文件路径
     */
    void autoFormatFile(String inPutPath, String outPutPath);

}
