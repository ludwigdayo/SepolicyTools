package Utils.Impl;

import Gui.SepolicyToolsGUI;
import Utils.Logger;

public class LoggerImpl implements Logger {
    @Override
    public void println(String log) {
        SepolicyToolsGUI.log(log);
    }
}
