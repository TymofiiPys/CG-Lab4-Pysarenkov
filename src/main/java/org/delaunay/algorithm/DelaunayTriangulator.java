package org.delaunay.algorithm;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.util.Pair;
import lombok.Getter;
import lombok.extern.java.Log;
import org.delaunay.util.Geometric;

@Log
@Getter
public class DelaunayTriangulator {
    private final ArrayList<Point2D.Double> points;
    private final ArrayList<Triangle> triangles;
    private final ArrayList<DelaunayNotInsertedPoint> notInsertedPoints;
    private int triangleId = 0;
    private int curPointId = 0;
    // FIXME: instead of assigning concrete values to the magic triangle vertices,
    //  use symbolic approach. Current approach is used because I haven't figured out
    //  how to find if point lies inside a triangle where
    //  at least one vertex is of the magic triangle
    private final int VERY_VERY_BIG_INT = 100000000;
    /**
     * A hashmap of half-edges that uses half-edge start and endpoint ids as key
     */
    private final HashMap<Pair<Integer, Integer>, DelaunayHalfEdge> halfEdges;
    private String lastEventInfo;
    private int n;

    public DelaunayTriangulator(ArrayList<Point2D.Double> points) {
        // Shuffle points because we use Random incremental construction
        this.points = points;
        Collections.shuffle(this.points);
        log.info("Created DelaunayTriangulator, got " + points.size() + " points, shuffled");

        this.triangles = new ArrayList<>();
        this.notInsertedPoints = new ArrayList<>();
        this.halfEdges = new HashMap<>();
    }

    //Called when doing everything step-by-step
    public void nextEvent() {
        if (curPointId == 0) {
            this.halfEdges.clear();
            this.triangles.clear();

            n = points.size();
            // link all points to the triangle
            this.notInsertedPoints.addAll(
                    this.points.stream().map(
                            point -> new DelaunayNotInsertedPoint(point, 0)
                    ).toList());
            this.points.add(new Point2D.Double(0, VERY_VERY_BIG_INT));
            this.points.add(new Point2D.Double(-VERY_VERY_BIG_INT, -VERY_VERY_BIG_INT));
            this.points.add(new Point2D.Double(VERY_VERY_BIG_INT, -VERY_VERY_BIG_INT));
            //and create the triangle
            this.addTriangle(n, n + 1, n + 2, this.notInsertedPoints);

            log.info("Created list of " + n + " not inserted points");
            log.info("=========================================================");
        }
        if (curPointId == n)
            return;
        // get ith not-inserted point
        final DelaunayNotInsertedPoint point = this.notInsertedPoints.get(curPointId);
        log.info("Processing " + curPointId + "th point:");

        // get the triangle the point's in
        final int triangleId = point.getTriangleId();
        Triangle tr = triangles.get(triangleId);
        log.info("Contained in triangle #" + triangleId + ": " + tr.toString());

        // get other points that the triangle contains
        ArrayList<DelaunayNotInsertedPoint> pointsInTriangle = tr.getContainedPoints();
        // separate the points between three future triangles
        ArrayList<DelaunayNotInsertedPoint> pointsInSubTr1 = this.pointsInsideSubTriangle(pointsInTriangle, tr.getId1(), tr.getId2(), curPointId);
        ArrayList<DelaunayNotInsertedPoint> pointsInSubTr2 = this.pointsInsideSubTriangle(pointsInTriangle, tr.getId2(), tr.getId3(), curPointId);
        ArrayList<DelaunayNotInsertedPoint> pointsInSubTr3 = this.pointsInsideSubTriangle(pointsInTriangle, tr.getId3(), tr.getId1(), curPointId);
        // create the triangles
        this.addTriangle(tr.getId1(), tr.getId2(), curPointId, pointsInSubTr1);
        this.addTriangle(tr.getId2(), tr.getId3(), curPointId, pointsInSubTr2);
        this.addTriangle(tr.getId3(), tr.getId1(), curPointId, pointsInSubTr3);
        // and legalize edges
        this.legalize(this.halfEdges.get(new Pair<>(tr.getId1(), tr.getId2())), curPointId);
        this.legalize(this.halfEdges.get(new Pair<>(tr.getId2(), tr.getId3())), curPointId);
        this.legalize(this.halfEdges.get(new Pair<>(tr.getId3(), tr.getId1())), curPointId);
        this.formLastEventInfo();
        this.curPointId++;
        if (curPointId == n) {
            this.notInsertedPoints.clear();
//            this.points.removeLast();
//            this.points.removeLast();
//            this.points.removeLast();
        }
        log.info("=========================================================");
    }

