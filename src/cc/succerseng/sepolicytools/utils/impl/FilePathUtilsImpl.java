package cc.succerseng.sepolicytools.utils.impl;


import cc.succerseng.sepolicytools.utils.AboutSystem;
import cc.succerseng.sepolicytools.utils.FilePathUtils;

public class FilePathUtilsImpl implements FilePathUtils {
    @Override
    public String catPath(String dir, String fileName) {
        int systemType = new AboutSystemImpl().getSystemType();

        if (systemType == AboutSystem.WINDOWS) {
            return dir + "\\" + fileName;
        }

        return dir + "/" + fileName;
    }
}
