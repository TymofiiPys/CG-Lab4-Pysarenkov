package org.delaunay.GUI;

import lombok.extern.java.Log;
import org.delaunay.util.PointParser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

@Log
public class Lab1MenuBar extends JMenuBar {
    public StringBuilder filePath = new StringBuilder();

    public Lab1MenuBar(JFrame parent, MainWindow mw) {
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openMI = new JMenuItem("Відкрити");
        FileFilter textFilesFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".txt");
            }

            @Override
            public String getDescription() {
                return "(*.txt) Текстовий файл";
            }
        };
        openMI.addActionListener(new OpenFileDialogActionListener(parent, textFilesFilter, filePath, () -> {
            mw.dtDrawer.setPoints(PointParser.readFromFile(filePath.toString()));
            log.info("Parsed points from file " + filePath.toString());
            mw.nextEventButton.setEnabled(true);
            mw.dtDrawer.drawDT();
            mw.clearEventBoxes();
        }));
        JMenuItem exitMI = new JMenuItem("Вийти");
        exitMI.addActionListener(e -> System.exit(0));
        fileMenu.add(openMI);
        fileMenu.add(exitMI);
        this.add(fileMenu);
    }
}