    // Here the insertion of points happens.
    // Why is it called update? Because this method is meant to be called
    // when the list we passed into the constructor when creating DelaunayTriangulator object
    // is mutated
    public void update() {
        // Before adding any points, we create that magic triangle from
        // https://www.coursera.org/lecture/geometric-algorithms/randomized-incremental-construction-analysis-6l1Pf.
        // The vertices of the magic triangle are stored as nth,
        // n+1st and n+2nd points, they are ordered counter-clockwise.

        // TODO: do same stuff that is in nextEvent cause I changed it quite a lot,
        //  if you want to get the finished triangulation in one click
        //  and link it to some button you'll have to create as well

        this.halfEdges.clear();
        this.triangles.clear();

        final int n = points.size();
        // link all points to the triangle
        this.notInsertedPoints.addAll(
                this.points.stream().map(
                        point -> new DelaunayNotInsertedPoint(point, 0)
                ).toList());
        //and create the triangle
        this.addTriangle(n, n + 1, n + 2, this.notInsertedPoints);

        for (int i = 0; i < n; i++) {
            // get ith not-inserted point
            final DelaunayNotInsertedPoint point = this.notInsertedPoints.get(i);
            // get the triangle the point's in
            final int id = point.getTriangleId();
            Triangle tr = triangles.get(id);
            // get other points that the triangle contains
            ArrayList<DelaunayNotInsertedPoint> pointsInTriangle = tr.getContainedPoints();
            // separate the points between three future triangles
            ArrayList<DelaunayNotInsertedPoint> pointsInSubTr1 = this.pointsInsideSubTriangle(pointsInTriangle, tr.getId1(), tr.getId2(), i);
            ArrayList<DelaunayNotInsertedPoint> pointsInSubTr2 = this.pointsInsideSubTriangle(pointsInTriangle, tr.getId2(), tr.getId3(), i);
            ArrayList<DelaunayNotInsertedPoint> pointsInSubTr3 = this.pointsInsideSubTriangle(pointsInTriangle, tr.getId3(), tr.getId1(), i);
            // create the triangles
            this.addTriangle(tr.getId1(), tr.getId2(), i, pointsInSubTr1);
            this.addTriangle(tr.getId2(), tr.getId3(), i, pointsInSubTr2);
            this.addTriangle(tr.getId3(), tr.getId1(), i, pointsInSubTr3);
            // and legalize edges
            this.legalize(this.halfEdges.get(new Pair<>(tr.getId1(), tr.getId2())), id);
            this.legalize(this.halfEdges.get(new Pair<>(tr.getId2(), tr.getId3())), id);
            this.legalize(this.halfEdges.get(new Pair<>(tr.getId3(), tr.getId1())), id);
            this.curPointId = i;
        }

        this.notInsertedPoints.clear();

        // in the end of triangulation, magic triangle vertices are deleted
        // or just ignored when drawing idk
    }

    /**
     * @param pointList list of points
     * @param i1        triangle vertex 1
     * @param i2        triangle vertex 2
     * @param i3        triangle vertex 3
     * @return list of points inside the triangle bound by i1, i2 and i3
     */
    private ArrayList<DelaunayNotInsertedPoint>
    pointsInsideSubTriangle(ArrayList<DelaunayNotInsertedPoint> pointList,
                            int i1,
                            int i2,
                            int i3) {
        // TODO: checking points inside triangle bound by magic triangle vertices
        //  if you want to improve the vertices by setting them symbolically,
        //  because the current stuff requires point coordinates whichever the point is
        return new ArrayList<>(pointList.stream().filter(pt ->
                Geometric.liesInsideTriangle(
                        this.points.get(i1),
                        this.points.get(i2),
                        this.points.get(i3),
                        pt.getLocation()
                ) && !pt.getLocation().equals(this.points.get(i3))
        ).toList());
    }

    /**
     * Adds a triangle, constructing edges.
     * Note: i1-th, i2-th and i3-th points must be ordered counter-clockwise
     *
     * @param i1 index of point to add to a triangle
     * @param i2 index of point to add to a triangle
     * @param i3 index of point to add to a triangle
     */
    private void addTriangle(int i1, int i2, int i3, ArrayList<DelaunayNotInsertedPoint> pointsInsideTriangle) {
        // Create half-edges
        // (two of them will exist if the triangle is created as a result of edge flip,
        // then just change id of triangle the HEs point at)
        DelaunayHalfEdge edge1 = createEdgeIfDoesntExist(i1, i2, triangleId);
        DelaunayHalfEdge edge2 = createEdgeIfDoesntExist(i2, i3, triangleId);
        DelaunayHalfEdge edge3 = createEdgeIfDoesntExist(i3, i1, triangleId);
        // Find and link them to their twins
        DelaunayHalfEdge edge1twin = this.halfEdges.get(new Pair<>(i2, i1));
        DelaunayHalfEdge edge2twin = this.halfEdges.get(new Pair<>(i3, i2));
        DelaunayHalfEdge edge3twin = this.halfEdges.get(new Pair<>(i1, i3));
        if (edge1twin != null) {
            edge1.setTwin(edge1twin);
            edge1twin.setTwin(edge1);
        }
        if (edge2twin != null) {
            edge2.setTwin(edge2twin);
            edge2twin.setTwin(edge2);
        }
        if (edge3twin != null) {
            edge3.setTwin(edge3twin);
            edge3twin.setTwin(edge3);
        }
        // Make points of the list point to this triangle
        pointsInsideTriangle.forEach(pt -> pt.setTriangleId(triangleId));
        // And create the triangle and link edges to it
        this.triangles.add(new Triangle(i1, i2, i3, pointsInsideTriangle));
        edge1.setTriangle(triangleId);
        edge2.setTriangle(triangleId);
        edge3.setTriangle(triangleId);
        log.info("Created triangle #" + triangleId + ": " + triangles.get(triangleId).toString());
        triangleId++;
    }

