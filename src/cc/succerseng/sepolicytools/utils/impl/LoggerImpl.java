package cc.succerseng.sepolicytools.utils.impl;

import cc.succerseng.sepolicytools.gui.SePolicyToolsGUI;
import cc.succerseng.sepolicytools.utils.Logger;

public class LoggerImpl implements Logger {
    @Override
    public void println(String log) {
        System.out.println(log);
//        SePolicyToolsGUI.log(log);
    }
}
