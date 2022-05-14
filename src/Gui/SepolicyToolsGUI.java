package Gui;

import javax.swing.*;
import java.awt.*;
import java.io.File;


/**
 * 图形界面
 */
public class SepolicyToolsGUI extends JFrame {
    Font globalFont = new Font(null, Font.BOLD, 30);
    Font textFont = new Font(null, Font.BOLD, 20);
    Font buttonFont = new Font(null, Font.BOLD, 15);
    Color themeColor = new Color(195, 233, 242, 95);

    File sourceFIle = null;

    public SepolicyToolsGUI() {
        Container contentPane = getContentPane();

        {
            JLabel sourceDirLabel = new JLabel("Sepolicy文件夹路径:");
            sourceDirLabel.setFont(textFont);

            JTextField sourceDirString = new JTextField(35);
            sourceDirString.setFont(textFont);

            JButton selectDirButton = new JButton("选择文件");
            selectDirButton.setBackground(themeColor);
            selectDirButton.setFont(buttonFont);

            JPanel selectSourcePanel = new JPanel();
            selectSourcePanel.add(sourceDirLabel);
            selectSourcePanel.add(sourceDirString);
            selectSourcePanel.add(selectDirButton);

            contentPane.add(selectSourcePanel, BorderLayout.NORTH);
        }

        {
            JTextArea textArea = new JTextArea();
            textArea.setColumns(30);
            textArea.setRows(20);
            textArea.setFont(textFont);
            textArea.setEditable(true);

            JButton formatTEFilesButton = new JButton("格式化TE文件");
            formatTEFilesButton.setFont(buttonFont);
            formatTEFilesButton.setBackground(themeColor);

            JPanel leftPanel = new JPanel();
            leftPanel.add(textArea);
            contentPane.add(leftPanel, BorderLayout.WEST);

            JPanel rightPanel = new JPanel();
            rightPanel.add(formatTEFilesButton);
            contentPane.add(rightPanel, BorderLayout.EAST);
        }

        setTitle("Sepolicy工具");
        setFont(globalFont);
        setBounds(200, 100, 1000, 618);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new SepolicyToolsGUI();
    }
}
