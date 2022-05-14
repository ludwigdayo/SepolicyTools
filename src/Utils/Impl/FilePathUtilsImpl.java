package Utils.Impl;

import Utils.AboutSystem;

public class FilePathUtilsImpl implements Utils.FilePathUtils {
    @Override
    public String catPath(String dir, String fileName) {
        int systemType = new AboutSystemImpl().getSystemType();

        if (systemType == AboutSystem.WINDOWS) {
            return dir + "\\" + fileName;
        }

        return dir + "/" + fileName;
    }
}
