package client;

import client.clientConnection.ClientThreadHandling;
import message.Message;
import message.MessageInterface;
import utils.Utils;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.Scanner;

import static utils.Utils.getHost;
import static utils.Utils.getPort;

public class Main {
  public static void main(String[] args) {
    String host = getHost();
    int port = getPort();
    Scanner scanner = new Scanner(System.in);
    ClientThreadHandling clientThreadHandling = new ClientThreadHandling();

    System.out.println("Write your name");
    String name = scanner.nextLine();

    try (
      Socket clientSocket = new Socket(host, port);
      ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())
    ) {
      clientThreadHandling.clientInThread(in).start();

      MessageInterface messageToSend = new Message(name, name + " has joined the chat", Instant.now(), Utils.MessageTypes.ADMINISTRATIVE);
      out.writeObject(messageToSend);
      out.flush();

      while (true) {
        String messageText = scanner.nextLine();
        if("exit".equals(messageText)) {
          messageToSend = new Message(name, messageText, Instant.now(), Utils.MessageTypes.COMMON);
          out.writeObject(messageToSend);
          out.flush();
          break;
        }
        messageToSend = new Message(name, messageText, Instant.now(), Utils.MessageTypes.COMMON);
        out.writeObject(messageToSend);
        out.flush();
      }

    } catch (Exception e) {
      System.out.println("Client side error " + e.getMessage());
    }
  }
}

