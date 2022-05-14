package GeneralFilesTools.Impl;

import GeneralFilesTools.SepolicyDirUtils;
import Gui.SepolicyToolsGUI;
import Utils.Impl.StreamHelperImpl;

import java.io.BufferedReader;
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
    private String[] readAllLineFromFiles(String[] files) {
        ArrayList<String> arrayList = new ArrayList<>();
        StreamHelperImpl streamHelper = new StreamHelperImpl();
        String[] result = null;

        for (String file : files) {
            BufferedReader bufferReader = streamHelper.getBufferReader(file);
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
     * 得到dir下TE文件的名字
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
            SepolicyToolsGUI.log("格式化" + inPutDir + "/" + file);
            autoFormatFile(inPutDir + "/" + file, outPutDir + "/" + file);
            SepolicyToolsGUI.log("格式化" + inPutDir + "/" + file + "完成");
        }
    }

    /**
     * 将重新创建te文件
     *
     * @param inPutDir      sepolicy文件夹
     * @param outPutDir
     * @param file_contexts 描述文件
     */
    @Override
    public void reWriteTeFiles(String inPutDir, String outPutDir, String file_contexts) {
        // 放全部结果
        HashSet<Map<String, ArrayList<String>>> resultSet = null;
        String[] result = null;

        // 存储一个标签对应的所有语句
        Map<String, ArrayList<String>> labelMap = null;

        // 存储语句
        ArrayList<String> labelLines = null;

        // 得到te文件列表
        String[] teFileList = getTEFileList(inPutDir);
        if (teFileList == null) {
            SepolicyToolsGUI.log("没有找到任何te文件" + inPutDir);
            return;
        }

        // 得到标签
        String[] labelFromFile_contexts = getLabelFromFile_contexts(file_contexts);

        // 得到文件内容
        String[] allContents = readAllLineFromFiles(teFileList);

        // 关联标签与包含此标签的行
        for (String lable : labelFromFile_contexts) {
            labelMap = new HashMap<>();
            labelLines = new ArrayList<>();
            for (String line : allContents) {
                if (line.contains(lable)) {
                    labelLines.add(line);
                }
            }

            // 将 aaa_bbbb_cccccc 中的aaa作为标签，如果没有_就整个作为标签
            if (lable.contains("_")) {
                labelMap.put(lable.substring(0, lable.indexOf("_")), labelLines);
            } else {
                labelMap.put(lable, labelLines);
            }
        }
    }

    public static void main(String[] args) {
        new SepolicyDirUtilsImpl().formatFiles("sepolicy", "output");
    }

}
