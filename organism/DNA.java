package organism;

public class DNA {
  public Gene[] gene;

  public DNA(Gene[] gene) {
    this.gene = gene;
  }
  public Object clone() {
    Gene[] gene = new Gene[this.gene.length];
    for (int i=0; i<this.gene.length; i++)
      gene[i]=(Gene)this.gene[i].clone();
    DNA dna = new DNA(gene);
    return dna;
  }
}
