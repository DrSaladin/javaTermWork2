package server.connectionHandling;

import client.clientConnection.ClientConnection;
import message.Message;
import server.messageHandling.MessageHandling;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class ConnectionHandling {
  MessageHandling messageHandling = null;

  public ConnectionHandling(MessageHandling messageHandling) {
    this.messageHandling = messageHandling;
  }

  public void handleNewClient(
    Socket clientSocket,
    ConcurrentHashMap<String, ClientConnection> usersInfo
  ) {
    AtomicReference<Message> receivedMessage = new AtomicReference<>();
    new Thread(() -> {
      try (
        ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())
      ) {
        while (true) {
          receivedMessage.set((Message) in.readObject());

          updateUserInfo(clientSocket, in, out, receivedMessage.get(), usersInfo);
          messageHandling.showNUpdateMessageHistory(receivedMessage.get());
        }
      } catch (EOFException | SocketException e) {
        if (receivedMessage.get() != null) {
          usersInfo.remove(receivedMessage.get().getName());
          System.out.println("User " + receivedMessage.get().getName() + " disconnected");
        }
      } catch (Exception e) {
        System.out.println("Server side error " + e.getMessage());
      } finally {
        if (clientSocket != null) {
          try {
            clientSocket.close();
          } catch (IOException e) {
            System.out.println("Error closing client socket: " + e.getMessage());
          }
        }
      }
    }).start();
  }


  public void updateUserInfo(
    Socket clientSocket,
    ObjectInputStream inputStream,
    ObjectOutputStream outputStream,
    Message receivedMessage,
    ConcurrentHashMap<String, ClientConnection> usersInfo
  ) {
    ClientConnection clientConnection = new ClientConnection(clientSocket, inputStream, outputStream);
    usersInfo.put(receivedMessage.getName(), clientConnection);
  }
}
