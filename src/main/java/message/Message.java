package message;

import utils.Utils;

import java.io.Serializable;
import java.time.Instant;

public class Message implements MessageInterface, Serializable {
  private String name;
  private String message;
  private Instant date;
  private Utils.MessageTypes type;

  public Message(String name, String message, Instant date, Utils.MessageTypes type) {
    this.name = name;
    this.date = date;
    this.message = message;
    this.type = type;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public Instant getDate() {
    return date;
  }

  @Override
  public String getType() {
    return String.valueOf(type);
  }
}
