package environment;

public class deneme {
  public static void main(String[] args) {
    int reproduceTime = 1;
    while (true) {
      if (reproduceTime > 0) {
        if (reproduceTime < 10)
          reproduceTime++;
        else
          reproduceTime = 0;
      }

      System.out.println(reproduceTime);
    }
  }
}
