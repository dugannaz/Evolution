package environment;

import java.awt.Color;
import javax.swing.JPanel;

public class DisplayCanvas extends JPanel {
  Environment env;
  Color[] c = new Color[20];

  public DisplayCanvas(Environment env) {
    this.env = env;
    setBackground(new Color(0,0,0));
    setSize((int)this.env.size +2,(int)this.env.size +2);
    setLocation(2,2);
    setVisible(true);
    c[0] = new Color(225,0,0);
    c[1] = new Color(0,225,0);
    c[2] = new Color(0,0,225);
    c[3] = new Color(225,0,225);
    c[4] = new Color(0,225,225);
    c[5] = new Color(225,225,0);
    c[6] = new Color(225,225,225);
    c[7] = new Color(112,112,0);
    c[8] = new Color(0,112,112);
    c[9] = new Color(225,110,30);
    c[10] = new Color(145,198,47);
    c[11] = new Color(134,3,241);
    c[12] = new Color(122,122,122);
    c[13] = new Color(128,0,64);
    c[14] = new Color(120,120,225);
  }
  public void paint(java.awt.Graphics g) {
	  
	super.paint(g);  
	  
    for (int i = 0; i < env.population.length; i++) {
      g.setColor(c[i]);
      for (int j = 0; j < env.population[i].organisma.length; j++) {
        boolean alive = env.population[i].organisma[j].alive;
        if (alive) {
          int x = (int)(env.population[i].organisma[j].inf.position.x);
          int y = (int)(env.population[i].organisma[j].inf.position.y);
          g.drawRect(x,y,2,2);
        }
      }
    }
  }
  public void update() {
    this.repaint();
  }
}
