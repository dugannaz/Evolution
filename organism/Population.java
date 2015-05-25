package organism;

public class Population {
  public int initialPopulation;
  public double crowdFactor;
  public int currentSituation;
  public Organism[] organisma;
  public long age;

  public double[] getAverage() {
    int aliveNumber =0;
    for (int i = 0; i < organisma.length; i++) {
      if (organisma[i].alive) {
        aliveNumber++;
      }
    }
    if (aliveNumber >0) {
      double energyAvg = 0;
      double[] result = new double[organisma[0].dna.gene.length +1];
      for (int i = 0; i < organisma.length; i++) {
        if (organisma[i].alive) {
          energyAvg += organisma[i].energy;
        }
      }
      energyAvg = energyAvg / ( (double) (aliveNumber));
      for (int i = 0; i < organisma[0].dna.gene.length; i++) {
        result[i] = 0.0;
        for (int j = 0; j < organisma.length; j++)
          if (organisma[j].alive)
            result[i] += organisma[j].dna.gene[i].value;

        result[i] = result[i] / ( (double) (aliveNumber));
        //System.out.println(this.organisma[0].getGeneeDef(i) + " = " + result[i]);
      }
      result[result.length -1] = energyAvg;
      //System.out.println("Energy Average = " + energyAvg);
      //System.out.println(aliveNumber);
    return result;
    }
    return null;
  }
  public void getIDs() {
    for (int i=0; i< organisma.length; i++)
      if (organisma[i].alive)
        System.out.println(organisma[i].inf.id.pIndex + " " + organisma[i].inf.id.oIndex);
  }

  public void setCrowdFactor() {
    this.crowdFactor = ((double)(initialPopulation)-(double)(currentSituation))/
        (double)(initialPopulation);
    //if (this.crowdFactor < 0.0)
      //this.crowdFactor *= 1.4;
    //else
      //this.crowdFactor *= 1.4;
  }
  public void share(double energy) {
    double energyPerIndividual = energy / (double)(initialPopulation + currentSituation);
    for (int i=0; i < organisma.length; i++)
      if (organisma[i].alive)
        organisma[i].energy += energyPerIndividual;
  }

  public double getFitness() {
    double fitness =0;
    for (int i=0; i < organisma.length; i++)
      if (organisma[i].alive)
        fitness += organisma[i].energy;
    return fitness;
  }

  public Population duplicate(int pIndex) {
    Population p = new Population();
    p.crowdFactor = crowdFactor;
    p.currentSituation = currentSituation;
    p.initialPopulation = initialPopulation;
    p.organisma = new Organism[organisma.length];
    for (int i=0; i < organisma.length; i++) {
      Gene[] gene = new Gene[organisma[0].dna.gene.length];
      for (int j=0; j < organisma[0].dna.gene.length; j++) {
        gene[j] = new Gene();
        gene[j].setValue(organisma[i].dna.gene[j].value);
      }
      DNA dna = new DNA(gene);
      p.organisma[i] = new JavaSapiens(organisma[0].environment, pIndex, i, dna);
      if (organisma[i].alive == false)
        p.organisma[i].alive = false;
    }
    return p;
  }

  public int findEmptyPlace() {
    int i = 0;
    while (i < organisma.length) {
      if (organisma[i].alive == false) {
        return i;
      }
      i++;
    }

    int newLength = organisma.length + 1;
    Organism[] temp = new Organism[newLength];
    System.arraycopy(organisma, 0, temp, 0, newLength - 1);
    organisma = temp;

    return i;
  }

  public void addMember(Organism o) {
    currentSituation++;
    setCrowdFactor();
    int i = findEmptyPlace();
    organisma[i]=o;
    organisma[i].inf.id.oIndex = i;
    o.environment.addToCluster(organisma[i].inf.id);
  }
}
