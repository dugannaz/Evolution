package environment;

import javax.swing.JFrame;

public class Display {
  JFrame frame;
  public DisplayCanvas canvas;
  InfoCanvas infoCanvas;
  int step;

  public Display(Environment env) {
    frame = new JFrame();
    frame.getContentPane().setLayout(null);
    frame.setSize((int)(env.size)+300,(int)(env.size) + 33);
    canvas = new DisplayCanvas(env);
    frame.getContentPane().add(this.canvas);
    infoCanvas = new InfoCanvas(env);
    frame.getContentPane().add(infoCanvas);
    frame.setVisible(true);
    step =0;
  }
  public void update() {
    canvas.update();
    if (step == 50) {
      infoCanvas.update();
      step = 0;
    }
    step++;
  }
}
