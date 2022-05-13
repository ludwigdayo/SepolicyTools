package FormatTools.impl;

import FormatTools.LineUtils;

public class LineUtilsImpl implements LineUtils {

    @Override
    public boolean isNote(String s) {
        return s.startsWith("#");
    }

    @Override
    public String deleteDuplicateSpace(String s) {
        char[] chars = s.toCharArray();
        char[] chars_output = new char[chars.length];

        chars_output[0] = chars[0];
        int i = 1;
        int charsOutputLength = 1; // 统计砍掉重复空格后字符串的长度
        for (; i < chars.length; i++) {
            if (chars[i] == ' ' && chars[i - 1] == ' ') continue;
            chars_output[charsOutputLength++] = chars[i];
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

    public static void main(String[] args) {
        System.out.println(new LineUtilsImpl().deleteDuplicateSpace("      a     b c d  e  "));
    }
}
