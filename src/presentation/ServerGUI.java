/**
 * @author RALAIVITA Jonathan Mikaia
 */

package presentation;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.security.AccessControlException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import metier.AcceptClient;

public class ServerGUI extends JFrame implements Observer {

  /**************************************
   *               DONNEES
   ***************************************/

  private static final long serialVersionUID = 1L;

  private JPanel pan1;
  private JPanel adressePanel;
  private JLabel adresseLabel;
  private JTextField adresseText;
  private JPanel portPanel;
  private JLabel portLabel;
  private JTextField portText;
  private JButton submitServerSpec;
  private JTextArea serverStatusTextarea;
  private Container frameContent;
  public static Map<String, ObjectOutputStream> listConnected;
  public static ServerSocket filesocket;
  public Thread accept;

  public ServerGUI() {
    this.serverStatusTextarea = new JTextArea(7, 25);
  }

  public ServerGUI(String title) {
    super(title);
    this.pan1 = new JPanel();
    this.adressePanel = new JPanel();
    this.adresseLabel = new JLabel("Adresse: ");
    this.portPanel = new JPanel();
    this.adresseText = new JTextField(15);
    this.portLabel = new JLabel("Port: ");
    this.portText = new JTextField(15);
    this.submitServerSpec = new JButton("Valider");
    this.serverStatusTextarea = new JTextArea(7, 25);
    this.frameContent = this.getContentPane();
    ServerGUI.listConnected =
      Collections.synchronizedMap(new HashMap<String, ObjectOutputStream>());
    this.setSize(300, 300);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  public void loadGUI() {
    // ******************** Fenetre Principale ************************
    this.frameContent.setLayout(new FlowLayout());

    // conteneur 1
    this.pan1.setLayout(new GridLayout(3, 1, 10, 10));
    this.frameContent.add(this.pan1);
    this.serverStatusTextarea.setBackground(new Color(2));
    this.serverStatusTextarea.setForeground(Color.green);
    this.serverStatusTextarea.setWrapStyleWord(true);
    this.serverStatusTextarea.setLineWrap(true);

    this.serverStatusTextarea.setText(
        "**************************************\n" +
        "*         ETAT DU SERVEUR             *\n" +
        "**************************************"
      );

    this.serverStatusTextarea.setVisible(false);
    this.serverStatusTextarea.setEditable(true);
    this.frameContent.add(new JScrollPane(this.serverStatusTextarea));

    // Field adresse
    this.adressePanel.setLayout(new GridLayout(2, 1));
    this.adresseLabel.setPreferredSize(new Dimension(75, 10));
    this.adressePanel.add(this.adresseLabel);
    this.adressePanel.add(this.adresseText);

    // Field port
    this.portPanel.setLayout(new GridLayout(2, 1));
    this.portLabel.setPreferredSize(new Dimension(75, 10));
    this.portPanel.add(this.portLabel);
    this.portPanel.add(this.portText);

    // Submit button
    this.submitServerSpec.addActionListener(new ManageButtonClick(this));

    // afficher tous les widgets
    this.pan1.add(this.adressePanel);
    this.pan1.add(this.portPanel);
    this.pan1.add(this.submitServerSpec);
  }

  /********************************************************
   *               Class Listeners (EVENT HANDLER)
   ***************************************************/
  private class ManageButtonClick implements ActionListener {

    ServerGUI caller;

    public ManageButtonClick(ServerGUI caller) {
      this.caller = caller;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
      Object obj = event.getSource();

      if (obj.equals(submitServerSpec)) {
        if (adresseText.getText().equals("") || portText.getText().equals("")) {
          JOptionPane.showMessageDialog(
            null,
            "Veuillez remplir tous les champs",
            "Erreur",
            JOptionPane.WARNING_MESSAGE
          );
        } else {
          try {
            ServerSocket server = new ServerSocket(
              Integer.parseInt(portText.getText()),
              100,
              InetAddress.getByName(adresseText.getText())
            );

            ServerGUI.filesocket =
              new ServerSocket(
                1098,
                100,
                InetAddress.getByName(adresseText.getText())
              );

            AcceptClient accept = new AcceptClient(server, this.caller);

            (new Thread(accept)).start();

            this.caller.serverStatusTextarea.setVisible(true);
            this.caller.serverStatusTextarea.setEditable(false);

            this.caller.serverStatusTextarea.append(
                "\nServeur démarré \n Adresse:" +
                adresseText.getText() +
                "\n Port: " +
                portText.getText()
              );

            this.caller.serverStatusTextarea.append(
                "\n\n # Le serveur est en mode écoute"
              );
            adresseText.setText("");
            portText.setText("");
          } catch (AccessControlException e) {
            this.caller.serverStatusTextarea.setVisible(true);
            this.caller.serverStatusTextarea.append(
                "\nL'accès à ce serveur est interdit"
              );
            JOptionPane.showMessageDialog(
              null,
              "L'accès à ce serveur est interdit",
              "Erreur",
              JOptionPane.WARNING_MESSAGE
            );
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @Override
  public void update(Observable arg0, Object arg1) {
    if (arg0 instanceof AcceptClient) {
      if (
        ((AcceptClient) arg0).isClientExist()
      ) this.serverStatusTextarea.append(
          "\n # Un client  tente de se connecté"
        );
      //((AcceptClient) arg0).setClientExist(false);
    }
  }

  /***********************************************************
   * 							GETTERS/SETTERS
   ************************************************************/

  public JPanel getPan1() {
    return pan1;
  }

  public void setPan1(JPanel pan1) {
    this.pan1 = pan1;
  }

  public JPanel getAdressePanel() {
    return adressePanel;
  }

  public void setAdressePanel(JPanel adressePanel) {
    this.adressePanel = adressePanel;
  }

  public JLabel getAdresseLabel() {
    return adresseLabel;
  }

  public void setAdresseLabel(JLabel adresseLabel) {
    this.adresseLabel = adresseLabel;
  }

  public JTextField getAdresseText() {
    return adresseText;
  }

  public void setAdresseText(JTextField adresseText) {
    this.adresseText = adresseText;
  }

  public JPanel getPortPanel() {
    return portPanel;
  }

  public void setPortPanel(JPanel portPanel) {
    this.portPanel = portPanel;
  }

  public JLabel getPortLabel() {
    return portLabel;
  }

  public void setPortLabel(JLabel portLabel) {
    this.portLabel = portLabel;
  }

  public JTextField getPortText() {
    return portText;
  }

  public void setPortText(JTextField portText) {
    this.portText = portText;
  }

  public JButton getSubmitServerSpec() {
    return submitServerSpec;
  }

  public void setSubmitServerSpec(JButton submitServerSpec) {
    this.submitServerSpec = submitServerSpec;
  }

  public JTextArea getServerStatusTextarea() {
    return serverStatusTextarea;
  }

  public void setServerStatusTextarea(JTextArea serverStatusTextarea) {
    this.serverStatusTextarea = serverStatusTextarea;
  }

  public Container getFrameContent() {
    return frameContent;
  }

  public void setFrameContent(Container frameContent) {
    this.frameContent = frameContent;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }
}
