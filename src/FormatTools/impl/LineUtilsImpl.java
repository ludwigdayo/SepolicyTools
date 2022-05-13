package FormatTools.impl;

import FormatTools.LineUtils;

public class LineUtilsImpl implements LineUtils {

    @Override
    public String deleteDuplicateSpace(String s) {
        char[] chars = s.toCharArray();
        char[] chars_output = new char[chars.length];

        int charsOutputLength = 1; // 新字符串的下标
        chars_output[0] = chars[0];
        for (int i = 1; i < chars.length; i++) {
            if (chars[i] == ' ' && chars[i - 1] == ' ') continue;

            chars_output[charsOutputLength] = chars[i];
            charsOutputLength++;
        }

        String substring = new String(chars_output).substring(0, charsOutputLength);// 截掉没有设值的部分

        // 去掉开头的空格
        while (substring.startsWith(" ")) {
            substring = substring.substring(1);
        }

        // 去掉结尾的空格
        while (substring.endsWith(" ")) {
            substring = substring.substring(0, substring.length() - 1);
        }

        return substring;
    }


    /**
     * 统计一共有几个symbol
     */
    private int symbolCount(String source, char symbol) {
        int i = 0, count = 0;
        do {
            i = source.indexOf(symbol, i);
            if (i != -1) count++;
        } while (i != -1 && ++i <= source.length());

        return count;
    }

    /**
     * 在symbol左边加一个空格
     *
     * @param source 源
     * @param symbol 符号
     * @param count  符号数量
     * @return 结果
     */
    private String addLeftSpace(String source, char symbol, int count) {
        if (source.isEmpty()) return source;

        char[] chars = source.toCharArray();
        char[] chars_output = new char[chars.length + count]; // 大小要加上添加的空格的数量

        for (int j = 0, i = 0; i < chars.length; i++, j++) {
            // 发现符号就在符号前加空格
            if (chars[i] == symbol) {
                chars_output[j] = ' ';
                j++;
            }

            chars_output[j] = chars[i];
        }

        return new String(chars_output);
    }

    /**
     * 在symbol右边加一个符号
     *
     * @param source 源
     * @param symbol 符号
     * @return 结果
     */
    private String addRightSpace(String source, char symbol, int count) {
        if (source.isEmpty()) return source;

        char[] chars = source.toCharArray();
        char[] chars_output = new char[chars.length + count];// 大小要加上添加的空格的数量


        for (int j = 0, i = 0; i < chars.length; i++, j++) {
            chars_output[j] = chars[i];

            // 发现符号就在符号后加空格
            if (chars[i] == symbol) {
                j++;
                chars_output[j] = ' ';
            }
        }

        return new String(chars_output);
    }

    /**
     * 删除symbol左边存在的空格
     *
     * @param source 源
     * @param symbol 符号
     * @return 结果
     */
    private String deleteLeftSpace(String source, char symbol) {
        if (source.length() < 2) return source;

        char[] chars = source.toCharArray();
        char[] chars_output = new char[chars.length];

        int count = chars.length;
        chars_output[0] = chars[0];
        for (int j = 1, i = 1; i < chars.length; i++, j++) {
            if (chars[i] == symbol && chars[i - 1] == ' ') {
                j--;
                count--;
            }

            chars_output[j] = chars[i];
        }

        return new String(chars_output).substring(0, count);
    }

    /**
     * 删除symbol右边存在的空格
     *
     * @param source 源
     * @param symbol 符号
     * @return 结果
     */
    private String deleteRightSpace(String source, char symbol) {
        if (source.length() < 2) return source;

        char[] chars = source.toCharArray();
        char[] chars_output = new char[chars.length];

        int count = chars.length;
        for (int j = 0, i = 0; i < chars.length; i++, j++) {
            if (chars[i] == symbol) {
                while (i + 1 < chars.length && chars[i + 1] == ' ') {
                    i++;
                    count--;
                }
            }

            chars_output[j] = chars[i];
        }

        return new String(chars_output).substring(0, count);
    }

    /**
     * 格式化符号
     *
     * @param source   源字符串
     * @param symbol   某字符
     * @param location 空格位置
     * @return 结果
     */
    @Override
    public String formatSymbol(String source, char symbol, int location, int operate) {
        if (!source.contains(Character.toString(symbol))) return source; // 要是不存在symbol直接返回原字符串

        int i = symbolCount(source, symbol); // 统计symbol数量

        if (operate == ADD) {
            if (location == LEFT) {
                source = addLeftSpace(source, symbol, i);
            } else if (location == RIGHT) {
                source = addRightSpace(source, symbol, i);
            }
        } else if (operate == DELETE) {
            if (location == LEFT) {
                source = deleteLeftSpace(source, symbol);
            } else if (location == RIGHT) {
                source = deleteRightSpace(source, symbol);
            }
        }

        return deleteDuplicateSpace(source); // 去除多余重复的空格
    }

    public static void main(String[] args) {
        System.out.println(new LineUtilsImpl().formatSymbol("asdjfljdl (asdf)", '(', LEFT, DELETE));
        System.out.println(new LineUtilsImpl().formatSymbol("asdjfljdl (asdf)", '(', LEFT, ADD));
        System.out.println(new LineUtilsImpl().formatSymbol("asdjfljdl (asdf)", '(', LEFT, ADD));
        System.out.println(new LineUtilsImpl().formatSymbol("asdffffffff)", ')', RIGHT, DELETE));
    }
}
