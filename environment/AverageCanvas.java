package environment;

import java.awt.Canvas;

public class AverageCanvas extends Canvas{
  organism.Population population;

  public AverageCanvas(organism.Population population) {
    this.population = population;
    setSize(400,600);
    setVisible(true);
  }

  public void paint(java.awt.Graphics g) {
    g.drawString("Population " + population.organisma[0].inf.id.pIndex, 10, 15);
    double[] result = population.getAverage();
    for (int i=0; i < result.length -1; i++) {
      g.drawString(population.organisma[0].getGeneDef(i) + " = " + result[i], 10, (i+2) * 15);
    }
    g.drawString("Energy Average = " + result[result.length-1], 10, (result.length+1) *15);
  }
}
