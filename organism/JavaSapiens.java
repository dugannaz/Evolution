package organism;

import environment.Environment;

/*
 * Genes and functions of organism is defined here
 */
public class JavaSapiens extends Organism {
  public double direction;
  public static String[] geneDef;

public JavaSapiens(Environment env, int pIndex, int oIndex, DNA dna) {
    super(env, pIndex, oIndex);
    this.dna = this.initialize(dna);
    maxInitEnergy = 100.0;
    energy = this.dna.gene[9].value * maxInitEnergy;
    direction = environment.rand.nextDouble() * Math.PI * 2.0;

  }

  /*
   * Gene definitions
   * Desired genes are for mating (not currently in use)
   * Genes are randomly initialized
   */
  DNA initialize(DNA dna) {
    if (dna == null) {
      int geneNumber = 16;
      Gene[] gene = new Gene[geneNumber * 2];
      geneDef = new String[geneNumber * 2];
      gene[0] = new Gene(); geneDef[0]= "speed";
      gene[1] = new Gene(); geneDef[1]= "shotSucces";
      gene[2] = new Gene(); geneDef[2]= "shotMagnitudeDistanceRelation";
      gene[3] = new Gene(); geneDef[3]= "maxShotMagnitude";
      gene[4] = new Gene(); geneDef[4]= "vision";
      gene[5] = new Gene(); geneDef[5]= "bravery";
      gene[6] = new Gene(); geneDef[6]= "moveTreshold";
      gene[7] = new Gene(); geneDef[7]= "shotTreshold";
      gene[8] = new Gene(); geneDef[8]= "reprodureTolerance";
      gene[9] = new Gene(); geneDef[9]= "energy";
      gene[10] = new Gene(); geneDef[10]= "randomWalk";
      gene[11] = new Gene(); geneDef[11]= "shotToSame";
      gene[12] = new Gene(); geneDef[12]= "movementAgainstSame";
      gene[13] = new Gene(); geneDef[13]= "helpfullness";
      gene[14] = new Gene(); geneDef[14]= "shotToEnemy";
      gene[15] = new Gene(); geneDef[15]= "age";
      for (int i = 0; i < geneNumber; i++) {
        gene[i + geneNumber] = new Gene();
        geneDef[i + geneNumber] = "desired " + geneDef[i];
      }
      for (int i = 0; i < geneNumber * 2; i++) {
        gene[i].setValue(environment.rand.nextDouble());
      }
      dna = new DNA(gene);
    }
    return dna;
  }

  public void act() {
    ID[] seen = look();
    decide(seen);
  }

  public void eat(double energy) {
    energy += energy * (1.0 - dna.gene[13].value);
    if (this.energy > dna.gene[9].value * maxInitEnergy * 5.0) {
      this.energy = dna.gene[9].value * maxInitEnergy * 5.0;
    }
    environment.population[inf.id.pIndex].share(energy * dna.gene[13].value);
  }

  public void hitWall() {
    direction = environment.rand.nextDouble() * Math.PI * 2.0;
  }

  ID[] look() {
    return environment.seen();
  }

  void decide(ID[] seen) {
    if (energy > maxInitEnergy * dna.gene[9].value * 3.0) {
      if (environment.population[inf.id.pIndex].crowdFactor > 0)
        duplicate();
      return;
    }
    if (seen.length > 0) {
      double minDist = this.environment.getDistance(seen[0]);
      if (reproduce(seen) == false) {
        if (minDist < dna.gene[7].value && seen[0].pIndex != inf.id.pIndex) {
          shot(seen[0], minDist);
        }
        else if (minDist < dna.gene[6].value) {
          move(seen);
        }
        else {
          auto();
        }
      }
      else {
        auto();
      }
    }
    else {
      auto();
    }
  }

  void move(ID[] seen) {
    double diff;
    if (seen[0].pIndex == inf.id.pIndex)
      diff = (dna.gene[12].value - 0.5) * 2.0;
    else
      diff = (dna.gene[5].value - 0.5) * 2.0;
    double bearing = environment.getBearing(seen[0]);
    double dist = dna.gene[0].value * diff;
    environment.move(bearing, dist);
  }

  void move() {
    direction += (environment.rand.nextDouble() - 0.5) * Math.PI *
        dna.gene[10].value / 5.0;
    double bearing = direction;
    double dist = dna.gene[0].value;
    environment.move(bearing, dist);
  }

  void shot(ID target, double dist) {
    double magnitude;
    double factor = dna.gene[2].value;
    magnitude = dna.gene[3].value * (1.0 - dist * factor) * 0.25;
    if (energy > 0.0) {
      if (magnitude > energy) {
        magnitude = energy;
      }
      environment.shot(target, magnitude);
    }
  }

  boolean reproduce(ID[] seen) {
    if (energy < environment.reproduceEnergy == false && reproduceTime == 0) {
      int end =0;
      if (seen.length < 6) end = seen.length;
      else end = 5;
      for (int i = 0; i < end; i++) {
        if (seen[i].pIndex == inf.id.pIndex) {
          if (geneCheck(seen[i])) {
            return environment.reproduce(seen[i]);
          }
        }
      }
    }
    return false;
  }

  public boolean geneCheck(ID partner) {
    Gene[] partnerDNA = environment.getDNA(partner);
    double diff =0.0;
    for (int i = 0; i < dna.gene.length / 2; i++) {
      diff += Math.abs(dna.gene[i].value - partnerDNA[i].value); // + this.dna.gene.length/2].value
    }
    diff = diff / dna.gene.length * 2;
    if (diff < dna.gene[8].value * environment.population[inf.id.pIndex].crowdFactor)
      return true;
    else
      return false;
  }

  void auto() {
    this.move();
  }

  public String getGeneDef(int index) {
    return geneDef[index];
  }

  public void duplicate() {
    energy -= maxInitEnergy * dna.gene[9].value;
    DNA dna = (DNA)this.dna.clone();
    JavaSapiens j = new JavaSapiens(environment, inf.id.pIndex , 0, dna);
    j.inf.position.x = inf.position.x;
    j.inf.position.y = inf.position.y;
    j.reproduceTime =1;
    j.geneDef = geneDef;
    this.environment.population[this.inf.id.pIndex].addMember(j);
  }
  public static Population getPopulation(Environment env, int pIndex, int population) {
    Population p = new Population();
    p.initialPopulation = population;
    p.crowdFactor = 0.0;
    p.currentSituation = 0;
    p.age = 0;
    p.organisma = new JavaSapiens[population];
    for (int i = 0; i < population; i++) {
      p.organisma[i] = new JavaSapiens(env, pIndex, i, null);
    }
    return p;
  }
}
