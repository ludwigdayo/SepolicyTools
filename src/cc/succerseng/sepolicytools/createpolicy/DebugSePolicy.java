package cc.succerseng.sepolicytools.createpolicy;

/**
 * 调试Selinux
 */
public interface DebugSePolicy {

    /**
     * 自动调试
     *
     * 不事先在sePolicy加任何许可，而是通过log信息按需要来添加
     */
    void autoDebug(String sePolicyDir,String outDir,String buildBootImageCommand);
}
