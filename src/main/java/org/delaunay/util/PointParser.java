package org.delaunay.util;

import lombok.SneakyThrows;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class PointParser {
    @SneakyThrows(IOException.class)
    public static ArrayList<Point2D.Double> readFromFile(String filename) {
        Stream<String> fileLines;
        List<String> lines;
        fileLines = Files.lines(Paths.get(filename));
        lines = fileLines.toList();
        fileLines.close();

        ArrayList<Point2D.Double> points = new ArrayList<>();
        Scanner lineScanner;
        double x, y;
        for (String line : lines) {
            lineScanner = new Scanner(line);
            try {
                x = lineScanner.nextDouble();
                y = lineScanner.nextDouble();
            } catch (Exception e) {
                throw new IllegalArgumentException("File opened has wrong format");
            }
            points.add(new Point2D.Double(x, y));
        }

        return points;
    }
}
