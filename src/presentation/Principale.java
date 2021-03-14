/**
 * @author Mikaia Ralaivita
 * @description Classe principale
 */

package presentation;

public class Principale {

  /**
   * @param args
   *
   * INPUT:
   * OUTPUT:
   * DESCRIPTION: Main thead
   * ASSUMPTION:
   * @return
   */

  public static void main(String[] args) {
    ServerGUI is = new ServerGUI("Call Home - Server");

    is.loadGUI();
    is.setVisible(true);
  }
}
