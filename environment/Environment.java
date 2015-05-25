package environment;

import organism.*;
import common.*;
import java.util.Random;

public class Environment {
  double size;
  double maxVision;
  double maxMovement;
  double shotDamage;
  double movementEnergy;
  public double reproduceEnergy;
  ID turn;
  ID[] result;
  public Population[] population;
  public Random rand = new Random();
  public Cluster[][] cluster;
  public Display display;

  public Environment(int populationNumber, double size, double maxVision,
      double maxMovement, double shotDamage, double movementEnergy, double reproduceEnergy) {
    population = new Population[populationNumber];
    this.size = size;
    this.maxVision = maxVision;
    this.maxMovement = maxMovement;
    this.shotDamage = shotDamage;
    this.movementEnergy = movementEnergy;
    this.reproduceEnergy = reproduceEnergy;
  }

  public static void main(String[] args) {
    Environment env = new Environment(6, 700.0, 50.0, 0.04, 30.0, 0.002, 10.0);
    for (int i = 0; i < env.population.length; i++) {
      env.population[i] = JavaSapiens.getPopulation(env, i, 25);
    }
    env.initializeRandom();
    env.cluster();
    env.run();
  }

  void initializeRandom() {
    for (int i = 0; i < population.length; i++) {
      for (int j = 0; j < population[i].organisma.length; j++) {
        Point p = new Point(rand.nextDouble() * size, rand.nextDouble() * size);
        population[i].organisma[j].setPosition(p);
      }
    }
  }

  void cluster() {
    int clustNum = (int) (size / maxVision);
    cluster = new Cluster[clustNum][clustNum];
    int organismaNumber = 0;
    for (int i = 0; i < population.length; i++) {
      organismaNumber += population[i].organisma.length;
    }
    int initialLength = ( (int) (organismaNumber / clustNum / clustNum) + 1) * 5;
    for (int i = 0; i < clustNum; i++) {
      for (int j = 0; j < clustNum; j++) {
        java.awt.Point coord = new java.awt.Point(i, j);
        common.Point min = new common.Point(i * maxVision, j * maxVision);
        common.Point max = new common.Point( (i + 1) * maxVision, (j + 1) * maxVision);
        this.cluster[i][j] = new Cluster(initialLength, coord, min, max);
      }
    }
    for (int i = 0; i < population.length; i++) {
      for (int j = 0; j < population[i].organisma.length; j++) {
        ID id = new ID(i, j);
        addToCluster(id);
      }
    }
  }

  public void addToCluster(ID id) {
    int X = (int) (getOrganisma(id).inf.position.x / this.maxVision);
    int Y = (int) (getOrganisma(id).inf.position.y / this.maxVision);
    if (X == (int) (size / maxVision)) {
      X--;
    }
    if (Y == (int) (size / maxVision)) {
      Y--;
    }
    getOrganisma(id).cluster = new java.awt.Point(X, Y);
    int k;
    for (k = 0; k < cluster[X][Y].orgs.length; k++) {
      if (cluster[X][Y].orgs[k] == null) {
        cluster[X][Y].orgs[k] = id;
        getOrganisma(id).inClusterIndex = k;
        if (cluster[X][Y].orgs[cluster[X][Y].orgs.length - 1] == null) {
          ID[] temp = new ID[cluster[X][Y].orgs.length - 1];
          System.arraycopy(cluster[X][Y].orgs, 0, temp, 0, cluster[X][Y].orgs.length - 1);
          cluster[X][Y].orgs = temp;
        }
        break;
      }
    }
    if (k == cluster[X][Y].orgs.length) {
      ID[] temp = new ID[cluster[X][Y].orgs.length + 1];
      System.arraycopy(cluster[X][Y].orgs, 0, temp, 0, cluster[X][Y].orgs.length);
      cluster[X][Y].orgs = temp;
      cluster[X][Y].orgs[cluster[X][Y].orgs.length - 1] = id;
      getOrganisma(id).inClusterIndex = cluster[X][Y].orgs.length - 1;
    }
  }

  void removeFromCluster(ID id) {
    int X = getOrganisma(id).cluster.x;
    int Y = getOrganisma(id).cluster.y;
    cluster[X][Y].orgs[getOrganisma(id).inClusterIndex] = null;
    if (cluster[X][Y].orgs[cluster[X][Y].orgs.length - 1] == null) {
      ID[] temp = new ID[cluster[X][Y].orgs.length - 1];
      System.arraycopy(cluster[X][Y].orgs, 0, temp, 0, cluster[X][Y].orgs.length - 1);
      cluster[X][Y].orgs = temp;
    }
  }

