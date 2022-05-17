package cc.succerseng.sepolicytools.utils.impl;

import cc.succerseng.sepolicytools.gui.SepolicyToolsGUI;
import cc.succerseng.sepolicytools.utils.Logger;

public class LoggerImpl implements Logger {
    @Override
    public void println(String log) {
        SepolicyToolsGUI.log(log);
    }
}
