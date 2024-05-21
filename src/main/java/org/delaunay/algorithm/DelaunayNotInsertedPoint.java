package org.delaunay.algorithm;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.awt.geom.Point2D;

/**
 * Used for storing points that are yet to be inserted
 * into the triangulation, storing id of a triangle the point's in
 */
@Data
@AllArgsConstructor
public class DelaunayNotInsertedPoint {
    private final Point2D.Double location;
    private int triangleId;
}
