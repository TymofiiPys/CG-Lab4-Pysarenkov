package org.delaunay.GUI;

import org.delaunay.algorithm.DTInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainWindow extends Container {
    public JButton nextEventButton;
    private JPanel mainPanel;
    private JPanel controlsPanel;
    private JPanel graphicsPanel;
    private JLabel offsetLabel;
    private JLabel offset1Label;
    private JLabel offset2Label;
    private JTextField offset1TextField;
    private JTextField offset2TextField;
    private JPanel controlsInsidePanel;
    private JButton applyOffsetsButton;
    private JLabel nInsertionsLabel;
    private JLabel nPointsLabel;
    private JLabel eventInfoLabel;
    public final Dimension mainWindowDims = new Dimension(600, 500);
    public final DTDrawer dtDrawer;

    public MainWindow() {
        // TODO: labels are in Ukrainian, but if you look at the code,
        //  these are self-explanatory. If you wish, translate into English.
        nextEventButton.setText("Наступна подія");
        nextEventButton.setEnabled(false);

        dtDrawer = new DTDrawer(graphicsPanel);

        //button action listeners
        nextEventButton.addActionListener(e -> {
            if (dtDrawer.DTSet()) {
                DTInfo info = dtDrawer.nextEvent();
                nPointsLabel.setText("Усього точок: " + info.getTotalPts());
                nInsertionsLabel.setText("З них вставлено: " + info.getInsertedPts());
                eventInfoLabel.setText(info.getLastEventInfo());
                if (!dtDrawer.checkNextEvent()) {
                    nextEventButton.setEnabled(false);
                }
            }
        });

        applyOffsetsButton.addActionListener(e -> {
            if (!offset1TextField.getText().isBlank() && !offset2TextField.getText().isBlank()) {
                try {
                    int offset1 = Integer.parseInt(offset1TextField.getText());
                    int offset2 = Integer.parseInt(offset2TextField.getText());
                    dtDrawer.setOffsets(new int[]{offset1, offset2});
                    dtDrawer.drawDT();
                } catch (NumberFormatException ignored) {
                }
            }
        });

        //graphics panel listeners
        graphicsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (dtDrawer.DTSet()) {
                    dtDrawer.drawDT();
                }
            }
        });
    }

    public void clearEventBoxes() {
        this.nPointsLabel.setText("");
        this.nInsertionsLabel.setText("");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ЛР№4. Триангуляція Делоне – рандомізований інкрементний алгоритм.");
        MainWindow mw = new MainWindow();
        Lab1MenuBar menuBar = new Lab1MenuBar(frame, mw);
        frame.setJMenuBar(menuBar);
        frame.setContentPane(mw.mainPanel);
        frame.setMinimumSize(mw.mainWindowDims);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
