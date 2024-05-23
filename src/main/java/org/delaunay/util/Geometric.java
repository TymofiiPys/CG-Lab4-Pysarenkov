package org.delaunay.util;

import java.awt.geom.Point2D;

public class Geometric {
    /** Returns {@code true} if {@code p} lies inside the circumcircle of triangle abc,
     * otherwise {@code false}. IMPORTANT: a, b, and c should be ordered counterclockwise,
     * no reordering is done here, and it's all up to you
     * @param a triangle vertex
     * @param b triangle vertex
     * @param c triangle vertex
     * @param p point to check
     * @return {@code true} if {@code p} lies inside the circumcircle of triangle abc,
     * otherwise {@code false}
     */
    public static boolean inCircle(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double p) {
        final double dx = a.x - p.x;
        final double dy = a.y - p.y;
        final double ex = b.x - p.x;
        final double ey = b.y - p.y;
        final double fx = c.x - p.x;
        final double fy = c.y - p.y;

        final double ap = dx * dx + dy * dy;
        final double bp = ex * ex + ey * ey;
        final double cp = fx * fx + fy * fy;

        return dx * (ey * cp - bp * fy) -
                dy * (ex * cp - bp * fx) +
                ap * (ex * fy - ey * fx) > 0;
    }

    public static boolean liesInsideTriangle(Point2D.Double a, Point2D.Double b, Point2D.Double c, Point2D.Double p) {
        // TODO: test
        double denominator = (b.y - c.y) * (a.x - c.x) + (c.x - b.x) * (a.y - c.y);
        double ad = ((b.y - c.y) * (p.x - c.x) + (c.x - b.x) * (p.y - c.y)) / denominator;
        double bd = ((c.y - a.y) * (p.x - c.x) + (a.x - c.x) * (p.y - c.y)) / denominator;
        double cd = 1 - ad - bd;
        return ad >= 0 && bd >= 0 && cd >= 0;
    }
}
