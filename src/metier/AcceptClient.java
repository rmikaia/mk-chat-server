/**
 * @author RALAIVITA Jonathan Mikaia
 */

package metier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import presentation.ServerGUI;

public class AcceptClient extends Observable implements Runnable {

  private ServerSocket serverSocket;
  private Socket socket;
  private int countConnected;
  private boolean clientExist;
  private ObjectInputStream inObj;
  ServerGUI serverGUI;
  public Thread t1;

  public AcceptClient(ServerSocket serverSocket, ServerGUI serverGUI) {
    this.serverSocket = serverSocket;
    this.serverGUI = serverGUI;
  }

  @Override
  public void run() {
    try {
      while (true) {
        this.socket = this.serverSocket.accept();
        countConnected++;

        this.inObj = new ObjectInputStream(this.socket.getInputStream());
        Message msg = ((Message) this.inObj.readObject());

        this.serverGUI.getServerStatusTextarea()
          .append(
            "\n # Le " + countConnected + " ème client souhaite se connecter\n"
          );

        t1 = new Thread(new AuthClient(this.socket, msg, this.serverGUI));
        t1.start();
      }
    } catch (IOException e) {
      System.err.println("Erreur serveur AcceptClient");
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public boolean isClientExist() {
    return clientExist;
  }

  public void setClientExist(boolean clientExist) {
    this.clientExist = clientExist;
    setChanged();
    notifyObservers();
  }
}
