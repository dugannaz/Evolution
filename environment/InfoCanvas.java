package environment;
import java.awt.Canvas;
import java.awt.Color;

public class InfoCanvas extends Canvas {
  Environment env;

  public InfoCanvas(Environment env) {
    this.env = env;
    setSize(300,(int)this.env.size + 2);
    setBackground(new Color(0));
    setLocation((int)this.env.size + 5 , 2);
    addMouseListener(new canvasMouseListener(env));
    setVisible(true);
  }
  public void paint(java.awt.Graphics g) {
    for (int i=0; i < env.population.length; i++) {
      g.setColor(env.display.canvas.c[i]);
      g.drawString("Population " + i + ":", 10, (i + 1) * 40);
      int population = env.population[i].initialPopulation + env.population[i].currentSituation;
      g.drawString("Organisma # : " + population, 10, (i + 1) * 40 + 15);
      g.drawString("Age : " + env.population[i].age, 120, (i + 1) * 40 + 15);
      int fitness = (int)(env.population[i].getFitness());
      g.drawString("Fitness : " + fitness, 200, (i + 1) * 40 + 15);
    }
  }
  public void update() {
    for (int i=0; i < env.population.length; i++ )
      env.population[i].age++;
    repaint();
  }
}
