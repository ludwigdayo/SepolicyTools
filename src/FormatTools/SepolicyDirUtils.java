package FormatTools;

/**
 * 对sepolicy文件夹的操作
 */
public interface SepolicyDirUtils extends SepolicyFileUtils {
    /**
     * 格式化dir下TE文件
     *
     * @param inPutDir  存有te文件的文件夹路径
     * @param outPutDir 输出文件夹路径
     */
    void formatFiles(String inPutDir, String outPutDir);
}
