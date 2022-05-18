package cc.succerseng.sepolicytools.utils;

/**
 * 构建android源码
 */
public interface AndroidBuildHelper {
    /**
     * 构建
     *
     * @param target  构建目标
     * @param deviceType lunch后跟着的东西
     * @param rootDir 源码根目录
     * @return 是否成功
     */
    boolean make(String rootDir, String deviceType, String target);
}
