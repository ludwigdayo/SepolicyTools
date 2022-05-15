package ContextFileTools;

public interface ServiceContextsFormat {

    /**
     * 自动格式化service_contexts文件
     *
     * @param inPutPath  输入文件
     * @param outPutPath 输出文件
     */
    void autoFormatServiceContexts(String inPutPath, String outPutPath);
}
