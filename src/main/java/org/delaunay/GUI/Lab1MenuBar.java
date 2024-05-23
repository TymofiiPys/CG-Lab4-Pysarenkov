package org.delaunay.GUI;

//import org.example.regtree.RTree2D;

import org.cglab3.util.SegmentParser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

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
            mw.BODrawer.setSegments(SegmentParser.readFromFile(filePath.toString()));
            mw.nextEventButton.setEnabled(true);
            mw.BODrawer.drawBO();
            mw.clearEventBoxes();
        }));
        JMenuItem exitMI = new JMenuItem("Вийти");
        exitMI.addActionListener(e -> System.exit(0));
        fileMenu.add(openMI);
        fileMenu.add(exitMI);
        this.add(fileMenu);
    }
}
