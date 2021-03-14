/**
 * @author RALAIVITA Jonathan Mikaia
 */
package metier;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileTransfert {

  public static void sendFile(
    InputStream in,
    OutputStream out,
    boolean closeStream
  ) {
    byte buf[] = new byte[1024];
    int n;

    try {
      while ((n = in.read(buf)) != -1) {
        out.write(buf, 0, n);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
