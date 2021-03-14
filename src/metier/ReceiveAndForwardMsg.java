/**
 * @author RALAIVITA Jonathan Mikaia
 */
package metier;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import presentation.ServerGUI;

public class ReceiveAndForwardMsg implements Runnable {

  ObjectInputStream in;
  ObjectOutputStream out;
  Socket socket;
  Socket fileSocket;
  ServerGUI guiRef;

  public ReceiveAndForwardMsg(Socket socket, ServerGUI guiRef) {
    this.socket = socket;
    this.guiRef = guiRef;

    (new Thread(new ListenFileTransfert(ServerGUI.filesocket))).start();
  }

  @Override
  public void run() {
    try {
      this.in = new ObjectInputStream(this.socket.getInputStream());

      while (true) {
        Message msg = (Message) this.in.readObject();

        if (
          msg.getMsgType() == 4
        ) {
          ServerGUI.listConnected
            .get(msg.getSenderPseudo())
            .writeObject(
              new Message("serveur", msg.getSenderPseudo(), "Tu veux partir", 4)
            );
          ServerGUI.listConnected.get(msg.getSenderPseudo()).flush();

          ServerGUI.listConnected.remove(msg.getSenderPseudo());
          System.out.println("logout");

          for (String mapKey : ServerGUI.listConnected.keySet()) {
            ObjectOutputStream oos = ServerGUI.listConnected.get(mapKey);
            oos.writeObject(
              new Message(msg.getSenderPseudo(), "all", " a quitté le chat", 3)
            );
            oos.flush();
          }

          this.guiRef.getServerStatusTextarea()
            .append("\n # " + msg.getSenderPseudo() + " est déconnecté\n");
        } else if (
          msg.getMsgType() == 5
        ) { // cas réception Msg Privé
          System.out.println(
            "Server: receive MP " +
            msg.getSenderPseudo() +
            " and forwarding to => " +
            msg.getReceveirPseudo()
          );

          ServerGUI.listConnected
            .get(msg.getReceveirPseudo())
            .writeObject(
              new Message(
                msg.getSenderPseudo(),
                msg.getReceveirPseudo(),
                msg.getMsg(),
                5
              )
            );
          ServerGUI.listConnected.get(msg.getSenderPseudo()).flush();
        } else if (
          msg.getMsgType() == 6
        ) {
          String str = "";
          int i = 0;

          for (String mapKey : ServerGUI.listConnected.keySet()) {
            str += mapKey;

            i++;
            if (i < ServerGUI.listConnected.size()) str += "|";
          }

          System.out.println("Server: fetchallusers => " + str);
          ServerGUI.listConnected
            .get(msg.getSenderPseudo())
            .writeObject(new Message("serveur", msg.getSenderPseudo(), str, 6));
          ServerGUI.listConnected.get(msg.getSenderPseudo()).flush();
        } else {
          System.out.println("broadcast => ");

          for (String mapKey : ServerGUI.listConnected.keySet()) {
            ObjectOutputStream oos = ServerGUI.listConnected.get(mapKey);
            oos.writeObject(msg);
            oos.flush();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}
