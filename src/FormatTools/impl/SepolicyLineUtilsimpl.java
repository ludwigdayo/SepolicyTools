package FormatTools.impl;

import FormatTools.SepolicyLineUtils;

/**
 * 对sepolicy的行的操作的实现
 */
public class SepolicyLineUtilsimpl extends LineUtilsImpl implements SepolicyLineUtils {

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
     * 格式化符号
     *
     * @param source   源字符串
     * @param symbol   某字符
     * @param location 空格位置
     * @return 结果
     */
    @Override
    public String formatSymbol(String source, char symbol, int location) {
        int i = symbolCount(source, symbol); // 统计symbol数量

        if (location == LEFT) {
            return addLeftSpace(source, symbol, i);
        } else if (location == RIGHT) {
            return addRightSpace(source, symbol, i);
        }

        return deleteDuplicateSpace(source); // 去除多余重复的空格
    }

    public static void main(String[] args) {
        System.out.println(new SepolicyLineUtilsimpl().formatSymbol("aj,,,sdfj,sdf",',',RIGHT));
        System.out.println(new SepolicyLineUtilsimpl().formatSymbol("aj,sd,,,,,fj,sdf",',',LEFT));
        System.out.println(new SepolicyLineUtilsimpl().formatSymbol("aj,sd,,,,,fj,sdf",'(',LEFT));
    }
}
