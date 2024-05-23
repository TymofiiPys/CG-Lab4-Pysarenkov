package org.delaunay.algorithm;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@RequiredArgsConstructor
@ToString
public class Triangle {
    private final int id1;
    private final int id2;
    private final int id3;

    @ToString.Exclude
    private final ArrayList<DelaunayNotInsertedPoint> containedPoints;
}
