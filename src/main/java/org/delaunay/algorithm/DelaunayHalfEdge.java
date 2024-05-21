package org.delaunay.algorithm;

import lombok.Data;

@Data
public class DelaunayHalfEdge {
    private final int start;
    private final int end;
    private int triangle;
    private DelaunayHalfEdge twin;
}
