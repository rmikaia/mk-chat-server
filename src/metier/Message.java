/**
 * @author RALAIVITA Jonathan Mikaia
 */
package metier;

import java.io.Serializable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  private String senderPseudo;
  private String receveirPseudo;
  private String msg;

  private int msgType;

  public Message(
    String senderPseudo,
    String receveirPseudo,
    String msg,
    int msgType
  ) {
    this.senderPseudo = senderPseudo;
    this.receveirPseudo = receveirPseudo;
    this.msg = msg;
    this.msgType = msgType;
  }

  public String getSenderPseudo() {
    return senderPseudo;
  }

  public void setSenderPseudo(String senderPseudo) {
    this.senderPseudo = senderPseudo;
  }

  public String getReceveirPseudo() {
    return receveirPseudo;
  }

  public void setReceveirPseudo(String receveirPseudo) {
    this.receveirPseudo = receveirPseudo;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getMsgType() {
    return msgType;
  }

  public void setMsgType(int msgType) {
    this.msgType = msgType;
  }
}
