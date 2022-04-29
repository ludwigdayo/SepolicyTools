import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * 处理file_contexts文件
 */
class FormatFileContexts {
    File file;

    public FormatFileContexts(String file_contexts) {
        file = new File(file_contexts);
    }

    /**
     * 获得存放在file_contexts里的文件路径字符串
     *
     * @return 文件路径的列表
     */
    private static String[] getPathList(File file) throws IOException {
        String[] strings;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));
        TreeSet<String> treeSet = new TreeSet<>(String::compareTo);

        while (true) {
            String s = bufferedReader.readLine();
            if (s == null) break;
            if (s.startsWith("#") || s.isEmpty()) continue;

            // substring
            int index = s.indexOf(' ');
            if (index == -1) index = s.indexOf('\t');
            if (index == -1) continue;
            String substring = s.substring(0, index);

            // replace
            substring = substring.replace("(/.*)?", "");
            substring = substring.replace("[0-9]+\\", "*");
            substring = substring.replace("[0-9]+", "*");
            substring = substring.replace("\\.0", "*");
            if (substring.contains("/(system\\/vendor|vendor)")) {
                treeSet.add(substring.replace("/(system\\/vendor|vendor)", "/system/vendor"));
                treeSet.add(substring.replace("/(system\\/vendor|vendor)", "/vendor"));
                continue;
            }

            treeSet.add(substring);
        }
        bufferedReader.close();

        strings = new String[treeSet.size()];
        treeSet.toArray(strings);
        return strings;
    }

    /**
     * 通过adb检查文件是否存在
     *
     * @return 不存在的文件的路径列表
     */
    private static String[] checkPath(String[] paths) {
        ArrayList<String> unExistedList = new ArrayList<>();
        String[] result;

        // send command to judge whether file exists or not
        try {
            for (String path : paths) {
                // send command
                Process process = Runtime.getRuntime().exec("adb shell num=$(ls -l " + path + " 2>/dev/null | wc -l); if [ $num -ge 1 ]; then echo true; else echo false;fi 2>/dev/null");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                BufferedWriter notFound = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("notFound.txt", false)));

                // read result
                String s = bufferedReader.readLine();
                if (s != null && !s.equals("true")) {
                    unExistedList.add(path);
                    notFound.write(path);
                    notFound.newLine();
                    System.out.println(path + " not found");
                }

                bufferedReader.close();
                notFound.close();
            }
        } catch (IOException e) {
            System.out.println("error: Disconnected");
            return null;
        }

        // return array result
        result = new String[unExistedList.size()];
        unExistedList.toArray(result);
        return result;
    }

    /**
     * 替换空格为制表符并去除多余空格
     *
     * @param source 源字符
     * @return 结果
     */
    public static String removeRepetitiveSpace(String source) {
        if (source.contains(" ")) source = source.replace(' ', '\t');

        int j = 0;
        do {
            int i;
            if ((i = source.indexOf('\t', j)) != -1 && i < source.length() - 1 && source.charAt(i + 1) == '\t')
                source = source.substring(0, i).concat(source.substring(i + 1));
            else j++;
        } while (j <= source.length());

        return source;
    }

    /**
     * 根据文件路径字符串，删除文件中包含此字符串的行，顺便格式化语句
     *
     * @param file    目标文件
     * @param strings 带有删除目标的字符串数组
     */
    public static void deleteLine(File file, String[] strings) throws IOException {
        if (strings == null || file == null) return;

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));
        BufferedWriter bufferedWriter;

        // read from file
        ArrayList<String> arrayList = new ArrayList<>();
        while (true) {
            boolean flag = true;
            String s = bufferedReader.readLine();
            if (s == null) break;

            for (String string : strings) {
                if (s.contains(string)) {
                    flag = false;
                    System.out.println("Delete: " + s);
                    break;
                }
            }

            if (flag) arrayList.add(s);
        }
        bufferedReader.close();

        // write to file
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false)));
        for (String s : arrayList) {
            bufferedWriter.write(removeRepetitiveSpace(s));
            bufferedWriter.newLine();
        }
        System.out.println("Write back to file success");
        bufferedWriter.close();
    }

    /**
     * 获得存放在file_contexts里的u:object_r:后的字符串
     *
     * @return
     */
    public TreeSet<String> getObject_rPathList() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));
        TreeSet<String> treeSet = new TreeSet<>(String::compareTo);

        while (true) {
            String s = bufferedReader.readLine();
            if (s == null) break;
            if (s.startsWith("#") || s.isEmpty()) continue;

            String substring = s.substring(s.indexOf("u:object_r:") + "u:object_r:".length(), s.indexOf(":s0"));
            treeSet.add(substring);
        }
        bufferedReader.close();

        return treeSet;
    }

    /**
     * 格式化file_contexts文件
     */
    public void formatFileContexts() throws IOException {
        String[] paths;

        // 获得路径列表
        paths = getPathList(file);

        // 通过adb检查文件是否存在
        String[] strings = checkPath(paths);

        // 删除不存在文件 顺便格式化语句
        deleteLine(file, strings);

        // 删除注释 删除多余空行
        FormatFileUtils.formatFile(file, false, false, false, false);
    }

}
