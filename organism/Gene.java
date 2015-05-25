package organism;

public class Gene {
  public double value;

  public Gene() {
    value = 0.0;
  }

  public void setValue(double value) {
    this.value = value;
  }
  public double getValue() {
    return value;
  }

  public Object clone() {
    Gene gene = new Gene();
    gene.value = value;
    return gene;
  }
}
