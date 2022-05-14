package Utils;

/**
 * 对行的操作
 */
public interface LineUtils {
    int LEFT = 0;
    int RIGHT = 1;

    int ADD = 0;
    int DELETE = 1;

    /**
     * 在字符串某字符的左或右放置一个空格
     *
     * @param source   源字符串
     * @param symbol   某字符
     * @param location 空格位置
     * @return 修改后的新字符串
     */
    String formatSymbol(String source, char symbol, int location, int operate);

    /**
     * 删除重复空格
     *
     * @return 新字符串
     */
    String deleteDuplicateSpace(String s);
}
