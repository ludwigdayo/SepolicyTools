package cc.succerseng.sepolicytools.createpolicy;

public interface CreateRuleFromLog {

    /**
     * 从 log生成sePolicy
     * @return 结果
     */
    String[] logToSePolicy(String[] log);
}