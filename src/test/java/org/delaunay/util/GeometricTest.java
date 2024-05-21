package org.delaunay.util;

import junit.framework.TestCase;

import java.awt.geom.Point2D;

public class GeometricTest extends TestCase {

    public void testInCircle() {
        Point2D.Double p1 = new Point2D.Double(2, 4);
        Point2D.Double p2 = new Point2D.Double(0, 2);
        Point2D.Double p3 = new Point2D.Double(4, 2);
        Point2D.Double p = new Point2D.Double(2, 2);
        assertTrue(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(6, 2);
        assertFalse(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(2, 0);
        // the value compared to zero in return statement should be zero, so should return false
        assertFalse(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(2, 0.0000001);
        assertTrue(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(0, 4);
        assertFalse(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(1, 3);
        assertTrue(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(3, 3);
        assertTrue(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(4, 4);
        assertFalse(Geometric.inCircle(p1, p2, p3, p));
        p = new Point2D.Double(0, 0);
        assertFalse(Geometric.inCircle(p1, p2, p3, p));
    }
}