package server.messageHandling;

import client.clientConnection.ClientConnection;
import message.Message;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import static utils.Utils.formatMessage;

public class MessageHandling {
  /** Path to log file */
  private final String logFilePath = "serverLog/log.txt";
  private BlockingQueue<Message> messageQueue = new LinkedBlockingQueue<>();
  /** */
  ConcurrentHashMap<String, ClientConnection> userInfo = null;

  public MessageHandling(ConcurrentHashMap<String, ClientConnection> userInfo) {
    this.userInfo = userInfo;
  }

  public void showNUpdateMessageHistory(Message message) {
    String chatMessage = formatMessage(message);
    System.out.println(chatMessage);
    updateUsersChat(message);
  }

  public void updateUsersChat(Message chatMessage) {
    messageQueue.add(chatMessage);
    updateLogFile(chatMessage);
  }

  private void updateLogFile(Message message) {
    String filePath = logFilePath;

    try (
      FileWriter fileWriter = new FileWriter(filePath, true);
      BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)
    ) {

      bufferedWriter.write(formatMessage(message));
      bufferedWriter.newLine();

    } catch (IOException e) {
      System.err.println("Error during file writing: " + e.getMessage());
    }
  }

  public void processMessages() {
    while (true) {
      try {
        Message message = messageQueue.take();

        for (Map.Entry<String, ClientConnection> entry : this.userInfo.entrySet()) {
          if (!entry.getKey().equals(message.getName())) {
            entry.getValue().getOutputStream().writeObject(message);
          }
        }
      } catch (InterruptedException | IOException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }
}
