package org.delaunay.algorithm;

import lombok.Data;

/**
 * Represents a half-edge, containing start, endpoint ids, id of a triangle to the left and
 * a pointer to its twin, which has start and endpoint ids swapped and id of a triangle
 * to the right. Better use with a hashmap where a pair of start and end ids is a key,
 * and the half-edge is the value.
 */
@Data
public class DelaunayHalfEdge {
    private final int start;
    private final int end;
    private int triangle;
    private DelaunayHalfEdge twin;
}
