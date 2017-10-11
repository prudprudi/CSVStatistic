package com.prudprudi4.csvstatistic;

import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class GUI extends JFrame {
    private Path sourceDir;
    private Path resultDir;

    public GUI() {
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final JFileChooser fromChooser = new JFileChooser();
        fromChooser.setCurrentDirectory(new File("D:\\"));
        fromChooser.setDialogTitle("Source directory");
        fromChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        final JFileChooser toChooser = new JFileChooser();
        toChooser.setCurrentDirectory(new File("D:\\"));
        toChooser.setDialogTitle("Result directory");
        toChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        final JButton fromButton = new JButton("Source");
        final JButton toButton = new JButton("Result");
        final JButton okButton = new JButton("Ok");

        fromButton.addActionListener((listener) -> {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("D:\\"));
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int ret = fc.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                sourceDir = fc.getSelectedFile().toPath();
            }

        });

        toButton.addActionListener((listener) -> {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("D:\\"));
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int ret = fc.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                resultDir = fc.getSelectedFile().toPath();
            }

        });

        okButton.addActionListener((listener) -> {
            if (sourceDir == null || resultDir == null) return;

            CSVStatistic csv = new CSVStatistic(sourceDir, resultDir);
            csv.createScanReport();
            csv.createTotalReport();

            dispose();
        });

        JPanel panel = new JPanel();
        panel.add(fromButton);
        panel.add(toButton);
        panel.add(okButton);

        add(panel);
        pack();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        EventQueue.invokeLater(() -> new GUI().setVisible(true));
    }
}
