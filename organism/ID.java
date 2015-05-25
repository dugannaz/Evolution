package organism;

public class ID {
  public int pIndex;
  public int oIndex;

  public ID() {

  }
  public ID(int pIndex, int oIndex) {
    this.pIndex = pIndex;
    this.oIndex = oIndex;
  }

  public static boolean isEqual(ID a, ID b) {
    if(a.pIndex == b.pIndex && a.oIndex == b.oIndex)
      return true;
    else
      return false;
  }
}
