package FormatTools;

/**
 * 对sepolicy行的操作
 */
public interface SepolicyLineUtils {
    int LEFT = 0;
    int RIGHT = 1;

    /**
     * 在字符串某字符的左或右放置一个空格
     * @param source 源字符串
     * @param symbol 某字符
     * @param location 空格位置
     * @return 修改后的新字符串
     */
    String formatSymbol(String source, char symbol, int location);
}
