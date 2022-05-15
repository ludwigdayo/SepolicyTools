package Gui;

import ContextFileTools.ContextsUtils;
import ContextFileTools.Impl.ContextsUtilsImpl;
import ContextFileTools.Impl.FileContextsFormatImpl;
import GeneralFilesTools.Impl.SepolicyDirUtilsImpl;
import GeneralFilesTools.SepolicyDirUtils;
import Utils.Impl.FilePathUtilsImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * 图形界面
 */
public class SepolicyToolsGUI extends JFrame {
    private static final StringBuilder logStringBuilder = new StringBuilder();
    private static JTextArea textArea = null;
    private final SepolicyDirUtils sepolicyDirUtils = new SepolicyDirUtilsImpl();
    private final FileContextsFormatImpl fileContextsFormat = new FileContextsFormatImpl();
    private final ContextsUtils contextsUtils = new ContextsUtilsImpl();
    private final Font globalFont = new Font(null, Font.BOLD, 30);
    private final Font textFont = new Font(null, Font.BOLD, 20);
    private final Font buttonFont = new Font(null, Font.BOLD, 15);
    private final Color themeColor = new Color(195, 233, 242);
    private File sourceFile = new File(".");
    private Container contentPane = null;
    private JLabel sourceDirLabel = null;
    private JTextField sourceDirString = null;
    private JButton selectDirButton = null;
    private JPanel selectSourcePanel = null;
    private JButton formatTEFilesButton = null;
    private JButton formatFileContextsButton = null;
    private JButton reWriteTEFilesButton = null;
    private JButton formatAllContextsFileButton = null;
    private JPanel centerPanel = null;
    private JPanel southPanel = null;
    private JButton autoRun = null;

