package FormatTools.impl;

import FormatTools.SepolicyLineUtils;

public class SepolicyLineUtilsImpl extends LineUtilsImpl implements SepolicyLineUtils {
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
        source = formatSymbol(source, '(', LEFT, DELETE);
        source = formatSymbol(source, '(', RIGHT, DELETE);
        source = formatSymbol(source, ')', LEFT, DELETE);
        source = formatSymbol(source, ')', RIGHT, DELETE);

        return source;
    }

    /**
     * 格式化带操作字符串
     *
     * @param source 源
     * @return 结果
     */
    private String formatOperator(String source) {
        source = formatSymbol(source, ',', LEFT, DELETE);
        source = formatSymbol(source, ',', RIGHT, ADD);

        if (source.contains("{")) {
            source = formatSymbol(source, '{', LEFT, ADD);
            source = formatSymbol(source, '{', RIGHT, ADD);
        }

        if (source.contains("}")) {
            source = formatSymbol(source, '}', LEFT, ADD);
            source = formatSymbol(source, '}', RIGHT, DELETE);
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
        source = formatSymbol(source, '#', RIGHT, ADD);
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
        int lineType = getLineType(source);

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
