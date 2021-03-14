/**
 * @author RALAIVITA Jonathan Mikaia
 */
package metier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenFileTransfert implements Runnable {

  ServerSocket fileServerSocket;
  InputStream in;
  OutputStream out;
  Socket fileSocket;
  File file;

  public ListenFileTransfert(ServerSocket ss) {
    this.fileServerSocket = ss;
  }

  @Override
  public void run() {
    while (true) {
      try {
        System.out.println("filing");
        fileSocket = this.fileServerSocket.accept();

        (
          new File((new File("").getAbsolutePath()).toString() + "\\upload")
        ).mkdir();

        FileTransfert.sendFile(
          fileSocket.getInputStream(),
          new FileOutputStream(
            (new File("").getAbsolutePath()).toString() + "\\upload\\a.txt"
          ),
          true
        );

        file =
          new File(
            (new File("").getAbsolutePath()).toString() + "\\upload\\a.txt"
          );

        System.out.println(file.getAbsolutePath());

        FileTransfert.sendFile(
          new FileInputStream(file.getAbsolutePath()),
          fileSocket.getOutputStream(),
          true
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
