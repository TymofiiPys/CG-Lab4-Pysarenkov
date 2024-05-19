package org.delaunay.algorithm;

import lombok.Getter;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DelaunayTriangulator {
    private final ArrayList<Point2D.Double> points;
    private int[] triangles;
    private int triangleLen = 0;
    private int[] halfedges;
    private int[] ids;
    public DelaunayTriangulator(ArrayList<Point2D.Double> points) {
        // Shuffle points because we use Random incremental construction
        this.points = points;
        Collections.shuffle(this.points);

        final int n = points.size();
        final int maxTriangles = Math.max(2 * n - 5, 0);
        this.triangles = new int[maxTriangles * 3];
        this.halfedges = new int[maxTriangles * 3];

        this.ids = new int[n + 3];
        for (int i = 0; i < n; i++) this.ids[i] = i;
        this.ids[n] = -3;
        this.ids[n + 1] = -2;
        this.ids[n + 2] = -1;

        this.update();
    }

    public void update() {
        //find region which contains all the points
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < points.size(); i++) {
            final double x = points.get(i).x;
            final double y = points.get(i).y;
            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            this.ids[i] = i;
        }

        final double cx = (minX + maxX) / 2;
        final double cy = (minY + maxY) / 2;

        // assume the following: the circumcircle of the aforementioned region
        // will be the incircle of that magic triangle of https://www.coursera.org/lecture/geometric-algorithms/randomized-incremental-construction-analysis-6l1Pf
        // TODO: this magic triangle or just symbolic treatment

        // symbolic : the vertices of the magic triangle are stored as nth,
        // n+1st and n+2nd points, they are ordered counter-clockwise.

        // before adding any points, we add that magic triangle
        this.addTriangle()

        for (int i = 0; i < points.size(); i++) {
            Point2D.Double p = points.get(i);

        }
    }

    /**
     *
     * @param i0 index of point to add to a triangle
     * @param i1 index of point to add to a triangle
     * @param i2 index of point to add to a triangle
     * @param a halfedge id associated with i0
     * @param b halfedge id associated with i1
     * @param c halfedge id associated with i2
     * @return
     */
    private int addTriangle(int i0, int i1, int i2, int a, int b, int c) {
        final int t = this.triangleLen;
        this.triangles[t] = i0;
        this.triangles[t + 1] = i1;
        this.triangles[t + 2] = i2;

        this.triangleLen += 3;
        return t;
    }

    private void insertPoint() {

    }

    private void legalize() {

    }

    class TriangleGraph {
        private final Node root;
        TriangleGraph(Triangle rootTr) {
            this.root = new Node(rootTr);
        }
        @Getter
        private class Node {
            private final Triangle t;
            private final ArrayList<Triangle> children;
            Node(Triangle t) {
                this.t = t;
                this.children = new ArrayList<>();
            }

            private void split(Point2D.Double p) {
                int
            }
        }

        public void insertPoint(Point2D.Double p) {

        }
    }
}
