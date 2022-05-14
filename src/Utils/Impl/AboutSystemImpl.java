package Utils.Impl;

import Gui.SepolicyToolsGUI;
import Utils.AboutSystem;

public class AboutSystemImpl implements AboutSystem {

    @Override
    public int getSystemType() {
        String property = System.getProperty("os.name");
        if (property.contains("Windows")) {
            return WINDOWS;
        } else if (property.contains("Linux")) {
            return LINUX;
        } else {
            return OTHER;
        }
    }
}
