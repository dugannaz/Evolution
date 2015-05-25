package common;

/*
 * Array operations
 */
public class Array {

  public static int[] resize(int[] array, int newSize) {
    int resized[] = new int[newSize];
    System.arraycopy(array, 0, resized, 0, array.length);
    return resized;
  }
  public static double[] resize(double[] array, int newSize) {
    double resized[] = new double[newSize];
    System.arraycopy(array, 0, resized, 0, array.length);
    return resized;
  }
  public static Object[] resize(Object[] array, int newSize) {
    Object resized[] = new Object[newSize];
    System.arraycopy(array, 0, resized, 0, array.length);
    return resized;
  }
  public static int findEmptyPlace(Object[] o) {
    int i = 0;
    while (i < o.length) {
      if (o[i] == null) {
        return i;
      }
      i++;
    }
    o = resize(o, o.length + 1);
    return i;
  }

}
