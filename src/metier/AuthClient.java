/**
 * @author RALAIVITA Jonathan Mikaia
 */

package metier;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import presentation.ServerGUI;

public class AuthClient implements Runnable {

  private PrintWriter writer;
  private Socket socket;
  private Message msg;
  private boolean auth;
  ServerGUI serverGUI;
  public Thread t2;

  public AuthClient(Socket socket, Message msg, ServerGUI serverGUI) {
    this.socket = socket;
    this.msg = msg;
    this.serverGUI = serverGUI;
  }

  @Override
  public void run() {
    try {
      while (!this.isAuth()) {
        if (this.msg.getMsgType() == 1) {
          int count = 0;

          this.setAuth(true);

          this.writer = new PrintWriter(this.socket.getOutputStream());

          for (String mapKey : ServerGUI.listConnected.keySet()) {
            if (this.msg.getSenderPseudo().equals(mapKey)) count++;
          }

          if (count == 0) {
            this.writer.println("ok");
            this.writer.flush();

            this.serverGUI.getServerStatusTextarea()
              .append("\n # " + this.msg.getSenderPseudo() + " est connecté\n");

            System.out.println("avant ===> " + ServerGUI.listConnected.size());
            ServerGUI.listConnected.put(
              this.msg.getSenderPseudo(),
              new ObjectOutputStream(this.socket.getOutputStream())
            );
            System.out.println("après ===> " + ServerGUI.listConnected.size());

            for (String mapKey : ServerGUI.listConnected.keySet()) {
              ServerGUI.listConnected.get(mapKey);

              ObjectOutputStream oos = ServerGUI.listConnected.get(mapKey);

              oos.writeObject(
                new Message(
                  this.msg.getSenderPseudo(),
                  "all",
                  " a rejoint le chat",
                  3
                )
              );

              oos.flush();

              System.out.println("notify connected \n\n");
            }

            ReceiveAndForwardMsg forwardMsgReceived = new ReceiveAndForwardMsg(
              this.socket,
              serverGUI
            );

            (new Thread(forwardMsgReceived)).start();
          } else {
            this.writer.println("nonOk");
            this.writer.flush();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public boolean isAuth() {
    return auth;
  }

  public void setAuth(boolean auth) {
    this.auth = auth;
  }
}
