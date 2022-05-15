package ContextFileTools;

public interface ContextsUtils {

    /**
     * 格式化contexts文件每一行
     *
     * @param content contexts文件内容
     * @return 结果
     */
    String[] formatAllLine(String[] content);

    /**
     * 把文件读取到字符串数组
     *
     * @param inPutPath 输入文件路径
     * @return 结果
     */
    String[] fileToString(String inPutPath);

    /**
     * 从contexts获取标签
     *
     * @param contexts 文件
     * @return 结果
     */
    String[] getLabelFromContexts(String contexts);

    /**
     * 从dir下的*_contexts获得标签
     */
    String[] getAllLabels(String dir);

    /**
     * 格式化dir下所有context文件
     * 除了file_contexts
     */
    void autoFormatAllContext(String dir);

}
