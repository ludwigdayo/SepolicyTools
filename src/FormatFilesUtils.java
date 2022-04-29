import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * 处理多个selinux policy文件
 */
class FormatFilesUtils {
    /**
     * 获得某目录下的文件列表
     *
     * @param path 某目录
     * @return 文件列表
     */
    private static File[] getList(String path) {
        File dir;
        File[] files;

        // use path to get dir
        if (!((dir = new File(path)).isDirectory())) return null;

        // get file list
        if ((files = dir.listFiles()) != null) System.out.println("Get list success");

        return files;
    }

    /**
     * get name from a line
     *
     * @param string
     * @return
     */
    private static String getName(String string) {
        int start;
        int end;

        // get name
        if (string.startsWith("allow")) {
            // allow "\space'name'\space"
            start = string.indexOf(' ');
            end = string.indexOf(' ', start + 1);
        } else if (string.startsWith("type")) {
            start = string.indexOf(' ');
            end = string.indexOf(',');
        } else {
            // function(xx) or function(xx, xx)
            start = string.indexOf('(');
            end = string.indexOf(',', start);
            if (end == -1) end = string.indexOf(')', start);
        }
        if (start == -1 || end == -1 || start >= end) return null;
        start++;

        return string.substring(start, end);
    }

    /**
     * rewrite all files content
     */
    private static void reWriteAllFile(ArrayList<File> filesList, TreeSet<String> object_rReallyUsed) throws IOException {
        TreeSet<String> filesContent = new TreeSet<>();

        for (File file : filesList) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath())));
            String string;
            while ((string = bufferedReader.readLine()) != null) filesContent.add(string);

            bufferedReader.close();
        }

        BufferedWriter bufferedWriter = null;
        String lastName = "";
        for (String string : filesContent) {
            String name = getName(string);
            if (name == null) continue;

            if (object_rReallyUsed.contains(name)) {
                // delay close stream
                if (!name.equals(lastName)) {
                    if (bufferedWriter != null) bufferedWriter.close();

                    String path = name + ".te";
                    File file = new File(path);
                    if (!file.exists() && !file.createNewFile()) System.out.println("Create file failed in " + path);

                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
                }

                if (bufferedWriter != null) {
                    bufferedWriter.write(string);
                    bufferedWriter.newLine();
                }
                lastName = name;
            }
        }
    }

    /**
     * 格式化某文件夹下的.te文件
     *
     * @param dir 文件夹路径
     */
    public static void formatFiles(String dir, FormatFileContexts formatFileContexts) throws IOException {
        File[] list;
        if ((list = getList(dir)) == null) return;

        ArrayList<File> filesList = new ArrayList<>();

        // file_contexts object_r
        TreeSet<String> object_rReallyUsed = formatFileContexts.getObject_rPathList();

        // get selinux config file object to arraylist
        for (File file : list) if (file.getName().endsWith("te")) filesList.add(file);

        // delete empty and notes line
        for (File file : filesList) FormatFileUtils.formatFile(file, false, false, false, false);

        // rewrite all
        reWriteAllFile(filesList, object_rReallyUsed);
    }
}
