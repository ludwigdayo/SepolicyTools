package FormatTools.impl;

import FormatTools.SepolicyDirUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SepolicyDirUtilsImpl extends SepolicyFileUtilsImpl implements SepolicyDirUtils {
    /**
     * 得到dir下TE文件的名字
     *
     * @return 列表
     */
    private List<String> getTEFileList(String dir) {
        File directory = new File(dir);
        List<String> list = new ArrayList<>();

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

        return list;
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
        List<String> teFileList = getTEFileList(inPutDir);
        if (teFileList == null) {
            System.out.println("未找到TE文件在" + inPutDir);
            return;
        }

        File outPutFd = new File(outPutDir);

        if (!outPutFd.exists() && outPutFd.mkdirs()) {
            for (String file : teFileList) {
                autoFormatFile(inPutDir + "/" + file, outPutDir + "/" + file);
            }
        } else {
            System.out.println("输出文件夹" + outPutDir + "已存在");
            return;
        }
    }

    public static void main(String[] args) {
        new SepolicyDirUtilsImpl().formatFiles("sepolicy", "output");
    }
}