    private DelaunayHalfEdge createEdgeIfDoesntExist(int startId, int endId, int triangleId) {
        DelaunayHalfEdge edge;
        if (this.halfEdges.containsKey(new Pair<>(startId, endId))) {
            edge = this.halfEdges.get(new Pair<>(startId, endId));
            edge.setTriangle(triangleId);
        } else {
            edge = new DelaunayHalfEdge(startId, endId);
            this.halfEdges.put(new Pair<>(startId, endId), edge);
        }
        return edge;
    }

    /**
     * Legalize edge
     *
     * @param edge    edge to legalize
     * @param pointId id of the point that is not an endpoint of {@code edge}
     */
    private void legalize(DelaunayHalfEdge edge, int pointId) {
        // Get edge twin if it exists
        DelaunayHalfEdge twin = edge.getTwin();
        if (twin == null) return;
        // Get the triangle the twin points at and
        // get id of the point that is not an endpoint of the twin
        int triangleTwinId = twin.getTriangle();
        Triangle triangleTwin = this.triangles.get(triangleTwinId);
        int pdId = -1;
        if (triangleTwin.getId1() != twin.getStart() && triangleTwin.getId1() != twin.getEnd()) {
            pdId = triangleTwin.getId1();
        } else if (triangleTwin.getId2() != twin.getStart() && triangleTwin.getId2() != twin.getEnd()) {
            pdId = triangleTwin.getId2();
        } else if (triangleTwin.getId3() != twin.getStart() && triangleTwin.getId3() != twin.getEnd()) {
            pdId = triangleTwin.getId3();
        } else {
            System.out.println("IMPOSSIBLE");
        }
        // now get the points by their ids and do the incircle test
        // note: the points a, b and c MUST be ordered counterclockwise
        // for the check to work correctly
        Point2D.Double pa = this.points.get(pointId);
        Point2D.Double pb = this.points.get(edge.getStart());
        Point2D.Double pc = this.points.get(edge.getEnd());
        Point2D.Double pd = this.points.get(pdId);
        boolean illegal = Geometric.inCircle(pa, pb, pc, pd);
        if (illegal) {
            log.info("Flipping edges when legalizing edge:" + edge);
            // I checked, and it seems that these points are indeed ordered counter-clockwise
            // Flip edge
            // Get points from triangles incident to edge to be flipped and its twin
            ArrayList<DelaunayNotInsertedPoint> pointsInOldSubTrs = triangles.get(edge.getTriangle()).getContainedPoints();
            pointsInOldSubTrs.addAll(triangleTwin.getContainedPoints());
            // Find points contained in new triangles
            ArrayList<DelaunayNotInsertedPoint> pointsInNewSubTr1 =
                    this.pointsInsideSubTriangle(
                            pointsInOldSubTrs, twin.getStart(), pointId, pdId
                    );

            ArrayList<DelaunayNotInsertedPoint> pointsInNewSubTr2 =
                    this.pointsInsideSubTriangle(
                            pointsInOldSubTrs, twin.getEnd(), pdId, pointId
                    );
            // Construct 2 new triangles with flipped edge
            this.addTriangle(twin.getStart(), pointId, pdId, pointsInNewSubTr1);
            this.addTriangle(twin.getEnd(), pdId, pointId, pointsInNewSubTr2);
            // Legalize them :)
            this.legalize(this.halfEdges.get(new Pair<>(twin.getEnd(), pdId)), pointId);
            this.legalize(this.halfEdges.get(new Pair<>(pdId, twin.getStart())), pointId);
            this.halfEdges.remove(new Pair<>(edge.getStart(), edge.getEnd()));
            this.halfEdges.remove(new Pair<>(twin.getStart(), twin.getEnd()));
        }
    }

    public void formLastEventInfo() {
        lastEventInfo = "<html>Координата останньої<br>" +
                "вставленої точки: (" + Math.round(points.get(curPointId).x) + ", " + Math.round(points.get(curPointId).y) + ") </html>";
    }
}
