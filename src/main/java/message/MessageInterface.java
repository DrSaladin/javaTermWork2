package message;

import java.time.Instant;

public interface MessageInterface {

  public String getName();

  public String getMessage();

  public Instant getDate();

  public String getType();
}
