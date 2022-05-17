package cc.succerseng.sepolicytools.createpolicy.impl;

import cc.succerseng.sepolicytools.createpolicy.CreateRuleFromLog;
import cc.succerseng.sepolicytools.utils.Logger;
import cc.succerseng.sepolicytools.utils.impl.LoggerImpl;

import java.util.TreeSet;

public class CreateRuleFromLogImpl implements CreateRuleFromLog {
    private static Logger logger = new LoggerImpl();

    /**
     * se信息的行转许可政策
     *
     * @param selogLine 如：W init : type=1400 audit(0.0:9): avc: denied { create } for name="blkid.tab" scontext=u:r:init:s0 tcontext=u:object_r:system_file:s0 tclass=file permissive=0
     * @return 如：allow init system_file:file { create };
     */
    private String seLogToAllowPolicy(String selogLine) {
        if (selogLine == null || selogLine.isEmpty()) return null;

        // 放相关信息
        String scontext = null;
        String tcontext = null;
        String tclass = null;
        String authority = null;

        int start = 0;
        int end = 0;

        start = selogLine.indexOf(" { ");
        end = selogLine.indexOf(" } ", start);
        if (start == -1 || end == -1) return null;
        authority = selogLine.substring(start + " { ".length(), end);

        start = selogLine.indexOf("scontext=u:r:", start);
        end = selogLine.indexOf(":s0", start);
        if (start == -1 || end == -1) return null;
        scontext = selogLine.substring(start + "scontext=u:r:".length(), end);

        start = selogLine.indexOf("tcontext=u:object_r:", start);
        end = selogLine.indexOf(":s0", start);
        if (start == -1 || end == -1) return null;
        tcontext = selogLine.substring(start + "tcontext=u:object_r:".length(), end);

        start = selogLine.indexOf("tclass=", start);
        end = selogLine.indexOf(" ", start);
        if (start == -1 || end == -1) return null;
        tclass = selogLine.substring(start + "tclass=".length(), end);

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
            logger.println("没有抓到log哟~");
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

    public static void main(String[] args) {
        String s = new CreateRuleFromLogImpl().seLogToAllowPolicy("W init : type=1400 audit(0.0:9): avc: denied { create } for name=\"blkid.tab\" scontext=u:r:init:s0 tcontext=u:object_r:system_file:s0 tclass=file permissive=0");
        System.out.println(s);
    }
}
