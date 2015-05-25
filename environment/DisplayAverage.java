package environment;

import javax.swing.JFrame;

public class DisplayAverage {
  JFrame frame;

  public DisplayAverage(organism.Population population) {
    frame = new JFrame();
    frame.getContentPane().setLayout(null);
    frame.setSize(400,600);
    AverageCanvas canvas = new AverageCanvas(population);
    frame.getContentPane().add(canvas);
    frame.setVisible(true);;
  }
}
