package cc.succerseng.sepolicytools.modifypolicy.generalfiles.impl;


import cc.succerseng.sepolicytools.modifypolicy.generalfiles.SePolicyLineUtils;
import cc.succerseng.sepolicytools.utils.LineUtils;
import cc.succerseng.sepolicytools.utils.impl.LineUtilsImpl;

public class SePolicyLineUtilsImpl implements SePolicyLineUtils {
    private final LineUtils lineUtils = new LineUtilsImpl();

    /**
     * 获取某行的类型
     *
     * @param source 源
     * @return 类型
     */
    private static int getLineType(String source) {

        if (source.startsWith("#")) return NOTES;
        if (source.contains(";")) return OPERATE;
        if (source.contains("(") && source.contains(")") && !source.contains(";")) return FUNCTION;
        return OTHER;
    }

    /**
     * 格式化带函数字符串
     *
     * @param source 源
     * @return 结果
     */
    private String formatFunction(String source) {
        source = lineUtils.formatSymbol(source, '(', lineUtils.LEFT, lineUtils.DELETE);
        source = lineUtils.formatSymbol(source, '(', lineUtils.RIGHT, lineUtils.DELETE);
        source = lineUtils.formatSymbol(source, ')', lineUtils.LEFT, lineUtils.DELETE);
        source = lineUtils.formatSymbol(source, ')', lineUtils.RIGHT, lineUtils.DELETE);

        return source;
    }

    /**
     * 格式化带操作字符串
     *
     * @param source 源
     * @return 结果
     */
    private String formatOperator(String source) {
        source = lineUtils.formatSymbol(source, ',', lineUtils.LEFT, lineUtils.DELETE);
        source = lineUtils.formatSymbol(source, ',', lineUtils.RIGHT, lineUtils.ADD);

        if (source.contains("{")) {
            source = lineUtils.formatSymbol(source, '{', lineUtils.LEFT, lineUtils.ADD);
            source = lineUtils.formatSymbol(source, '{', lineUtils.RIGHT, lineUtils.ADD);
        }

        if (source.contains("}")) {
            source = lineUtils.formatSymbol(source, '}', lineUtils.LEFT, lineUtils.ADD);
            source = lineUtils.formatSymbol(source, '}', lineUtils.RIGHT, lineUtils.DELETE);
        }
        return source;
    }

    /**
     * 格式化注释
     *
     * @param source 源
     * @return 结果
     */
    private String formatNotes(String source) {
        source = lineUtils.formatSymbol(source, '#', lineUtils.RIGHT, lineUtils.ADD);
        return source;
    }

    /**
     * 格式化一行，自动判断行的类型（会去除多余空格）
     *
     * @param source 源字符串
     * @return 结果
     */
    @Override
    public String formatLine(String source) {
        // if start with space delete it
        while (source.startsWith(" ")) {
            source = source.substring(1);
        }

        int lineType = getLineType(source);

        source = source.replace('\t', ' ');

        if (lineType == FUNCTION) {
            source = formatFunction(source);
        } else if (lineType == OPERATE) {
            source = formatOperator(source);
        } else if (lineType == NOTES) {
            source = ""; // 注释不要
        }

        return source;
    }
}
