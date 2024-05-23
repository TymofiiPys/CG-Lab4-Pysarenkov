package org.delaunay.GUI;

import javafx.util.Pair;
import lombok.Setter;
import org.delaunay.algorithm.DTInfo;
import org.delaunay.algorithm.DelaunayTriangulator;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class DTDrawer {
    private DelaunayTriangulator DT;
    private final JPanel panelDraw;
    private final int nodesRad = 4;
    private final Color notInsertedPointColor = Color.BLACK;
    private final Color insertedPointColor = Color.GREEN;
    private final Color edgesColor = Color.RED;
    @Setter
    private int[] offsets = new int[]{0, 0};

    public DTDrawer(JPanel panelDraw) {
        this.panelDraw = panelDraw;
    }

    public void setPoints(ArrayList<Point2D.Double> points) {
        this.DT = new DelaunayTriangulator(points);
    }

    public boolean DTSet() {
        return this.DT != null;
    }

    public Point2D.Double adaptToPanel(Point2D.Double p, int[] offsets) {
        return new Point2D.Double(p.x + offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    public Point2D.Double adaptFromPanel(Point2D.Double p, int[] offsets) {
        return new Point2D.Double(p.x - offsets[0], panelDraw.getHeight() - (p.y + offsets[1]));
    }

    private int[] calcOffsets() {
        return new int[]{offsets[0] + panelDraw.getWidth() / 2, offsets[1] + panelDraw.getHeight() / 2};
    }

    private void drawAxes(int[] offsets) {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.drawLine(0, panelDraw.getHeight() - offsets[1], panelDraw.getWidth(), panelDraw.getHeight() - offsets[1]);
        gr.drawLine(offsets[0], 0, offsets[0], panelDraw.getHeight());
        gr.drawLine(50 + offsets[0],
                panelDraw.getHeight() - (10 + offsets[1]),
                50 + offsets[0],
                panelDraw.getHeight() - (-10 + offsets[1]));
        gr.drawLine(-10 + offsets[0],
                panelDraw.getHeight() - (50 + offsets[1]),
                10 + offsets[0],
                panelDraw.getHeight() - (50 + offsets[1]));
        gr.drawString("50", 45 + offsets[0], panelDraw.getHeight() - (-25 + offsets[1]));
        gr.drawString("50", -30 + offsets[0], panelDraw.getHeight() - (45 + offsets[1]));
    }

    public void drawDT() {
        Graphics2D gr = (Graphics2D) panelDraw.getGraphics();
        gr.clearRect(0, 0, panelDraw.getWidth(), panelDraw.getHeight());
        int[] panelOffsets = calcOffsets();
        drawAxes(panelOffsets);
        // Draw triangulation edges
        gr.setColor(edgesColor);
        ArrayList<Point2D.Double> points = DT.getPoints();
        for (Pair<Integer, Integer> edge : DT.getHalfEdges().keySet()) {
            Point2D.Double startAdapted = adaptToPanel(points.get(edge.getKey()), panelOffsets);
            Point2D.Double endAdapted = adaptToPanel(points.get(edge.getValue()), panelOffsets);
            gr.drawLine((int) startAdapted.x, (int) startAdapted.y, (int) endAdapted.x, (int) endAdapted.y);
        }
        // Draw inserted points
        gr.setColor(insertedPointColor);
        for (int i = 0; i < DT.getCurPointId(); i++) {
            Point2D.Double p = adaptToPanel(points.get(i), panelOffsets);
            gr.fillOval((int) (p.x - nodesRad), (int) (p.y - nodesRad), 2 * nodesRad, 2 * nodesRad);
        }
        // Draw inserted points
        gr.setColor(notInsertedPointColor);
        for (int i = DT.getCurPointId(); i < points.size(); i++) {
            Point2D.Double p = adaptToPanel(points.get(i), panelOffsets);
            gr.fillOval((int) (p.x - nodesRad), (int) (p.y - nodesRad), 2 * nodesRad, 2 * nodesRad);
        }
    }

    public DTInfo nextEvent() {
        DT.nextEvent();
        this.drawDT();
        return new DTInfo(
                DT.getPoints().size(),
                DT.getCurPointId() - 1,
                DT.getLastEventInfo()
        );
    }

    public boolean checkNextEvent() {
        return !DT.getNotInsertedPoints().isEmpty();
    }
}
