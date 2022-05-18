package cc.succerseng.sepolicytools.createpolicy.impl;

import cc.succerseng.sepolicytools.createpolicy.CreateRuleFromLog;
import cc.succerseng.sepolicytools.utils.AdbUtils;
import cc.succerseng.sepolicytools.utils.Logger;
import cc.succerseng.sepolicytools.utils.impl.AdbUtilsImpl;
import cc.succerseng.sepolicytools.utils.impl.LoggerImpl;

import java.util.TreeSet;

public class CreateRuleFromLogImpl implements CreateRuleFromLog {
    private static final AdbUtils adbUtils = new AdbUtilsImpl();
    private static final Logger logger = new LoggerImpl();

    /**
     * se信息的行转许可政策
     *
     * @param seLogLine 如：W init : type=1400 audit(0.0:9): avc: denied { create } for name="blkid.tab" scontext=u:r:init:s0 tcontext=u:object_r:system_file:s0 tclass=file permissive=0
     * @return 如：allow init system_file:file { create };
     */
    private String seLogToAllowPolicy(String seLogLine) {
        if (seLogLine == null || seLogLine.isEmpty()) return null;

        // 放相关信息
        String scontext = null;
        String tcontext = null;
        String tclass = null;
        String authority = null;

        int start = 0;
        int end = 0;

        start = seLogLine.indexOf(" { ");
        end = seLogLine.indexOf(" } ", start);
        if (start == -1 || end == -1) return null;
        authority = seLogLine.substring(start + " { ".length(), end);

        start = seLogLine.indexOf("scontext=u:r:", start);
        end = seLogLine.indexOf(":s0", start);
        if (start == -1 || end == -1) return null;
        scontext = seLogLine.substring(start + "scontext=u:r:".length(), end);

        start = seLogLine.indexOf("tcontext=u:object_r:", start);
        end = seLogLine.indexOf(":s0", start);
        if (start == -1 || end == -1) return null;
        tcontext = seLogLine.substring(start + "tcontext=u:object_r:".length(), end);

        start = seLogLine.indexOf("tclass=", start);
        end = seLogLine.indexOf(" ", start);
        if (start == -1 || end == -1) return null;
        tclass = seLogLine.substring(start + "tclass=".length(), end);

        return "allow " + scontext + " " + tcontext + ":" + tclass + " { " + authority + " };";
    }

    /**
     * 根据log中的selinux信息生成相应的许可政策
     *
     * @param log 日志
     * @return 结果
     */
    @Override
    public String[] logToSePolicy(String[] log) {
        String[] result = null;

        if (log == null) {
            return null;
        }

        // 放selinux相关日志
        TreeSet<String> sePermissionSet = new TreeSet<>();
        TreeSet<String> resultSet = new TreeSet<>();


        // 如果日志里面这一行带permission 那这一行就是selinux相关的 放到集合里
        for (String line : log) {
            if (line.contains("permission")) {
                sePermissionSet.add(line);
            }
        }

        // seLog转成seAllowPolicy
        for (String line : sePermissionSet) {
            String s = seLogToAllowPolicy(line);
            if (s != null) {
                resultSet.add(s);
            }
        }

        result = new String[resultSet.size()];
        resultSet.toArray(result);
        return result;
    }

    /**
     * 抓取一万行log生成se政策
     * @return 生成物：se政策
     */
    @Override
    public String[] logCatToSePolicy() {
        return logToSePolicy(adbUtils.logcat());
    }

    public static void main(String[] args) {
        String s = new CreateRuleFromLogImpl().seLogToAllowPolicy("W init : type=1400 audit(0.0:9): avc: denied { create } for name=\"blkid.tab\" scontext=u:r:init:s0 tcontext=u:object_r:system_file:s0 tclass=file permissive=0");
        System.out.println(s);
    }
}
