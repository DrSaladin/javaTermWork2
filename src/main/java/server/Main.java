package server;
import client.clientConnection.ClientConnection;
import server.connectionHandling.ConnectionHandling;
import server.messageHandling.MessageHandling;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.Utils.getPort;

public class Main {
  public static void main(String[] args) {
    /** Welcome text */
    System.out.println("The coolest chat is online, enjoy!");
    /** Get a port from config file */
    int port = getPort();
    /** User info */
    ConcurrentHashMap<String, ClientConnection> usersInfo = new ConcurrentHashMap<>();
    MessageHandling messageHandling = new MessageHandling(usersInfo);
    ConnectionHandling connectionHandling = new ConnectionHandling(messageHandling);
    /** Threads pool */
    ExecutorService executorService = Executors.newFixedThreadPool(100);

    new Thread(messageHandling::processMessages).start();

    try (ServerSocket serverSocket = new ServerSocket(port)) {
      while (true) {
        Socket clientSocket = serverSocket.accept();
        executorService.execute(() -> connectionHandling.handleNewClient(clientSocket, usersInfo));
      }
    } catch (IOException e) {
      System.out.println("Error creating server socket: " + e.getMessage());
    }
  }
}
