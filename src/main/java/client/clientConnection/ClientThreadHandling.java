package client.clientConnection;

import message.Message;

import java.io.*;

import static utils.Utils.formatMessage;

public class ClientThreadHandling {
  private final String logFilePath = "clientLog/log.txt";

  public Thread clientInThread(ObjectInputStream in) {
    return new Thread(() -> {
      try {
        while (true) {
          Object receivedObject = in.readObject();
          if (receivedObject instanceof Message) {
            System.out.println(formatMessage((Message) receivedObject));
            updateLogFile((Message) receivedObject);
          }
        }
      } catch (EOFException e) {
        System.out.println("Server disconnected");
      } catch (IOException | ClassNotFoundException e) {
        System.out.println("Client side error " + e.getMessage());
      }
    });
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
}
