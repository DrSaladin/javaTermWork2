package client.clientConnection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection {
  private final Socket clientSocket;
  private final ObjectInputStream inputStream;
  private final ObjectOutputStream outputStream;

  public ClientConnection(Socket clientSocket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
    this.clientSocket = clientSocket;
    this.inputStream = inputStream;
    this.outputStream = outputStream;
  }

  public Socket getClientSocket() {
    return clientSocket;
  }

  public ObjectInputStream getInputStream() {
    return inputStream;
  }

  public ObjectOutputStream getOutputStream() {
    return outputStream;
  }
}
