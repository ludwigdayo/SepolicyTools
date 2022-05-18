package cc.succerseng.sepolicytools.createpolicy;

public interface CreateRuleFromLog {

    /**
     * 直接从 log生成sePolicy
     *
     * @return 生成的政策
     */
    String[] logToSePolicy(String[] log);

    /**
     * 抓取log生成sePolicy
     * 需要连接您的设备
     *
     * @return 生成的se政策
     */
    String[] logCatToSePolicy();
}
