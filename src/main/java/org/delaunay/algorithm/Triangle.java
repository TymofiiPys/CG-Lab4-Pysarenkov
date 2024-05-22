package org.delaunay.algorithm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
class Triangle {
    private final int id1;
    private final int id2;
    private final int id3;

    // Possible alternative, maybe pointers to edges will be stored instead of ids
    // because hashmap is used:
//    private final int halfEdge1;
//    private final int halfEdge2;
//    private final int halfEdge3;

    private final ArrayList<DelaunayNotInsertedPoint> containedPoints;
}
