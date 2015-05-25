package environment;

public class canvasMouseListener extends java.awt.event.MouseAdapter {
  public Environment env;

  public canvasMouseListener(Environment env) {
    this.env = env;
  }

  public void mouseClicked(java.awt.event.MouseEvent e) {
    int y = e.getY();
    for (int i=0; i < this.env.population.length; i++) {
      if (y > (i+1) * 40 - 10 && y < (i+2) * 40 - 10) {
        DisplayAverage d = new DisplayAverage(this.env.population[i]);
      }
    }
  }

}
