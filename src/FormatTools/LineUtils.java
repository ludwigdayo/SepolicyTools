package FormatTools;

/**
 * 对行的操作
 */
public interface LineUtils {
    /**
     * 根据字符串的开头判断是否为注释
     * @return 是否为注释
     */
    boolean isNote(String s);

    /**
     * 删除重复空格
     * @return 新字符串
     */
    String deleteDuplicateSpace(String s);

}
