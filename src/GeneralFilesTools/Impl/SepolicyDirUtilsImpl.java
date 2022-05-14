package GeneralFilesTools.Impl;

import GeneralFilesTools.SepolicyDirUtils;
import Gui.SepolicyToolsGUI;
import Utils.Impl.FilePathUtilsImpl;
import Utils.Impl.StreamHelperImpl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class SepolicyDirUtilsImpl extends SepolicyFileUtilsImpl implements SepolicyDirUtils {

    /**
     * 从多个文件中读取
     *
     * @param files 文件列表
     * @return 合并的文件内容
     */
    private String[] readAllLineFromFiles(String inputDir, String[] files) {
        ArrayList<String> arrayList = new ArrayList<>();
        StreamHelperImpl streamHelper = new StreamHelperImpl();
        String[] result = null;

        for (String file : files) {
            BufferedReader bufferReader = streamHelper.getBufferReader(new FilePathUtilsImpl().catPath(inputDir, file));
            try {
                String line;
                while ((line = bufferReader.readLine()) != null) {
                    arrayList.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            streamHelper.close(bufferReader);
        }

        result = new String[arrayList.size()];
        arrayList.toArray(result);

        return result;
    }

    /**
     * 从file_contexts获取标签
     *
     * @param file_contexts 描述文件
     * @return 结果
     */
    private String[] getLabelFromFile_contexts(String file_contexts) {

        StreamHelperImpl streamHelper = new StreamHelperImpl();
        BufferedReader bufferReader = streamHelper.getBufferReader(file_contexts);
        TreeSet<String> resultSet = new TreeSet<>();
        String[] result = null;

        if (bufferReader == null) return null;

        String line = null;
        try {
            while ((line = bufferReader.readLine()) != null) {
                if (line.startsWith("#") || !line.contains("u:object_r:")) continue;

                int start = line.indexOf("u:object_r:") + "u:object_r:".length();
                int end = line.lastIndexOf(":s0");

                resultSet.add(line.substring(start, end));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        streamHelper.close(bufferReader);

        result = new String[resultSet.size()];
        resultSet.toArray(result);
        return result;
    }


    /**
     * 得到dir下TE文件的相对路径
     *
     * @return 列表
     */
    private String[] getTEFileList(String dir) {
        File directory = new File(dir);
        List<String> list = new ArrayList<>();
        String[] result = null;

        if (!directory.isDirectory()) {
            return null;
        }

        File[] files = directory.listFiles();
        if (files == null) {
            return null;
        }

        String path = null;
        for (File f : files) {
            path = f.getAbsolutePath();
            if (path.endsWith(".te")) {
                list.add(f.getName());
            }
        }

        result = new String[list.size()];
        list.toArray(result);
        return result;
    }

    /**
     * 格式化整个文件夹下的te文件
     * 列出文件列表，再格式化
     *
     * @param inPutDir  存有te文件的文件夹路径
     * @param outPutDir 输出文件夹路径
     */
    @Override
    public void formatFiles(String inPutDir, String outPutDir) {
        String[] tEFileList = getTEFileList(inPutDir);

        if (tEFileList == null) {
            SepolicyToolsGUI.log("打开" + inPutDir + "失败");
            return;
        }

        SepolicyToolsGUI.log("在" + inPutDir + "共找到" + tEFileList.length + "个te文件");

        File outPutFd = new File(outPutDir);

        if (!outPutFd.exists()) {
            SepolicyToolsGUI.log("创建文件夹" + outPutFd);
            outPutFd.mkdirs();
        }

        for (String file : tEFileList) {
            SepolicyToolsGUI.log("格式化" + file);
            autoFormatFile(new FilePathUtilsImpl().catPath(inPutDir, file), new FilePathUtilsImpl().catPath(outPutDir, file));
            SepolicyToolsGUI.log("格式化" + file + "完成");
        }
    }

    /**
     * 将重新创建te文件
     *
     * @param inPutDir      sepolicy文件夹
     * @param outPutDir     输出文件夹
     * @param file_contexts 描述文件
     */
    @Override
    public void reWriteTeFiles(String inPutDir, String outPutDir, String file_contexts) {
        StreamHelperImpl streamHelper = new StreamHelperImpl();

        // 存储一个标签对应的所有语句
        Map<String, TreeSet<String>> labelMap = new HashMap<>();

        // 存储语句
        TreeSet<String> labelLines = null;

        // 得到te文件列表
        String[] teFileList = getTEFileList(inPutDir);
        if (teFileList == null) {
            SepolicyToolsGUI.log("没有找到任何te文件" + inPutDir);
            return;
        }

        // 得到标签
        if (!new File(file_contexts).exists()) {
            SepolicyToolsGUI.log("没有找到file_contexts文件在" + file_contexts);
            return;
        }
        String[] labelFromFile_contexts = getLabelFromFile_contexts(file_contexts);
        if (labelFromFile_contexts == null) {
            SepolicyToolsGUI.log("file_contexts文件为空" + file_contexts);
            return;
        }

        // 得到文件内容
        String[] allContents = readAllLineFromFiles(inPutDir, teFileList);

        // 输入与输出的是同一个文件夹就删除旧te文件
        if (inPutDir.equals(outPutDir)) {
            for (String file : teFileList) {
                new File(file).delete();
            }
        }

        // 关联标签与包含此标签的行
        for (String label : labelFromFile_contexts) {
            labelLines = new TreeSet<>();
            for (String line : allContents) {
                if (line.contains(label)) {
                    labelLines.add(line);
                }
            }

            // 将 aaa_bbbb_cccccc 中的aaa作为标签，如果没有_就整个作为标签
            if (label.contains("_")) label = label.substring(0, label.indexOf("_"));

            // 如果key已存在则要添加上已添加的数据，再放回map
            if (labelMap.containsKey(label)) {
                TreeSet<String> oldData = labelMap.get(label);
                labelLines.addAll(oldData);
            }

            labelMap.put(label, labelLines);
        }

        // 写入新文件
        for (String lable : labelMap.keySet()) {
            String path = new FilePathUtilsImpl().catPath(new File(outPutDir).getPath(), lable) + ".te";
            BufferedWriter bufferWriter = streamHelper.getBufferWriter(path, false);

            TreeSet<String> data = labelMap.get(lable);
            try {
                for (String next : data) {
                    bufferWriter.write(next);
                    bufferWriter.newLine();
                }
            } catch (IOException e) {
                SepolicyToolsGUI.log("写入文件失败" + path);
            }

            streamHelper.close(bufferWriter);

            // 删除空文件
            File newFile = new File(path);
            if (newFile.length() == 0) {
                newFile.delete();
            }
        }
    }

    public static void main(String[] args) {
        new SepolicyDirUtilsImpl().reWriteTeFiles("sepolicy", "sepolicy", "sepolicy/file_contexts");
    }

}
