package organism;

import common.Point;
import environment.Environment;

public abstract class Organism {
  Environment environment;
  public Information inf;
  public DNA dna;
  public double energy;
  public double maxInitEnergy;
  public int reproduceTime =0;
  public java.awt.Point cluster;
  public int inClusterIndex;

  public boolean alive;

  public Organism(Environment env, int pIndex, int oIndex) {
    environment = env;
    alive = true;
    inf = new Information();
    inf.id.pIndex = pIndex;
    inf.id.oIndex = oIndex;
    inf.age = 0;
  }

  public Point getPosition() {
    return inf.position;
  }

  public void setPosition(Point p) {
    inf.position = p;
  }

  public void getTurn() {
    inf.age++;
    if (energy > dna.gene[9].value * maxInitEnergy * 5.0) {
      energy = dna.gene[9].value * maxInitEnergy * 5.0;
    }
    if (inf.age > (int)(dna.gene[15].value * 100000.0) || energy < 1.0) {
      environment.kill(inf.id);
      return;
    }
    if (reproduceTime > 0) {
      if (reproduceTime < 1000)
        reproduceTime++;
      else
        reproduceTime =0;
    }
    this.act();
  }

  public abstract void act();
  public abstract void eat(double energy);
  public abstract void hitWall();
  public abstract boolean geneCheck(ID id);
  public abstract String getGeneDef(int index);

  public void decreaseEnergy(double energy) {
    this.energy -= energy;
    if (this.energy < 0.0)
      environment.kill(inf.id);
  }
}
