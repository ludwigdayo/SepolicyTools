package cc.succerseng.sepolicytools.modifypolicy.generalfiles;

/**
 * 对sepolicy文件夹的操作
 */
public interface SePolicyDirUtils {
    /**
     * 格式化dir下TE文件
     *
     * @param inPutDir  存有te文件的文件夹路径
     * @param outPutDir 输出文件夹路径
     */
    void formatFiles(String inPutDir, String outPutDir);

    /**
     * 整理te文件
     * 根据file_contexts的标签清理te
     *
     * @param inPutDir      sepolicy文件夹
     * @param file_contexts 描述文件
     */
    void reWriteTeFiles(String inPutDir, String outPutDir, String file_contexts);
}
