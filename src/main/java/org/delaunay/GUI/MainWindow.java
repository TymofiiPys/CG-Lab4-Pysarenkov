package org.delaunay.GUI;

//import org.example.GUI.Lab1MenuBar;
//import org.example.GUI.RegTreeDrawer;

import org.cglab3.BentleyOttmann.BOInfo;

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
    private JLabel nIntersectionsLabel;
    private JLabel nEventsLabel;
    private JLabel eventInfoLabel;
    public final Dimension mainWindowDims = new Dimension(600, 500);
    public final BODrawer BODrawer;

    public MainWindow() {
        nextEventButton.setText("Наступна подія");
        nextEventButton.setEnabled(false);

        BODrawer = new BODrawer(graphicsPanel);

        //button action listeners
        nextEventButton.addActionListener(e -> {
            if (BODrawer.BOSet()) {
                BOInfo info = BODrawer.nextEvent();
                nEventsLabel.setText("Оброблено подій: " + info.getNEvents());
                nIntersectionsLabel.setText("З них перетинів: " + info.getNIntersections());
                eventInfoLabel.setText(info.getLastEventInfo());
                if (!BODrawer.checkNextEvent()) {
                    nextEventButton.setEnabled(false);
                }
            }
        });

        applyOffsetsButton.addActionListener(e -> {
            if (!offset1TextField.getText().isBlank() && !offset2TextField.getText().isBlank()) {
                try {
                    int offset1 = Integer.parseInt(offset1TextField.getText());
                    int offset2 = Integer.parseInt(offset2TextField.getText());
                    BODrawer.setOffsets(new int[]{offset1, offset2});
                    BODrawer.drawBO();
                } catch (NumberFormatException ignored) {
                }
            }
        });

        //graphics panel listeners
        graphicsPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                if (BODrawer.BOSet()) {
                    BODrawer.drawBO();
                }
            }
        });
    }

    public void clearEventBoxes() {
        this.nEventsLabel.setText("");
        this.nIntersectionsLabel.setText("");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ЛР№3. Перетин відрізків на площині (Bentley–Ottmann).");
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
