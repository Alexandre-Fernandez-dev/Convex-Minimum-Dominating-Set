import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;

public class RandomPointsGenerator {
    private static String filename = "input.points";
    private static int numberOfPoints = 1000;
    private static int maxWidth = 1400;
    private static int maxHeight = 900;
    private static int radius = 140;

    public static double distanceToCenter(int x, int y) {
        return Math.min(Math.min(Math.min(Math.sqrt(Math.pow(x - maxWidth / 2, 2.0) + Math.pow(y - maxHeight / 2, 2.0)), Math.sqrt(Math.pow((double)x - 2.5 * (double)maxWidth / 6.0, 2.0) + Math.pow(y - 2 * maxHeight / 6, 2.0))), Math.min(Math.sqrt(Math.pow(x - 4 * maxWidth / 6, 2.0) + Math.pow(y - 2 * maxHeight / 6, 2.0)), Math.sqrt(Math.pow(x - 2 * maxWidth / 6, 2.0) + Math.pow(y - 4 * maxHeight / 6, 2.0)))), Math.sqrt(Math.pow(x - 4 * maxWidth / 6, 2.0) + Math.pow(y - 4 * maxHeight / 6, 2.0)));
    }

    public static void main(String[] args) {
        try {
            for (int index = 0; index < 1; ++index) {
                PrintStream output = new PrintStream(new FileOutputStream("tests/input" + index + ".points"));
                Random generator = new Random();
                ArrayList<Point> points = new ArrayList<Point>();
                int edgeThreshold = 48;
                for (int i = 0; i < numberOfPoints; ++i) {
                    int x;
                    int y;
                    do {
                        if (!(RandomPointsGenerator.distanceToCenter(x = generator.nextInt(maxWidth), y = generator.nextInt(maxHeight)) < (double)radius * 1.4 || RandomPointsGenerator.distanceToCenter(x, y) < (double)radius * 1.6 && generator.nextInt(5) == 1 || RandomPointsGenerator.distanceToCenter(x, y) < (double)radius * 1.8 && generator.nextInt(10) == 1 || maxHeight / 9 < x && x < 4 * maxHeight / 5 && maxHeight / 9 < y && y < 7 * maxHeight / 9 && generator.nextInt(100) == 1)) {
                            continue;
                        }
                        Point p = new Point(x, y);
                        int deg = 0;
                        for (Point q : points) {
                            if (p.distance(q) > (double)edgeThreshold) continue;
                            ++deg;
                        }
                        if (deg < 5) break;
                    } while (true);
                    points.add(new Point(x, y));
                    output.println(Integer.toString(x) + " " + Integer.toString(y));
                }
                output.close();
            }
        }
        catch (FileNotFoundException e) {
            System.err.println("I/O exception: unable to create " + filename);
        }
    }

    public static void generate(int nbPoints) {
        try {
            PrintStream output = new PrintStream(new FileOutputStream(filename));
            Random generator = new Random();
            for (int i = 0; i < nbPoints; ++i) {
                int y;
                int x;
                while (!(RandomPointsGenerator.distanceToCenter(x = generator.nextInt(maxWidth), y = generator.nextInt(maxHeight)) < (double)radius * 1.4 || RandomPointsGenerator.distanceToCenter(x, y) < (double)radius * 1.6 && generator.nextInt(5) == 1 || RandomPointsGenerator.distanceToCenter(x, y) < (double)radius * 1.8 && generator.nextInt(10) == 1 || maxHeight / 5 < x && x < 4 * maxHeight / 5 && maxHeight / 5 < y && y < 4 * maxHeight / 5 && generator.nextInt(100) == 1)) {
                }
                output.println(Integer.toString(x) + " " + Integer.toString(y));
            }
            output.close();
        }
        catch (FileNotFoundException e) {
            System.err.println("I/O exception: unable to create " + filename);
        }
    }
}
