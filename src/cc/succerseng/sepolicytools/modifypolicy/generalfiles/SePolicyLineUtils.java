package cc.succerseng.sepolicytools.modifypolicy.generalfiles;

/**
 * 对sepolicy行的操作
 */
public interface SePolicyLineUtils {

    //描述某行的类型
    int FUNCTION = 0;
    int OPERATE = 1;
    int OTHER = 2;
    int NOTES = 3;

    /**
     * 格式化一行
     *
     * @param source 源字符串
     * @return 结果
     */
    String formatLine(String source);
}
