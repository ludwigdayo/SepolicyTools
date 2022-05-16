package Gui;

import ContextFileTools.ContextsUtils;
import ContextFileTools.Impl.ContextsUtilsImpl;
import ContextFileTools.Impl.FileContextsFormatImpl;
import GeneralFilesTools.Impl.SepolicyDirUtilsImpl;
import GeneralFilesTools.SepolicyDirUtils;
import Utils.Impl.AdbUtilsImpl;
import Utils.Impl.CreateRuleFromLogImpl;
import Utils.Impl.FilePathUtilsImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
    private JButton clearLogButton = null;
    private JPanel selectSourcePanel = null;
    private JButton formatTEFilesButton = null;
    private JButton formatFileContextsButton = null;
    private JButton reWriteTEFilesButton = null;
    private JButton logToSePolicyButton = null;
    private JButton formatAllContextsFileButton = null;
    private JScrollPane centerPanel = null;
    private JPanel eastPanel = null;
    private JButton autoRun = null;

    /**
     * 界面
     */
    public SepolicyToolsGUI() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        /*
            北边
        */
        {
            sourceDirLabel = new JLabel("工作路径:   ");
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

                    updatedSourceDir();
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
                        updatedSourceDir();
                    }
                }
            });

            clearLogButton = new JButton("清除日志");
            clearLogButton.setBackground(themeColor);
            clearLogButton.setFont(buttonFont);
            clearLogButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logStringBuilder.delete(0, logStringBuilder.length());
                    log("");
                }
            });

            selectSourcePanel = new JPanel();
            selectSourcePanel.setLayout(new FlowLayout());
            selectSourcePanel.add(sourceDirLabel);
            selectSourcePanel.add(sourceDirString);
            selectSourcePanel.add(selectDirButton);
            selectSourcePanel.add(clearLogButton);
            selectSourcePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            contentPane.add(selectSourcePanel, BorderLayout.NORTH);
        }

        /*
            中间
         */
        {
            textArea = new JTextArea();
            textArea.setLineWrap(true);
            textArea.setColumns(57);
            textArea.setRows(15);
            textArea.setFont(textFont);
            textArea.setEditable(false);

            // 滚动面板
            centerPanel = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
            contentPane.add(centerPanel, BorderLayout.CENTER);
        }

        /*
            东边
         */
        {
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

            logToSePolicyButton = new JButton("抓取并生成SePolicy");
            logToSePolicyButton.setFont(buttonFont);
            logToSePolicyButton.setBackground(themeColor);
            logToSePolicyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    logToSePolicyFunction();
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

            eastPanel = new JPanel();

            GridLayout gridLayout = new GridLayout(10, 1);
            gridLayout.setVgap(15);
            gridLayout.setHgap(15);
            eastPanel.setLayout(gridLayout);
            eastPanel.add(formatTEFilesButton);
            eastPanel.add(formatFileContextsButton);
            eastPanel.add(reWriteTEFilesButton);
            eastPanel.add(formatAllContextsFileButton);
            eastPanel.add(logToSePolicyButton);
            eastPanel.add(autoRun);
            eastPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 10));
            contentPane.add(eastPanel, BorderLayout.EAST);
        }

        setContentPane(contentPane);

        new DropTarget(textArea, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    try {
                        @SuppressWarnings("unchecked") java.util.List<File> list = (java.util.List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                        for (File file : list) {
                            if (file.isDirectory()) {
                                if (!file.getPath().equals(sourceFile.getPath())) log("切换工作目录");
                                sourceFile = file;
                                updatedSourceDir();
                                break;
                            }
                        }
                    } catch (UnsupportedFlavorException | IOException e) {
                        e.printStackTrace();
                    }

                    dtde.dropComplete(true);
                } else {
                    dtde.rejectDrop();
                }
            }
        });

        setTitle("Sepolicy工具");
        setResizable(false);
        setFont(globalFont);
        setBounds(200, 100, 1000, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * 界面日志输出
     */
    public static void log(String log) {
        // 加到缓冲
        logStringBuilder.append("\r\n");
        logStringBuilder.append(log);

        // 更新到界面
        if (textArea != null) {
            textArea.setText(logStringBuilder.toString());
        }
    }

    public static void main(String[] args) {
        new SepolicyToolsGUI();
    }

    /**
     * 更新路径显示
     */
    private void updatedSourceDir() {
        sourceDirString.setText(sourceFile.getAbsolutePath());
    }

    /**
     * 抓取log生成sePolicy政策
     */
    private void logToSePolicyFunction() {
        AdbUtilsImpl adbUtils = new AdbUtilsImpl();
        CreateRuleFromLogImpl createRuleFromLog = new CreateRuleFromLogImpl();

        // 抓取log
        String[] logcat = adbUtils.logcat();

        // 生成政策
        String[] allowPolicy = createRuleFromLog.logToSePolicy(logcat);

        if (allowPolicy != null) {
            log("======================生成信息============================");
            for (String line : allowPolicy) {
                log(line);
            }
            log("=========================================================");
        }
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
