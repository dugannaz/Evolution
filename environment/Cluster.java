package environment;

import java.awt.Point;
import organism.ID;

public class Cluster {
  public Point coordinate;
  public common.Point min;
  public common.Point max;
  public ID[] orgs;

  public Cluster(int initialLength, Point coord, common.Point min, common.Point max) {
    this.coordinate = coord;
    this.min = min;
    this.max = max;
    orgs = new ID[initialLength];
  }
}
