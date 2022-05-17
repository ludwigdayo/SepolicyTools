package cc.succerseng.sepolicytools.utils;

public interface AboutSystem {
    int WINDOWS = 0;
    int LINUX = 1;
    int OTHER = 2;

    /**
     * 获取系统类型
     */
    int getSystemType();
}