  void run() {
    this.display = new Display(this);
    int step = 0;
    while (true) {
      int[] sequence = this.setSequence();
      for (int i = 0; i < sequence.length; i++) {
        for (int j = 0; j < population[sequence[i]].organisma.length; j++) {
          ID current = new ID(sequence[i], j);
          if (getOrganisma(current).alive) {
            turn = getOrganisma(current).inf.id;
            getOrganisma(current).getTurn();
          }
        }
      }
      if (step == 5) {
        display.update();
        step = 0;
      }
      step++;
    }
  }

  int[] setSequence() {
    int[] sequence = new int[population.length];
    sequence[0] = rand.nextInt(this.population.length);
    for (int i = 1; i < population.length; i++) {
      boolean again = true;
      while (again) {
        sequence[i] = rand.nextInt(this.population.length);
        again = false;
        for (int j = 0; j < i; j++) {
          if (sequence[i] == sequence[j]) {
            again = true;
            break;
          }
        }
      }
    }
    return sequence;
  }

  public ID[] seen() {
    double vision = getOrganisma(turn).dna.gene[4].value;
    //double vision = 1.0;
    int[] result = new int[0];
    ID nearest = new ID();
    double nearestDist = this.maxVision * vision;
    int clusterXStart = 0;
    int clusterXEnd = 0;
    if (getOrganisma(turn).cluster.x > 0 && getOrganisma(turn).cluster.x < cluster[0].length - 1) {
      clusterXStart = getOrganisma(turn).cluster.x - 1;
      clusterXEnd = getOrganisma(turn).cluster.x + 1;
    }
    else if (getOrganisma(turn).cluster.x == 0) {
      clusterXStart = 0;
      clusterXEnd = 1;
    }
    else if (getOrganisma(turn).cluster.x == cluster[0].length - 1) {
      clusterXStart = cluster[0].length - 2;
      clusterXEnd = cluster[0].length - 1;
    }
    int clusterYStart = 0;
    int clusterYEnd = 0;
    if (getOrganisma(turn).cluster.y > 0 && getOrganisma(turn).cluster.y < cluster[0].length - 1) {
      clusterYStart = getOrganisma(turn).cluster.y - 1;
      clusterYEnd = getOrganisma(turn).cluster.y + 1;
    }
    else if (getOrganisma(turn).cluster.y == 0) {
      clusterYStart = 0;
      clusterYEnd = 1;
    }
    else if (getOrganisma(turn).cluster.y == cluster[0].length - 1) {
      clusterYStart = cluster[0].length - 2;
      clusterYEnd = cluster[0].length - 1;
    }
    int myClusterX = getOrganisma(turn).cluster.x;
    int myClusterY = getOrganisma(turn).cluster.y;
    for (int k = 0; k < cluster[myClusterX][myClusterY].orgs.length; k++) {
      ID dummyID = cluster[myClusterX][myClusterY].orgs[k];
      if (dummyID != null) {
        if (dummyID.oIndex < population[dummyID.pIndex].organisma.length) {
          if (getOrganisma(dummyID).alive) {
            double dist = Point.calculateDistance(getOrganisma(turn).inf.position,
                getOrganisma(dummyID).inf.position);
            if (dist < vision * maxVision) {
              if (ID.isEqual(dummyID, turn) == false) {
                if (dist < nearestDist) {
                  nearestDist = dist;
                  nearest.pIndex = myClusterX;
                  nearest.oIndex = myClusterY;
                }
                result = Array.resize(result, result.length + 2);
                result[result.length - 2] = getOrganisma(cluster[myClusterX][myClusterY].orgs[k]).inf.id.pIndex;
                result[result.length - 1] = getOrganisma(cluster[myClusterX][myClusterY].orgs[k]).inf.id.oIndex;
              }
            }
          }
        }
      }
    }
    boolean check = true;
    if (result.length > 9) {
      if (nearestDist < Math.abs(getOrganisma(turn).inf.position.x - cluster[myClusterX][myClusterY].min.x))
        if (nearestDist < Math.abs(getOrganisma(turn).inf.position.x - cluster[myClusterX][myClusterY].max.x))
          if (nearestDist < Math.abs(getOrganisma(turn).inf.position.y - cluster[myClusterX][myClusterY].min.y))
            if (nearestDist < Math.abs(getOrganisma(turn).inf.position.y - cluster[myClusterX][myClusterY].max.y))
              check = false;
    }
    if (check) {
      for (int i = clusterXStart; i < clusterXEnd + 1; i++) {
        for (int j = clusterYStart; j < clusterYEnd + 1; j++) {
          if (i != myClusterX || j != myClusterY) {
            for (int k = 0; k < cluster[i][j].orgs.length; k++) {
              ID dummyID = cluster[i][j].orgs[k];
              if (dummyID != null) {
                if (dummyID.oIndex < population[dummyID.pIndex].organisma.length) {
                  if (getOrganisma(dummyID).alive) {
                    double dist = Point.calculateDistance(getOrganisma(turn).inf.position,
                        getOrganisma(dummyID).inf.position);
                    if (dist < vision * maxVision) {
                      if (ID.isEqual(dummyID, turn) == false) {
                        if (dist < nearestDist) {
                          nearestDist = dist;
                          nearest.pIndex = i;
                          nearest.oIndex = j;
                        }
                        result = Array.resize(result, result.length + 2);
                        result[result.length - 2] = getOrganisma(cluster[i][j].orgs[k]).inf.id.pIndex;
                        result[result.length - 1] = getOrganisma(cluster[i][j].orgs[k]).inf.id.oIndex;
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    //if (nearestDist < 1.0)
      //getOrganisma(this.turn).energy -= 5.0;
    ID[] resultID = new ID[result.length / 2];
    for (int i = 0; i < resultID.length; i++) {
      resultID[i] = new ID(result[2 * i], result[2 * i + 1]);
    }
    if (resultID.length > 1) {
      for (int i = 0; i < resultID.length; i++) {
        if (ID.isEqual(nearest, resultID[i])) {
          if (i > 0) {
            ID temp = new ID();
            temp.pIndex = resultID[0].pIndex;
            temp.oIndex = resultID[0].oIndex;
            resultID[0].pIndex = resultID[i].pIndex;
            resultID[0].oIndex = resultID[i].oIndex;
            resultID[i].pIndex = temp.pIndex;
            resultID[i].oIndex = temp.oIndex;
          }
          break;
        }
      }
    }
    this.result = resultID;
    return resultID;
  }

  public double getDistance(ID id) {
    if (checkID(id)) {
      double dist = Point.calculateDistance(getPosition(turn), getPosition(id));
      return dist / maxVision;
    }
    else {
      return -1.0;
    }
  }

  public double getBearing(ID id) {
    if (checkID(id)) {
      double angle = Point.calculateBearing(getPosition(turn), getPosition(id));
      return angle;
    }
    else {
      return -1.0;
    }
  }

  public boolean shot(ID id, double magnitude) {
    getOrganisma(turn).energy -= magnitude;
    double probability = (1.0 - getDistance(id)) * getOrganisma(turn).dna.gene[1].value * 0.2;
    if (rand.nextDouble() < probability) {
      getOrganisma(id).decreaseEnergy(magnitude * shotDamage);
      return true;
    }
    else {
      return false;
    }
  }

  public void move(double bearing, double dist) {
    if (getOrganisma(turn).energy < dist * movementEnergy) {
      dist = getOrganisma(turn).energy / movementEnergy;
    }
    getOrganisma(turn).energy -= dist * movementEnergy;
    double distX = maxMovement * dist * Math.cos(bearing);
    double distY = maxMovement * dist * Math.sin(bearing);
    getOrganisma(turn).inf.position.x += distX;
    getOrganisma(turn).inf.position.y += distY;
    double x = getOrganisma(turn).inf.position.x;
    double y = getOrganisma(turn).inf.position.y;
    if (x < 0.0) {
      getOrganisma(turn).inf.position.x = 0.0;
      x = 0.0;
      getOrganisma(turn).hitWall();
    }
    else if (x > size) {
      getOrganisma(turn).inf.position.x = size;
      x = size;
      getOrganisma(turn).hitWall();
    }
    if (y < 0.0) {
      getOrganisma(turn).inf.position.y = 0.0;
      y = 0.0;
      getOrganisma(turn).hitWall();
    }
    else if (y > size) {
      getOrganisma(turn).inf.position.y = size;
      y = size;
      getOrganisma(turn).hitWall();
    }

    if (x < cluster[getOrganisma(turn).cluster.x][getOrganisma(turn).cluster.y].min.x) {
      removeFromCluster(turn);
      addToCluster(turn);
    }
    else if (x > cluster[getOrganisma(turn).cluster.x][getOrganisma(turn).cluster.y].max.x) {
      removeFromCluster(turn);
      addToCluster(turn);
    }
    if (y < cluster[getOrganisma(turn).cluster.x][getOrganisma(turn).cluster.y].min.y) {
      removeFromCluster(turn);
      addToCluster(turn);
    }
    else if (y > cluster[getOrganisma(turn).cluster.x][getOrganisma(turn).cluster.y].max.y) {
      removeFromCluster(turn);
      addToCluster(turn);
    }
  }

  public void kill(ID id) {
    getOrganisma(id).alive = false;
    removeFromCluster(id);
    if (ID.isEqual(id, this.turn) == false) {
      getOrganisma(this.turn).eat(getOrganisma(id).maxInitEnergy / 2.0);
    }
    population[id.pIndex].currentSituation--;
    population[id.pIndex].setCrowdFactor();
    if (this.population[id.pIndex].currentSituation == -1 * population[id.pIndex].initialPopulation) {
      double fit = 0;
      int fitIndex = -1;
      for (int i = 0; i < population.length; i++) {
        if (population[i].currentSituation > - population[i].initialPopulation + 5) {
          if (population[i].getFitness() > fit) {
            fit = population[i].getFitness();
            fitIndex = i;
          }
        }
      }
      /*if (fitIndex > -1)
        this.population[id.pIndex] = this.population[fitIndex].duplicate(id.pIndex);
      else*/
        population[id.pIndex] = JavaSapiens.getPopulation(this, id.pIndex,
            population[id.pIndex].initialPopulation);

      for (int i = 0; i < population[id.pIndex].organisma.length; i++) {
        Point p = new Point(rand.nextDouble() * size, rand.nextDouble() * size);

        population[id.pIndex].organisma[i].setPosition(p);
        addToCluster(population[id.pIndex].organisma[i].inf.id);
      }
    }
  }

  public boolean reproduce(ID id) {
    if (getOrganisma(id).energy > reproduceEnergy &&
        getOrganisma(turn).energy > reproduceEnergy) {
      if (getOrganisma(id).geneCheck(turn)) {
        getOrganisma(turn).energy -= reproduceEnergy;
        getOrganisma(id).energy -= reproduceEnergy;
        Gene[] gene = new Gene[getOrganisma(id).dna.gene.length];
        for (int i = 0; i < gene.length; i++) {
          gene[i] = new Gene();
          gene[i].setValue( (getOrganisma(id).dna.gene[i].value +
              getOrganisma(turn).dna.gene[i].value) / 2.0);
        }
        DNA dna = new DNA(gene);
        dna = this.mutation(dna);
        JavaSapiens j = new JavaSapiens(this, id.pIndex, 0, dna);
        j.energy = reproduceEnergy * 2.0;
        double x = (getOrganisma(turn).inf.position.x + getOrganisma(id).inf.position.x) / 2.0;
        double y = (getOrganisma(turn).inf.position.y + getOrganisma(id).inf.position.y) / 2.0;
        Point p = new Point(x, y);
        j.setPosition(p);
        j.reproduceTime = 1;
        population[id.pIndex].addMember(j);
        getOrganisma(turn).reproduceTime = 1;
        getOrganisma(id).reproduceTime = 1;
        return true;
      }
    }
    return false;
  }

  public Gene[] getDNA(ID id) {
    Gene[] dna = new Gene[getOrganisma(id).dna.gene.length / 2];
    System.arraycopy(getOrganisma(id).dna.gene, 0, dna, 0, getOrganisma(id).dna.gene.length / 2);
    return dna;
  }

  boolean checkID(ID id) {
    for (int i = 0; i < result.length; i++) {
      if (ID.isEqual(id, result[i])) {
        return true;
      }
    }
    return false;
  }

  Point getPosition(ID id) {
    return getOrganisma(id).getPosition();
  }

  Organism getOrganisma(ID id) {
    return this.population[id.pIndex].organisma[id.oIndex];
  }

  public DNA mutation(DNA dna) {
    for (int i = 0; i < 1; i++) {
      if (rand.nextDouble() > 0.9) {
        int mutation = rand.nextInt(dna.gene.length);
        dna.gene[mutation].value = rand.nextDouble();
      }
    }
    return dna;
  }
}