    /**
     * 界面
     */
    public SepolicyToolsGUI() {
        contentPane = getContentPane();

        sourceDirLabel = new JLabel("工作路径:");
        sourceDirLabel.setFont(textFont);

        sourceDirString = new JTextField(35);
        sourceDirString.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File path = new File(sourceDirString.getText());

                if (path.exists()) {
                    sourceFile = path;
                    SepolicyToolsGUI.log("切换文件路径" + sourceFile.getAbsolutePath());
                }

                sourceDirString.setText(sourceFile.getAbsolutePath());
            }
        });
        sourceDirString.setFont(textFont);
        sourceDirString.setText(sourceFile.getAbsolutePath());

        selectDirButton = new JButton("选择工作目录");
        selectDirButton.setBackground(themeColor);
        selectDirButton.setFont(buttonFont);
        selectDirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = null;
                if ((file = selectDirDialog()) == null) {
                    SepolicyToolsGUI.log("未选择文件");
                } else {
                    sourceFile = file;
                    SepolicyToolsGUI.log("工作目录切换到" + file.getPath());
                    sourceDirString.setText(sourceFile.getAbsolutePath());
                }
            }
        });

        selectSourcePanel = new JPanel();
        selectSourcePanel.add(sourceDirLabel, BorderLayout.WEST);
        selectSourcePanel.add(sourceDirString, BorderLayout.CENTER);
        selectSourcePanel.add(selectDirButton, BorderLayout.EAST);

        contentPane.add(selectSourcePanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setColumns(57);
        textArea.setRows(50);
        textArea.setFont(textFont);
        textArea.setEditable(false);

        centerPanel = new JPanel();
        centerPanel.add(textArea);
        contentPane.add(centerPanel, BorderLayout.CENTER);

        formatTEFilesButton = new JButton("格式化TE文件");
        formatTEFilesButton.setFont(buttonFont);
        formatTEFilesButton.setBackground(themeColor);
        formatTEFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatTEFilesFunction();
            }
        });

        reWriteTEFilesButton = new JButton("清理并重命名TE文件");
        reWriteTEFilesButton.setBackground(themeColor);
        reWriteTEFilesButton.setFont(buttonFont);
        reWriteTEFilesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reWriteTEFilesFunction();
            }
        });

        formatFileContextsButton = new JButton("处理file_contexts");
        formatFileContextsButton.setFont(buttonFont);
        formatFileContextsButton.setBackground(themeColor);
        formatFileContextsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatFileContextsFunction();
            }
        });

        formatAllContextsFileButton = new JButton("格式化Context");
        formatAllContextsFileButton.setFont(buttonFont);
        formatAllContextsFileButton.setBackground(themeColor);
        formatAllContextsFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatAllContextsFileFunction();
            }
        });

        autoRun = new JButton("自动运行");
        autoRun.setBackground(themeColor);
        autoRun.setFont(buttonFont);
        autoRun.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                formatFileContextsFunction();
                formatAllContextsFileFunction();
                reWriteTEFilesFunction();
                formatTEFilesFunction();
            }
        });

        southPanel = new JPanel();

        GridLayout gridLayout = new GridLayout(1, 10);
        gridLayout.setVgap(15);
        gridLayout.setHgap(15);
        southPanel.setLayout(gridLayout);
        southPanel.add(formatTEFilesButton);
        southPanel.add(formatFileContextsButton);
        southPanel.add(reWriteTEFilesButton);
        southPanel.add(formatAllContextsFileButton);
        southPanel.add(autoRun);
        contentPane.add(southPanel, BorderLayout.SOUTH);

        setTitle("Sepolicy工具");
        setResizable(false);
        setFont(globalFont);
        setBounds(200, 100, 1000, 618);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * 界面日志输出
     * 带有高级自动清除功能（才不是不会用滚动面板
     */
    public static void log(String log) {
        while (log.length() > 75) {
            logStringBuilder.append(log, 0, 75);
            logStringBuilder.append("\r\n");
            log = log.substring(75);
        }

        logStringBuilder.append(log);
        logStringBuilder.append("\r\n");

        int line = 0;
        while ((logStringBuilder.indexOf("\r\n", line)) != -1) line++;

        if (textArea != null) {
            textArea.setText(logStringBuilder.toString());
        }

        if (line > 27 * 10) logStringBuilder.delete(0, logStringBuilder.length());
    }

    public static void main(String[] args) {
        new SepolicyToolsGUI();
    }

    /**
     * 格式化file_contexts文件
     */
    private void formatFileContextsFunction() {
        String fileContextsPath = new FilePathUtilsImpl().catPath(sourceFile.getPath(), "file_contexts");
        File file = new File(fileContextsPath);
        if (!file.exists()) {
            SepolicyToolsGUI.log(fileContextsPath + "文件不存在");
            SepolicyToolsGUI.log("未执行任何操作");
        } else {
            SepolicyToolsGUI.log("处理文件" + file.getPath());
            fileContextsFormat.autoFormatFileContexts(file.getAbsolutePath(), file.getAbsolutePath());
            SepolicyToolsGUI.log("新文件" + file.getPath());
            SepolicyToolsGUI.log("完成");
        }
    }

    /**
     * 格式化所有Context文件
     */
    private void formatAllContextsFileFunction() {
        File dir = new File(sourceFile.getPath());
        if (!dir.exists()) {
            SepolicyToolsGUI.log(sourceFile.getPath() + "文件不存在");
            SepolicyToolsGUI.log("未执行任何操作");
        } else {
            SepolicyToolsGUI.log("处理文件" + dir.getPath());
            contextsUtils.autoFormatAllContext(dir.getAbsolutePath());
            SepolicyToolsGUI.log("新文件" + dir.getPath());
            SepolicyToolsGUI.log("完成");
        }
    }

    /**
     * 重写te文件
     */
    private void reWriteTEFilesFunction() {

        if (!new File(sourceFile.getPath()).exists()) {
            SepolicyToolsGUI.log(sourceFile.getPath() + "不存在");
        } else {
            SepolicyToolsGUI.log("重写开始");
            sepolicyDirUtils.reWriteTeFiles(sourceFile.getPath(), sourceFile.getPath(), new FilePathUtilsImpl().catPath(sourceFile.getPath(), "file_contexts"));
            SepolicyToolsGUI.log("完成");
        }
    }

    /**
     * 格式化te文件
     */
    private void formatTEFilesFunction() {
        if (!new File(sourceFile.getPath()).exists()) {
            SepolicyToolsGUI.log(sourceFile.getPath() + "不存在");
        } else {
            SepolicyToolsGUI.log("TE文件格式化开始");
            sepolicyDirUtils.formatFiles(sourceFile.getAbsolutePath(), sourceFile.getAbsolutePath());
            SepolicyToolsGUI.log("完成！！！");
        }
    }

    /**
     * 文件选择弹窗
     */
    private File selectDirDialog() {
        JFileChooser jFileChooser = new JFileChooser(".");
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = jFileChooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return jFileChooser.getSelectedFile();
        }
        return null;
    }
}
