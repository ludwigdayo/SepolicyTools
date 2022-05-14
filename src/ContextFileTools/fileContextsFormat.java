package ContextFileTools;

import Utils.AdbUtils;

public interface fileContextsFormat {
    /**
     * 获得file_contexts文件中文件路径列表
     *
     * @param content file_contexts文件内容
     * @return 结果
     */
    String[] getPathList(String[] content);

    /**
     * 格式化file_contexts文件每一行
     *
     * @param content file_contexts文件内容
     * @return 结果
     */
    String[] formatAllLine(String[] content);

    /**
     * 清理没有用的行
     * 原理：通过adb判断这一行的文件是否存在
     *
     * @param content file_contexts文件内容
     * @return 结果
     */
    String[] cleanNotExistedLine(String[] content);

    /**
     * 自动格式化file_contexts文件
     * 1、获得文件列表
     * 2、清理无用行
     * 3、格式化剩下行
     *
     * @param inPutPath  输入文件
     * @param outPutPath 输出文件
     */
    void autoFormatFileContexts(String inPutPath, String outPutPath);
}
