package common;

public class Point {
  public double x;
  public double y;

  public Point() {

  }
  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void add(Point p) {
    this.x += p.x;
    this.y += p.y;
  }
  public static double calculateDistance(Point a,Point b) {
    double dist = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    return dist;
  }
  public static double calculateBearing(Point a, Point b) {
    double angle = Math.atan2(b.y - a.y, b.x - a.x);
    return angle;
  }
}


