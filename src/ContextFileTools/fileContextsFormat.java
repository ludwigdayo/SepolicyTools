package ContextFileTools;

import Utils.AdbUtils;

public interface fileContextsFormat extends AdbUtils {
    /**
     * 获得file_contexts文件中文件路径列表
     * @param content file_contexts文件内容
     * @return 结果
     */
    String[] getPathList(String[] content);

    /**
     * 格式化file_contexts文件每一行
     * @param content file_contexts文件内容
     * @return 结果
     */
    String[] formatAllLine(String[] content);

    /**
     * 清理没有用的行
     * 原理：通过adb判断这一行的文件是否存在
     * @param content file_contexts文件内容
     * @return 结果
     */
    String[] cleanNotExistedLine(String[] content);
}
